package cn.ieclipse.smartqq.adapter;

import java.util.List;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

import com.scienjus.smartqq.model.Category;
import com.scienjus.smartqq.model.Discuss;
import com.scienjus.smartqq.model.DiscussInfo;
import com.scienjus.smartqq.model.Friend;
import com.scienjus.smartqq.model.Group;
import com.scienjus.smartqq.model.GroupInfo;
import com.scienjus.smartqq.model.Recent;

import cn.ieclipse.smartqq.QQPlugin;
import cn.ieclipse.smartqq.views.LetterImageFactory;

public class FriendAdapter {
    public TreeViewer viewer;
    
    public FriendAdapter(TreeViewer viewer) {
        this.viewer = viewer;
        init();
    }
    
    protected void init() {
        viewer.setContentProvider(new FriendContentProvider(this));
        viewer.setLabelProvider(new FriendLabelProvider());
        viewer.addDoubleClickListener(new IDoubleClickListener() {
            
            @Override
            public void doubleClick(DoubleClickEvent event) {
                IStructuredSelection isel = (IStructuredSelection) event
                        .getSelection();
                Object obj = isel.getFirstElement();
                if (obj instanceof Friend) {
                    Friend f = (Friend) obj;
                    QQPlugin.getDefault().findConsole(f, true);
                }
                else if (obj instanceof Group) {
                    Group g = (Group) obj;
                    QQPlugin.getDefault().findConsole(g, true);
                }
                else if (obj instanceof Discuss) {
                    Discuss g = (Discuss) obj;
                    QQPlugin.getDefault().findConsole(g, true);
                }
                else if (obj instanceof Recent) {
                    Recent r = (Recent) obj;
                    Object src = null;
                    if (r.getType() == 0) {
                        Friend f = FriendAdapter.getFriend(r.getUin());
                        src = f;
                    }
                    else if (r.getType() == 1) {
                        Group g = FriendAdapter.getGroup(r.getUin());
                        src = g;
                    }
                    else if (r.getType() == 2) {
                        Discuss d = FriendAdapter.getDiscuss(r.getUin());
                        src = d;
                    }
                    if (src != null) {
                        QQPlugin.getDefault().findConsole(src, true);
                    }
                }
            }
        });
    }
    
    public void update() {
    
    }
    
    public static Friend getFriend(long uin) {
        return QQPlugin.getDefault().getClient().getFriend(uin);
    }
    
    public static Group getGroup(long uin) {
        return QQPlugin.getDefault().getClient().getGroup(uin);
    }
    
    public static Discuss getDiscuss(long uin) {
        return QQPlugin.getDefault().getClient().getDiscuss(uin);
    }
    
    public static GroupInfo getGroupInfo(Group group) {
        return QQPlugin.getDefault().getClient().getGroupInfo(group);
    }
    
    public static DiscussInfo getDiscussInfo(Discuss discuss) {
        return QQPlugin.getDefault().getClient().getDiscussInfo(discuss);
    }
    
    public static String getName(Object obj) {
        return QQPlugin.getDefault().getClient().getName(obj);
    }
}

class FriendContentProvider
        implements IStructuredContentProvider, ITreeContentProvider {
    FriendAdapter adapter;
    
    public FriendContentProvider(FriendAdapter adapter) {
        this.adapter = adapter;
    }
    
    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof Category) {
            return ((Category) parentElement).getFriends().toArray();
        }
        return null;
    }
    
    @Override
    public Object getParent(Object element) {
        // TODO Auto-generated method stub
        return null;
    }
    
    @Override
    public boolean hasChildren(Object element) {
        if (element instanceof Category) {
            return true;
        }
        return false;
    }
    
    @Override
    public Object[] getElements(Object inputElement) {
        if ("recent".equals(inputElement)) {
            List<Recent> recents = QQPlugin.getDefault().getClient()
                    .getRecentList();
            return recents == null ? null : recents.toArray();
        }
        else if ("group".equals(inputElement)) {
            List<Group> groups = QQPlugin.getDefault().getClient()
                    .getGroupList();
            return groups == null ? null : groups.toArray();
        }
        else if ("discuss".equals(inputElement)) {
            List<Discuss> groups = QQPlugin.getDefault().getClient()
                    .getDiscussList();
            return groups == null ? null : groups.toArray();
        }
        List<Category> categories = QQPlugin.getDefault().getClient()
                .getFriendListWithCategory();
        return categories == null ? null : categories.toArray();
    }
}

class FriendLabelProvider extends LabelProvider {
    
    public String getText(Object obj) {
        if (obj instanceof Category) {
            return ((Category) obj).getName();
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
