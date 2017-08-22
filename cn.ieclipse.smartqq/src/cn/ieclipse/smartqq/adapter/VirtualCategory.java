package cn.ieclipse.smartqq.adapter;

import java.util.List;

public class VirtualCategory<T> {
    public String name;
    public List<T> list;
    
    public VirtualCategory(String name, List<T> list) {
        this.name = name;
        this.list = list;
    }
    
    public boolean hasChildren() {
        return list != null && !list.isEmpty();
    }
    
    public Object[] getChildren() {
        return list.toArray();
    }
    
}