package cn.ieclipse.smartqq.views;

import java.util.Collections;
import java.util.List;

import org.eclipse.jface.viewers.Viewer;

import com.scienjus.smartqq.client.SmartQQClient;
import com.scienjus.smartqq.model.Category;
import com.scienjus.smartqq.model.Discuss;
import com.scienjus.smartqq.model.Group;
import com.scienjus.smartqq.model.QQContact;
import com.scienjus.smartqq.model.Recent;

import cn.ieclipse.smartim.model.VirtualCategory;
import cn.ieclipse.smartim.views.IMContactContentProvider;
import cn.ieclipse.smartim.views.IMContactView;

public class FriendContentProvider extends IMContactContentProvider {
    
    public FriendContentProvider(IMContactView view, boolean check) {
        super(view, check);
    }
    
    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }
    
    @Override
    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof Category) {
            return ((Category) parentElement).getFriends().toArray();
        }
        else if (parentElement instanceof VirtualCategory) {
            return ((VirtualCategory<?>) parentElement).getChildren();
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
            return ((VirtualCategory<?>) element).hasChildren();
        }
        return false;
    }
    
    @Override
    public Object[] getElements(Object inputElement) {
        SmartQQClient client = (SmartQQClient) fView.getClient();
        if ("recent".equals(inputElement)) {
            // List<Recent> recents = client.getRecentList();
            // return recents == null ? null : recents.toArray();
            return getRecentTargets(client).toArray();
        }
        else if ("group".equals(inputElement)) {
            List<Group> groups = client.getGroupList();
            if (check) {
                return new Object[] { new VirtualCategory<>("Group", groups) };
            }
            return groups == null ? null : groups.toArray();
        }
        else if ("discuss".equals(inputElement)) {
            List<Discuss> groups = client.getDiscussList();
            if (check) {
                return new Object[] {
                        new VirtualCategory<>("Discuss", groups) };
            }
            return groups == null ? null : groups.toArray();
        }
        else if ("friend".equals(inputElement)) {
            List<Category> categories = client.getFriendListWithCategory();
            return categories == null ? null : categories.toArray();
        }
        else {
            return null;
        }
    }
    
    public List<QQContact> getRecentTargets(SmartQQClient client) {
        List<Recent> recents = client.getRecentList();
        List<QQContact> list = client.getRecents2();
        Collections.sort(list);
        System.out.println(list);
        return list;
    }
}