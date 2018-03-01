/*
 * Copyright 2014-2018 ieclipse.cn.
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
package cn.ieclipse.smartim.console;

import java.lang.reflect.Method;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.eclipse.ui.actions.RetargetAction;
import org.eclipse.ui.internal.WorkbenchMessages;
import org.eclipse.ui.part.ResourceTransfer;

import cn.ieclipse.smartim.actions.ClearHistoryAction;
import cn.ieclipse.smartim.dialogs.PasteFileDialog;
import cn.ieclipse.smartim.dialogs.ReviewDialog;
import cn.ieclipse.smartim.preferences.HotKeyFieldEditor;
import cn.ieclipse.util.StringUtils;
import icons.SmartIcons;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2018年2月27日
 *       
 */
public class HistoryOperation {
    private Browser browser;
    private Text input;
    private IMChatConsole console;
    
    public HistoryOperation(Browser browser, IMChatConsole console,
            Text input) {
        this.browser = browser;
        this.console = console;
        this.input = input;
        init();
    }
    
    private void init() {
        final MenuManager manager = new MenuManager();
        Menu menu = manager.createContextMenu(browser);
        browser.setMenu(menu);
        // manager.add(new CopyAction());
        final PasteAction paste = new PasteAction();
        paste.addTo(manager);
        manager.add(new ClearHistoryAction(console));
        
        // browser.addMouseListener(new MouseAdapter() {
        // public void mouseDown(MouseEvent event) {
        // if (event.button == 3) {
        // browser.execute(
        // "document.oncontextmenu = function() {return false;}");
        // manager.update(true);
        // }
        // }
        // });
        
        browser.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (paste.getAccelerator() == (e.keyCode | e.stateMask)) {
                    doPaste();
                    return;
                }
                String key = HotKeyFieldEditor.keyEvent2String(e);
                if (key.equals("Ctrl + V")) {
                    doPaste();
                }
            }
        });
    }
    
    private void doPaste() {
        Clipboard cb = new Clipboard(browser.getDisplay());
        Transfer transfer = ResourceTransfer.getInstance();
        if (isAvalible(cb, transfer)) {
            IResource[] obj = (IResource[]) cb.getContents(transfer);
            if (obj != null && obj[0] != null) {
                IResource src = obj[0];
                pasteResouce(src);
                return;
            }
        }
        transfer = FileTransfer.getInstance();
        if (isAvalible(cb, transfer)) {
            Object obj = cb.getContents(transfer);
            if (obj != null) {
                pasteFile(((String[]) obj)[0]);
                return;
            }
        }
        transfer = TextTransfer.getInstance();
        if (isAvalible(cb, transfer)) {
            Object obj = cb.getContents(transfer);
            if (obj != null && obj instanceof String) {
                String text = (String) obj;
                if (!StringUtils.isEmpty(text)) {
                    if (input != null) {
                        input.append(text);
                    }
                }
            }
        }
        return;
    }
    
    private boolean isAvalible(Clipboard cb, Transfer transfer) {
        TransferData[] tds = cb.getAvailableTypes();
        for (TransferData td : tds) {
            if (transfer.isSupportedType(td)) {
                return true;
            }
        }
        return false;
    }
    
    protected void pasteResouce(IResource src) {
        if (!(src instanceof IFile)) {
            return;
        }
        String path = src.getLocation().toString();
        int op = PasteFileDialog.open(browser.getShell(), path);
        if (op == 0) {
            if (console != null) {
                console.sendFile(src.getLocation().toOSString());
            }
        }
        else if (op == 1) {
            ReviewDialog dialog = new ReviewDialog(browser.getShell());
            dialog.setData((IFile) src, null);
            dialog.open();
        }
    }
    
    protected void pasteFile(String src) {
        String msg = String.format("确认发送文件(%s)吗？\n", src);
        boolean ok = MessageDialog.openConfirm(browser.getShell(), "发送文件", msg);
        if (ok) {
            if (console != null) {
                console.sendFile(src);
            }
        }
    }
    
    class CopyAction extends Action {
        public CopyAction() {
            setText("复制");
            setImageDescriptor(SmartIcons.copy);
            setAccelerator(SWT.CTRL + 'C');
        }
        
        @Override
        public void run() {
            super.run();
        }
        
        @Override
        public boolean isEnabled() {
            return true;
        }
    }
    
    class PasteAction extends Action {
        public PasteAction() {
            setAccelerator(SWT.CTRL + 'V');
            setId(ActionFactory.PASTE.getId());
            setActionDefinitionId(ActionFactory.PASTE.getCommandId());
        }
        
        public void init() {
            setText(WorkbenchMessages.Workbench_paste);
            setToolTipText(WorkbenchMessages.Workbench_pasteToolTip);
            setImageDescriptor(SmartIcons.paste);
        }
        
        public void addTo(MenuManager manager) {
            final IWorkbenchAction action = ActionFactory.PASTE.create(
                    PlatformUI.getWorkbench().getActiveWorkbenchWindow());
            if (action instanceof RetargetAction) {
                try {
                    Method m = action.getClass().getDeclaredMethod(
                            "setActionHandler", IAction.class);
                    m.setAccessible(true);
                    m.invoke(action, this);
                    manager.add(action);
                } catch (Exception e) {
                    init();
                    manager.add(this);
                }
            }
        }
        
        @Override
        public boolean isEnabled() {
            Clipboard cb = new Clipboard(browser.getDisplay());
            Transfer transfer = ResourceTransfer.getInstance();
            return isAvalible(cb, transfer)
                    || isAvalible(cb, FileTransfer.getInstance())
                    || isAvalible(cb, TextTransfer.getInstance());
        }
        
        // @Override
        // public void runWithEvent(Event event) {
        // System.out.println(event);
        // }
        
        @Override
        public void run() {
            doPaste();
        }
    }
}
