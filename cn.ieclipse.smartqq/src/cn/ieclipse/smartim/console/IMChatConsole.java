package cn.ieclipse.smartim.console;

import java.util.List;

import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;

import cn.ieclipse.smartim.IMHistoryManager;
import cn.ieclipse.smartim.IMPlugin;
import cn.ieclipse.smartim.SmartClient;
import cn.ieclipse.smartim.actions.ClearHistoryAction;
import cn.ieclipse.smartim.actions.ScrollLockAction;
import cn.ieclipse.smartim.actions.SendFileAction;
import cn.ieclipse.smartim.actions.SendImageAction;
import cn.ieclipse.smartim.actions.SendProjectFileAction;
import cn.ieclipse.smartim.common.IDEUtils;
import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.dialogs.OpenFileDialog;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.model.impl.AbstractContact;
import cn.ieclipse.smartim.views.IMContactView;
import cn.ieclipse.util.BareBonesBrowserLaunch;
import cn.ieclipse.util.EncodeUtils;

/**
 * Created by Jamling on 2017/7/1.
 */
public abstract class IMChatConsole extends CTabItem {
    public static final String ENTER_KEY = "\r\n";
    public Image IMG_NORMAL = null;
    public Image IMG_SELECTED = null;
    protected IContact contact;
    protected String uin;
    protected IMContactView imPanel;
    
    public IMChatConsole(IContact target, IMContactView imPanel) {
        super(imPanel.getTabbedChat(), SWT.NONE);
        this.contact = target;
        this.imPanel = imPanel;
        this.uin = target.getUin();
        setText(contact.getName());
        initUI();
        new Thread() {
            public void run() {
                loadHistories();
            };
        }.start();
    }
    
    public String getName() {
        return getText();
    }
    
    public void hideAll() {
        if (imPanel != null) {
            imPanel.hide();
        }
    }
    
    public SmartClient getClient() {
        return imPanel.getClient();
    }
    
    public abstract void loadHistory(String raw);
    
    public abstract void post(final String msg);
    
    public String getHistoryFile() {
        return EncodeUtils.getMd5(contact.getName());
    }
    
    public String getUin() {
        return uin;
    }
    
    public String trimMsg(String msg) {
        if (msg.endsWith(ENTER_KEY)) {
            return msg;
        }
        return msg + ENTER_KEY;
    }
    
    public void loadHistories() {
        SmartClient client = getClient();
        if (client != null) {
            List<String> ms = IMHistoryManager.getInstance().load(client,
                    getHistoryFile());
            int size = ms.size();
            for (int i = 0; i < size; i++) {
                String raw = ms.get(i);
                if (!IMUtils.isEmpty(raw)) {
                    try {
                        loadHistory(raw);
                    } catch (Exception e) {
                        error("历史消息记录：" + raw);
                    }
                }
            }
        }
    }
    
    public void clearHistories() {
        IMHistoryManager.getInstance().clear(getClient(), getHistoryFile());
        composite.clearHistory();
    }
    
    public void clearUnread() {
        if (contact != null && contact instanceof AbstractContact) {
            ((AbstractContact) contact).clearUnRead();
            imPanel.notifyUpdateContacts(0, true);
        }
    }
    
    public boolean hideMyInput() {
        return false;
    }
    
    public boolean checkClient(SmartClient client) {
        if (client == null || client.isClose()) {
            error("连接已关闭");
            return false;
        }
        if (!client.isLogin()) {
            error("请先登录");
            return false;
        }
        return true;
    }
    
    public void send(final String input) {
        SmartClient client = getClient();
        if (!checkClient(client)) {
            return;
        }
        String name = client.getAccount().getName();
        String msg = IMUtils.formatHtmlMyMsg(System.currentTimeMillis(), name,
                input);
        if (!hideMyInput()) {
            insertDocument(msg);
            IMHistoryManager.getInstance().save(client, getHistoryFile(), msg);
        }
        new Thread() {
            @Override
            public void run() {
                post(input);
            }
        }.start();
    }
    
    public void sendFile(final String file) {
        new Thread() {
            public void run() {
                uploadLock = true;
                try {
                    sendFileInternal(file);
                } catch (Exception e) {
                    IMPlugin.getDefault().log("发送文件失败", e);
                    error(String.format("发送文件失败：%s(%s)", file, e.getMessage()));
                } finally {
                    uploadLock = false;
                }
            }
        }.start();
    }
    
    protected void sendFileInternal(final String file) throws Exception {
    
    }
    
    public void error(Throwable e) {
        error(e == null ? "null" : e.toString());
    }
    
    public void error(final String msg) {
        insertDocument(String.format("<div class=\"error\">%s</div>", msg));
    }
    
    private void createUIComponents() {
    
    }
    
    public void write(final String msg) {
        insertDocument(msg);
    }
    
    // protected ChatHistoryPane top;
    // protected ChatInputPane bottom;
    protected Browser historyWidget;
    protected Text inputWidget;
    protected Button btnSend;
    TabComposite composite;
    
    public void initUI() {
        composite = new TabComposite(this);
        ToolBar toolBar = composite.getToolBar();
        initToolBar(toolBar);
        historyWidget = composite.getHistoryWidget();
        inputWidget = composite.getInputWidget();
        initHistoryWidget();
        
        setControl(composite);
    }
    
    protected void initToolBar(ToolBar toolBar) {
        ToolBarManager manager = new ToolBarManager(toolBar);
        manager.add(new SendImageAction(this));
        manager.add(new SendFileAction(this));
        manager.add(new SendProjectFileAction(this));
        manager.add(new ClearHistoryAction(this));
        manager.add(new ScrollLockAction(this));
        manager.update(true);
    }
    
    protected boolean scrollLock = false;
    protected boolean uploadLock = false;
    
    public boolean enableUpload() {
        return !uploadLock;
    }
    
    public void setScrollLock(boolean scrollLock) {
        this.scrollLock = scrollLock;
    }
    
    protected void initHistoryWidget() {
    
    }
    
    protected boolean hyperlinkActivated(String desc) {
        if (desc.startsWith("user://")) {
            String user = desc.substring(7);
            try {
                inputWidget.append("@" + user + " ");
            } catch (Exception e) {
            
            }
        }
        else if (desc.startsWith("code://")) {
            String code = desc.substring(7);
            int pos = code.lastIndexOf(':');
            String file = code.substring(0, pos);
            int line = Integer.parseInt(code.substring(pos + 1).trim());
            if (line > 0) {
                line--;
            }
            IDEUtils.open(file, line);
        }
        else if (desc.startsWith("file://")) {
            if (!OpenFileDialog.open(getParent().getShell(), desc)) {
                BareBonesBrowserLaunch.openURL(desc);
            }
        }
        else {
            if (!IDEUtils.openInternalBrowser(desc, false)) {
                BareBonesBrowserLaunch.openURL(desc);
            }
        }
        return false;
    }
    
    protected void insertDocument(String msg) {
        try {
            if (!composite.isDisposed()) {
                composite.addHistory(msg, scrollLock);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
