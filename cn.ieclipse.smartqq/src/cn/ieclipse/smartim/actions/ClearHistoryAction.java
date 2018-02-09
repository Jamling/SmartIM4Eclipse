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
package cn.ieclipse.smartim.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.internal.console.IInternalConsoleConstants;

import cn.ieclipse.smartim.htmlconsole.IMChatConsole;

/**
 * 清除聊天记录
 * 
 * @author Jamling
 * @date 2018年2月8日
 *       
 */
public class ClearHistoryAction extends Action {
    protected IMChatConsole fConsole;
    
    public ClearHistoryAction(IMChatConsole console) {
        super("", Action.AS_PUSH_BUTTON);
        this.fConsole = console;
        setText("Clear");
        setToolTipText("Clear history");
        setImageDescriptor(ConsolePlugin
                .getImageDescriptor(IInternalConsoleConstants.IMG_ELCL_CLEAR));
    }
    
    @Override
    public void run() {
        if (fConsole != null) {
            fConsole.clearHistories();
        }
    }
}
