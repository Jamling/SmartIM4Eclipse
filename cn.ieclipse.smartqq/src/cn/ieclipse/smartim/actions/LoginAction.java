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
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import cn.ieclipse.smartim.IMPlugin;
import cn.ieclipse.smartim.dialogs.LoginDialog;
import cn.ieclipse.smartim.views.IMContactView;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年6月20日
 *       
 */
public class LoginAction extends Action {
    private IMContactView fView;
    
    public LoginAction(IMContactView view) {
        super();
        setText("Login");
        setToolTipText("Click to show qrcode");
        // setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
        // .getImageDescriptor(ISharedImages.IMG_ELCL_SYNCED));
        setImageDescriptor(IMPlugin.getImageDescriptor("icons/qrcode.png"));
        this.fView = view;
    }
    
    @Override
    public void run() {
        if (fView != null) {
            Shell shell = fView.getSite().getShell();
            boolean ok = true;
            if (fView.getClient().isLogin()) {
                ok = MessageDialog.openConfirm(shell, null,
                        "您已处于登录状态，确定要重新登录吗？");
            }
            if (ok) {
                LoginDialog dialog = new LoginDialog(shell);
                dialog.setClient(fView.getClient());
                dialog.open();
            }
            fView.initContacts();
        }
    }
}
