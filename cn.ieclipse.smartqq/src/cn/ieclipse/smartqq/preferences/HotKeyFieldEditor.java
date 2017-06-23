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
package cn.ieclipse.smartqq.preferences;

import org.eclipse.jface.preference.StringButtonFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Composite;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年6月21日
 *       
 */
public class HotKeyFieldEditor extends StringButtonFieldEditor {
    
    public HotKeyFieldEditor(String name, String labelText, Composite parent) {
        super(name, labelText, parent);
    }
    
    @Override
    protected void doFillIntoGrid(Composite parent, int numColumns) {
        super.doFillIntoGrid(parent, numColumns);
        
        getTextControl().addKeyListener(new KeyListener() {
            
            @Override
            public void keyReleased(KeyEvent e) {
            }
            
            @Override
            public void keyPressed(KeyEvent e) {
                e.doit = false;
                getTextControl().setText(keyEvent2String(e));
            }
        });
    }
    
    public static String keyEvent2String(KeyEvent e) {
        StringBuilder sb = new StringBuilder();
        if ((e.stateMask & SWT.CTRL) != 0) {
            sb.append("Ctrl + ");
        }
        if ((e.stateMask & SWT.SHIFT) != 0) {
            sb.append("Shift + ");
        }
        if ((e.stateMask & SWT.ALT) != 0) {
            sb.append("Alt + ");
        }
        sb.append(keyCode(e.keyCode));
        return sb.toString();
    }
    
    static String keyCode(int keyCode) {
        switch (keyCode) {
            
            /* Keyboard and Mouse Masks */
            case SWT.ALT:
                return "ALT";
            case SWT.SHIFT:
                return "SHIFT";
            case SWT.CONTROL:
                return "CONTROL";
            case SWT.COMMAND:
                return "COMMAND";
                
            /* Non-Numeric Keypad Keys */
            case SWT.ARROW_UP:
                return "ARROW_UP";
            case SWT.ARROW_DOWN:
                return "ARROW_DOWN";
            case SWT.ARROW_LEFT:
                return "ARROW_LEFT";
            case SWT.ARROW_RIGHT:
                return "ARROW_RIGHT";
            case SWT.PAGE_UP:
                return "PAGE_UP";
            case SWT.PAGE_DOWN:
                return "PAGE_DOWN";
            case SWT.HOME:
                return "HOME";
            case SWT.END:
                return "END";
            case SWT.INSERT:
                return "INSERT";
                
            /* Virtual and Ascii Keys */
            case SWT.BS:
                return "BS";
            case SWT.CR:
                return "CR";
            case SWT.DEL:
                return "DEL";
            case SWT.ESC:
                return "ESC";
            case SWT.LF:
                return "LF";
            case SWT.TAB:
                return "TAB";
            case SWT.SPACE:
                return "SPACE";
                
            /* Functions Keys */
            case SWT.F1:
                return "F1";
            case SWT.F2:
                return "F2";
            case SWT.F3:
                return "F3";
            case SWT.F4:
                return "F4";
            case SWT.F5:
                return "F5";
            case SWT.F6:
                return "F6";
            case SWT.F7:
                return "F7";
            case SWT.F8:
                return "F8";
            case SWT.F9:
                return "F9";
            case SWT.F10:
                return "F10";
            case SWT.F11:
                return "F11";
            case SWT.F12:
                return "F12";
            case SWT.F13:
                return "F13";
            case SWT.F14:
                return "F14";
            case SWT.F15:
                return "F15";
                
            /* Numeric Keypad Keys */
            case SWT.KEYPAD_ADD:
                return "KEYPAD_ADD";
            case SWT.KEYPAD_SUBTRACT:
                return "KEYPAD_SUBTRACT";
            case SWT.KEYPAD_MULTIPLY:
                return "KEYPAD_MULTIPLY";
            case SWT.KEYPAD_DIVIDE:
                return "KEYPAD_DIVIDE";
            case SWT.KEYPAD_DECIMAL:
                return "KEYPAD_DECIMAL";
            case SWT.KEYPAD_CR:
                return "KEYPAD_CR";
            case SWT.KEYPAD_0:
                return "KEYPAD_0";
            case SWT.KEYPAD_1:
                return "KEYPAD_1";
            case SWT.KEYPAD_2:
                return "KEYPAD_2";
            case SWT.KEYPAD_3:
                return "KEYPAD_3";
            case SWT.KEYPAD_4:
                return "KEYPAD_4";
            case SWT.KEYPAD_5:
                return "KEYPAD_5";
            case SWT.KEYPAD_6:
                return "KEYPAD_6";
            case SWT.KEYPAD_7:
                return "KEYPAD_7";
            case SWT.KEYPAD_8:
                return "KEYPAD_8";
            case SWT.KEYPAD_9:
                return "KEYPAD_9";
            case SWT.KEYPAD_EQUAL:
                return "KEYPAD_EQUAL";
                
            /* Other keys */
            case SWT.CAPS_LOCK:
                return "CAPS_LOCK";
            case SWT.NUM_LOCK:
                return "NUM_LOCK";
            case SWT.SCROLL_LOCK:
                return "SCROLL_LOCK";
            case SWT.PAUSE:
                return "PAUSE";
            case SWT.BREAK:
                return "BREAK";
            case SWT.PRINT_SCREEN:
                return "PRINT_SCREEN";
            case SWT.HELP:
                return "HELP";
        }
        return String.valueOf(Character.toUpperCase((char) keyCode));
    }
    
    @Override
    protected String changePressed() {
        HotKeyInputDialog dialog = new HotKeyInputDialog(getShell(),
                "Set hot key for " + getLabelText(), "Please press key",
                getTextControl().getText(), null);
        if (dialog.open() == 0) {
            getTextControl().setText(dialog.getValue());
        }
        return null;
    }
}
