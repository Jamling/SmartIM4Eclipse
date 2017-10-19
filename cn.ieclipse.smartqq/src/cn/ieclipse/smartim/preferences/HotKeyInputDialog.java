package cn.ieclipse.smartim.preferences;

import org.eclipse.jface.dialogs.IInputValidator;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class HotKeyInputDialog extends InputDialog {
    
    public HotKeyInputDialog(Shell parentShell, String dialogTitle,
            String dialogMessage, String initialValue,
            IInputValidator validator) {
        super(parentShell, dialogTitle, dialogMessage, initialValue, validator);
    }
    
    public HotKeyInputDialog(Shell parentShell, HotKeyFieldEditor editor) {
        this(parentShell, "Input key",
                "Set hot key for '" + editor.getLabelText() + "'",
                editor.getTextControl(null).getText(), null);
    }
    
    @Override
    protected Control createDialogArea(Composite parent) {
        Control control = super.createDialogArea(parent);
        getText().addKeyListener(new KeyListener() {
            
            @Override
            public void keyReleased(KeyEvent e) {
            
            }
            
            @Override
            public void keyPressed(KeyEvent e) {
                e.doit = false;
                getText().setText(HotKeyFieldEditor.keyEvent2String(e));
            }
        });
        return control;
    }
    
    @Override
    protected Button createButton(Composite parent, int id, String label,
            boolean defaultButton) {
        return super.createButton(parent, id, label, false);
    }
}
