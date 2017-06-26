package cn.ieclipse.smartqq;

import java.net.URLEncoder;

import org.eclipse.jface.preference.IPreferenceStore;

import com.alibaba.fastjson.JSONObject;
import com.scienjus.smartqq.model.Group;
import com.scienjus.smartqq.model.GroupFrom;
import com.scienjus.smartqq.model.GroupInfo;
import com.scienjus.smartqq.model.GroupMessage;
import com.scienjus.smartqq.model.GroupUser;
import com.scienjus.smartqq.model.UserInfo;

import cn.ieclipse.smartqq.console.ChatConsole;
import cn.ieclipse.smartqq.preferences.RobotPreferencePage;
import net.dongliu.requests.Requests;

public class Robot {
    public static boolean isEnable() {
        IPreferenceStore store = QQPlugin.getDefault().getPreferenceStore();
        return (store.getBoolean(RobotPreferencePage.ROBOT_ENABLE));
    }
    
    public static boolean atMe(GroupMessage m) {
        if (m.at != null && !m.at.isEmpty()) {
            try {
                Group g = QQPlugin.getDefault().getClient()
                        .getGroup(m.getGroupId());
                GroupInfo info = QQPlugin.getDefault().getClient()
                        .getGroupInfo(g);
                UserInfo me = QQPlugin.getDefault().getClient()
                        .getAccountInfo();
                if (info != null && me != null) {
                    long uin = Long.parseLong(me.getUin());
                    GroupUser me2 = info.getGroupUser(uin);
                    if (me2 != null) {
                        if (m.at.equals("@" + me2.getName())) {
                            return true;
                        }
                    }
                }
                
                else if (me != null) {
                    if (m.at.equals("@" + me.getNick())) {
                        return true;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    
    public static void answer(GroupFrom from, GroupMessage m,
            ChatConsole console) {
        if (!isEnable()) {
            return;
        }
        IPreferenceStore store = QQPlugin.getDefault().getPreferenceStore();
        String robotName = store.getString(RobotPreferencePage.ROBOT_NAME);
        if (from.isNewbie()) {
            String welcome = store.getString(RobotPreferencePage.GROUP_WELCOME);
            if (welcome != null && !welcome.isEmpty()) {
                Group g = QQPlugin.getDefault().getClient()
                        .getGroup(m.getGroupId());
                GroupInfo info = QQPlugin.getDefault().getClient()
                        .getGroupInfo(g);
                GroupUser gu = QQPlugin.getDefault().getClient()
                        .getGroupUser(m);
                String input = welcome;
                if (gu != null) {
                    input = input.replaceAll("{user}", gu.getName());
                }
                if (info != null) {
                    input = input.replaceAll("{memo}", info.getMemo());
                }
                console.post(robotName + input);
            }
        }
        if (!atMe(m)) {
            return;
        }
        
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
                        console.post(robotName + "@" + from.getName() + input);
                    }
                    // }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}
