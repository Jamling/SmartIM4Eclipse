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

import org.eclipse.jface.action.Action;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.scienjus.smartqq.model.Friend;

import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.htmlconsole.MockChatConsole;
import cn.ieclipse.smartim.views.IMContactView;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年6月20日
 *       
 */
public class MockConsoleAction extends Action {
    IMContactView contactView;
    
    public MockConsoleAction(IMContactView contactView) {
        this.contactView = contactView;
        setText("Mock");
        setToolTipText("Mock chat");
        setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
                .getImageDescriptor(ISharedImages.IMG_DEC_FIELD_WARNING));
    }
    
    @Override
    public void run() {
        Friend f = new Friend();
        f.setUserId(System.currentTimeMillis());
        f.setMarkname("Test" + System.currentTimeMillis());
        MockChatConsole console = new MockChatConsole(f, contactView);
        contactView.getTabbedChat().setSelection(console);
        contactView.randBling();
        String msg = IMUtils.formatHtmlMsg(System.currentTimeMillis(), "明月",
                "我的未来不是梦http://www.baidu.com咕咕");
        console.write(msg);
    }
}
