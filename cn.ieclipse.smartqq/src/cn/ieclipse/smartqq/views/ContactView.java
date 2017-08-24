package cn.ieclipse.smartqq.views;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.IShowInTarget;
import org.eclipse.ui.part.ShowInContext;
import org.eclipse.ui.part.ViewPart;

import com.scienjus.smartqq.model.Friend;

import cn.ieclipse.smartqq.QQPlugin;
import cn.ieclipse.smartqq.Utils;
import cn.ieclipse.smartqq.actions.BroadcastAction;
import cn.ieclipse.smartqq.actions.DisconnectAction;
import cn.ieclipse.smartqq.actions.LoginAction;
import cn.ieclipse.smartqq.actions.SettingAction;
import cn.ieclipse.smartqq.adapter.FriendAdapter;
import cn.ieclipse.smartqq.console.ChatConsole;

public class ContactView extends ViewPart implements IShowInTarget {
    
    /**
     * The ID of the view as specified by the extension.
     */
    public static final String ID = "cn.ieclipse.smartqq.views.ContactView";
    private DrillDownAdapter drillDownAdapter;
    private Action login;
    private Action settings;
    private Action exit;
    private Action broadcast;
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
        ftvRecent = new TreeViewer(composite1, SWT.NONE);
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
        ftvGroup = new TreeViewer(composite3, SWT.NONE);
        new FriendAdapter(ftvGroup);
        
        TabItem tiDiscuss = new TabItem(tabFolder, SWT.NONE);
        tiDiscuss.setText("Discuss");
        Composite composite4 = new Composite(tabFolder, SWT.NONE);
        tiDiscuss.setControl(composite4);
        composite4.setLayout(new TreeColumnLayout());
        ftvDiscuss = new TreeViewer(composite4, SWT.NONE);
        new FriendAdapter(ftvDiscuss);
        
        makeActions();
        hookContextMenu();
        hookDoubleClickAction();
        contributeToActionBars();
        
        initFriends();
    }
    
    public void initFriends() {
        if (QQPlugin.getDefault().isLogin()) {
            try {
                QQPlugin.getDefault().getClient().reload();
                ftvFriend.setInput("friend");
                ftvGroup.setInput("group");
                ftvDiscuss.setInput("discuss");
                ftvRecent.setInput("recent");
                QQPlugin.getDefault().start();
            } catch (Exception e) {
                ftvRecent.getTree().setHeaderVisible(true);
                IStatus info = new Status(IStatus.ERROR, QQPlugin.PLUGIN_ID,
                        "初始化失败" + e.toString());
                QQPlugin.getDefault().getLog().log(info);
            }
        }
        else {
            ftvFriend.setInput(null);
            ftvGroup.setInput(null);
            ftvDiscuss.setInput(null);
            ftvRecent.setInput(null);
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
        manager.add(login);
        manager.add(new Separator());
        manager.add(settings);
    }
    
    private void fillContextMenu(IMenuManager manager) {
        manager.add(login);
        manager.add(exit);
        manager.add(new Separator());
        manager.add(broadcast);
        manager.add(settings);
        // drillDownAdapter.addNavigationActions(manager);
        // Other plug-ins can contribute there actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }
    
    private void fillLocalToolBar(IToolBarManager manager) {
        manager.add(login);
        manager.add(exit);
        manager.add(new Separator());
        manager.add(broadcast);
        manager.add(settings);
        // manager.add(doubleClickAction);
        // drillDownAdapter.addNavigationActions(manager);
    }
    
    private void makeActions() {
        login = new LoginAction(this);
        settings = new SettingAction();
        exit = new DisconnectAction(this);
        
        broadcast = new BroadcastAction();
        doubleClickAction = new Action() {
            public void run() {
                Friend f = new Friend();
                f.setUserId(System.currentTimeMillis());
                f.setMarkname("Test" + System.currentTimeMillis());
                ChatConsole console = QQPlugin.getDefault().findConsole(f,
                        true);
                console.write(Utils.formatMsg(System.currentTimeMillis(), "明月",
                        "我的未来不是梦http://www.baidu.com咕咕"));
            }
        };
        doubleClickAction.setText("Test");
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
    
    @Override
    public boolean show(ShowInContext context) {
        ISelection sel = context.getSelection();
        if (sel instanceof IStructuredSelection) {
            Object obj = ((IStructuredSelection) sel).getFirstElement();
            return true;
        }
        return false;
    }
}
