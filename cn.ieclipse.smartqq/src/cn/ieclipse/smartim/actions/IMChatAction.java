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

import cn.ieclipse.smartim.console.IMChatConsole;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2018年2月24日
 *       
 */
public class IMChatAction extends Action {
    protected IMChatConsole fConsole;
    
    public IMChatAction(IMChatConsole console) {
        this.fConsole = console;
    }
    
    public IMChatAction(IMChatConsole console, String name, int style) {
        super(name, style);
        this.fConsole = console;
    }
}
