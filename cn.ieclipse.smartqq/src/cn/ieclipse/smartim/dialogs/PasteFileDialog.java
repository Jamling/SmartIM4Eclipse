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
public class PasteFileDialog extends MessageDialog {
    int selection = 0;
    
    public PasteFileDialog(Shell parent, String title, String message) {
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
        rb1.setText("发送文件，文件上传成功后将发送给对方");
        rb1.addSelectionListener(listener);
        
        rb2 = new Button(composite, SWT.RADIO);
        rb2.setText("代码评审，发送代码位置，并输入评语");
        rb2.addSelectionListener(listener);
        
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
        };
    };
    private Button rb1;
    private Button rb2;
    
    public static int open(Shell shell, String url) {
        String title = "请选择操作";
        String message = String.format("SmartQQ检测到您粘贴了一个工作区中的文件(%s)，请选择以下操作",
                url);
        PasteFileDialog dialog = new PasteFileDialog(
                shell == null ? new Shell() : shell, title, message);
        if (dialog.open() == OK) {
            return dialog.selection;
        }
        return -1;
    }
    
    public static void main(String[] args) {
        new PasteFileDialog(new Shell(), "title", "message").open();
        Display display = Display.getDefault();
        while (!display.isDisposed()) {
            if (!display.readAndDispatch()) {
                display.sleep();
            }
        }
    }
}
