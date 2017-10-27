package cn.ieclipse.smartim.views;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;

import cn.ieclipse.smartim.IMPlugin;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.model.impl.AbstractContact;

public abstract class IMContactDoubleClicker implements IDoubleClickListener {
    
    protected IMContactView fView;
    
    public IMContactDoubleClicker(IMContactView view) {
        this.fView = view;
    }
    
    @Override
    public void doubleClick(DoubleClickEvent event) {
        if (fView.getClient() == null || !fView.getClient().isLogin()) {
            return;
        }
        IStructuredSelection isel = (IStructuredSelection) event.getSelection();
        Object obj = isel.getFirstElement();
        if (obj != null) {
            if (obj instanceof AbstractContact) {
                ((AbstractContact) obj).clearUnRead();
            }
            click(obj);
        }
    }
    
    public abstract void click(Object obj);
}
