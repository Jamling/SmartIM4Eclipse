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
    public static final String ROBOT_DEPLAY = QQPlugin.PLUGIN_ID
            + ".robot.deplay";
    public static final String TURING_KEY = QQPlugin.PLUGIN_ID + ".turing.key";
    public static final String TURING_API = QQPlugin.PLUGIN_ID
            + ".turing.api";
            
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
        // addField(new BooleanFieldEditor(ROBOT_DEPLAY, "Enable the robot",
        // getFieldEditorParent()));
        addField(new StringFieldEditor(TURING_KEY, "Turing Key",
                getFieldEditorParent()));
    }
    
    @Override
    public void init(IWorkbench workbench) {
        // TODO Auto-generated method stub
        
    }
    
}
