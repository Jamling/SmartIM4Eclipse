package cn.ieclipse.smartim.common;

import java.awt.Insets;
import java.awt.Toolkit;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import org.eclipse.core.runtime.Platform;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.ieclipse.smartim.IMPlugin;
import cn.ieclipse.smartim.actions.SettingAction;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.preferences.SettingsPerferencePage;
import cn.ieclipse.smartim.views.IMContactView;

public class Notifications extends Shell {
    
    /**
     * Launch the application.
     * 
     * @param args
     */
    public static void main(String args[]) {
        try {
            final Display display = Display.getDefault();
//            Notifications.getInstance(new Shell(display)).setMock(true)
//                    .setMessage(null, "test");
//            Notifications.notify("title", "message");
            Notifications instance = new Notifications(display);
            instance.open();
            instance.layout();
            instance.setLocation(100, 100);
            instance.setVisible(true);
            instance.setMessage("title", "message");
            while (!instance.isDisposed()) {
                if (!display.readAndDispatch()) {
                    display.sleep();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private Label fIcon;
    private Label fTitle;
    private Label fText;
    private TranslateAnimation ta;
    private Timer timer;
    
    /**
     * @wbp.parser.constructor
     */
    public Notifications(Display display) {
	this(display, SWT.ON_TOP | SWT.CLOSE | SWT.TITLE);
    }
    
    /**
     * Create the shell.
     * 
     * @param display
     */
    public Notifications(Display display, int style) {
        super(display, style);
        try {
            Class<?> clazz = Class.forName("org.eclipse.swt.internal.win32.OS");
            if (clazz != null) {
                long top = clazz.getField("HWND_TOPMOST").getLong(null);
                Method m = clazz.getMethod("SetWindowPos", long.class,
                        long.class, int.class, int.class, int.class, int.class,
                        int.class);
                m.invoke(null, this.handle, top, 0, 0, 0, 0, 0);
                // org.eclipse.swt.internal.win32.OS.SetWindowPos(this.handle,
                // org.eclipse.swt.internal.win32.OS.HWND_TOPMOST, 0, 0, 0,
                // 0, SWT.NULL);
            }
        } catch (Exception e) {
            // do nothing
        }
        setImage(SWTResourceManager.getImage(org.eclipse.ui.ide.IDE.class,
                "/icons/full/elcl16/smartmode_co.png"));
        createContents();
    }
    
    /**
     * Create contents of the shell.
     */
    protected void createContents() {
        setText("通知");
        setSize(300, 150);
        
        setLayout(new GridLayout(2, false));
        if (Platform.OS_LINUX.equals(Platform.getOS()))
        {
            fIcon = new Label(this, SWT.NONE);
            fIcon.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
            fIcon.setImage(SWTResourceManager.getImage(org.eclipse.ui.IWorkbench.class, 
        	    "/icons/full/etool16/delete_edit.png"));
            fIcon.addMouseListener(new MouseAdapter() {
        	@Override
        	public void mouseDown(MouseEvent e) {
        	    setVisible(false);
        	}
	    });
            
            fTitle = new Label(this, SWT.NONE);
            fTitle.setText("通知");
            fTitle.setLayoutData(
                    new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        }
        
        fText = new Label(this, SWT.WRAP);
        // fText.setEditable(false);
        fText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
        
        Link fSettings = new Link(this, SWT.NONE);
        fSettings.setLayoutData(
                new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        fSettings.setText("<a>提醒设置</a>");
        fSettings.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                SettingAction.open(SettingsPerferencePage.class.getName());
            }
        });
        
        Link fIgnore = new Link(this, SWT.NONE);
        fIgnore.setLayoutData(
                new GridData(SWT.RIGHT, SWT.CENTER, true, false, 1, 1));
        fIgnore.setText("<a>查看详情>></a>");
        fIgnore.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                // setVisible(false);
                if (target != null && fContactView != null) {
                    fContactView.show();
                    fContactView.openConsole(target);
                    setVisible(false);
                }
            }
        });
        
        Insets sd = Toolkit.getDefaultToolkit().getScreenInsets(
        	new JFrame().getGraphicsConfiguration());
        Rectangle screen = Display.getDefault().getClientArea();
        screen.height = screen.height - sd.bottom;
        ta = new TranslateAnimation(screen.width, screen.width - getSize().x,
                screen.height, screen.height - getSize().y).setTarget(this)
                        .setDuration(300);
        setLocation(screen.width, screen.height);
    }
    
    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
    
    private Notifications setMock(boolean mock) {
        this.mock = mock;
        return this;
    }
    
    public void setMessage(final String title, final CharSequence text) {
        final boolean enable = mock ? true
                : IMPlugin.getDefault().getPreferenceStore()
                        .getBoolean(SettingsPerferencePage.NOTIFY_ENABLE);
        final int dismiss = mock ? 5
                : IMPlugin.getDefault().getPreferenceStore()
                        .getInt(SettingsPerferencePage.NOTIFY_DISMISS);
        if (!enable) {
            return;
        }
        
        doSetMessage(title, text);
        if (dismiss > 0) {
            long delay = dismiss * 1000;
            if (timer != null) {
                timer.cancel();
            }
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    IMPlugin.runOnUI(new Runnable() {
                        @Override
                        public void run() {
                            if (!isDisposed()) {
                                setVisible(false);
                            }
                        }
                    });
                }
            }, delay);
        }
    }
    
    private void doSetMessage(String title, CharSequence text) {
        if (!isVisible()) {
            setVisible(true);
            ta.start();
        }
        if (title != null) {
            this.setText(title);
            if (fTitle != null) {
        	fTitle.setText(title);
            }
        }
        this.fText.setText(text == null ? "" : text.toString());
    }
    
    private IMContactView fContactView;
    private IContact target;
    private boolean mock;
    
    private void setTarget(IMContactView fContactView, IContact target) {
        this.fContactView = fContactView;
        this.target = target;
    }
    
    private static Notifications instance;
    
    public static Notifications getInstance(Shell parent) {
        if (instance == null || instance.isDisposed()) {
            Shell p = parent == null ? PlatformUI.getWorkbench()
                    .getActiveWorkbenchWindow().getShell() : parent;
            instance = new Notifications(p.getDisplay());
            instance.open();
            instance.layout();
            instance.setFocus();
            instance.setVisible(false);
        }
        return instance;
    }
    
    public static void notify(final String title, final CharSequence text) {
        notify(null, null, title, text);
    }
    
    public static void notify(final IMContactView contactView,
            final IContact target, final String title,
            final CharSequence text) {
        IMPlugin.runOnUI(new Runnable() {
            @Override
            public void run() {
                try {
                    Notifications n = getInstance(null);
                    n.setTarget(contactView, target);
                    n.setMessage(title, text);
                } catch (Exception e) {
                    IMPlugin.getDefault().log("弹出通知窗口异常", e);
                }
            }
        });
    }
}
