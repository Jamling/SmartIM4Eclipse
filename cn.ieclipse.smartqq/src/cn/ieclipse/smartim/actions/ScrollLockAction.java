package cn.ieclipse.smartim.actions;

import java.awt.event.ActionEvent;

import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import cn.ieclipse.smartim.IMPlugin;
import cn.ieclipse.smartim.htmlconsole.IMChatConsole;

public class ScrollLockAction extends Action {
    IMChatConsole console;
    
    public ScrollLockAction(IMChatConsole console) {
        super("", Action.AS_CHECK_BOX);
        this.setToolTipText("Scroll Lock");
        this.console = console;
        setImageDescriptor(IMPlugin.getImageDescriptor("icons/lock_co.png"));
    }
    
    @Override
    public void run() {
        console.setScrollLock(isChecked());
    }
}
