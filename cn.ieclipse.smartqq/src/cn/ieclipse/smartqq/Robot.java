package cn.ieclipse.smartqq;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import org.eclipse.jface.preference.IPreferenceStore;

import com.alibaba.fastjson.JSONObject;
import com.scienjus.smartqq.model.DefaultMessage;
import com.scienjus.smartqq.model.DiscussFrom;
import com.scienjus.smartqq.model.DiscussInfo;
import com.scienjus.smartqq.model.DiscussMessage;
import com.scienjus.smartqq.model.DiscussUser;
import com.scienjus.smartqq.model.FriendFrom;
import com.scienjus.smartqq.model.GroupFrom;
import com.scienjus.smartqq.model.GroupInfo;
import com.scienjus.smartqq.model.GroupMessage;
import com.scienjus.smartqq.model.GroupUser;
import com.scienjus.smartqq.model.MessageFrom;
import com.scienjus.smartqq.model.UserInfo;

import cn.ieclipse.smartqq.console.ChatConsole;
import cn.ieclipse.smartqq.preferences.RobotPreferencePage;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

public class Robot {
    private static final String SEP = " ";
    
    public static boolean isEnable() {
        IPreferenceStore store = QQPlugin.getDefault().getPreferenceStore();
        return (store.getBoolean(RobotPreferencePage.ROBOT_ENABLE));
    }
    
    public static boolean atMe(MessageFrom from, DefaultMessage m) {
        if (m.getAts() != null) {
            if (m instanceof GroupMessage) {
                GroupInfo info = ((GroupFrom) from).getGroup();
                UserInfo me = QQPlugin.getDefault().getClient()
                        .getAccountInfo();
                long uin = Long.parseLong(me.getUin());
                GroupUser me2 = info.getGroupUser(uin);
                if (m.hasAt(me2.getName()) || m.hasAt(me.getNick())) {
                    return true;
                }
            }
            
            else if (m instanceof DiscussMessage) {
                DiscussInfo info = ((DiscussFrom) from).getDiscuss();
                UserInfo me = QQPlugin.getDefault().getClient()
                        .getAccountInfo();
                long uin = Long.parseLong(me.getUin());
                DiscussUser me2 = info.getDiscussUser(uin);
                if (m.hasAt(me2.getName()) || m.hasAt(me.getNick())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static void answer(MessageFrom from, DefaultMessage m,
            ChatConsole console) {
        if (!isEnable()) {
            return;
        }
        
        IPreferenceStore store = QQPlugin.getDefault().getPreferenceStore();
        String robotName = store.getString(RobotPreferencePage.ROBOT_NAME);
        // auto reply friend
        if (from instanceof FriendFrom
                && store.getBoolean(RobotPreferencePage.FRIEND_REPLY_ANY)) {
            String reply = getTuringReply(String.valueOf(m.getUserId()),
                    m.getContent());
            if (reply != null) {
                String input = robotName + reply;
                if (console == null) {
                    QQPlugin.getDefault().getClient()
                            .sendMessageToFriend(m.getUserId(), input);
                }
                else {
                    console.post(input);
                }
            }
        }
        
        // newbie
        if (from.isNewbie() && from instanceof GroupFrom) {
            GroupFrom gf = (GroupFrom) from;
            String welcome = store.getString(RobotPreferencePage.GROUP_WELCOME);
            if (welcome != null && !welcome.isEmpty()) {
                GroupInfo info = gf.getGroup();
                GroupUser gu = gf.getGroupUser();
                String input = welcome;
                if (gu != null) {
                    input = input.replaceAll("{user}", gu.getName());
                }
                if (info != null) {
                    input = input.replaceAll("{memo}", info.getMemo());
                }
                if (console != null) {
                    console.post(robotName + input);
                }
                else {
                    QQPlugin.getDefault().getClient().sendMessageToGroup(
                            gf.getGroup().getGid(), robotName + input);
                }
            }
            return;
        }
        // @
        if (atMe(from, m)) {
            String msg = m.getContent(true);
            String reply = getTuringReply(String.valueOf(m.getUserId()), msg);
            if (reply != null) {
                String input = robotName + "@" + from.getName() + SEP + reply;
                if (console != null) {
                    console.post(input);
                }
                else {
                    if (m instanceof GroupMessage) {
                        QQPlugin.getDefault().getClient().sendMessageToGroup(
                                ((GroupMessage) m).getGroupId(), input);
                    }
                    else if (m instanceof DiscussMessage) {
                        QQPlugin.getDefault().getClient().sendMessageToDiscuss(
                                ((DiscussMessage) m).getDiscussId(), input);
                    }
                }
            }
            return;
        }
        // replay any
        if (store.getBoolean(RobotPreferencePage.GROUP_REPLY_ANY)) {
            if (from.isNewbie() || isMySend(m.getUserId())) {
                return;
            }
            if (console == null) {
                return;
            }
            
            if (from instanceof FriendFrom) {
                return;
            }
            else if (from instanceof GroupFrom) {
                if (((GroupFrom) from).getGroupUser().isUnknown()) {
                    return;
                }
            }
            else if (from instanceof DiscussFrom) {
                if (((DiscussFrom) from).getDiscussUser().isUnknown()) {
                    return;
                }
            }
            
            String reply = getTuringReply(String.valueOf(m.getUserId()),
                    m.getContent());
            if (reply != null) {
                String input = robotName + "@" + from.getName() + SEP + reply;
                if (console != null) {
                    console.post(input);
                }
            }
        }
    }
    
    private static boolean isMySend(long uin) {
        UserInfo me = QQPlugin.getDefault().getClient().getAccountInfo();
        if (me != null && me.getUin().equals(String.valueOf(uin))) {
            return true;
        }
        return false;
    }
    
    private static String getTuringReply(String userId, String message) {
        IPreferenceStore store = QQPlugin.getDefault().getPreferenceStore();
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
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(3, TimeUnit.SECONDS).build();
                Request request = new Request.Builder().url(url)
                        .post(RequestBody.create(
                                MediaType.parse("application/json"),
                                body.toJSONString()))
                        .build();
                String json = client.newCall(request).execute().body().string();
                // String json = Requests.post(url).data(body.toJSONString())
                // .timeout(3000).text(Charset.forName("utf-8")).getBody();
                JSONObject obj = JSONObject.parseObject(json);
                if (obj != null && obj.containsKey("text")) {
                    // if (100000 == obj.getIntValue("code")) {
                    String input = obj.getString("text");
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
