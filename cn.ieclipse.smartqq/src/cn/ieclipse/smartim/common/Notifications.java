package cn.ieclipse.smartim.common;

import java.util.Timer;
import java.util.TimerTask;

import org.eclipse.swt.SWT;
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
            new Thread() {
                public void run() {
                    IMPlugin.runOnUI(new Runnable() {
                        
                        @Override
                        public void run() {
                            Notifications.getInstance(new Shell(display))
                                    .setMessage(null, "test");
                        }
                    });
                };
            }.start();
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private Label fText;
    private TranslateAnimation ta;
    private Timer timer;
    
    /**
     * Create the shell.
     * 
     * @param display
     */
    public Notifications(Display display) {
        super(display, SWT.ON_TOP | SWT.CLOSE | SWT.TITLE);
        try {
            org.eclipse.swt.internal.win32.OS.SetWindowPos(this.handle,
                    org.eclipse.swt.internal.win32.OS.HWND_TOPMOST, 0, 0, 0, 0,
                    SWT.NULL);
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
        
        Rectangle screen = Display.getDefault().getClientArea();
        ta = new TranslateAnimation(screen.width, screen.width - getSize().x,
                screen.height, screen.height - getSize().y).setTarget(this)
                        .setDuration(300);
        setLocation(screen.width, screen.height);
    }
    
    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
    
    public void setMessage(final String title, final CharSequence text) {
        final boolean enable = IMPlugin.getDefault().getPreferenceStore()
                .getBoolean(SettingsPerferencePage.NOTIFY_ENABLE);
        final int dismiss = IMPlugin.getDefault().getPreferenceStore()
                .getInt(SettingsPerferencePage.NOTIFY_DISMISS);
        if (!enable) {
            return;
        }
        
        doSetMessage(title, text);
        // int dismiss = 5;
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
        }
        this.fText.setText(text == null ? "" : text.toString());
    }
    
    private IMContactView fContactView;
    private IContact target;
    
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
