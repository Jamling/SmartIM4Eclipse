package cn.ieclipse.smartim.actions;

import org.eclipse.jface.action.Action;

import cn.ieclipse.smartim.htmlconsole.IMChatConsole;
import icons.SmartIcons;

public class ScrollLockAction extends IMChatAction {
    
    public ScrollLockAction(IMChatConsole console) {
        super(console, "Scroll Lock", Action.AS_CHECK_BOX);
        this.setToolTipText("Scroll Lock");
        setImageDescriptor(SmartIcons.lock);
    }
    
    @Override
    public void run() {
        fConsole.setScrollLock(isChecked());
    }
}
