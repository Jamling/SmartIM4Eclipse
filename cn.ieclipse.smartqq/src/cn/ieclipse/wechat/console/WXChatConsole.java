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
package cn.ieclipse.wechat.console;

import org.eclipse.jface.resource.ImageDescriptor;

import cn.ieclipse.smartim.IMClientFactory;
import cn.ieclipse.smartim.IMPlugin;
import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.console.IMChatConsole;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.model.impl.AbstractFrom;
import io.github.biezhi.wechat.api.WechatClient;
import io.github.biezhi.wechat.model.WechatMessage;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年10月16日
 *       
 */
public class WXChatConsole extends IMChatConsole {
    private static ImageDescriptor icon = IMPlugin
            .getImageDescriptor("icons/wechat.png");
            
    public WXChatConsole(String id, String name, String uin) {
        super(id, name, uin);
        setImageDescriptor(icon);
    }
    
    public WXChatConsole(IContact contact) {
        super(contact);
        setImageDescriptor(icon);
    }
    
    @Override
    public void post(String msg) {
        WechatClient client = getClient();
        if (client.isLogin() && contact != null) {
            WechatMessage m = client.createMessage(0, msg, contact);
            client.sendMessage(m, contact);
        }
        else {
            error("发送失败，客户端异常（可能已断开连接或找不到联系人）");
        }
    }
    
    // @Override
    // public String getHistoryFile() {
    // return EncodeUtils.getMd5(uin);
    // }
    
    @Override
    public void loadHistory(String raw) {
        if (IMUtils.isMySendMsg(raw)) {
            write(raw);
            return;
        }
        WechatMessage m = (WechatMessage) getClient().handleMessage(raw);
        AbstractFrom from = getClient().getFrom(m);
        String name = from == null ? "未知用户" : from.getName();
        String msg = IMUtils.formatMsg(m.getTime(), name, m.getText());
        write(msg);
    }
    
    @Override
    public WechatClient getClient() {
        return (WechatClient) IMClientFactory.getInstance().getWechatClient();
    }
    
    @Override
    public boolean hideMyInput() {
        return false;
    }
    
    @Override
    public boolean enableUpload() {
        return false;
    }
}
