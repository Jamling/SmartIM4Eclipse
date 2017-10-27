package cn.ieclipse.smartim.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import cn.ieclipse.smartim.IMPlugin;

public class RobotPreferencePage extends FieldEditorPreferencePage
        implements IWorkbenchPreferencePage {
        
    public static final String ROBOT_ENABLE = IMPlugin.PLUGIN_ID
            + ".robot.enable"; //$NON-NLS-1$
            
    public static final String ROBOT_NAME = IMPlugin.PLUGIN_ID + ".robot.name"; //$NON-NLS-1$
    public static final String ROBOT_DEPLAY = IMPlugin.PLUGIN_ID
            + ".robot.deplay"; //$NON-NLS-1$
    public static final String TURING_KEY = IMPlugin.PLUGIN_ID + ".turing.key"; //$NON-NLS-1$
    public static final String TURING_API = IMPlugin.PLUGIN_ID + ".turing.api"; //$NON-NLS-1$
    
    public static final String GROUP_WELCOME = IMPlugin.PLUGIN_ID
            + ".group.welcome"; //$NON-NLS-1$
            
    public static final String GROUP_REPLY_ANY = IMPlugin.PLUGIN_ID
            + ".group.reply.any"; //$NON-NLS-1$
            
    public static final String FRIEND_REPLY_ANY = IMPlugin.PLUGIN_ID
            + ".friend.reply.any"; //$NON-NLS-1$
            
    public RobotPreferencePage() {
        super(GRID);
        setPreferenceStore(IMPlugin.getDefault().getPreferenceStore());
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
