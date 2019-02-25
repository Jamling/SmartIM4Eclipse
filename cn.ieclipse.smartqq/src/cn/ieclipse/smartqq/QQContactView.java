package cn.ieclipse.smartqq;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;

import com.scienjus.smartqq.client.SmartQQClient;

import cn.ieclipse.smartim.IMClientFactory;
import cn.ieclipse.smartim.IMPlugin;
import cn.ieclipse.smartim.IMSendCallback;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.views.IMContactDoubleClicker;
import cn.ieclipse.smartim.views.IMContactView;

public class QQContactView extends IMContactView {
    
    /**
     * The ID of the view as specified by the extension.
     */
    public static final String ID = "cn.ieclipse.smartqq.views.QQContactView";
    
    private TreeViewer ftvFriend;
    private TreeViewer ftvRecent;
    private TreeViewer ftvGroup;
    private TreeViewer ftvDiscuss;
    
    /**
     * The constructor.
     */
    public QQContactView() {
        viewId = ID;
        contentProvider = new QQContentProvider(this, false);
        labelProvider = new QQLabelProvider(this);
        doubleClicker = new IMContactDoubleClicker(this);
        
        receiveCallback = new QQReceiveCallback(this);
        robotCallback = new QQRobotCallback(this);
        sendCallback = new IMSendCallback(this);
        loadWelcome("qq");
    }
    
    /**
     * This is a callback that will allow us to create the viewer and initialize
     * it.
     */
    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        ftvRecent = createTab("Recents", tabFolder);
        ftvFriend = createTab("Friends", tabFolder);
        ftvGroup = createTab("Groups", tabFolder);
        ftvDiscuss = createTab("Discuss", tabFolder);
        tabFolder.setSelection(0);
        
        initTrees(ftvRecent, ftvFriend, ftvGroup, ftvDiscuss);
    }
    
    protected void doLoadContacts() {
        client = getClient();
        if (client.isLogin()) {
            try {
                client.init();
                notifyLoadContacts(true);
                client.setReceiveCallback(receiveCallback);
                client.addReceiveCallback(robotCallback);
                client.setSendCallback(sendCallback);
                client.setModificationCallbacdk(
                        new QQMidificationCallback(this));
                client.start();
            } catch (Exception e) {
                IMPlugin.getDefault().log("SmartQQ初始化失败", e);
            }
        }
        else {
            notifyLoadContacts(false);
        }
    }
    
    @Override
    protected void onLoadContacts(boolean success) {
        if (success) {
            ftvFriend.setInput("friend");
            ftvGroup.setInput("group");
            ftvDiscuss.setInput("discuss");
            ftvRecent.setInput("recent");
        }
        else {
            ftvFriend.setInput(null);
            ftvGroup.setInput(null);
            ftvDiscuss.setInput(null);
            ftvRecent.setInput(null);
        }
    }
    
    @Override
    protected void makeActions() {
        super.makeActions();
        broadcast = new QQBroadcastAction(this);
    }
    
    @Override
    public SmartQQClient getClient() {
        return (SmartQQClient) IMClientFactory.getInstance().getQQClient();
    }
    
    @Override
    protected void doUpdateContacts(int index) {
        super.doUpdateContacts(index);
        if (index == 0) {
            boolean focus = ftvRecent.getTree().isFocusControl();
            if (focus || !updateContactsOnlyFocus) {
                ftvRecent.refresh(true);
            }
        }
    }

    @Override
    public IMContactView createContactsUI() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public cn.ieclipse.smartim.console.IMChatConsole createConsoleUI(
            IContact contact) {
        return new QQChatConsole(contact, this);
    }
}
