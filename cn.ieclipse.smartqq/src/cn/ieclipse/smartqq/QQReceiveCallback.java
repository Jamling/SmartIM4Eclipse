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
package cn.ieclipse.smartqq;

import com.scienjus.smartqq.model.QQMessage;

import cn.ieclipse.smartim.IMPlugin;
import cn.ieclipse.smartim.Utils;
import cn.ieclipse.smartim.callback.ReceiveCallback;
import cn.ieclipse.smartim.model.impl.AbstractFrom;
import cn.ieclipse.smartim.model.impl.AbstractMessage;
import cn.ieclipse.smartqq.console.QQChatConsole;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年10月16日
 *       
 */
public class QQReceiveCallback implements ReceiveCallback {
    private QQChatConsole lastConsole;
    
    @Override
    public void onReceiveMessage(AbstractMessage message, AbstractFrom from) {
        QQChatConsole console = IMPlugin.getDefault()
                .findConsole(QQChatConsole.class, from.getContact(), false);
        if (console != null) {
            lastConsole = console;
            String name = from.getContact().getName();
            String msg = null;
            if (message instanceof QQMessage) {
                QQMessage m = (QQMessage) message;
                msg = Utils.formatMsg(m.getTime(), name, m.getContent());
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
            IMPlugin.getDefault().log("SmartQQ 接收异常", e);
        }
    }
    
}
