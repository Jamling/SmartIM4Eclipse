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
            + ".robot.enable";
            
    public static final String ROBOT_NAME = QQPlugin.PLUGIN_ID + ".robot.name";
    public static final String ROBOT_DEPLAY = QQPlugin.PLUGIN_ID
            + ".robot.deplay";
    public static final String TURING_KEY = QQPlugin.PLUGIN_ID + ".turing.key";
    public static final String TURING_API = QQPlugin.PLUGIN_ID + ".turing.api";
    
    public static final String GROUP_WELCOME = QQPlugin.PLUGIN_ID
            + ".group.welcome";
            
    public static final String GROUP_REPLY_ANY = QQPlugin.PLUGIN_ID
            + ".group.apply.any";
            
    public RobotPreferencePage() {
        super(FLAT);
        setPreferenceStore(QQPlugin.getDefault().getPreferenceStore());
        setDescription(
                "Robot settings, the robot can help you to answer the person who @ you at certain time");
    }
    
    @Override
    protected void createFieldEditors() {
        addField(new BooleanFieldEditor(ROBOT_ENABLE, "Enable the robot",
                getFieldEditorParent()));
        addField(new StringFieldEditor(ROBOT_NAME, "Robot name",
                getFieldEditorParent()));
        addField(new StringFieldEditor(TURING_KEY, "Turing Key",
                getFieldEditorParent()));
        addField(new StringFieldEditor(GROUP_WELCOME, "New member welcome",
                getFieldEditorParent()));
        addField(new BooleanFieldEditor(GROUP_REPLY_ANY, "Reply any member",
                getFieldEditorParent()));
    }
    
    @Override
    public void init(IWorkbench workbench) {
        // TODO Auto-generated method stub
        
    }
    
}
