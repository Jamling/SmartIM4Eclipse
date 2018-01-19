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
import java.util.concurrent.TimeUnit;

import org.eclipse.jface.preference.IPreferenceStore;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import cn.ieclipse.smartim.callback.ModificationCallback;
import cn.ieclipse.smartim.callback.ReceiveCallback;
import cn.ieclipse.smartim.preferences.RobotPreferencePage;
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
public abstract class IMRobotCallback implements ReceiveCallback, ModificationCallback {
    public static final String SEP = " ";
    
    public static boolean isEnable() {
        IPreferenceStore store = IMPlugin.getDefault().getPreferenceStore();
        return (store.getBoolean(RobotPreferencePage.ROBOT_ENABLE));
    }
    
    public static String getRobotName() {
        IPreferenceStore store = IMPlugin.getDefault().getPreferenceStore();
        String robotName = store.getString(RobotPreferencePage.ROBOT_NAME);
        return robotName;
    }
    
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
}
