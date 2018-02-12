package cn.ieclipse.smartim.views;

import java.util.ArrayList;
import java.util.Random;

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
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.DrillDownAdapter;
import org.eclipse.ui.part.IShowInTarget;
import org.eclipse.ui.part.ShowInContext;
import org.eclipse.ui.part.ViewPart;

import cn.ieclipse.smartim.IMPlugin;
import cn.ieclipse.smartim.IMRobotCallback;
import cn.ieclipse.smartim.IMSendCallback;
import cn.ieclipse.smartim.SmartClient;
import cn.ieclipse.smartim.actions.BroadcastAction;
import cn.ieclipse.smartim.actions.DisconnectAction;
import cn.ieclipse.smartim.actions.LoginAction;
import cn.ieclipse.smartim.actions.MockConsoleAction;
import cn.ieclipse.smartim.actions.SettingAction;
import cn.ieclipse.smartim.actions.ToggleContactsAction;
import cn.ieclipse.smartim.callback.ModificationCallback;
import cn.ieclipse.smartim.callback.ReceiveCallback;
import cn.ieclipse.smartim.common.IDEUtils;
import cn.ieclipse.smartim.htmlconsole.ClosableTabHost;
import cn.ieclipse.smartim.htmlconsole.IMChatConsole;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.preferences.SettingsPerferencePage;

public abstract class IMContactView extends ViewPart implements IShowInTarget {
    
    protected DrillDownAdapter drillDownAdapter;
    protected Action login;
    protected Action settings;
    protected Action exit;
    protected Action toggleLeft;
    protected Action broadcast;
    protected Action testAction;
    
    protected SmartClient client;
    
    protected IMContactContentProvider contentProvider;
    protected IMContactLabelProvider labelProvider;
    protected IMContactDoubleClicker doubleClicker;
    
    protected ReceiveCallback receiveCallback;
    protected IMRobotCallback robotCallback;
    protected IMSendCallback sendCallback;
    protected ModificationCallback modificationCallback;
    protected boolean updateContactsOnlyFocus = false;
    
    protected SashForm sashForm;
    protected TabFolder tabFolder;
    protected CTabFolder tabbedChat;
    
    protected String viewId;
    
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
        
        sashForm = new SashForm(parent, SWT.SMOOTH);
        tabFolder = new TabFolder(sashForm, SWT.NONE);
        tabbedChat = new ClosableTabHost(sashForm);
        
        sashForm.setBackground(
                IMPlugin.getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
        sashForm.setWeights(new int[] { 4, 10 });
        sashForm.setSashWidth(6);
    }
    
    @Override
    public void dispose() {
        getClient().close();
        closeAllChat();
        super.dispose();
    }
    
    public void initContacts() {
        new Thread() {
            public void run() {
                doLoadContacts();
            }
        }.start();
    }
    
    protected abstract void doLoadContacts();
    
    protected abstract void onLoadContacts(boolean success);
    
    protected void notifyLoadContacts(final boolean success) {
        IMPlugin.runOnUI(new Runnable() {
            @Override
            public void run() {
                onLoadContacts(success);
            }
        });
    }
    
    public void notifyUpdateContacts(final int index, boolean force) {
        boolean notify = IMPlugin.getDefault().getPreferenceStore()
                .getBoolean(SettingsPerferencePage.NOTIFY_UNREAD);
        if (notify || force) {
            IMPlugin.runOnUI(new Runnable() {
                @Override
                public void run() {
                    doUpdateContacts(index);
                }
            });
        }
    }
    
    protected void doUpdateContacts(final int index) {
    
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
        manager.add(toggleLeft);
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
        manager.add(toggleLeft);
        manager.add(broadcast);
        manager.add(settings);
        if (testAction != null) {
            // manager.add(testAction);
        }
        // drillDownAdapter.addNavigationActions(manager);
    }
    
    protected void makeActions() {
        login = new LoginAction(this);
        settings = new SettingAction();
        exit = new DisconnectAction(this);
        toggleLeft = new ToggleContactsAction(this);
        broadcast = new BroadcastAction(this);
        testAction = new MockConsoleAction(this);
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
    
    public CTabFolder getTabbedChat() {
        return tabbedChat;
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
    
    public abstract IMContactView createContactsUI();
    
    public abstract IMChatConsole createConsoleUI(IContact contact);
    
    public void hide() {
        if (viewId != null) {
            IDEUtils.toggleViewPart(viewId, false);
        }
    }
    
    public void show() {
        if (viewId != null) {
            IDEUtils.toggleViewPart(viewId, true);
        }
    }
    
    public void toggleContacts() {
        String key = "old_weights";
        Object old = sashForm.getData(key);
        if (old == null) {
            sashForm.setData(key, sashForm.getWeights());
            sashForm.setWeights(new int[] { 0, 1 });
        }
        else {
            sashForm.setData(key, null);
            sashForm.setWeights((int[]) old);
        }
        sashForm.layout();
    }
    
    public void randBling() {
        IMPlugin.runOnUI(new Runnable() {
            @Override
            public void run() {
                int size = tabbedChat.getItemCount();
                int i = new Random().nextInt(size);
                if (i >= 0 && tabbedChat instanceof ClosableTabHost) {
                    ((ClosableTabHost) tabbedChat).bling(i, "Random");
                }
            }
        });
    }
    
    public void highlight(final IMChatConsole console) {
        IMPlugin.runOnUI(new Runnable() {
            @Override
            public void run() {
                int i = tabbedChat.indexOf(console);
                if (i >= 0 && tabbedChat instanceof ClosableTabHost) {
                    ((ClosableTabHost) tabbedChat).bling(i, console.getName());
                }
            }
        });
    }
    
    public IMChatConsole findConsole(IContact contact, boolean add) {
        return findConsoleById(contact.getUin(), add);
    }
    
    public IMChatConsole findConsoleById(String id, boolean show) {
        int count = tabbedChat.getItemCount();
        for (int i = 0; i < count; i++) {
            if (tabbedChat.getItem(i) instanceof IMChatConsole) {
                IMChatConsole t = (IMChatConsole) tabbedChat.getItem(i);
                if (id.equals(t.getUin())) {
                    if (show) {
                        tabbedChat.setSelection(i);
                    }
                    return t;
                }
            }
        }
        return null;
    }
    
    public IMChatConsole openConsole(IContact contact) {
        IMChatConsole console = findConsoleById(contact.getUin(), true);
        if (console == null) {
            console = createConsoleUI(contact);
            tabbedChat.setSelection(console);
        }
        return console;
    }
    
    public void close() {
        getClient().close();
        closeAllChat();
        notifyLoadContacts(false);
    }
    
    public void closeAllChat() {
        CTabItem[] items = tabbedChat.getItems();
        if (items != null) {
            for (CTabItem item : items) {
                item.dispose();
            }
        }
    }
    
    public java.util.List<IMChatConsole> getConsoleList() {
        java.util.List<IMChatConsole> list = new ArrayList<>();
        if (tabbedChat != null) {
            int count = tabbedChat.getItemCount();
            for (int i = 0; i < count; i++) {
                if (tabbedChat.getItem(i) instanceof IMChatConsole) {
                    IMChatConsole t = (IMChatConsole) tabbedChat.getItem(i);
                    list.add(t);
                }
            }
        }
        return list;
    }
}
