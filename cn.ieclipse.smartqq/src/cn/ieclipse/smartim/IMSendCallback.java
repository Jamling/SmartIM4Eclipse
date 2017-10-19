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
package cn.ieclipse.smartim;

import cn.ieclipse.smartim.callback.SendCallback;
import cn.ieclipse.smartim.console.IMChatConsole;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年10月16日
 *       
 */
public class IMSendCallback implements SendCallback {
    
    @Override
    public void onSendResult(int type, String targetId, String msg,
            boolean success, Throwable t) {
        if (!success) {
            String s = Utils.isEmpty(msg) ? ""
                    : (msg.length() > 20 ? msg.substring(0, 20) + "..." : msg);
            IMChatConsole console = IMPlugin.getDefault()
                    .getChatConsole(targetId, true);
            if (console != null) {
                console.error(s);
            }
            else {
                IMPlugin.getDefault()
                        .log(String.format("发送到%s的信息（%s）", targetId, s), t);
            }
        }
    }
    
    protected void log() {
    
    }
}
