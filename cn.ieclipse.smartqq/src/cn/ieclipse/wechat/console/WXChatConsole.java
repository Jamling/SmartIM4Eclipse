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

import cn.ieclipse.smartim.IMClientFactory;
import cn.ieclipse.smartim.SmartClient;
import cn.ieclipse.smartim.console.IMChatConsole;
import cn.ieclipse.smartim.model.IContact;
import io.github.biezhi.wechat.api.WechatClient;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年10月16日
 *       
 */
public class WXChatConsole extends IMChatConsole {
    
    public WXChatConsole(String id, String name, String uin) {
        super(id, name, uin);
    }
    
    public WXChatConsole(IContact contact) {
        super(contact);
    }
    
    @Override
    public void post(String msg) {
        getClient().sendMessage(msg, uin);
    }
    
    @Override
    public WechatClient getClient() {
        return (WechatClient) IMClientFactory.getInstance().getWechatClient();
    }
    
}
