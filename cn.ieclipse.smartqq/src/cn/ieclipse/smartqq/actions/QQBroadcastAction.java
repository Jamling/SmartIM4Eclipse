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
package cn.ieclipse.smartqq.actions;

import org.eclipse.swt.widgets.Shell;

import cn.ieclipse.smartim.actions.BroadcastAction;
import cn.ieclipse.smartim.views.IMContactView;
import cn.ieclipse.smartqq.views.QQBroadcastDialog;
import cn.ieclipse.smartqq.views.QQContactView;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年10月19日
 *       
 */
public class QQBroadcastAction extends BroadcastAction {
    QQContactView contactView;
    
    public QQBroadcastAction(IMContactView contactView) {
        super(contactView);
        this.contactView = (QQContactView) contactView;
        setText("QQ消息群发");
    }
    
    @Override
    public void run() {
        if (contactView != null) {
            QQBroadcastDialog dialog = new QQBroadcastDialog(new Shell(),
                    (QQContactView) contactView);
            dialog.open();
        }
    }
}
