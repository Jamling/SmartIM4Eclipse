package cn.ieclipse.smartqq;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.scienjus.smartqq.callback.LoginCallback;
import com.scienjus.smartqq.callback.MessageCallback2;
import com.scienjus.smartqq.client.SmartClient;
import com.scienjus.smartqq.model.Discuss;
import com.scienjus.smartqq.model.DiscussFrom;
import com.scienjus.smartqq.model.DiscussMessage;
import com.scienjus.smartqq.model.FriendFrom;
import com.scienjus.smartqq.model.Group;
import com.scienjus.smartqq.model.GroupFrom;
import com.scienjus.smartqq.model.GroupMessage;
import com.scienjus.smartqq.model.Message;
import com.scienjus.smartqq.model.Recent;

import cn.ieclipse.smartqq.console.ChatConsole;
import cn.ieclipse.smartqq.views.ContactView;

/**
 * The activator class controls the plug-in life cycle
 */
public class QQPlugin extends AbstractUIPlugin {
    
    // The plug-in ID
    public static final String PLUGIN_ID = "cn.ieclipse.smartqq"; //$NON-NLS-1$
    
    // The shared instance
    private static QQPlugin plugin;
    
    /**
     * The constructor
     */
    public QQPlugin() {
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
     * BundleContext)
     */
    public void start(BundleContext context) throws Exception {
        super.start(context);
        plugin = this;
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.
     * BundleContext)
     */
    public void stop(BundleContext context) throws Exception {
        plugin = null;
        super.stop(context);
    }
    
    /**
     * Returns the shared instance
     *
     * @return the shared instance
     */
    public static QQPlugin getDefault() {
        if (plugin == null) {
            plugin = new QQPlugin();
        }
        return plugin;
    }
    
    /**
     * Returns an image descriptor for the image file at the given plug-in
     * relative path
     *
     * @param path
     *            the path
     * @return the image descriptor
     */
    public static ImageDescriptor getImageDescriptor(String path) {
        return imageDescriptorFromPlugin(PLUGIN_ID, path);
    }
    
    // -------->
    private SmartClient client;
    
    public SmartClient getClient() {
        if (client == null) {
            client = new SmartClient();
        }
        return client;
    }
    
    public boolean isLogin() {
        return client != null && client.isLogin();
    }
    
    public void login(LoginCallback callback) {
        getClient().login(callback);
    }
    
    public void start() {
        MessageCallback2 callback = new MessageCallback2() {
            
            @Override
            public void onMessage(final Message message,
                    final FriendFrom from) {
                ChatConsole console = QQPlugin.getDefault()
                        .findConsole(from.getFriend(), false);
                Recent r = getClient().getRecent(0, message.getUserId());
                
                if (console != null) {
                    console.write(String.format("%s %s: %s",
                            new SimpleDateFormat("HH:mm:ss")
                                    .format(message.getTime()),
                            from.getName(), message.getContent()));
                }
                Robot.answer(from, message, console);
            }
            
            @Override
            public void onGroupMessage(final GroupMessage message,
                    final GroupFrom from) {
                Group g = getClient().getGroup(message.getGroupId());
                String name = from.getName();
                
                ChatConsole console = QQPlugin.getDefault().findConsole(g,
                        false);
                if (console != null) {
                    console.write(String.format("%s %s: %s",
                            new SimpleDateFormat("HH:mm:ss")
                                    .format(message.getTime()),
                            name, message.getContent()));
                }
                Robot.answer(from, message, console);
            }
            
            @Override
            public void onDiscussMessage(final DiscussMessage message,
                    final DiscussFrom from) {
                Discuss g = getClient().getDiscuss(message.getDiscussId());
                String name = from.getName();
                
                ChatConsole console = QQPlugin.getDefault().findConsole(g,
                        false);
                if (console != null) {
                    console.write(String.format("%s %s: %s",
                            new SimpleDateFormat("HH:mm:ss")
                                    .format(message.getTime()),
                            name, message.getContent()));
                }
                Robot.answer(from, message, console);
            }
        };
        getClient().setCallback(callback);
        getClient().start();
    }
    
    public ContactView getContactView() {
        IViewPart view = getWorkbench().getActiveWorkbenchWindow()
                .getActivePage().findView(ContactView.ID);
        return (ContactView) view;
    }
    
    public ChatConsole findConsole(Object obj, boolean add) {
        IConsoleManager manager = ConsolePlugin.getDefault()
                .getConsoleManager();
                
        IConsole[] existing = manager.getConsoles();
        
        ChatConsole console = null;
        for (int i = 0; i < existing.length; i++) {
            if (ChatConsole.isChatConsole(existing[i], obj)) {
                console = (ChatConsole) existing[i];
                break;
            }
        }
        if (console == null && add) {
            console = ChatConsole.create(obj);
            manager.addConsoles(new IConsole[] { console });
        }
        if (console != null && enable) {
            manager.showConsoleView(console);
        }
        return console;
    }
    
    public ChatConsole nextConsole(ChatConsole current, boolean next) {
        IConsoleManager manager = ConsolePlugin.getDefault()
                .getConsoleManager();
                
        IConsole[] existing = manager.getConsoles();
        
        ChatConsole console = null;
        boolean find = false;
        if (next) {
            for (int i = 0; i < existing.length; i++) {
                if (find && existing[i] instanceof ChatConsole) {
                    console = (ChatConsole) existing[i];
                    break;
                }
                if (existing[i] == current) {
                    find = true;
                }
            }
        }
        else if (existing.length > 0) {
            for (int i = existing.length - 1; i >= 0; i--) {
                if (find && existing[i] instanceof ChatConsole) {
                    console = (ChatConsole) existing[i];
                    break;
                }
                if (existing[i] == current) {
                    find = true;
                }
            }
        }
        if (console != null) {
            manager.showConsoleView(console);
        }
        return console;
    }
    
    public void closeAllChat() {
        IConsoleManager manager = ConsolePlugin.getDefault()
                .getConsoleManager();
                
        IConsole[] existing = manager.getConsoles();
        List<IConsole> list = new ArrayList<>();
        for (int i = 0; i < existing.length; i++) {
            if (existing[i] instanceof ChatConsole) {
                list.add(existing[i]);
            }
        }
        if (!list.isEmpty()) {
            manager.removeConsoles(list.toArray(new IConsole[list.size()]));
        }
    }
    
    public static void runOnUI(Runnable runnable) {
        Display display = Display.getDefault();
        if (display != null && !display.readAndDispatch()) {
            display.asyncExec(runnable);
        }
    }
    
    public boolean enable = true;
}
