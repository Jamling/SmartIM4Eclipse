package cn.ieclipse.smartim.views;

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

import cn.ieclipse.smartim.IMRobotCallback;
import cn.ieclipse.smartim.SmartClient;
import cn.ieclipse.smartim.actions.BroadcastAction;
import cn.ieclipse.smartim.actions.DisconnectAction;
import cn.ieclipse.smartim.actions.LoginAction;
import cn.ieclipse.smartim.actions.SettingAction;
import cn.ieclipse.smartim.callback.ModificationCallback;
import cn.ieclipse.smartim.callback.ReceiveCallback;
import cn.ieclipse.smartim.callback.SendCallback;

public abstract class IMContactView extends ViewPart implements IShowInTarget {
    
    protected DrillDownAdapter drillDownAdapter;
    protected Action login;
    protected Action settings;
    protected Action exit;
    protected Action broadcast;
    protected Action testAction;
    
    protected SmartClient client;
    
    protected IMContactContentProvider contentProvider;
    protected IMContactLabelProvider labelProvider;
    protected IMContactDoubleClicker doubleClicker;
    
    protected ReceiveCallback receiveCallback;
    protected IMRobotCallback robotCallback;
    protected SendCallback sendCallback;
    protected ModificationCallback modificationCallback;
    
    /**
     * The constructor.
     */
    public IMContactView() {
    }
    
    /**
     * This is a callback that will allow us to create the viewer and initialize
     * it.
     */
    public void createPartControl(Composite parent) {
        makeActions();
        hookContextMenu();
        hookDoubleClickAction();
        contributeToActionBars();
    }
    
    public void initContacts() {
    }
    
    private void hookContextMenu() {
        MenuManager menuMgr = new MenuManager("#PopupMenu");
        menuMgr.setRemoveAllWhenShown(true);
        menuMgr.addMenuListener(new IMenuListener() {
            public void menuAboutToShow(IMenuManager manager) {
                IMContactView.this.fillContextMenu(manager);
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
    
    protected void fillContextMenu(IMenuManager manager) {
        manager.add(login);
        manager.add(exit);
        manager.add(new Separator());
        manager.add(broadcast);
        manager.add(settings);
        // drillDownAdapter.addNavigationActions(manager);
        // Other plug-ins can contribute there actions here
        manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
    }
    
    protected void fillLocalToolBar(IToolBarManager manager) {
        manager.add(login);
        manager.add(exit);
        manager.add(new Separator());
        manager.add(broadcast);
        manager.add(settings);
        if (testAction != null) {
            manager.add(testAction);
        }
        // drillDownAdapter.addNavigationActions(manager);
    }
    
    protected void makeActions() {
        login = new LoginAction(this);
        settings = new SettingAction();
        exit = new DisconnectAction(this);
        broadcast = new BroadcastAction(this);
        
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
            return show(obj);
        }
        return false;
    }
    
    protected boolean show(Object selection) {
        return false;
    }
    
    protected TreeViewer createTab(String name, TabFolder tabFolder) {
        TabItem tabItem = new TabItem(tabFolder, SWT.NONE);
        tabItem.setText(name);
        Composite composite = new Composite(tabFolder, SWT.NONE);
        tabItem.setControl(composite);
        composite.setLayout(new TreeColumnLayout());
        TreeViewer tv = new TreeViewer(composite, SWT.NONE);
        return tv;
    }
    
    protected void initTrees(TreeViewer... treeViewers) {
        if (treeViewers != null) {
            for (TreeViewer tv : treeViewers) {
                initTree(tv);
            }
        }
    }
    
    protected void initTree(TreeViewer tv) {
        tv.setContentProvider(contentProvider);
        tv.setLabelProvider(labelProvider);
        tv.addDoubleClickListener(doubleClicker);
    };
    
    public abstract SmartClient getClient();
}
