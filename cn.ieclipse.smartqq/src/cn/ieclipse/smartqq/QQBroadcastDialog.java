package cn.ieclipse.smartqq;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.layout.TreeColumnLayout;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;

import com.scienjus.smartqq.client.SmartQQClient;
import com.scienjus.smartqq.model.Discuss;
import com.scienjus.smartqq.model.Friend;
import com.scienjus.smartqq.model.Group;

import cn.ieclipse.smartim.actions.BroadcastAction;
import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.model.IMessage;

public class QQBroadcastDialog extends Dialog {
    private Text text;
    private CheckboxTreeViewer ftvFriend;
    private CheckboxTreeViewer ftvRecent;
    private CheckboxTreeViewer ftvGroup;
    private CheckboxTreeViewer ftvDiscuss;
    
    private QQContactView qQContactView;
    
    /**
     * Create the dialog.
     * 
     * @param parentShell
     */
    public QQBroadcastDialog(Shell parentShell, QQContactView qQContactView) {
        super(parentShell);
        this.qQContactView = qQContactView;
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
        
        text = new Text(container, SWT.BORDER | SWT.V_SCROLL | SWT.MULTI);
        GridData gd_text = new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1);
        gd_text.heightHint = 50;
        text.setLayoutData(gd_text);
        text.setToolTipText("Please input your broadcast message here");
        
        TabFolder tabFolder = new TabFolder(container, SWT.NONE);
        tabFolder.setLayoutData(
                new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
                
        // TabItem tiRecents = new TabItem(tabFolder, SWT.NONE);
        // tiRecents.setText("Recents");
        // Composite composite1 = new Composite(tabFolder, SWT.NONE);
        // tiRecents.setControl(composite1);
        // composite1.setLayout(new TreeColumnLayout());
        // ftvRecent = new CheckboxTreeViewer(composite1);
        
        TabItem tiFriends = new TabItem(tabFolder, SWT.NONE);
        tiFriends.setText("Friends");
        Composite composite2 = new Composite(tabFolder, SWT.NONE);
        tiFriends.setControl(composite2);
        composite2.setLayout(new TreeColumnLayout());
        ftvFriend = new CheckboxTreeViewer(composite2, SWT.NONE);
        
        TabItem tiGroups = new TabItem(tabFolder, SWT.NONE);
        tiGroups.setText("Groups");
        Composite composite3 = new Composite(tabFolder, SWT.NONE);
        tiGroups.setControl(composite3);
        composite3.setLayout(new TreeColumnLayout());
        ftvGroup = new CheckboxTreeViewer(composite3, SWT.NONE);
        
        TabItem tiDiscuss = new TabItem(tabFolder, SWT.NONE);
        tiDiscuss.setText("Discuss");
        Composite composite4 = new Composite(tabFolder, SWT.NONE);
        tiDiscuss.setControl(composite4);
        composite4.setLayout(new TreeColumnLayout());
        ftvDiscuss = new CheckboxTreeViewer(composite4, SWT.NONE);
        
        ftvFriend.setContentProvider(
                new QQContentProvider(qQContactView, true));
        ftvFriend.setLabelProvider(new QQLabelProvider(qQContactView));
        ftvGroup.setContentProvider(
                new QQContentProvider(qQContactView, true));
        ftvGroup.setLabelProvider(new QQLabelProvider(qQContactView));
        ftvDiscuss.setContentProvider(
                new QQContentProvider(qQContactView, true));
        ftvDiscuss.setLabelProvider(new QQLabelProvider(qQContactView));
        
        ftvFriend.setInput("friend");
        ftvGroup.setInput("group");
        ftvDiscuss.setInput("discuss");
        
        ftvFriend.addCheckStateListener(checkListener);
        ftvGroup.addCheckStateListener(checkListener);
        ftvDiscuss.addCheckStateListener(checkListener);
        
        ftvGroup.expandAll();
        ftvDiscuss.expandAll();
        
        return container;
    }
    
    ICheckStateListener checkListener = new ICheckStateListener() {
        public void checkStateChanged(CheckStateChangedEvent arg0) {
            CheckboxTreeViewer checkboxTreeViewer = (CheckboxTreeViewer) arg0
                    .getSource();
            boolean checked = arg0.getChecked();
            checkboxTreeViewer.setSubtreeChecked(arg0.getElement(), checked);
            
            TreeItem ti = (TreeItem) checkboxTreeViewer
                    .testFindItem(arg0.getElement());
            checkboxTreeViewer.getTree().setSelection(ti);
            TreeItem parent = ti.getParentItem();
            if (parent == null) {
                return;
            }
            TreeItem[] items = parent.getItems();
            int checkItems = 0;
            for (TreeItem treeItem : items) {
                if (treeItem.getChecked() && !treeItem.getGrayed()) {
                    checkItems = checkItems + 1;
                }
            }
            ti.setChecked(checked);
            if (checkItems == 0) {
                parent.setChecked(false);
                return;
            }
            if (checkItems == items.length) {
                parent.setGrayed(false);
                parent.setChecked(true);
                return;
            }
            if (checkItems != items.length) {
                parent.setChecked(true);
                parent.setGrayed(true);
                return;
            }
        }
    };
    
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
    
    @Override
    protected void okPressed() {
        final String text = this.text.getText().trim();
        if (IMUtils.isEmpty(text)) {
            return;
        }
        final List<Object> target = new ArrayList<>();
        target.addAll(Arrays.asList(ftvFriend.getCheckedElements()));
        target.addAll(Arrays.asList(ftvGroup.getCheckedElements()));
        target.addAll(Arrays.asList(ftvDiscuss.getCheckedElements()));
        new Thread() {
            public void run() {
                sendInternal(text, target);
            };
        }.start();
        super.okPressed();
    }
    
    /**
     * Return the initial size of the dialog.
     */
    @Override
    protected Point getInitialSize() {
        return new Point(450, 500);
    }
    
    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("QQ消息群发");
    }
    
    private void sendInternal(String text, List<Object> targets) {
        // ((SmartQQClient) qQContactView.getClient()).broadcast(text,
        // targets.toArray());
        SmartQQClient client = qQContactView.getClient();
        int ret = 0;
        if (targets != null) {
            for (Object obj : targets) {
                if (obj != null && obj instanceof IContact) {
                    IContact target = (IContact) obj;
                    try {
                        IMessage m = createMessage(text, target, client);
                        client.sendMessage(m, target);
                        ret++;
                    } catch (Exception e) {
                    
                    }
                }
            }
        }
    }
    
    private IMessage createMessage(String text, IContact target,
            SmartQQClient client) {
        IMessage m = null;
        if (!client.isClose()) {
            if (target instanceof Friend) {
                m = client.createMessage(text, target);
            }
            else if (target instanceof Group || target instanceof Discuss) {
                String msg = text.replace(BroadcastAction.groupMacro,
                        target.getName());
                m = client.createMessage(msg, target);
            }
        }
        return m;
    }
}
