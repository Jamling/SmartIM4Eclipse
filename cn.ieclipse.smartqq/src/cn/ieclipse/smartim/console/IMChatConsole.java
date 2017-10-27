package cn.ieclipse.smartim.console;

import java.io.IOException;
import java.io.PrintStream;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.IOConsoleInputStream;
import org.eclipse.ui.console.IOConsoleOutputStream;
import org.eclipse.ui.console.TextConsolePage;
import org.eclipse.ui.part.IPageBookViewPage;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.ieclipse.smartim.AbstractSmartClient;
import cn.ieclipse.smartim.IMHistoryManager;
import cn.ieclipse.smartim.IMPlugin;
import cn.ieclipse.smartim.SmartClient;
import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.preferences.SettingsPerferencePage;

public abstract class IMChatConsole extends IOConsole {
    protected static final String ENTER_KEY = "\r\n";
    protected String id;
    protected String uin;
    protected IContact contact;
    protected IMChatConsolePage page;
    private static ImageDescriptor icon = IMPlugin
            .getImageDescriptor("icons/review.png");
            
    public IMChatConsole(String name, String consoleType,
            ImageDescriptor imageDescriptor, String encoding,
            boolean autoLifecycle) {
        super(name, consoleType, imageDescriptor, encoding, autoLifecycle);
    }
    
    public IMChatConsole(String name, ImageDescriptor imageDescriptor) {
        super(name, imageDescriptor);
    }
    
    public IMChatConsole(String id, String name, String uin) {
        this(name, icon);
        this.id = id;
        this.uin = uin;
    }
    
    public IMChatConsole(IContact contact) {
        this(contact.getUin(), contact.getName(), contact.getUin());
        this.contact = contact;
    }
    
    public boolean match(IContact contact) {
        if (this.contact != null && this.contact.getUin() != null
                && contact != null) {
            if (this.contact.getUin().equals(contact.getUin())) {
                return true;
            }
        }
        return false;
    }
    
    public String getId() {
        return uin;
    }
    
    public abstract SmartClient getClient();
    
    public String getHistoryFile() {
        return uin;
    }
    
    public abstract void loadHistory(String raw);
    
    @Override
    public IPageBookViewPage createPage(IConsoleView view) {
        IPageBookViewPage page = null;
        page = this.page = new IMChatConsolePage(this, view);
        return page;
    }
    
    public void loadHistories() {
        initStreams();
        SmartClient client = getClient();
        if (client != null && client instanceof AbstractSmartClient) {
            List<String> ms = IMHistoryManager.getInstance()
                    .load((AbstractSmartClient) client, getHistoryFile());
            for (String raw : ms) {
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
    
    public void write(String msg) {
        try {
            outputStream.write(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void writeMine(String input) {
        String name = getClient().getAccount().getName();
        String msg = IMUtils.formatMsg(System.currentTimeMillis(), name, input);
        try {
            mineStream.write(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public abstract void post(final String msg);
    
    public void sendMsg(final String msg) {
        SmartClient client = getClient();
        if (client == null || client.isClose()) {
            error("连接已关闭");
            return;
        }
        if (!client.isLogin()) {
            error("连接已关闭，请重新登录");
            return;
        }
        writeMine(msg);
        new Thread() {
            public void run() {
                post(msg);
            };
        }.start();
    }
    
    public void sendFile(final String file) {
    
    }
    
    public String readLine() {
        Scanner scanner = new Scanner(getInputStream(), getEncoding());
        String result;
        try {
            result = scanner.nextLine();
        } catch (NoSuchElementException endOfFile) {
            result = null;
        }
        return result;
    }
    
    private IOConsoleInputStream inputStream;
    private IOConsoleOutputStream errorStream;
    private IOConsoleOutputStream outputStream;
    private IOConsoleOutputStream promptStream;
    private IOConsoleOutputStream mineStream;
    
    @Override
    protected void init() {
        super.init();
        initStreams();
        
        // new Thread(inputRunnable).start();
    }
    
    private void initStreams() {
        if (inputStream == null) {
            inputStream = getInputStream();
            outputStream = newOutputStream();
            errorStream = newOutputStream();
            promptStream = newOutputStream();
            mineStream = newOutputStream();
            
            outputStream.setColor(SWTResourceManager.getColor(SWT.COLOR_BLACK));
            inputStream.setColor(SWTResourceManager.getColor(SWT.COLOR_BLACK));
            promptStream.setColor(
                    SWTResourceManager.getColor(SWT.COLOR_WIDGET_DARK_SHADOW));
            mineStream
                    .setColor(SWTResourceManager.getColor(SWT.COLOR_DARK_BLUE));
            errorStream.setColor(SWTResourceManager.getColor(SWT.COLOR_RED));
        }
    }
    
    private Runnable inputRunnable = new Runnable() {
        
        @Override
        public void run() {
            String input = null;
            do {
                try {
                    promptStream.write(">>");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                input = readLine();
                if (input != null) {
                    writeMine(input);
                    post(input);
                }
            } while (input != null && !page.getControl().isDisposed());
        }
    };
    
    public void hide() {
        toggleContactView(false);
        clearConsole();
    }
    
    public void close() {
        toggleContactView(false);
        IMPlugin.getDefault().closeAllChat();
    }
    
    public void toggleHide() {
        if (IMPlugin.getDefault().enable) {
            hide();
            IMPlugin.setEnable(false);
        }
        else {
            toggleContactView(true);
            IMPlugin.setEnable(true);
        }
        IMPlugin.setNotify(IMPlugin.getDefault().enable);
    }
    
    public void toggleClose() {
        if (IMPlugin.getDefault().enable) {
            close();
            IMPlugin.setEnable(false);
        }
        else {
            toggleContactView(true);
            IMPlugin.setEnable(true);
        }
    }
    
    private void toggleContactView(boolean show) {
        // IWorkbenchPage page = PlatformUI.getWorkbench()
        // .getActiveWorkbenchWindow().getActivePage();
        // if (show) {
        // try {
        // page.showView(QQContactView.ID);
        // } catch (PartInitException e) {
        // write(e.getMessage());
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // return;
        // }
        // IViewPart view = page.findView(QQContactView.ID);
        // if (view != null) {
        // page.hideView(view);
        // }
    }
    
    public void activeInput() {
        final StyledText text = getPage().getViewer().getTextWidget();
        Shell pshell = text.getShell();
        int x = 0;
        int y = text.getBounds().height;
        Control p = text.getParent();
        while (p != pshell) {
            x += p.getLocation().x;
            y += p.getLocation().y;
            p = p.getParent();
        }
        InputShell shell = InputShell.getInstance(pshell);
        shell.setConsole(this);
        shell.setLocation(x, y);
        shell.open();
        shell.layout();
        shell.setVisible(true);
    }
    
    public TextConsolePage getPage() {
        return page;
    }
    
    public void error(Throwable e) {
        e.printStackTrace(new PrintStream(errorStream));
    }
    
    public void error(String msg) {
        try {
            errorStream.write(msg);
            errorStream.write('\n');
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
