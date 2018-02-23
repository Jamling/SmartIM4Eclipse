package cn.ieclipse.smartim.actions;

import org.eclipse.jface.action.Action;

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
