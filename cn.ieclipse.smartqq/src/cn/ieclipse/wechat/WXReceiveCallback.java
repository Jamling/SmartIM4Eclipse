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
package cn.ieclipse.wechat;

import cn.ieclipse.smartim.IMHistoryManager;
import cn.ieclipse.smartim.IMPlugin;
import cn.ieclipse.smartim.callback.ReceiveCallback;
import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.common.Notifications;
import cn.ieclipse.smartim.model.impl.AbstractFrom;
import cn.ieclipse.smartim.model.impl.AbstractMessage;
import cn.ieclipse.smartim.preferences.SettingsPerferencePage;
import cn.ieclipse.wechat.console.WXChatConsole;
import cn.ieclipse.wechat.views.WXContactView;
import io.github.biezhi.wechat.api.WechatClient;
import io.github.biezhi.wechat.model.Contact;
import io.github.biezhi.wechat.model.GroupFrom;
import io.github.biezhi.wechat.model.UserFrom;
import io.github.biezhi.wechat.model.WechatMessage;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年10月14日
 *       
 */
public class WXReceiveCallback implements ReceiveCallback {
    private WXChatConsole lastConsole;
    private WXContactView fContactView;
    
    public WXReceiveCallback(WXContactView fContactView) {
        this.fContactView = fContactView;
    }
    
    @Override
    public void onReceiveMessage(AbstractMessage message, AbstractFrom from) {
        
        if (from != null && from.getContact() != null) {
            boolean unknown = false;
            boolean notify = IMPlugin.getDefault().getPreferenceStore()
                    .getBoolean(SettingsPerferencePage.NOTIFY_FRIEND);
            String uin = from.getContact().getUin();
            Contact contact = (Contact) from.getContact();
            contact.setLastMessage(message);
            if (from instanceof GroupFrom) {
                GroupFrom gf = (GroupFrom) from;
                unknown = gf.getMember() == null || gf.getMember().isUnknown();
                notify = IMPlugin.getDefault().getPreferenceStore()
                        .getBoolean(SettingsPerferencePage.NOTIFY_GROUP);
            }
            else {
                unknown = from.getMember() == null;
            }
            WechatClient client = fContactView.getClient();
            if (!unknown) {
                IMHistoryManager.getInstance().save(client, uin,
                        message.getRaw());
            }
            
            // IMHistoryManager.getInstance().save(client, uin,
            // message.getRaw());
            
            if (notify) {
                boolean hide = unknown && !IMPlugin.getDefault()
                        .getPreferenceStore()
                        .getBoolean(SettingsPerferencePage.NOTIFY_UNKNOWN);
                try {
                    hide = hide || from.getMember().getUin().equals(
                            fContactView.getClient().getAccount().getUin());
                } catch (Exception e) {
                    IMPlugin.getDefault().log("", e);
                }
                if (hide) {
                    // don't notify
                }
                else {
                    CharSequence content = (from instanceof UserFrom)
                            ? message.getText()
                            : from.getName() + ":" + message.getText();
                    Notifications.notify(WXChatConsole.class, from.getContact(),
                            from.getContact().getName(), content);
                }
            }
            
            WXChatConsole console = IMPlugin.getDefault()
                    .findConsole(WXChatConsole.class, from.getContact(), false);
            if (console != null) {
                lastConsole = console;
                String name = from.getName();
                String msg = null;
                if (message instanceof WechatMessage) {
                    WechatMessage m = (WechatMessage) message;
                    msg = IMUtils.formatMsg(m.CreateTime, name, m.getText());
                }
                console.write(msg);
            }
            else {
                contact.increaceUnRead();
            }
            
            fContactView.notifyUpdateContacts(0, false);
        }
    }
    
    @Override
    public void onReceiveError(Throwable e) {
        if (e == null) {
            return;
        }
        if (lastConsole != null) {
            lastConsole.error(e);
        }
        else {
            IMPlugin.getDefault().log("微信接收异常", e);
        }
    }
    
}
