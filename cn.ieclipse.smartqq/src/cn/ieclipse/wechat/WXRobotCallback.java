package cn.ieclipse.wechat;

import java.util.Map;

import org.eclipse.jface.preference.IPreferenceStore;

import cn.ieclipse.smartim.IMClientFactory;
import cn.ieclipse.smartim.IMHistoryManager;
import cn.ieclipse.smartim.IMPlugin;
import cn.ieclipse.smartim.IMRobotCallback;
import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.model.impl.AbstractFrom;
import cn.ieclipse.smartim.model.impl.AbstractMessage;
import cn.ieclipse.smartim.preferences.RobotPreferencePage;
import cn.ieclipse.util.StringUtils;
import cn.ieclipse.wechat.console.WXChatConsole;
import cn.ieclipse.wechat.views.WXContactView;
import io.github.biezhi.wechat.api.WechatClient;
import io.github.biezhi.wechat.model.Contact;
import io.github.biezhi.wechat.model.GroupFrom;
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
        if (from.isOut()) {
            return;
        }
        client = getClient();
        if (!client.isLogin()) {
            return;
        }
        try {
            WechatMessage m = (WechatMessage) message;
            if (m.MsgType == WechatMessage.MSGTYPE_TEXT) {
                console = (WXChatConsole) fContactView
                        .findConsoleById(from.getContact().getUin(), false);
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
                            input = input.replace("{memo}", "");
                            input = input.replace("{user}", "@" + n + " ");
                            if (console != null) {
                                console.send(robotName + input);
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
        String robotName = getRobotName();
        // auto reply friend
        if (from instanceof UserFrom) {
            if (store.getBoolean(RobotPreferencePage.FRIEND_REPLY_ANY)) {
                Contact contact = (Contact) from.getContact();
                if (contact.isSpecial() || contact.is3rdApp()) {
                    return;
                }
                String reply = getReply(m.getText().toString(), contact, null);
                if (reply != null) {
                    String input = robotName + reply;
                    send(from, input);
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
                        input = input.replace("{memo}", "");
                        input = input.replace("{user}",
                                gf.getMember().getName());
                    }
                    input = robotName + input;
                    send(from, input);
                }
            } // end newbie
              // @
            String name = getMyGroupName(from, m);
            String text = m.getText().toString();
            if (text.contains("@" + name)) {
                text = text.replace("@" + name, "");
                String reply = getReply(text, gf.getMember(), gf.getName());
                if (reply != null) {
                    String input = robotName + "@" + from.getMember().getName()
                            + SEP + reply;
                    send(from, input);
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
                
                String reply = getReply(text, gf.getMember(), gf.getName());
                if (reply != null) {
                    String input = robotName + "@" + from.getName() + SEP
                            + reply;
                    send(null, input);
                }
            } // end any
        } // end group
    }
    
    private String getMyGroupName(AbstractFrom from, WechatMessage m) {
        Contact c = (Contact) from.getContact();
        Contact gu = c.getMember(getAccount().getUin());
        String name;
        if (gu != null) {
            name = gu.getName();
        }
        else {
            name = getAccount().getName();
        }
        return name;
    }
    
    private Map<String, Object> getParams(String text, Contact contact,
            String groupId) {
        String key = getTuringApiKey();
        if (StringUtils.isEmpty(key)) {
            return null;
        }
        TuringRequestV2Builder builder = new TuringRequestV2Builder(key);
        builder.setText(text);
        String uid = encodeUid(contact.getName());
        String uname = contact.getName();
        String gid = groupId == null ? null : encodeUid(groupId);
        builder.setUserInfo(uid, uname, gid);
        builder.setLocation(contact.City, contact.Province, null);
        return builder.build();
    }
    
    private String getReply(String text, Contact contact, String groupId) {
        Map<String, Object> params = getParams(text, contact, groupId);
        if (params != null) {
            return getTuringReply(TURING_API_V2, params);
        }
        return null;
    }
    
    private boolean atMe(AbstractFrom from, WechatMessage m) {
        String text = (String) m.getText();
        Contact c = (Contact) from.getContact();
        Contact gu = c.getMember(getAccount().getUin());
        String name;
        if (gu != null) {
            name = gu.getName();
        }
        else {
            name = getAccount().getName();
        }
        if (text.contains("@" + name)) {
            return true;
        }
        return false;
    }
    
    private Contact getAccount() {
        IContact me = client.getAccount();
        return (Contact) me;
    }
    
    private void send(AbstractFrom from, String message) {
        if (console != null) {
            console.send(message);
        }
        else if (from != null) {
            WechatMessage msg = client.createMessage(0, message,
                    from.getContact());
            client.sendMessage(msg, from.getContact());
            String name = getAccount().getName();
            String log = IMUtils.formatHtmlMyMsg(System.currentTimeMillis(),
                    name, message);
            IMHistoryManager.getInstance().save(client,
                    from.getContact().getUin(), log);
        }
    }
    
    private WechatClient getClient() {
        return (WechatClient) IMClientFactory.getInstance().getWechatClient();
    }
    
    private boolean isMySend(String uin) {
        Contact me = getAccount();
        if (me != null && me.getUin().equals(uin)) {
            return true;
        }
        return false;
    }
}
