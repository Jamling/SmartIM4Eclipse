/*
 * Copyright 2014-2015 ieclipse.cn.
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
package cn.ieclipse.smartqq.test;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.scienjus.smartqq.model.DiscussMessage;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年6月19日
 *      
 */
public class DiscussMessageTest {
    
    /**
     * @param args
     */
    public static void main(String[] args) throws Exception {
        String msg = "{\"result\":[{\"poll_type\":\"discu_message\",\"value\":{\"content\":[[\"font\","
                + "{\"color\":\"000000\",\"name\":\"微软雅黑\",\"size\":10,\"style\":[0,0,0]}],\"@明月\",\"\",\" 好\"],"
                + "\"did\":1240987830,\"from_uin\":1240987830,\"msg_id\":156,\"msg_type\":5,\"send_uin\":1402134857,"
                + "\"time\":1497860096,\"to_uin\":157250921}}],\"retcode\":0}";
        JSONObject json = JSONObject.parseObject(msg);
        JSONArray array = json.getJSONArray("result");
        json = array.getJSONObject(0).getJSONObject("value");
        DiscussMessage dm = new DiscussMessage(json);
        System.out.println(dm.getFont());
    }
    
}
