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
import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.common.Notifications;
import cn.ieclipse.smartim.exception.HttpException;
import cn.ieclipse.smartim.exception.LogicException;
import cn.ieclipse.smartim.htmlconsole.IMChatConsole;
import cn.ieclipse.smartim.views.IMContactView;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年10月16日
 *       
 */
public class IMSendCallback implements SendCallback {
    protected IMContactView fContactView;
    
    public IMSendCallback(IMContactView contactView) {
        this.fContactView = contactView;
    }
    
    @Override
    public void onSendResult(int type, String targetId, CharSequence msg,
            boolean success, Throwable t) {
        if (success) {
            onSuccess(type, targetId, msg);
        }
        else {
            onFailure(type, targetId, msg, t);
        }
    }
    
    protected void onSuccess(int type, String targetId, CharSequence msg) {
        fContactView.notifyUpdateContacts(0, true);
    }
    
    protected void onFailure(int type, String targetId, CharSequence msg,
            Throwable t) {
        String s = IMUtils.isEmpty(msg) ? ""
                : (msg.length() > 20 ? msg.toString().substring(0, 20) + "..."
                        : msg.toString());
        String code = "";
        if (t != null) {
            if (t instanceof LogicException) {
                code = String.format("api code=%d",
                        ((LogicException) t).getCode());
            }
            else if (t instanceof HttpException) {
                code = String.format("http code=%d",
                        ((HttpException) t).getCode());
            }
        }
        IMChatConsole console = fContactView.findConsoleById(targetId,
                true);
        if (console != null) {
            console.error(String.format("%s 发送失败！%s", msg, code));
        }
        else {
            IMPlugin.getDefault()
                    .log(String.format("发送到%s的信息（%s）", targetId, msg), t);
            Notifications.notify("发送失败", String.format("%s 发送失败！%s", s, code));
        }
    }
}
