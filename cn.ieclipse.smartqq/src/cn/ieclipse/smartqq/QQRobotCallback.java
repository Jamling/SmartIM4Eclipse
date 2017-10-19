package cn.ieclipse.smartqq;

import org.eclipse.jface.preference.IPreferenceStore;

import com.scienjus.smartqq.client.SmartQQClient;
import com.scienjus.smartqq.model.QQMessage;
import com.scienjus.smartqq.model.DiscussFrom;
import com.scienjus.smartqq.model.DiscussInfo;
import com.scienjus.smartqq.model.DiscussMessage;
import com.scienjus.smartqq.model.DiscussUser;
import com.scienjus.smartqq.model.FriendFrom;
import com.scienjus.smartqq.model.GroupFrom;
import com.scienjus.smartqq.model.GroupInfo;
import com.scienjus.smartqq.model.GroupMessage;
import com.scienjus.smartqq.model.GroupUser;
import com.scienjus.smartqq.model.UserInfo;

import cn.ieclipse.smartim.IMClientFactory;
import cn.ieclipse.smartim.IMPlugin;
import cn.ieclipse.smartim.IMRobotCallback;
import cn.ieclipse.smartim.model.IFrom;
import cn.ieclipse.smartim.model.impl.AbstractFrom;
import cn.ieclipse.smartim.model.impl.AbstractMessage;
import cn.ieclipse.smartim.preferences.RobotPreferencePage;
import cn.ieclipse.smartqq.console.QQChatConsole;

public class QQRobotCallback extends IMRobotCallback {
    
    private QQChatConsole console;
    
    @Override
    public void onReceiveMessage(AbstractMessage message, AbstractFrom from) {
        if (!isEnable()) {
            return;
        }
        try {
            console = IMPlugin.getDefault().findConsole(QQChatConsole.class,
                    from.getContact(), false);
            answer(from, (QQMessage) message);
        } catch (Exception e) {
            IMPlugin.getDefault().log("机器人回应异常", e);
        }
    }
    
    @Override
    public void onReceiveError(Throwable e) {
        // TODO Auto-generated method stub
        
    }
    
    public void answer(AbstractFrom from, QQMessage m) {
        
        IPreferenceStore store = IMPlugin.getDefault().getPreferenceStore();
        String robotName = store.getString(RobotPreferencePage.ROBOT_NAME);
        // auto reply friend
        if (from instanceof FriendFrom
                && store.getBoolean(RobotPreferencePage.FRIEND_REPLY_ANY)) {
            String reply = getTuringReply(String.valueOf(m.getUserId()),
                    m.getContent());
            if (reply != null) {
                String input = robotName + reply;
                if (console == null) {
                    getClient().sendMessageToFriend(m.getUserId(), input);
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
                    getClient().sendMessageToGroup(gf.getGroup().getId(),
                            robotName + input);
                }
            }
            return;
        }
        // @
        if (atMe(from, m)) {
            String msg = m.getContent(true);
            String reply = getTuringReply(String.valueOf(m.getUserId()), msg);
            if (reply != null) {
                String input = robotName + "@" + from.getContact().getName()
                        + SEP + reply;
                if (console != null) {
                    console.post(input);
                }
                else {
                    if (m instanceof GroupMessage) {
                        getClient().sendMessageToGroup(
                                ((GroupMessage) m).getGroupId(), input);
                    }
                    else if (m instanceof DiscussMessage) {
                        getClient().sendMessageToDiscuss(
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
                String input = robotName + "@" + from.getContact().getName()
                        + SEP + reply;
                if (console != null) {
                    console.post(input);
                }
            }
        }
    }
    
    private boolean atMe(IFrom from, QQMessage m) {
        if (m.getAts() != null) {
            if (m instanceof GroupMessage) {
                GroupInfo info = ((GroupFrom) from).getGroup();
                UserInfo me = getAccount();
                long uin = Long.parseLong(me.getUin());
                GroupUser me2 = info.getGroupUser(uin);
                if (m.hasAt(me2.getName()) || m.hasAt(me.getNick())) {
                    return true;
                }
            }
            
            else if (m instanceof DiscussMessage) {
                DiscussInfo info = ((DiscussFrom) from).getDiscuss();
                UserInfo me = getAccount();
                long uin = Long.parseLong(me.getUin());
                DiscussUser me2 = info.getDiscussUser(uin);
                if (m.hasAt(me2.getName()) || m.hasAt(me.getNick())) {
                    return true;
                }
            }
        }
        return false;
    }
    
    private UserInfo getAccount() {
        UserInfo me = (UserInfo) IMClientFactory.getInstance().getQQClient()
                .getAccount();
        return me;
    }
    
    private SmartQQClient getClient() {
        return (SmartQQClient) IMClientFactory.getInstance().getQQClient();
    }
    
    private boolean isMySend(long uin) {
        UserInfo me = getAccount();
        if (me != null && me.getUin().equals(String.valueOf(uin))) {
            return true;
        }
        return false;
    }
}
