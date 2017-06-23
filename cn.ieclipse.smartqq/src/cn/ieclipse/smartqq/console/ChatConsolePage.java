package cn.ieclipse.smartqq.console;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.TextConsolePage;
import org.eclipse.ui.dialogs.PreferencesUtil;

import cn.ieclipse.smartqq.QQPlugin;
import cn.ieclipse.smartqq.preferences.HotKeyFieldEditor;
import cn.ieclipse.smartqq.preferences.HotKeyPreferencePage;

public class ChatConsolePage extends TextConsolePage {
    private ChatConsole fConsole;
    
    public ChatConsolePage(ChatConsole console, IConsoleView view) {
        super(console, view);
        fConsole = console;
    }
    
    @Override
    public void createControl(final Composite parent) {
        super.createControl(parent);
        
        final StyledText text = getViewer().getTextWidget();
        text.addKeyListener(new KeyListener() {
            
            @Override
            public void keyReleased(KeyEvent e) {
                String key = HotKeyFieldEditor.keyEvent2String(e);
                IPreferenceStore store = QQPlugin.getDefault()
                        .getPreferenceStore();
                if (key.equals(
                        store.getString(HotKeyPreferencePage.KEY_INPUT))) {
                    fConsole.activeInput();
                }
                else if (key.equals(
                        store.getString(HotKeyPreferencePage.KEY_NEXT))) {
                    QQPlugin.getDefault().nextConsole(fConsole, true);
                }
                else if (key.equals(
                        store.getString(HotKeyPreferencePage.KEY_PREV))) {
                    QQPlugin.getDefault().nextConsole(fConsole, false);
                }
                else if (key.equals(
                        store.getString(HotKeyPreferencePage.KEY_HIDE))) {
                    fConsole.toggleHide();
                }
                else if (key.equals(
                        store.getString(HotKeyPreferencePage.KEY_HIDE_CLOSE))) {
                    fConsole.toggleClose();
                }
                e.doit = false;
            }
            
            @Override
            public void keyPressed(KeyEvent e) {
            
            }
        });
    }
}
