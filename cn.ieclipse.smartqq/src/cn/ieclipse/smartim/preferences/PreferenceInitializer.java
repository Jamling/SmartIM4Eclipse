/*
 * Copyright 2010 the original author or authors.
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

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import cn.ieclipse.smartim.IMPlugin;

/**
 * Class used to initialize default preference values.
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
     * initializeDefaultPreferences()
     */
    public void initializeDefaultPreferences() {
        IPreferenceStore store = IMPlugin.getDefault().getPreferenceStore();
        
        store.setDefault(HotKeyPreferencePage.KEY_SEND, "CR"); //$NON-NLS-1$
        store.setDefault(HotKeyPreferencePage.KEY_INPUT, "CR"); //$NON-NLS-1$
        store.setDefault(HotKeyPreferencePage.KEY_INPUT_ESC, "ESC"); //$NON-NLS-1$
        store.setDefault(HotKeyPreferencePage.KEY_NEXT, "ARROW_RIGHT"); //$NON-NLS-1$
        store.setDefault(HotKeyPreferencePage.KEY_PREV, "ARROW_LEFT"); //$NON-NLS-1$
        store.setDefault(HotKeyPreferencePage.KEY_HIDE, "Alt + H"); //$NON-NLS-1$
        store.setDefault(HotKeyPreferencePage.KEY_HIDE_CLOSE, "Alt + TabComposite"); //$NON-NLS-1$
        
        store.setDefault(RobotPreferencePage.ROBOT_ENABLE, false);
        store.setDefault(RobotPreferencePage.ROBOT_NAME,
                Messages.PreferenceInitializer_robot_name);
        store.setDefault(RobotPreferencePage.TURING_API,
                "http://www.tuling123.com/openapi/api"); //$NON-NLS-1$
        store.setDefault(RobotPreferencePage.TURING_KEY, ""); //$NON-NLS-1$
        store.setDefault(RobotPreferencePage.GROUP_WELCOME,
                Messages.PreferenceInitializer_robot_group_welcome);
        store.setDefault(RobotPreferencePage.GROUP_REPLY_ANY, false);
        store.setDefault(RobotPreferencePage.FRIEND_REPLY_ANY, false);
        store.setDefault(RobotPreferencePage.ROBOT_EMPTY, "");
        
        store.setDefault(QiniuPerferencePage.ENABLE, false);
        store.setDefault(QiniuPerferencePage.TS, false);
        store.setDefault(QiniuPerferencePage.AK, "");
        store.setDefault(QiniuPerferencePage.SK, "");
        store.setDefault(QiniuPerferencePage.ZONE, "autoZone");
        store.setDefault(QiniuPerferencePage.BUCKET, "");
        store.setDefault(QiniuPerferencePage.DOMAIN, "");
        
        store.setDefault(SettingsPerferencePage.NOTIFY_ENABLE, true);
        store.setDefault(SettingsPerferencePage.NOTIFY_GROUP, false);
        store.setDefault(SettingsPerferencePage.NOTIFY_FRIEND, true);
        store.setDefault(SettingsPerferencePage.NOTIFY_DISMISS, 5);
        store.setDefault(SettingsPerferencePage.NOTIFY_UNREAD, true);
        store.setDefault(SettingsPerferencePage.NOTIFY_UNKNOWN, false);
        store.setDefault(SettingsPerferencePage.HIDE_MY_INPUT, true);
        store.setDefault(SettingsPerferencePage.LOG_HISTORY, true);
    }
    
}
