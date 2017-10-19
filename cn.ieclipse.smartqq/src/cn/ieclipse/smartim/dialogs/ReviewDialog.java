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

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.ieclipse.smartim.IMPlugin;
import cn.ieclipse.smartim.console.IMChatConsole;
import swing2swt.layout.FlowLayout;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年8月22日
 *       
 */
public class ReviewDialog extends Dialog {
    private Text text;
    StyledText styledText;
    ArrayList<IMChatConsole> consoles = new ArrayList<>();
    IFile file;
    TextSelection ts;
    
    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public ReviewDialog(Shell parentShell) {
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
        container.setLayout(new GridLayout(2, false));
        
        Label lblNewLabel = new Label(container, SWT.NONE);
        lblNewLabel.setLayoutData(
                new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
        lblNewLabel.setText("Location");
        
        text = new Text(container, SWT.WRAP | SWT.MULTI);
        text.setEditable(false);
        text.setForeground(
                SWTResourceManager.getColor(SWT.COLOR_LINK_FOREGROUND));
        text.setLayoutData(
                new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
                
        Label lblNewLabel_1 = new Label(container, SWT.NONE);
        lblNewLabel_1.setText("Content");
        
        styledText = new StyledText(container,
                SWT.BORDER | SWT.WRAP | SWT.V_SCROLL);
        styledText.setLayoutData(
                new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
                
        Group composite = new Group(container, SWT.NONE);
        composite.setText("Send to");
        composite.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
        composite.setLayoutData(
                new GridData(SWT.FILL, SWT.FILL, true, true, 2, 1));
                
        initData();
        createSendTarget(composite);
        
        return container;
    }
    
    private void createSendTarget(Group composite) {
        IConsoleManager manager = ConsolePlugin.getDefault()
                .getConsoleManager();
                
        IConsole[] existing = manager.getConsoles();
        for (int i = 0; i < existing.length; i++) {
            if (existing[i] instanceof IMChatConsole) {
                final IMChatConsole console = (IMChatConsole) existing[i];
                Button btn = new Button(composite, SWT.CHECK);
                btn.setText(console.getName());
                btn.setData(console);
                btn.addSelectionListener(new SelectionAdapter() {
                    @Override
                    public void widgetSelected(SelectionEvent e) {
                        Button source = (Button) e.getSource();
                        if (source.getSelection()) {
                            if (!consoles.contains(console)) {
                                consoles.add(console);
                            }
                        }
                        else {
                            consoles.remove(console);
                        }
                        getButton(IDialogConstants.OK_ID)
                                .setEnabled(!consoles.isEmpty());
                    }
                });
                
                if (console == IMPlugin.getDefault().console) {
                    consoles.add(console);
                    btn.setSelection(true);
                }
            }
        }
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
                
        getButton(IDialogConstants.OK_ID).setEnabled(!consoles.isEmpty());
    }
    
    @Override
    protected void okPressed() {
        String msg = String.format("%s(Reviews: %s)", text.getText(),
                styledText.getText());
        for (IMChatConsole console : consoles) {
            console.sendMsg(msg);
        }
        super.okPressed();
    }
    
    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(450, 300);
    }
    
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Code Review");
    }
    
    public void setData(IFile file, TextSelection ts) {
        this.file = file;
        this.ts = ts;
    }
    
    private void initData() {
        if (file != null) {
            int line = ts == null ? 0 : ts.getStartLine();
            line++;
            String text = ts == null ? "" : ts.getText();
            String msg = String.format("Code: %s:%s ", file.getFullPath(),
                    line);
            this.text.setText(msg);
            styledText.setText(text);
        }
    }
}
