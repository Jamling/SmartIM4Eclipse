package cn.ieclipse.smartqq.adapter;

import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;

import com.scienjus.smartqq.model.Discuss;
import com.scienjus.smartqq.model.DiscussInfo;
import com.scienjus.smartqq.model.Friend;
import com.scienjus.smartqq.model.Group;
import com.scienjus.smartqq.model.GroupInfo;
import com.scienjus.smartqq.model.Recent;

import cn.ieclipse.smartqq.QQPlugin;

public class FriendAdapter {
    public TreeViewer viewer;
    
    public FriendAdapter(TreeViewer viewer) {
        this.viewer = viewer;
        init();
    }
    
    protected void init() {
        viewer.setContentProvider(new FriendContentProvider());
        viewer.setLabelProvider(new FriendLabelProvider());
        viewer.addDoubleClickListener(new IDoubleClickListener() {
            
            @Override
            public void doubleClick(DoubleClickEvent event) {
                if (!QQPlugin.getDefault().getClient().isLogin()) {
                    return;
                }
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
