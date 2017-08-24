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
package cn.ieclipse.smartqq.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import cn.ieclipse.smartqq.QQPlugin;
import cn.ieclipse.smartqq.console.ChatConsole;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年6月20日
 *       
 */
public class PrevChatAction extends Action {
    private ChatConsole fConsole;
    
    public PrevChatAction(ChatConsole console) {
        this.fConsole = console;
        setText("Previous Chat");
        setToolTipText("Previous chat console");
        ImageDescriptor image = PlatformUI.getWorkbench().getSharedImages()
                .getImageDescriptor(ISharedImages.IMG_TOOL_BACK_DISABLED);
        setImageDescriptor(image);
    }
    
    @Override
    public void run() {
        QQPlugin.getDefault().nextConsole(fConsole, true);
    }
}
