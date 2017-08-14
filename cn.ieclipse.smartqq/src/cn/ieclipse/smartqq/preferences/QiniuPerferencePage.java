package cn.ieclipse.smartqq.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.ComboFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import cn.ieclipse.smartqq.QQPlugin;

public class QiniuPerferencePage extends FieldEditorPreferencePage
        implements IWorkbenchPreferencePage {
        
    public static final String AK = QQPlugin.PLUGIN_ID + ".qn.accessKey";
    public static final String SK = QQPlugin.PLUGIN_ID + ".qn.setretKey";
    public static final String BUCKET = QQPlugin.PLUGIN_ID + ".qn.bucket";
    public static final String ZONE = QQPlugin.PLUGIN_ID + ".qn.zone";
    public static final String DOMAIN = QQPlugin.PLUGIN_ID + ".qn.domain";
    public static final String TS = QQPlugin.PLUGIN_ID + ".qn.ts";
    public static final String ENABLE = QQPlugin.PLUGIN_ID + ".qn.enable";
    
    public static final String[][] ZONE_VALUE = { { "自动", "autoZone" },
            { "华东", "huadong" }, { "华北", "huabei" }, { "华南", "huanan" },
            { "北美", "beimei" } };
            
    /**
     * Create the preference page.
     */
    public QiniuPerferencePage() {
        super(FLAT);
        setPreferenceStore(QQPlugin.getDefault().getPreferenceStore());
        setDescription("因SmartQQ不支持文件上传，所以本插件先将文件上传到七牛云，然后再将文件的URL发送出去\n"
                + "如果您未设置七牛云储存，发送的文件将上传到本人私有储存空间（temp.ieclipse.cn），上传的文件的大小及保留天数将有限制，建议您自己注册七牛云并"
                + "\n注：如果使用您自己的七牛云，带*的accessKey和secretKey必填，否则不生效哦");
    }
    
    /**
     * Create contents of the preference page.
     */
    @Override
    protected void createFieldEditors() {
        // Create the field editors
        addField(new BooleanFieldEditor(ENABLE, "启用七牛云",
                getFieldEditorParent()));
        ComboFieldEditor zone = new ComboFieldEditor(ZONE, "机房", ZONE_VALUE,
                getFieldEditorParent());
        addField(new StringFieldEditor(AK, "AccessKey *",
                getFieldEditorParent()));
        addField(new StringFieldEditor(SK, "SecretKey *",
                getFieldEditorParent()));
        addField(zone);
        addField(new StringFieldEditor(BUCKET, "存储空间", getFieldEditorParent()));
        addField(new StringFieldEditor(DOMAIN, "空间域名", getFieldEditorParent()));
        addField(new BooleanFieldEditor(TS, "给上传的文件添加时间戳（下载时强制更新缓存）",
                getFieldEditorParent()));
    }
    
    /**
     * Initialize the preference page.
     */
    public void init(IWorkbench workbench) {
        // Initialize the preference page
        
    }
    
}
