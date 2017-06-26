package cn.ieclipse.smartqq.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.ViewPart;

import cn.ieclipse.smartqq.QQPlugin;
import cn.ieclipse.smartqq.actions.LoginAction;
import cn.ieclipse.smartqq.actions.SettingAction;
import cn.ieclipse.smartqq.adapter.FriendAdapter;

public class ContactView extends ViewPart {
    
    /**
     * The ID of the view as specified by the extension.
     */
    public static final String ID = "cn.ieclipse.smartqq.views.ContactView";
    private DrillDownAdapter drillDownAdapter;
    private Action action1;
    private Action action2;
    private Action doubleClickAction;
    
    private TreeViewer ftvFriend;
    private TreeViewer ftvRecent;
    private TreeViewer ftvGroup;
    private TreeViewer ftvDiscuss;
    
    /**
     * The constructor.
     */
    public ContactView() {
    }
    
    /**
     * This is a callback that will allow us to create the viewer and initialize
     * it.
     */
    public void createPartControl(Composite parent) {
        
        TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
        
        TabItem tiRecents = new TabItem(tabFolder, SWT.NONE);
        tiRecents.setText("Recents");
        Composite composite1 = new Composite(tabFolder, SWT.NONE);
        tiRecents.setControl(composite1);
        composite1.setLayout(new TreeColumnLayout());
        ftvRecent = new TreeViewer(composite1);
        new FriendAdapter(ftvRecent);
        
        TabItem tiFriends = new TabItem(tabFolder, SWT.NONE);
        tiFriends.setText("Friends");
        Composite composite2 = new Composite(tabFolder, SWT.NONE);
        tiFriends.setControl(composite2);
        composite2.setLayout(new TreeColumnLayout());
        ftvFriend = new TreeViewer(composite2, SWT.NONE);
        new FriendAdapter(ftvFriend);
        
        TabItem tiGroups = new TabItem(tabFolder, SWT.NONE);
        tiGroups.setText("Groups");
        Composite composite3 = new Composite(tabFolder, SWT.NONE);
        tiGroups.setControl(composite3);
        composite3.setLayout(new TreeColumnLayout());
        ftvGroup = new TreeViewer(composite3);
        new FriendAdapter(ftvGroup);
        
        TabItem tiDiscuss = new TabItem(tabFolder, SWT.NONE);
        tiDiscuss.setText("Discuss");
        Composite composite4 = new Composite(tabFolder, SWT.NONE);
        tiDiscuss.setControl(composite4);
        composite4.setLayout(new TreeColumnLayout());
        ftvDiscuss = new TreeViewer(composite4);
        new FriendAdapter(ftvDiscuss);
        
        makeActions();
        hookContextMenu();
        hookDoubleClickAction();
        contributeToActionBars();
        
        initFriends();
    }
    
    public void initFriends() {
        if (QQPlugin.getDefault().isLogin()) {
            QQPlugin.getDefault().getClient().reload();
            ftvFriend.setInput("friend");
            ftvGroup.setInput("group");
            ftvDiscuss.setInput("discuss");
            ftvRecent.setInput("recent");
            QQPlugin.getDefault().start();
        }
    }
    
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu");
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                ContactView.this.fillContextMenu(manager);
            }
        });
    }
    
    private void contributeToActionBars() {
        IActionBars bars = getViewSite().getActionBars();
        fillLocalPullDown(bars.getMenuManager());
        fillLocalToolBar(bars.getToolBarManager());
    }
    
    private void fillLocalPullDown(IMenuManager manager) {
        manager.add(action1);
        manager.add(new Separator());
        manager.add(action2);
    }
    
    private void fillContextMenu(IMenuManager manager) {
        manager.add(action1);
        manager.add(action2);
        manager.add(new Separator());
        // drillDownAdapter.addNavigationActions(manager);
        // Other plug-ins can contribute there actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }
    
    private void fillLocalToolBar(IToolBarManager manager) {
        manager.add(action1);
        manager.add(action2);
        manager.add(new Separator());
        // drillDownAdapter.addNavigationActions(manager);
    }
    
    private void makeActions() {
        action1 = new LoginAction(this);
        action2 = new SettingAction();
        doubleClickAction = new Action() {
            public void run() {
            
            }
        };
    }
    
    private void hookDoubleClickAction() {
    }
    
    private void showMessage(String message) {
    
    }
    
    /**
     * Passing the focus request to the viewer's control.
     */
    public void setFocus() {
    
    }
}
