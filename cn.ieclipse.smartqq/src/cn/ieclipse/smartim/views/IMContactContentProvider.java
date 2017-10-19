package cn.ieclipse.smartim.views;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;

public abstract class IMContactContentProvider
        implements IStructuredContentProvider, ITreeContentProvider {
    protected boolean check = false;
    protected IMContactView fView;
    
    public IMContactContentProvider(IMContactView view, boolean check) {
        this.fView = view;
        this.check = check;
    }
    
    public IMContactContentProvider(IMContactView view) {
        this.fView = view;
    }
    
    public IMContactContentProvider(boolean check) {
        this.check = check;
    }
}