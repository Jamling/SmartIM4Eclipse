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
package cn.ieclipse.smartim.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.IntegerFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import cn.ieclipse.smartim.IMPlugin;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年10月25日
 *       
 */
public class SettingsPerferencePage extends FieldEditorPreferencePage
        implements IWorkbenchPreferencePage {
        
    public static final String NOTIFY_ENABLE = IMPlugin.PLUGIN_ID
            + ".notify.enable"; //$NON-NLS-1$
            
    public static final String NOTIFY_DISMISS = IMPlugin.PLUGIN_ID
            + ".notify.dismiss"; //$NON-NLS-1$
            
    public static final String NOTIFY_GROUP = IMPlugin.PLUGIN_ID
            + ".notify.group"; //$NON-NLS-1$
            
    public static final String NOTIFY_FRIEND = IMPlugin.PLUGIN_ID
            + ".notify.friend"; //$NON-NLS-1$
            
    public static final String NOTIFY_UNREAD = IMPlugin.PLUGIN_ID
            + ".notify.unread"; //$NON-NLS-1$
            
    public SettingsPerferencePage() {
        super(FLAT);
        setPreferenceStore(IMPlugin.getDefault().getPreferenceStore());
        setDescription(Messages.SettingsPerferencePage_desc);
    }
    
    @Override
    protected void createFieldEditors() {
        addField(new BooleanFieldEditor(NOTIFY_ENABLE,
                Messages.SettingsPerferencePage_notify_enable,
                getFieldEditorParent()));
        addField(new BooleanFieldEditor(NOTIFY_GROUP,
                Messages.SettingsPerferencePage_notify_group,
                getFieldEditorParent()));
        addField(new BooleanFieldEditor(NOTIFY_FRIEND,
                Messages.SettingsPerferencePage_notify_friend,
                getFieldEditorParent()));
        addField(new IntegerFieldEditor(NOTIFY_DISMISS,
                Messages.SettingsPerferencePage_notify_dismiss,
                getFieldEditorParent()));
        addField(new BooleanFieldEditor(NOTIFY_UNREAD,
                Messages.SettingsPerferencePage_notify_unread,
                getFieldEditorParent()));
    }
    
    @Override
    public void init(IWorkbench workbench) {
        // TODO Auto-generated method stub
        
    }
}
