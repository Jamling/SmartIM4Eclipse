package cn.ieclipse.smartim.views;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.model.VirtualCategory;

public abstract class IMContactLabelProvider extends LabelProvider {
    
    protected IMContactView fView;
    
    public IMContactLabelProvider(IMContactView view) {
        this.fView = view;
    }
    
    public String getText(Object obj) {
        if (obj instanceof IContact) {
            return ((IContact) obj).getName();
        }
        else if (obj instanceof VirtualCategory) {
            return ((VirtualCategory<?>) obj).name;
        }
        return null;
    }
    
    public Image getImage(Object obj) {
        String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
        if (obj instanceof VirtualCategory)
            imageKey = ISharedImages.IMG_OBJ_FOLDER;
        return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
    }
}