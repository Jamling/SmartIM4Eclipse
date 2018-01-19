package cn.ieclipse.wechat;

import org.eclipse.jface.preference.IPreferenceStore;

import cn.ieclipse.smartim.IMClientFactory;
import cn.ieclipse.smartim.IMPlugin;
import cn.ieclipse.smartim.IMRobotCallback;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.model.impl.AbstractFrom;
import cn.ieclipse.smartim.model.impl.AbstractMessage;
import cn.ieclipse.smartim.preferences.RobotPreferencePage;
import cn.ieclipse.util.StringUtils;
import cn.ieclipse.wechat.console.WXChatConsole;
import cn.ieclipse.wechat.views.WXContactView;
import io.github.biezhi.wechat.api.WechatClient;
import io.github.biezhi.wechat.model.GroupFrom;
import io.github.biezhi.wechat.model.User;
import io.github.biezhi.wechat.model.UserFrom;
import io.github.biezhi.wechat.model.WechatMessage;

public class WXRobotCallback extends IMRobotCallback {
    
    private WXChatConsole console;
    private WechatClient client;
    private WXContactView fContactView;
    
    public WXRobotCallback(WXContactView fContactView) {
        this.fContactView = fContactView;
    }
    
    @Override
    public void onReceiveMessage(AbstractMessage message, AbstractFrom from) {
        if (!isEnable()) {
            return;
        }
        client = getClient();
        if (!client.isLogin()) {
            return;
        }
        try {
            WechatMessage m = (WechatMessage) message;
            if (m.MsgType == WechatMessage.MSGTYPE_TEXT) {
                console = IMPlugin.getDefault().findConsole(WXChatConsole.class,
                        from.getContact(), false);
                answer(from, m);
            }
            else if (m.MsgType == WechatMessage.MSGTYPE_SYS) {
                if (!StringUtils.isEmpty(m.Content)
                        && from instanceof GroupFrom) {
                    String c = m.Content;
                    if (c.contains("加入了群聊")) {
                        String n = c.replaceFirst(".*\"(.+)\".*", "$1");
                        GroupFrom gf = (GroupFrom) from;
                        IPreferenceStore store = IMPlugin.getDefault()
                                .getPreferenceStore();
                        String robotName = store
                                .getString(RobotPreferencePage.ROBOT_NAME);
                        String welcome = store
                                .getString(RobotPreferencePage.GROUP_WELCOME);
                        if (welcome != null && !welcome.isEmpty()
                                && gf != null) {
                            String input = welcome;
                            input = input.replaceAll("{memo}", "");
                            input = input.replaceAll("{user}", "@" + n + " ");
                            if (console != null) {
                                console.post(robotName + input);
                            }
                            else {
                                send(from, robotName + input);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            IMPlugin.getDefault().log("机器人回应异常", e);
        }
    }
    
    @Override
    public void onContactChanged(IContact contact) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void onReceiveError(Throwable e) {
        // TODO Auto-generated method stub
        
    }
    
    public void answer(AbstractFrom from, WechatMessage m) {
        
        IPreferenceStore store = IMPlugin.getDefault().getPreferenceStore();
        String robotName = store.getString(RobotPreferencePage.ROBOT_NAME);
        // auto reply friend
        if (from instanceof UserFrom) {
            if (store.getBoolean(RobotPreferencePage.FRIEND_REPLY_ANY)) {
                String reply = getTuringReply(String.valueOf(m.FromUserName),
                        m.getText().toString());
                if (reply != null) {
                    String input = robotName + reply;
                    if (console == null) {
                        send(from, input);
                    }
                    else {
                        console.post(input);
                    }
                }
            }
        }
        else if (from instanceof GroupFrom) {
            GroupFrom gf = (GroupFrom) from;
            // newbie
            if (from.isNewbie()) {
                String welcome = store
                        .getString(RobotPreferencePage.GROUP_WELCOME);
                if (welcome != null && !welcome.isEmpty() && gf != null) {
                    String input = welcome;
                    if (gf.getMember() != null) {
                        input = input.replaceAll("{memo}", "");
                        input = input.replaceAll("{user}",
                                gf.getMember().getName());
                    }
                    if (console != null) {
                        console.post(robotName + input);
                    }
                    else {
                        send(from, robotName + input);
                    }
                }
            } // end newbie
              // @
            if (atMe(from, m)) {
                String n = "@" + getClient().getAccount().getName();
                //String n1 = "@" + gf.getContact()
                String msg = m.getText().toString().replaceAll(n, "").trim();
                msg = msg.replaceAll(" ", "").trim();
                String reply = getTuringReply(String.valueOf(m.FromUserName),
                        msg);
                if (reply != null) {
                    String input = robotName + "@" + from.getMember().getName()
                            + SEP + reply;
                    if (console != null) {
                        console.post(input);
                    }
                    else {
                        send(from, input);
                    }
                }
                return;
            } // end @
              // replay any
            if (store.getBoolean(RobotPreferencePage.GROUP_REPLY_ANY)) {
                if (from.isNewbie() || isMySend(m.FromUserName)) {
                    return;
                }
                if (console == null) {
                    return;
                }
                
                if (from instanceof UserFrom) {
                    return;
                }
                else if (from instanceof GroupFrom) {
                    if (((GroupFrom) from).getMember().isUnknown()) {
                        return;
                    }
                }
                
                String reply = getTuringReply(String.valueOf(m.FromUserName),
                        m.getText().toString());
                if (reply != null) {
                    String input = robotName + "@" + from.getName() + SEP
                            + reply;
                    if (console != null) {
                        console.post(input);
                    }
                }
            } // end any
        } // end group
    }
    
    private boolean atMe(AbstractFrom from, WechatMessage m) {
        String text = (String) m.getText();
        if (text.contains("@" + getClient().getAccount().getName())) {
            return true;
        }
        return false;
    }
    
    private User getAccount() {
        IContact me = client.getAccount();
        return (User) me;
    }
    
    private void send(AbstractFrom from, String message) {
        WechatMessage msg = client.createMessage(0, message, from.getContact());
        client.sendMessage(msg, from.getContact());
    }
    
    private WechatClient getClient() {
        return (WechatClient) IMClientFactory.getInstance().getWechatClient();
    }
    
    private boolean isMySend(String uin) {
        User me = getAccount();
        if (me != null && me.getUin().equals(uin)) {
            return true;
        }
        return false;
    }
}
