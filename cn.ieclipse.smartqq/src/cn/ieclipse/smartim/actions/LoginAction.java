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

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import cn.ieclipse.smartim.dialogs.LoginDialog;
import cn.ieclipse.smartim.views.IMContactView;
import icons.SmartIcons;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年6月20日
 *       
 */
public class LoginAction extends IMPanelAction {
    
    public LoginAction(IMContactView view) {
        super(view);
        setText("Login");
        setToolTipText("Click to show qrcode");
        setImageDescriptor(SmartIcons.signin);
    }
    
    @Override
    public void run() {
        if (fContactView != null) {
            Shell shell = fContactView.getSite().getShell();
            boolean ok = true;
            if (fContactView.getClient().isLogin()) {
                ok = MessageDialog.openConfirm(shell, null,
                        "您已处于登录状态，确定要重新登录吗？");
            }
            if (ok) {
                LoginDialog dialog = new LoginDialog(shell);
                dialog.setClient(fContactView.getClient());
                dialog.open();
            }
            fContactView.initContacts();
        }
    }
}
