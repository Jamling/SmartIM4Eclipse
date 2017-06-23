package cn.ieclipse.smartqq.console;

import java.io.File;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wb.swt.ResourceManager;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.ieclipse.smartqq.QQPlugin;
import cn.ieclipse.smartqq.preferences.HotKeyFieldEditor;
import cn.ieclipse.smartqq.preferences.HotKeyPreferencePage;

public class InputShell extends Shell {
    
    private Text text;
    
    /**
     * Create the shell.
     * 
     * @param parent
     */
    private InputShell(Shell parent) {
        super(parent, SWT.ON_TOP | SWT.RESIZE);
        createContents();
    }
    
    public String getText() {
        return null;
    }
    
    /**
     * Create contents of the shell.
     */
    protected void createContents() {
        setText("SWT Application");
        setSize(450, 100);
        
        GridLayout gridLayout = new GridLayout(3, false);
        gridLayout.marginHeight = 0;
        gridLayout.marginWidth = 0;
        setLayout(gridLayout);
        
        text = new Text(this, SWT.WRAP | SWT.V_SCROLL | SWT.MULTI);
        text.setBackground(
                SWTResourceManager.getColor(SWT.COLOR_INFO_BACKGROUND));
        text.setForeground(
                SWTResourceManager.getColor(SWT.COLOR_INFO_FOREGROUND));
        // gd_text.widthHint = 300;
        // gd_text.heightHint = 180;
        text.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1));
        
        ToolBar toolBar = new ToolBar(this, SWT.FLAT | SWT.RIGHT);
        GridData gd_toolBar = new GridData(SWT.LEFT, SWT.CENTER, false, false,
                1, 1);
        gd_toolBar.horizontalIndent = 5;
        toolBar.setLayoutData(gd_toolBar);
        
        ToolItem tbClose = new ToolItem(toolBar, SWT.PUSH);
        tbClose.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (!isDisposed()) {
                    dispose();
                }
            }
        });
        // tbClose.setImage(PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_ETOOL_DELETE));
        tbClose.setToolTipText("Close");
        new Label(this, SWT.NONE);
        
        Label label = new Label(this, SWT.NONE);
        label.setLayoutData(
                new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        label.setText("Press 'Ctrl + Enter ' to send");
        
        text.addKeyListener(inputListener);
    }
    
    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
    
    private String input;
    private InputListener inputListener = new InputListener();
    
    private class InputListener implements KeyListener {
        
        @Override
        public void keyPressed(KeyEvent e) {
            String key = HotKeyFieldEditor.keyEvent2String(e);
            IPreferenceStore store = QQPlugin.getDefault().getPreferenceStore();
            if (key.equals(store.getString(HotKeyPreferencePage.KEY_SEND))) {
                e.doit = false;
                input = text.getText();
                if (console != null) {
                    console.writeMine(input);
                    new Thread() {
                        public void run() {
                            console.post(input);
                        };
                    }.start();
                }
                text.setText("");
                setVisible(false);
            }
            else if (key
                    .equals(store.getString(HotKeyPreferencePage.KEY_HIDE))) {
                e.doit = false;
                setVisible(false);
                if (console != null) {
                    console.hide();
                }
            }
            else if (key.equals(
                    store.getString(HotKeyPreferencePage.KEY_HIDE_CLOSE))) {
                e.doit = false;
                setVisible(false);
                if (console != null) {
                    console.close();
                }
            }
            else if (key.equals(
                    store.getString(HotKeyPreferencePage.KEY_INPUT_ESC))) {
                e.doit = false;
                setVisible(false);
            }
        }
        
        @Override
        public void keyReleased(KeyEvent e) {
        
        }
        
    }
    
    private ChatConsole console;
    
    public void setConsole(ChatConsole console) {
        this.console = console;
    }
    
    private static InputShell instance;
    
    public static InputShell getInstance(Shell parent) {
        if (instance == null) {
            instance = new InputShell(parent);
        }
        else if (instance.isDisposed()) {
            instance = new InputShell(parent);
        }
        return instance;
    }
}
