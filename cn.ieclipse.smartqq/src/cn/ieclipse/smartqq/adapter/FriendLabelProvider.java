package cn.ieclipse.smartqq.adapter;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.scienjus.smartqq.model.Category;
import com.scienjus.smartqq.model.Discuss;
import com.scienjus.smartqq.model.Friend;
import com.scienjus.smartqq.model.Group;
import com.scienjus.smartqq.model.Recent;

import cn.ieclipse.smartqq.views.LetterImageFactory;

public class FriendLabelProvider extends LabelProvider {
    
    public String getText(Object obj) {
        if (obj instanceof Category) {
            return ((Category) obj).getName();
        }
        else if (obj instanceof VirtualCategory) {
            return ((VirtualCategory) obj).name;
        }
        else if (obj instanceof Recent) {
            Recent r = (Recent) obj;
            if (r.getType() == 0) {
                Friend f = FriendAdapter.getFriend(r.getUin());
                if (f != null) {
                    return FriendAdapter.getName(f);
                }
            }
            else if (r.getType() == 1) {
                Group g = FriendAdapter.getGroup(r.getUin());
                if (g != null) {
                    return g.getName();
                }
            }
            else if (r.getType() == 2) {
                Discuss d = FriendAdapter.getDiscuss(r.getUin());
                if (d != null) {
                    return d.getName();
                }
            }
        }
        return FriendAdapter.getName(obj);
    }
    
    public Image getImage(Object obj) {
        if (obj instanceof Recent) {
            int type = ((Recent) obj).getType();
            if (type == 0) {
                return LetterImageFactory.create('F', SWT.COLOR_DARK_GREEN);
            }
            else if (type == 1) {
                return LetterImageFactory.create('G', SWT.COLOR_DARK_BLUE);
            }
            else if (type == 2) {
                return LetterImageFactory.create('D', SWT.COLOR_DARK_CYAN);
            }
        }
        String imageKey = ISharedImages.IMG_OBJ_ELEMENT;
        if (obj instanceof Category)
            imageKey = ISharedImages.IMG_OBJ_FOLDER;
        return PlatformUI.getWorkbench().getSharedImages().getImage(imageKey);
    }
}