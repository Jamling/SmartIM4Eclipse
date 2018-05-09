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
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.ieclipse.smartim.console.IMChatConsole;
import cn.ieclipse.smartim.views.IMContactView;
import cn.ieclipse.smartqq.QQContactView;
import cn.ieclipse.wechat.WXContactView;

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
    protected Control createDialogArea(final Composite parent) {
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
        GridData gd_styledText = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
        gd_styledText.minimumHeight = 100;
        styledText.setLayoutData(gd_styledText);
                
        initData();
        
        Label lblNewLabel_2 = new Label(container, SWT.NONE);
        lblNewLabel_2.setText("Send to");
        
        final ScrolledComposite scrolledComposite = new ScrolledComposite(container, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
        scrolledComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        scrolledComposite.setExpandHorizontal(true);
        scrolledComposite.setExpandVertical(true);
        
        Composite composite = new Composite(scrolledComposite, SWT.NONE);
        composite.setLayout(new FillLayout(SWT.VERTICAL));
        createSendTarget(composite);
        scrolledComposite.setContent(composite);
        // scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
        scrolledComposite.addControlListener(new ControlAdapter() {  
            public void controlResized(ControlEvent e) {  
                Rectangle r = scrolledComposite.getClientArea();  
                scrolledComposite.setMinSize(parent.computeSize(r.width,  
                        SWT.DEFAULT));  
            }  
        });
        
        return container;
    }
    
    private void createSendTarget(Composite composite) {
        IWorkbenchPage page = PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getActivePage();
        String[] ids = new String[] { QQContactView.ID, WXContactView.ID };
        int count = 0;
        for (String id : ids) {
            IViewPart view = page.findView(id);
            if (view != null) {
                IMContactView contactView = (IMContactView) view;
                List<IMChatConsole> chats = contactView.getConsoleList();
                if (!chats.isEmpty()) {
                    Group group = new Group(composite, SWT.NONE);
                    group.setText(contactView.getTitle());
                    group.setLayout(new RowLayout());
                    for (final IMChatConsole console : chats) {
                        Button btn = new Button(group, SWT.CHECK);
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
                    }
                }
                else {
                    count++;
                }
            }
        }
        if (count == ids.length) {
            new Label(composite, SWT.NONE).setText("暂无聊天会话，无法发送！请先打开聊天会话窗口再试");
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
            console.send(msg);
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
