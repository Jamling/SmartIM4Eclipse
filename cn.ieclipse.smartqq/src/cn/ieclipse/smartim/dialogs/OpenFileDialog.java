/*
 * Copyright 2014-2017 ieclipse.cn.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ieclipse.smartim.dialogs;

import java.io.File;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import cn.ieclipse.pde.explorer.ExplorerPlugin;
import cn.ieclipse.smartim.common.IDEUtils;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2018年2月8日
 *       
 */
public class OpenFileDialog extends MessageDialog {
    int selection = 0;
    
    public OpenFileDialog(Shell parent, String title, String message) {
        super(parent, title, null, message, CONFIRM, new String[] {
                IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL }, 0);
    }
    
    @Override
    protected Control createCustomArea(Composite parent) {
        Composite composite = new Composite(parent, SWT.NONE);
        FillLayout fl_composite = new FillLayout(SWT.VERTICAL);
        fl_composite.spacing = 10;
        fl_composite.marginHeight = 10;
        composite.setLayout(fl_composite);
        composite.setLayoutData(
                new GridData(SWT.FILL, SWT.TOP, true, true, 1, 1));
                
        rb1 = new Button(composite, SWT.RADIO);
        rb1.setSelection(true);
        rb1.setText("Eclipse Explorer 在操作系统的浏览器中打开，可以对文件自由操作");
        rb1.addSelectionListener(listener);
        
        rb2 = new Button(composite, SWT.RADIO);
        rb2.setText("Eclipse 内置浏览器，适用于可查看的文件，比如文本，图像");
        rb2.addSelectionListener(listener);
        
        rb3 = new Button(composite, SWT.RADIO);
        rb3.setText("操作系统默认浏览器，适用范围同上");
        rb3.addSelectionListener(listener);
        
        return composite;
    }
    
    private SelectionListener listener = new SelectionAdapter() {
        public void widgetSelected(org.eclipse.swt.events.SelectionEvent e) {
            if (e.widget == rb1) {
                selection = 0;
            }
            else if (e.widget == rb2) {
                selection = 1;
            }
            else if (e.widget == rb3) {
                selection = 2;
            }
        };
    };
    private Button rb1;
    private Button rb2;
    private Button rb3;
    
    public static boolean open(Shell shell, String url) {
        String title = "请选择打开方式";
        String message = "SmartQQ检测到您试图打开一个本地文件，请选择以下打开方式";
        OpenFileDialog dialog = new OpenFileDialog(
                shell == null ? new Shell() : shell, title, message);
        if (dialog.open() == OK) {
            if (dialog.selection == 0) {
                String file = url.substring("file://".length());
                if (ExplorerPlugin.getOS() == ExplorerPlugin.OS_WINDOWS) {
                    if (file.startsWith("/")) {
                        file = file.substring(1);
                        // file = file.replace("/", "\\");
                    }
                }
                File f = new File(file);
                ExplorerPlugin.explorer(f.getParent(), f.getAbsolutePath());
                return true;
            }
            else if (dialog.selection == 1) {
                return IDEUtils.openInternalBrowser(url, false);
            }
            else if (dialog.selection == 2) {
                return IDEUtils.openInternalBrowser(url, true);
            }
        }
        return false;
    }
    
    public static void main(String[] args) {
        new OpenFileDialog(new Shell(), "title", "message").open();
        Display display = Display.getDefault();
        while (!display.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }
}
