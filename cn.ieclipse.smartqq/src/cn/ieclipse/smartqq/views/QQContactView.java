package cn.ieclipse.smartqq.views;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TabFolder;

import com.scienjus.smartqq.client.SmartQQClient;
import com.scienjus.smartqq.model.Friend;

import cn.ieclipse.smartim.IMClientFactory;
import cn.ieclipse.smartim.IMHistoryManager;
import cn.ieclipse.smartim.IMPlugin;
import cn.ieclipse.smartim.callback.SendCallback;
import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.console.IMChatConsole;
import cn.ieclipse.smartim.views.IMContactView;
import cn.ieclipse.smartqq.QQReceiveCallback;
import cn.ieclipse.smartqq.QQRobotCallback;
import cn.ieclipse.smartqq.QQSendCallback;
import cn.ieclipse.smartqq.actions.QQBroadcastAction;
import cn.ieclipse.smartqq.console.QQChatConsole;

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
        contentProvider = new FriendContentProvider(this, false);
        labelProvider = new FriendLabelProvider(this);
        doubleClicker = new QQDoubleClicker(this);
        
        receiveCallback = new QQReceiveCallback();
        robotCallback = new QQRobotCallback();
        sendCallback = new QQSendCallback(this);
    }
    
    /**
     * This is a callback that will allow us to create the viewer and initialize
     * it.
     */
    public void createPartControl(Composite parent) {
        super.createPartControl(parent);
        
        TabFolder tabFolder = new TabFolder(parent, SWT.NONE);
        ftvRecent = createTab("Recents", tabFolder);
        ftvFriend = createTab("Friends", tabFolder);
        ftvGroup = createTab("Groups", tabFolder);
        ftvDiscuss = createTab("Discuss", tabFolder);
        
        initTrees(ftvRecent, ftvFriend, ftvGroup, ftvDiscuss);
    }
    
    public void initContacts() {
        client = getClient();
        if (client.isLogin()) {
            try {
                IMPlugin.getDefault().log("初始化QQ");
                client.init();
                IMPlugin.getDefault().log("QQ初始化完成，联系人加载中...");
                ftvFriend.setInput("friend");
                ftvGroup.setInput("group");
                ftvDiscuss.setInput("discuss");
                ftvRecent.setInput("recent");
                client.setReceiveCallback(receiveCallback);
                client.addReceiveCallback(robotCallback);
                client.setSendCallback(sendCallback);
                IMPlugin.getDefault().log("QQ联系人加载完成，消息监听中...");
                client.start();
            } catch (Exception e) {
                IMPlugin.getDefault().log("SmartQQ初始化失败", e);
            }
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
        testAction = new Action() {
            public void run() {
                Friend f = new Friend();
                f.setUserId(System.currentTimeMillis());
                f.setMarkname("Test" + System.currentTimeMillis());
                IMChatConsole console = IMPlugin.getDefault()
                        .findConsole(QQChatConsole.class, f, true);
                console.write(IMUtils.formatMsg(System.currentTimeMillis(),
                        "明月", "我的未来不是梦http://www.baidu.com咕咕"));
            }
        };
        testAction.setText("Test");
    }
    
    @Override
    public SmartQQClient getClient() {
        return (SmartQQClient) IMClientFactory.getInstance().getQQClient();
    }
    
}
