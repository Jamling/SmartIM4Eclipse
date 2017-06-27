package cn.ieclipse.smartqq;

import java.net.URLEncoder;
import java.nio.charset.Charset;

import org.eclipse.jface.preference.IPreferenceStore;

import com.alibaba.fastjson.JSONObject;
import com.scienjus.smartqq.model.DiscussFrom;
import com.scienjus.smartqq.model.DiscussMessage;
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
            return;
        }
        if (atMe(m)) {
            String msg = m.getContent().replace(m.at, "");
            reply(store, console, String.valueOf(m.getUserId()), msg,
                    robotName + "@" + from.getName());
            return;
        }
        if (store.getBoolean(RobotPreferencePage.GROUP_REPLY_ANY)) {
            if (from.isNewbie() || from.isUnknow() || isMySend(m.getUserId())) {
                return;
            }
            reply(store, console, String.valueOf(m.getUserId()), m.getContent(),
                    robotName + "@" + from.getName());
        }
    }
    
    public static void answer(DiscussFrom from, DiscussMessage m,
            ChatConsole console) {
        if (!isEnable()) {
            return;
        }
        
        IPreferenceStore store = QQPlugin.getDefault().getPreferenceStore();
        String robotName = store.getString(RobotPreferencePage.ROBOT_NAME);
        // if (atMe(m)) {
        // String msg = m.getContent().replace(m.at, "");
        // reply(store, console, String.valueOf(m.getUserId()), msg,
        // robotName + "@" + from.getName());
        // return;
        // }
        if (store.getBoolean(RobotPreferencePage.GROUP_REPLY_ANY)) {
            if (from.isNewbie() || from.isUnknow() || isMySend(m.getUserId())) {
                return;
            }
            
            reply(store, console, String.valueOf(m.getUserId()), m.getContent(),
                    robotName + "@" + from.getName());
        }
    }
    
    private static boolean isMySend(long uin) {
        UserInfo me = QQPlugin.getDefault().getClient().getAccountInfo();
        if (me != null && me.getUin().equals(String.valueOf(uin))) {
            return true;
        }
        return false;
    }
    
    private static void reply(IPreferenceStore store, ChatConsole console,
            String userId, String message, String prefix) {
        String key = store.getString(RobotPreferencePage.TURING_KEY);
        if (key != null && !key.isEmpty()) {
            String url = store.getString(RobotPreferencePage.TURING_API);
            try {
                String info = URLEncoder.encode(message, "UTF-8");
                String param = String.format("?key=%s&userid=%s&info=%s", key,
                        userId, info);
                JSONObject body = new JSONObject();
                body.put("key", key);
                body.put("userid", userId);
                body.put("info", info);
                String json = Requests.post(url).data(body.toJSONString())
                        .text(Charset.forName("utf-8")).getBody();
                JSONObject obj = JSONObject.parseObject(json);
                if (obj != null && obj.containsKey("text")) {
                    // if (100000 == obj.getIntValue("code")) {
                    String input = obj.getString("text");
                    if (input != null && !input.isEmpty()) {
                        if (prefix != null) {
                            input = prefix + input;
                        }
                        console.post(input);
                    }
                    // }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
