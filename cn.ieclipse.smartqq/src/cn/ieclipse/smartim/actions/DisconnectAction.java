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
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import cn.ieclipse.smartim.IMPlugin;
import cn.ieclipse.smartim.views.IMContactView;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年6月20日
 *       
 */
public class DisconnectAction extends Action {
    IMContactView contactView;
    
    public DisconnectAction(IMContactView contactView) {
        this.contactView = contactView;
        setText("Close");
        setToolTipText("Disconnect from server");
        setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
                .getImageDescriptor(ISharedImages.IMG_ELCL_STOP));
    }
    
    @Override
    public void run() {
        IMPlugin.getDefault().closeAllChat();
        IMPlugin.setEnable(true);
        if (contactView != null) {
            contactView.getClient().close();
            contactView.initContacts();
        }
    }
}
