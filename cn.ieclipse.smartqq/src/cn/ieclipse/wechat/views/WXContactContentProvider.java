/*
 * Copyright 2014-2017 ieclipse.cn.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ieclipse.wechat.views;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;
import java.util.TreeSet;

import org.eclipse.jface.viewers.Viewer;

import cn.ieclipse.smartim.Utils;
import cn.ieclipse.smartim.model.VirtualCategory;
import cn.ieclipse.smartim.views.IMContactContentProvider;
import cn.ieclipse.smartim.views.IMContactView;
import io.github.biezhi.wechat.api.WechatClient;
import io.github.biezhi.wechat.model.Contact;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年10月14日
 *       
 */
public class WXContactContentProvider extends IMContactContentProvider {
    
    public WXContactContentProvider(IMContactView view, boolean check) {
        super(view, check);
    }
    
    @Override
    public Object[] getElements(Object inputElement) {
        WechatClient client = (WechatClient) fView.getClient();
        if ("recent".equals(inputElement)) {
            return client.getRecentList().toArray();
        }
        else if ("group".equals(inputElement)) {
            List<Contact> groups = client.getGroupList();
            if (check) {
                return new Object[] { new VirtualCategory<>("Group", groups) };
            }
            return groups == null ? null : groups.toArray();
        }
        else if ("friend".equals(inputElement)) {
            List<Contact> list = client.getMemberList();
            if (check) {
                return getContactGroup(list).toArray();
            }
            List<VirtualCategory<Contact>> cates = new ArrayList<>();
            cates.add(new VirtualCategory<>("groups", client.getGroupList()));
            cates.addAll(getContactGroup(list));
            
            return cates.toArray();
        }
        else if ("public".equals(inputElement)) {
            List<Contact> list = client.getPublicUsersList();
            return list == null ? null : list.toArray();
        }
        return null;
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
        if (parentElement instanceof Contact) {
            Contact contact = (Contact) parentElement;
            return contact.MemberList.toArray();
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
        if (element instanceof Contact) {
            Contact contact = (Contact) element;
            return !Utils.isEmpty(contact.MemberList);
        }
        else if (element instanceof VirtualCategory) {
            return ((VirtualCategory<?>) element).hasChildren();
        }
        return false;
    }
    
    public List<VirtualCategory<Contact>> getContactGroup(List<Contact> list) {
        List<VirtualCategory<Contact>> cates = new ArrayList<>();
        if (!Utils.isEmpty(list)) {
            List<Contact> unA = new ArrayList<>();
            TreeMap<String, List<Contact>> maps = new TreeMap<>();
            for (Contact c : list) {
                String py = c.getPYInitial();
                char A = Utils.isEmpty(py) ? '#' : py.charAt(0);
                if (A >= 'A' && A <= 'Z' || A >= 'a' && A <= 'z') {
                    String a = String.valueOf(A).toUpperCase();
                    List<Contact> values = maps.get(a);
                    if (values == null) {
                        values = new ArrayList<>();
                        maps.put(a, values);
                    }
                    values.add(c);
                }
                else {
                    unA.add(c);
                }
            }
            for (String n : maps.keySet()) {
                cates.add(new VirtualCategory<>(n, maps.get(n)));
            }
            if (!Utils.isEmpty(unA)) {
                cates.add(new VirtualCategory<>("#", unA));
            }
        }
        return cates;
    }
}
