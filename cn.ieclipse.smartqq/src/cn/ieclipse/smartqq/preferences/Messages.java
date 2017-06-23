package cn.ieclipse.smartqq.preferences;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "cn.ieclipse.smartqq.preferences.messages"; //$NON-NLS-1$
    public static String HotKeyPreferencePage_desc;
    public static String SmartQQPreferencePage_desc;
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }
    
    private Messages() {
    }
}
