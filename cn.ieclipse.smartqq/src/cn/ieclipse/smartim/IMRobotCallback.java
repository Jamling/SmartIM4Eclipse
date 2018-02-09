/*
 * Copyright 2014-2017 ieclipse.cn.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ieclipse.smartim;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.eclipse.jface.preference.IPreferenceStore;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cn.ieclipse.smartim.callback.ModificationCallback;
import cn.ieclipse.smartim.callback.ReceiveCallback;
import cn.ieclipse.smartim.preferences.RobotPreferencePage;
import cn.ieclipse.util.EncodeUtils;
import cn.ieclipse.util.StringUtils;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年10月16日
 *       
 */
public abstract class IMRobotCallback
        implements ReceiveCallback, ModificationCallback {
    public static final String SEP = " ";
    public static int TIMEOUT = 3;
    public static final String TURING_API_V2 = "http://openapi.tuling123.com/openapi/api/v2";
    
    public static boolean isEnable() {
        IPreferenceStore store = IMPlugin.getDefault().getPreferenceStore();
        return (store.getBoolean(RobotPreferencePage.ROBOT_ENABLE));
    }
    
    public static String getRobotName() {
        IPreferenceStore store = IMPlugin.getDefault().getPreferenceStore();
        String robotName = store.getString(RobotPreferencePage.ROBOT_NAME);
        return robotName;
    }
    
    /**
     * 对userId或groupId进行加密
     * 
     * @param id
     *            userId
     * @return 加密后的md5字串
     */
    public static String encodeUid(String id) {
        return EncodeUtils.getMd5(id);
    }
    
    @Deprecated
    public static String getTuringReply(String userId, String message) {
        IPreferenceStore store = IMPlugin.getDefault().getPreferenceStore();
        String key = store.getString(RobotPreferencePage.TURING_KEY);
        if (key != null && !key.isEmpty()) {
            String url = store.getString(RobotPreferencePage.TURING_API);
            try {
                String info = URLEncoder.encode(message, "UTF-8");
                String param = String.format("?key=%s&userid=%s&info=%s", key,
                        userId, info);
                JsonObject body = new JsonObject();
                body.addProperty("key", key);
                body.addProperty("userid", userId);
                body.addProperty("info", info);
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(3, TimeUnit.SECONDS).build();
                Request request = new Request.Builder().url(url)
                        .post(RequestBody.create(
                                MediaType.parse("application/json"),
                                body.toString()))
                        .build();
                String json = client.newCall(request).execute().body().string();
                // String json = Requests.post(url).data(body.toJSONString())
                // .timeout(3000).text(Charset.forName("utf-8")).getBody();
                JsonObject obj = new JsonParser().parse(json).getAsJsonObject();
                if (obj != null && obj.has("text")) {
                    // if (100000 == obj.getIntValue("code")) {
                    String input = obj.get("text").getAsString();
                    if (input != null && !input.isEmpty()) {
                        return input;
                    }
                    // }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    public static String getTuringApiKey() {
        IPreferenceStore store = IMPlugin.getDefault().getPreferenceStore();
        String key = store.getString(RobotPreferencePage.TURING_KEY);
        if (key != null && !key.isEmpty()) {
            return key;
        }
        return null;
    }
    
    public static String getTuringReply(String url,
            Map<String, Object> params) {
        try {
            if (params == null) {
                return null;
            }
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS).build();
            String body = new Gson().toJson(params);
            Request request = new Request.Builder().url(url)
                    .post(RequestBody
                            .create(MediaType.parse("application/json"), body))
                    .build();
            String result = client.newCall(request).execute().body().string();
            JsonObject obj = new JsonParser().parse(result).getAsJsonObject();
            if (obj != null && obj.has("results")) {
                JsonElement ele = obj.get("results");
                JsonObject ret = null;
                if (ele instanceof JsonObject) {
                    ret = ele.getAsJsonObject();
                }
                else if (ele instanceof JsonArray) {
                    ret = ele.getAsJsonArray().get(0).getAsJsonObject();
                }
                // System.out.println("响应json" + ret);
                if (ret != null && ret.has("values")) {
                    String type = ret.get("resultType").getAsString();
                    ret = ret.getAsJsonObject("values");
                    if ("text".equals(type)) {
                        String text = ret.get("text").getAsString();
                        // text = null;
                        return text;
                    }
                    else if ("image".equals(type)) {
                        String image = ret.get("image").getAsString();
                        return image;
                    }
                    else if ("url".equals(type)) {
                        String link = ret.get("url").getAsString();
                        return link;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static void main(String[] args) {
        String url = "http://openapi.tuling123.com/openapi/api/v2";
        String key = "";
        TuringRequestV2Builder builder = new TuringRequestV2Builder(key);
        builder.setText("南京");
        builder.setUserInfo("Jamling", "Jamling", null);
        String result = getTuringReply(url, builder.build());
        System.out.println(result);
    }
    
    public static class TuringRequestV2Builder {
        public int reqType = 0;
        public String text;
        public String apiKey;
        public String userId;
        public String groupId;
        public String userIdName;
        public String city;
        public String province;
        public String street;
        
        public TuringRequestV2Builder(String apiKey) {
            this.apiKey = apiKey;
        }
        
        public TuringRequestV2Builder setText(String text) {
            this.text = text.length() > 128 ? text.substring(0, 128) : text;
            return this;
        }
        
        public TuringRequestV2Builder setUserInfo(String userId,
                String userName, String groupId) {
            this.userId = userId;
            this.userIdName = userName;
            this.groupId = groupId;
            return this;
        }
        
        public TuringRequestV2Builder setLocation(String city, String province,
                String street) {
            this.city = city;
            this.province = province;
            this.street = street;
            return this;
        }
        
        public Map<String, Object> build() {
            if (StringUtils.isEmpty(text)) {
                return null;
            }
            Map<String, Object> params = new HashMap<>();
            params.put("reqType", reqType);
            
            Map<String, Object> perception = new HashMap<>();
            params.put("perception", perception);
            
            Map<String, Object> input = new HashMap<>();
            input.put("text", text);
            perception.put("inputText", input);
            
            Map<String, Object> location = new HashMap<>();
            if (!StringUtils.isEmpty(city)) {
                location.put("city", city);
            }
            if (!StringUtils.isEmpty(province)) {
                location.put("province", province);
            }
            if (!StringUtils.isEmpty(street)) {
                location.put("street", street);
            }
            if (!location.isEmpty()) {
                Map<String, Object> self = new HashMap<>();
                self.put("location", location);
                perception.put("selfInfo", self);
            }
            
            location = new HashMap<>();
            if (!StringUtils.isEmpty(userId)) {
                location.put("userId", userId);
            }
            if (!StringUtils.isEmpty(userIdName)) {
                location.put("userIdName", userIdName);
            }
            if (!StringUtils.isEmpty(groupId)) {
                location.put("groupId", groupId);
            }
            location.put("apiKey", apiKey);
            params.put("userInfo", location);
            // System.out.println(
            // String.format("图灵请求：uid=%s,name=%s,gid=%s,text=%s,city=%s",
            // userId, userIdName, groupId, text, city));
            return params;
        }
    }
}
