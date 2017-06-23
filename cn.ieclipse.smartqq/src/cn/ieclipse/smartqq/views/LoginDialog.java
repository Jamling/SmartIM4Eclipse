package cn.ieclipse.smartqq.views;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import com.scienjus.smartqq.callback.LoginCallback;
import com.scienjus.smartqq.client.SmartQQClient;
import com.scienjus.smartqq.model.UserInfo;

import cn.ieclipse.smartqq.QQPlugin;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.wb.swt.SWTResourceManager;

public class LoginDialog extends Dialog {
    
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
            public void onLogin(UserInfo user) {
                Display.getDefault().asyncExec(new Runnable() {
                    
                    @Override
                    public void run() {
                        close();
                    }
                });
            }
        });
        
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
