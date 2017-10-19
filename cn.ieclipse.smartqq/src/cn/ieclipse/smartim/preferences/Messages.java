package cn.ieclipse.smartim.preferences;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
    private static final String BUNDLE_NAME = "cn.ieclipse.smartim.preferences.messages"; //$NON-NLS-1$
    public static String HotKeyPreferencePage_close_chat;
    public static String HotKeyPreferencePage_desc;
    public static String HotKeyPreferencePage_hide_chat;
    public static String HotKeyPreferencePage_hide_input;
    public static String HotKeyPreferencePage_input;
    public static String HotKeyPreferencePage_next_chat;
    public static String HotKeyPreferencePage_prev_chat;
    public static String HotKeyPreferencePage_send;
    public static String PreferenceInitializer_robot_group_welcome;
    public static String PreferenceInitializer_robot_name;
    public static String RobotPreferencePage_desc;
    public static String RobotPreferencePage_enable;
    public static String RobotPreferencePage_member_welcome;
    public static String RobotPreferencePage_name;
    public static String RobotPreferencePage_reply_friend;
    public static String RobotPreferencePage_reply_member;
    public static String RobotPreferencePage_turing_key;
    public static String SmartQQPreferencePage_desc;
    public static String SmartQQPreferencePage_github;
    
    static {
        // initialize resource bundle
        NLS.initializeMessages(BUNDLE_NAME, Messages.class);
    }
    
    private Messages() {
    }
}
