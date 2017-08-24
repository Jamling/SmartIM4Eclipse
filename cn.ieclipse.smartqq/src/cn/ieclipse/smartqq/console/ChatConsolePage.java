package cn.ieclipse.smartqq.console;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.custom.VerifyKeyListener;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.dnd.FileTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.dnd.TransferData;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.VerifyEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.IHyperlink;
import org.eclipse.ui.console.TextConsolePage;
import org.eclipse.ui.console.TextConsoleViewer;
import org.eclipse.ui.part.ResourceTransfer;

import cn.ieclipse.smartqq.QQPlugin;
import cn.ieclipse.smartqq.preferences.HotKeyFieldEditor;
import cn.ieclipse.smartqq.preferences.HotKeyPreferencePage;
import cn.ieclipse.smartqq.views.ReviewDialog;

public class ChatConsolePage extends TextConsolePage {
    private ChatConsole fConsole;
    
    public ChatConsolePage(ChatConsole console, IConsoleView view) {
        super(console, view);
        fConsole = console;
    }
    
    protected void pasteResouce(IResource src) {
        if (!(src instanceof IFile)) {
            return;
        }
        String path = src.getLocation().toString();
        String send = "发送";
        String review = "Code Review";
        String msg = String.format("文件：%s，请选择操作：%s or %s\n", path, send,
                review);
        int line = getViewer().getDocument().getLength();
        getViewer().getTextWidget().append(msg);
        int pos = msg.lastIndexOf(send);
        try {
            fConsole.addHyperlink(new SendLink(src.getLocation().toOSString()),
                    line + pos, send.length());
            pos = msg.lastIndexOf(review);
            fConsole.addHyperlink(new ReviewLink((IFile) src), line + pos,
                    review.length());
        } catch (BadLocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    protected void pasteFile(String src) {
        String send = "发送";
        String msg = String.format("确认发送文件(%s)吗？%s\n", src, send);
        int line = getViewer().getDocument().getLength();
        getViewer().getTextWidget().append(msg);
        int pos = msg.lastIndexOf(send);
        try {
            fConsole.addHyperlink(new SendLink(src), line + pos, send.length());
        } catch (BadLocationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    protected TextConsoleViewer createViewer(Composite parent) {
        return new TextConsoleViewer(parent, fConsole, getConsoleView()) {
            @Override
            public void doOperation(int operation) {
                if (operation == PASTE) {
                    Clipboard cb = new Clipboard(getControl().getDisplay());
                    Transfer transfer = ResourceTransfer.getInstance();
                    if (isAvalible(cb, transfer)) {
                        IResource[] obj = (IResource[]) cb
                                .getContents(transfer);
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
                    return;
                }
                super.doOperation(operation);
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
        };
    }
    
    @Override
    public void createControl(final Composite parent) {
        super.createControl(parent);
        
        final StyledText text = getViewer().getTextWidget();
        InputListener listener = new InputListener();
        text.addKeyListener(listener);
        text.addVerifyKeyListener(listener);
    }
    
    private class InputListener implements KeyListener, VerifyKeyListener {
        
        IPreferenceStore store = QQPlugin.getDefault().getPreferenceStore();
        
        @Override
        public void keyReleased(KeyEvent e) {
            e.doit = false;
        }
        
        @Override
        public void keyPressed(KeyEvent e) {
            String key = HotKeyFieldEditor.keyEvent2String(e);
            System.out.println("input " + key);
        }
        
        @Override
        public void verifyKey(VerifyEvent e) {
            String key = HotKeyFieldEditor.keyEvent2String(e);
            System.out.println("verify " + key);
            if (key.equals(store.getString(HotKeyPreferencePage.KEY_INPUT))) {
                fConsole.activeInput();
            }
            else if (key
                    .equals(store.getString(HotKeyPreferencePage.KEY_NEXT))) {
                QQPlugin.getDefault().nextConsole(fConsole, true);
            }
            else if (key
                    .equals(store.getString(HotKeyPreferencePage.KEY_PREV))) {
                QQPlugin.getDefault().nextConsole(fConsole, false);
            }
            else if (key
                    .equals(store.getString(HotKeyPreferencePage.KEY_HIDE))) {
                fConsole.toggleHide();
            }
            else if (key.equals(
                    store.getString(HotKeyPreferencePage.KEY_HIDE_CLOSE))) {
                fConsole.toggleClose();
            }
            e.doit = false;
        }
    }
    
    private class SendLink implements IHyperlink {
        String path;
        
        public SendLink(String path) {
            this.path = path;
        }
        
        @Override
        public void linkExited() {
        }
        
        @Override
        public void linkEntered() {
        }
        
        @Override
        public void linkActivated() {
            if (path != null) {
                fConsole.sendFile(path);
            }
        }
    };
    
    private class ReviewLink implements IHyperlink {
        IFile path;
        
        public ReviewLink(IFile path) {
            this.path = path;
        }
        
        @Override
        public void linkExited() {
        }
        
        @Override
        public void linkEntered() {
        }
        
        @Override
        public void linkActivated() {
            if (path != null) {
                ReviewDialog dialog = new ReviewDialog(getControl().getShell());
                dialog.setData(path, null);
                dialog.open();
            }
        }
    };
}
