package cn.ieclipse.smartqq.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import cn.ieclipse.smartqq.QQPlugin;

public class RobotPreferencePage extends FieldEditorPreferencePage
        implements IWorkbenchPreferencePage {
        
    public static final String ROBOT_ENABLE = QQPlugin.PLUGIN_ID
            + ".robot.enable"; //$NON-NLS-1$
            
    public static final String ROBOT_NAME = QQPlugin.PLUGIN_ID + ".robot.name"; //$NON-NLS-1$
    public static final String ROBOT_DEPLAY = QQPlugin.PLUGIN_ID
            + ".robot.deplay"; //$NON-NLS-1$
    public static final String TURING_KEY = QQPlugin.PLUGIN_ID + ".turing.key"; //$NON-NLS-1$
    public static final String TURING_API = QQPlugin.PLUGIN_ID + ".turing.api"; //$NON-NLS-1$
    
    public static final String GROUP_WELCOME = QQPlugin.PLUGIN_ID
            + ".group.welcome"; //$NON-NLS-1$
            
    public static final String GROUP_REPLY_ANY = QQPlugin.PLUGIN_ID
            + ".group.reply.any"; //$NON-NLS-1$
            
    public static final String FRIEND_REPLY_ANY = QQPlugin.PLUGIN_ID
            + ".friend.reply.any"; //$NON-NLS-1$
            
    public RobotPreferencePage() {
        super(FLAT);
        setPreferenceStore(QQPlugin.getDefault().getPreferenceStore());
        setDescription(
                Messages.RobotPreferencePage_desc);
    }
    
    @Override
    protected void createFieldEditors() {
        addField(new BooleanFieldEditor(ROBOT_ENABLE, Messages.RobotPreferencePage_enable,
                getFieldEditorParent()));
        addField(new StringFieldEditor(ROBOT_NAME, Messages.RobotPreferencePage_name,
                getFieldEditorParent()));
        addField(new StringFieldEditor(TURING_KEY, Messages.RobotPreferencePage_turing_key,
                getFieldEditorParent()));
        addField(new StringFieldEditor(GROUP_WELCOME, Messages.RobotPreferencePage_member_welcome,
                getFieldEditorParent()));
        addField(new BooleanFieldEditor(GROUP_REPLY_ANY,
                Messages.RobotPreferencePage_reply_member,
                getFieldEditorParent()));
        addField(new BooleanFieldEditor(FRIEND_REPLY_ANY, Messages.RobotPreferencePage_reply_friend,
                getFieldEditorParent()));
    }
    
    @Override
    public void init(IWorkbench workbench) {
        // TODO Auto-generated method stub
        
    }
    
}
