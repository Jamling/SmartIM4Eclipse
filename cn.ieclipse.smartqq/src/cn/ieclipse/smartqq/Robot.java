package cn.ieclipse.smartqq;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Base64.Encoder;

import org.eclipse.jface.preference.IPreferenceStore;

import com.alibaba.fastjson.JSONObject;
import com.scienjus.smartqq.model.Group;
import com.scienjus.smartqq.model.GroupMessage;
import com.scienjus.smartqq.model.GroupUser;

import cn.ieclipse.smartqq.console.ChatConsole;
import cn.ieclipse.smartqq.preferences.RobotPreferencePage;
import net.dongliu.requests.Requests;

public class Robot {
    public static boolean isEnable() {
        IPreferenceStore store = QQPlugin.getDefault().getPreferenceStore();
        return (store.getBoolean(RobotPreferencePage.ROBOT_ENABLE));
    }
    
    public static void answer(GroupUser gu, GroupMessage m,
            ChatConsole console) {
        if (!isEnable()) {
            return;
        }
        IPreferenceStore store = QQPlugin.getDefault().getPreferenceStore();
        String key = store.getString(RobotPreferencePage.TURING_KEY);
        if (key != null && !key.isEmpty()) {
            String url = store.getString(RobotPreferencePage.TURING_API);
            try {
                String info = URLEncoder
                        .encode(m.getContent().replace(m.at, ""), "UTF-8");
                String param = String.format("?key=%s&userid=%s&info=%s", key,
                        m.getUserId(), info);
                String json = Requests.get(url + param).text().getBody();
                JSONObject obj = JSONObject.parseObject(json);
                if (obj != null && obj.containsKey("text")) {
                    // if (100000 == obj.getIntValue("code")) {
                    String input = obj.getString("text");
                    if (input != null && !input.isEmpty()) {
                        console.post("@" + gu.getName() + input);
                    }
                    // }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}
