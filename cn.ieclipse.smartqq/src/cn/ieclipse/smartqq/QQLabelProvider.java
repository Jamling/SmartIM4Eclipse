package cn.ieclipse.smartqq;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;

import com.scienjus.smartqq.client.SmartQQClient;
import com.scienjus.smartqq.model.Category;
import com.scienjus.smartqq.model.Recent;

import cn.ieclipse.smartim.IMPlugin;
import cn.ieclipse.smartim.common.LetterImageFactory;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.model.VirtualCategory;
import cn.ieclipse.smartim.views.IMContactLabelProvider;
import cn.ieclipse.smartim.views.IMContactView;

public class QQLabelProvider extends IMContactLabelProvider {
    
    public QQLabelProvider(IMContactView view) {
        super(view);
    }
    
    public String getText(Object obj) {
        SmartQQClient client = (SmartQQClient) fView.getClient();
        if (obj instanceof Category) {
            return ((Category) obj).getName();
        }
        else if (obj instanceof VirtualCategory) {
            return ((VirtualCategory<?>) obj).name;
        }
        else if (obj instanceof Recent) {
            Recent r = (Recent) obj;
            Object target = client.getRecentTarget(r);
            if (target != null) {
                return super.getText(target);
            }
            else {
                return null;
            }
        }
        return super.getText(obj);
    }
    
    @Override
    public Image getContactImage(Object obj) {
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
        else if (obj instanceof Category) {
            return IMPlugin.getSharedImage(ISharedImages.IMG_OBJ_FOLDER)
                    .createImage();
        }
        if (obj instanceof IContact) {
            IContact target = (IContact) obj;
            char ch = QQUtils.getContactChar(target);
            if (ch == 'F') {
                return LetterImageFactory.create(ch, SWT.COLOR_DARK_GREEN);
            }
            else if (ch == 'G') {
                return LetterImageFactory.create(ch, SWT.COLOR_DARK_BLUE);
            }
            else if (ch == 'D') {
                return LetterImageFactory.create(ch, SWT.COLOR_DARK_CYAN);
            }
        }
        return null;
    }
}