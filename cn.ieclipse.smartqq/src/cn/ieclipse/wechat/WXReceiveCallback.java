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

import cn.ieclipse.smartim.IMClientFactory;
import cn.ieclipse.smartim.IMHistoryManager;
import cn.ieclipse.smartim.IMPlugin;
import cn.ieclipse.smartim.callback.ReceiveCallback;
import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.common.Notifications;
import cn.ieclipse.smartim.model.impl.AbstractFrom;
import cn.ieclipse.smartim.model.impl.AbstractMessage;
import cn.ieclipse.smartim.preferences.SettingsPerferencePage;
import cn.ieclipse.wechat.console.WXChatConsole;
import io.github.biezhi.wechat.api.WechatClient;
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
    
    @Override
    public void onReceiveMessage(AbstractMessage message, AbstractFrom from) {
        
        if (from != null && from.getContact() != null) {
            boolean notify = IMPlugin.getDefault().getPreferenceStore()
                    .getBoolean(SettingsPerferencePage.NOTIFY_FRIEND);
            String uin = from.getContact().getUin();
            if (from instanceof GroupFrom) {
                notify = IMPlugin.getDefault().getPreferenceStore()
                        .getBoolean(SettingsPerferencePage.NOTIFY_GROUP);
            }
            
            WechatClient client = IMClientFactory.getInstance()
                    .getWechatClient();
            IMHistoryManager.getInstance().save(client, uin, message.getRaw());
            
            if (notify) {
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
