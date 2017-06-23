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
package cn.ieclipse.smartqq.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

import cn.ieclipse.smartqq.QQPlugin;

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
        IPreferenceStore store = QQPlugin.getDefault().getPreferenceStore();
        
        store.setDefault(HotKeyPreferencePage.KEY_SEND, "Ctrl + CR");
        store.setDefault(HotKeyPreferencePage.KEY_INPUT, "Alt + S");
        store.setDefault(HotKeyPreferencePage.KEY_INPUT_ESC, "ESC");
        store.setDefault(HotKeyPreferencePage.KEY_NEXT, "ARROW_RIGHT");
        store.setDefault(HotKeyPreferencePage.KEY_PREV, "ARROW_LEFT");
        store.setDefault(HotKeyPreferencePage.KEY_HIDE, "Alt + H");
        store.setDefault(HotKeyPreferencePage.KEY_HIDE_CLOSE, "Alt + C");
        
        store.setDefault(RobotPreferencePage.ROBOT_ENABLE, false);
        store.setDefault(RobotPreferencePage.TURING_API,
                "http://www.tuling123.com/openapi/api");
        store.setDefault(RobotPreferencePage.TURING_KEY, "");
    }
    
}
