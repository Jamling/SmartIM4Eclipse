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

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;

import cn.ieclipse.smartqq.QQPlugin;
import cn.ieclipse.smartqq.views.BroadcastDialog;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年8月22日
 *       
 */
public class BroadcastAction extends Action {
    
    public BroadcastAction() {
        setText("&BroadCast");
        setToolTipText("Broadcast message to SmartQQ group/discuss/friends");
        setImageDescriptor(QQPlugin.getImageDescriptor("icons/broadcast.png"));
    }
    
    @Override
    public void run() {
        if (QQPlugin.getDefault().getClient().isLogin()) {
            BroadcastDialog dialog = new BroadcastDialog(new Shell());
            dialog.open();
        }
    }
    
}
