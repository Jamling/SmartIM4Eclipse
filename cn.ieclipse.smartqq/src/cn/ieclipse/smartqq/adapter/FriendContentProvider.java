package cn.ieclipse.smartqq.adapter;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

import com.scienjus.smartqq.model.Category;
import com.scienjus.smartqq.model.Discuss;
import com.scienjus.smartqq.model.Group;
import com.scienjus.smartqq.model.Recent;

import cn.ieclipse.smartqq.QQPlugin;

public class FriendContentProvider
        implements IStructuredContentProvider, ITreeContentProvider {
    private boolean check = false;
    
    public FriendContentProvider() {
        this.check = false;
    }
    
    public FriendContentProvider(boolean check) {
        this.check = check;
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
        else if (parentElement instanceof VirtualCategory) {
            return ((VirtualCategory) parentElement).getChildren();
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
        else if (element instanceof VirtualCategory) {
            return ((VirtualCategory) element).hasChildren();
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
            if (check) {
                return new Object[] { new VirtualCategory<>("Group", groups) };
            }
            return groups == null ? null : groups.toArray();
        }
        else if ("discuss".equals(inputElement)) {
            List<Discuss> groups = QQPlugin.getDefault().getClient()
                    .getDiscussList();
            if (check) {
                return new Object[] {
                        new VirtualCategory<>("Discuss", groups) };
            }
            return groups == null ? null : groups.toArray();
        }
        List<Category> categories = QQPlugin.getDefault().getClient()
                .getFriendListWithCategory();
        return categories == null ? null : categories.toArray();
    }
}