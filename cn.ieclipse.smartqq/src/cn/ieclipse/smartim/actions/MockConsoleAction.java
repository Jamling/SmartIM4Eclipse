/*
 * Copyright 2014-2015 ieclipse.cn.
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
package cn.ieclipse.smartim.actions;

import com.scienjus.smartqq.model.Friend;

import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.console.MockChatConsole;
import cn.ieclipse.smartim.views.IMContactView;
import cn.ieclipse.wechat.WXChatConsole;
import cn.ieclipse.wechat.WXChatConsoleMock;
import cn.ieclipse.wechat.WXContactView;
import icons.SmartIcons;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年6月20日
 *       
 */
public class MockConsoleAction extends IMPanelAction {
    
    public MockConsoleAction(IMContactView contactView) {
        super(contactView);
        setText("Mock");
        setToolTipText("Mock chat");
        setImageDescriptor(SmartIcons.test);
    }
    
    @Override
    public void run() {
        Friend f = new Friend();
        f.setUserId(System.currentTimeMillis());
        f.setMarkname("Test" + System.currentTimeMillis());
        MockChatConsole console = null;
        if (fContactView instanceof WXContactView) {
            console = new WXChatConsoleMock(f, fContactView);
        } else {
            console =new MockChatConsole(f, fContactView);
        }
        console.initMockMsg();
        fContactView.getTabbedChat().setSelection(console);
        fContactView.randBling();
    }
}
