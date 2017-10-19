package cn.ieclipse.smartim.dialogs;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.ieclipse.smartim.IMPlugin;
import cn.ieclipse.smartim.SmartClient;
import cn.ieclipse.smartim.callback.LoginCallback;

public class LoginDialog extends Dialog {
    private SmartClient client;
    private Text text;
    private Label qrcode;
    
    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public LoginDialog(Shell parentShell) {
        super(parentShell);
    }
    
    /**
     * Create contents of the dialog.
     * 
     * @param parent
     */
    @Override
    protected Control createDialogArea(Composite parent) {
        Composite container = (Composite) super.createDialogArea(parent);
        container.setLayout(new GridLayout(1, false));
        
        qrcode = new Label(container, SWT.NONE);
        qrcode.setAlignment(SWT.CENTER);
        qrcode.setImage(SWTResourceManager.getImage(LoginDialog.class,
                "/icons/full/progress/waiting.gif"));
        qrcode.setLayoutData(
                new GridData(SWT.CENTER, SWT.CENTER, true, true, 1, 1));
                
        text = new Text(container, SWT.READ_ONLY | SWT.WRAP);
        text.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
        text.setLayoutData(
                new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
        new Thread() {
            public void run() {
                doLogin();
            }
        }.start();
        
        return container;
    }
    
    /**
     * Create contents of the button bar.
     * 
     * @param parent
     */
    @Override
    protected void createButtonsForButtonBar(Composite parent) {
        createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL,
                true);
        createButton(parent, IDialogConstants.CANCEL_ID,
                IDialogConstants.CANCEL_LABEL, false);
    }
    
    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(450, 300);
    }
    
    public void setClient(SmartClient client) {
        this.client = client;
    }
    
    protected void doLogin() {
        if (client == null) {
            return;
        }
        LoginCallback callback = new LoginCallback() {
            @Override
            public void onQrcode(final String path) {
                Display.getDefault().asyncExec(new Runnable() {
                    
                    @Override
                    public void run() {
                        if (qrcode == null || qrcode.isDisposed()) {
                            return;
                        }
                        Image image;
                        try {
                            image = new Image(Display.getDefault(),
                                    new FileInputStream(path));
                            Point p = qrcode.getParent().getSize();
                            Rectangle p2 = image.getBounds();
                            int x = (p.x - p2.width) >> 1;
                            int y = (p.y - p2.height) >> 1;
                            
                            if (x < 0 || y < 0) {
                                qrcode.setBounds(new Rectangle(0, 0, p2.width,
                                        p2.height));
                            }
                            else {
                                Rectangle p3 = new Rectangle(x, y, p2.width,
                                        p2.height);
                                qrcode.setBounds(p3);
                            }
                            qrcode.setImage(image);

                            getShell().pack();
                        } catch (FileNotFoundException e) {
                            if (text == null || text.isDisposed()) {
                                return;
                            }
                            text.setText("二维码图片不存在，请确认" + path + "目录可读写");
                        }
                    }
                });
            }
            
            @Override
            public void onLogin(final boolean ok, final Exception e) {
                Display.getDefault().asyncExec(new Runnable() {
                    
                    @Override
                    public void run() {
                        if (ok) {
                            close();
                        }
                        else {
                            if (text == null || text.isDisposed()) {
                                return;
                            }
                            text.setText(e == null ? "" : e.toString());
                            IMPlugin.getDefault().log("登录失败", e);
                        }
                    }
                });
            }
        };
        client.setLoginCallback(callback);
        client.login();
    }
}
