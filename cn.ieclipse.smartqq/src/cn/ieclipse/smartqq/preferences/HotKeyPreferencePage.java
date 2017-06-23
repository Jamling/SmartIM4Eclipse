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

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import cn.ieclipse.smartqq.QQPlugin;

public class HotKeyPreferencePage extends FieldEditorPreferencePage
        implements IWorkbenchPreferencePage {
        
    public static final String KEY_SEND = QQPlugin.PLUGIN_ID + ".key.send";
    public static final String KEY_INPUT = QQPlugin.PLUGIN_ID + ".key.input";
    public static final String KEY_NEXT = QQPlugin.PLUGIN_ID + ".key.next";
    public static final String KEY_PREV = QQPlugin.PLUGIN_ID + ".key.prev";
    public static final String KEY_INPUT_ESC = QQPlugin.PLUGIN_ID
            + ".key.input.esc";
    public static final String KEY_HIDE = QQPlugin.PLUGIN_ID + ".key.hide";
    public static final String KEY_HIDE_CLOSE = QQPlugin.PLUGIN_ID
            + ".key.hide_close";
            
    public HotKeyPreferencePage() {
        super(FLAT);
        setPreferenceStore(QQPlugin.getDefault().getPreferenceStore());
        setDescription(Messages.HotKeyPreferencePage_desc);
    }
    
    /**
     * Creates the field editors. Field editors are abstractions of the common
     * GUI blocks needed to manipulate various types of preferences. Each field
     * editor knows how to save and restore itself.
     */
    public void createFieldEditors() {
        HotKeyFieldEditor send = new HotKeyFieldEditor(KEY_SEND, "Send message",
                getFieldEditorParent());
        addField(send);
        
        addField(new HotKeyFieldEditor(KEY_INPUT, "Active input",
                getFieldEditorParent()));
        addField(new HotKeyFieldEditor(KEY_INPUT_ESC, "Hide input",
                getFieldEditorParent()));
        addField(new HotKeyFieldEditor(KEY_NEXT, "Next chat",
                getFieldEditorParent()));
        addField(new HotKeyFieldEditor(KEY_PREV, "Previous chat",
                getFieldEditorParent()));
        addField(new HotKeyFieldEditor(KEY_HIDE, "Hide chat",
                getFieldEditorParent()));
        addField(new HotKeyFieldEditor(KEY_HIDE_CLOSE, "Hide & close chat",
                getFieldEditorParent()));
    }
    
    @Override
    public void init(IWorkbench workbench) {
    }
}