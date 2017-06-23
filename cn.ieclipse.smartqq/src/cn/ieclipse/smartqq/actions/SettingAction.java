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
package cn.ieclipse.smartqq.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.PreferencesUtil;

import cn.ieclipse.smartqq.preferences.HotKeyPreferencePage;
import cn.ieclipse.smartqq.preferences.RobotPreferencePage;
import cn.ieclipse.smartqq.preferences.SmartQQPreferencePage;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年6月20日
 *       
 */
public class SettingAction extends Action {
    public SettingAction() {
        setText("Settings");
        setToolTipText("Settings & Helps");
        setImageDescriptor(PlatformUI.getWorkbench().getSharedImages()
                .getImageDescriptor(ISharedImages.IMG_LCL_LINKTO_HELP));
    }
    
    @Override
    public void run() {
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                .getShell();
        String pageId = SmartQQPreferencePage.class.getName();
        String[] ids = { pageId, HotKeyPreferencePage.class.getName(),
                SmartQQPreferencePage.class.getName(),
                RobotPreferencePage.class.getName() };
        PreferencesUtil.createPreferenceDialogOn(shell, pageId, ids, null)
                .open();
    }
}
