package cn.ieclipse.smartqq.views;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
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

import com.scienjus.smartqq.callback.LoginCallback;

import cn.ieclipse.smartqq.QQPlugin;

public class LoginDialog extends Dialog {
    private Text text;
    
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
        
        final Label qrcode = new Label(container, SWT.NONE);
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
                QQPlugin.getDefault().login(new LoginCallback() {
                    
                    @Override
                    public void onQrcode(final String path) {
                        Display.getDefault().asyncExec(new Runnable() {
                            
                            @Override
                            public void run() {
                                if (qrcode.isDisposed()) {
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
                                    
                                    Rectangle p3 = new Rectangle(x, y, p2.width,
                                            p2.height);
                                    qrcode.setBounds(p3);
                                    qrcode.setImage(image);
                                } catch (FileNotFoundException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
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
                                    text.setText(e.toString());
                                    IStatus info = new Status(IStatus.ERROR,
                                            QQPlugin.PLUGIN_ID,
                                            "登录失败" + e.toString());
                                    QQPlugin.getDefault().getLog().log(info);
                                }
                            }
                        });
                    }
                });
            };
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
}
