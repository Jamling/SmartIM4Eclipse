package cn.ieclipse.smartqq.console;

import java.io.File;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.console.IConsole;

import com.scienjus.smartqq.QNUploader;
import com.scienjus.smartqq.QNUploader.UploadInfo;
import com.scienjus.smartqq.client.SmartQQClient;
import com.scienjus.smartqq.model.Discuss;
import com.scienjus.smartqq.model.DiscussInfo;
import com.scienjus.smartqq.model.Friend;
import com.scienjus.smartqq.model.Group;
import com.scienjus.smartqq.model.GroupInfo;
import com.scienjus.smartqq.model.UserInfo;

import cn.ieclipse.smartim.IMClientFactory;
import cn.ieclipse.smartim.IMPlugin;
import cn.ieclipse.smartim.SmartClient;
import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.console.IMChatConsole;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.model.IMessage;
import cn.ieclipse.smartim.preferences.QiniuPerferencePage;

public class QQChatConsole extends IMChatConsole {
    
    public QQChatConsole(String id, String name, String uin) {
        super(id, name, uin);
    }
    
    public QQChatConsole(IContact contact) {
        super(contact);
    }
    
    @Override
    public SmartClient getClient() {
        return IMClientFactory.getInstance().getQQClient();
    }
    
    @Deprecated
    public static QQChatConsole create(Object obj) {
        if (obj instanceof Friend) {
            Friend f = (Friend) obj;
            return new QQChatConsole("F_" + f.getUserId(), f.getMarkname(),
                    f.getUin());
        }
        else if (obj instanceof Group) {
            Group g = (Group) obj;
            return new QQChatConsole("G_" + g.getUin(), g.getName(),
                    g.getUin());
        }
        else if (obj instanceof Discuss) {
            Discuss d = (Discuss) obj;
            return new QQChatConsole("D_" + d.getUin(), d.getName(),
                    d.getUin());
        }
        else if (obj instanceof GroupInfo) {
            GroupInfo g = (GroupInfo) obj;
            return new QQChatConsole("G_" + g.getUin(), g.getName(),
                    g.getUin());
        }
        else if (obj instanceof DiscussInfo) {
            DiscussInfo g = (DiscussInfo) obj;
            return new QQChatConsole("D_" + g.getUin(), g.getName(),
                    g.getUin());
        }
        return null;
    }
    
    private static long getContactId(Object obj) {
        if (obj instanceof IContact) {
            String uin = ((IContact) obj).getUin();
            return Long.parseLong(uin);
        }
        return 0;
    }
    
    @Deprecated
    public static boolean isChatConsole(IConsole existing, Object obj) {
        if (existing instanceof QQChatConsole) {
            QQChatConsole console = ((QQChatConsole) existing);
            String id = console.id;
            String name = existing.getName();
            
            if (obj instanceof Friend) {
                return ("F_" + getContactId(obj)).equals(id);
            }
            else if (obj instanceof Group) {
                return ("G_" + getContactId(obj)).equals(id);
            }
            else if (obj instanceof GroupInfo) {
                return ("G_" + getContactId(obj)).equals(id);
            }
            else if (obj instanceof Discuss) {
                return ("D_" + ((Discuss) obj).getId()).equals(id);
            }
            else if (obj instanceof DiscussInfo) {
                return ("D_" + getContactId(obj)).equals(id);
            }
        }
        return false;
    }
    
    public void post(final String msg) {
        long uin = Long.parseLong(this.uin);
        SmartQQClient client = (SmartQQClient) getClient();
        if (this.contact != null) {
            // if (this.id != null && id.startsWith("F_")) {
            // client.sendMessageToFriend(uin, msg);
            // }
            // else if (this.id != null && id.startsWith("G_")) {
            // client.sendMessageToGroup(uin, msg);
            // }
            // else if (this.id != null && id.startsWith("D_")) {
            // client.sendMessageToDiscuss(uin, msg);
            // }
            IMessage m = client.createMessage(msg, contact);
            client.sendMessage(m, this.contact);
        }
    }
    
    public void sendFile(final String file) {
        final File f = new File(file);
        new Thread() {
            public void run() {
                try {
                    QNUploader uploader = IMPlugin.getDefault().getUploader();
                    IPreferenceStore store = IMPlugin.getDefault()
                            .getPreferenceStore();
                    String ak = store.getString(QiniuPerferencePage.AK);
                    String sk = store.getString(QiniuPerferencePage.SK);
                    String bucket = store.getString(QiniuPerferencePage.BUCKET);
                    String domain = store.getString(QiniuPerferencePage.DOMAIN);
                    String qq = ((UserInfo) getClient().getAccount())
                            .getAccount();
                    boolean enable = store
                            .getBoolean(QiniuPerferencePage.ENABLE);
                    boolean ts = store.getBoolean(QiniuPerferencePage.TS);
                    if (!enable) {
                        ak = "";
                        sk = "";
                    }
                    UploadInfo info = uploader.upload(qq, f, ak, sk, bucket,
                            null);
                    String url = info.getUrl(domain, ts);
                    
                    String msg = String.format(
                            "来自SmartQQ的文件: %s (大小%s), 点击链接%s查看",
                            IMUtils.getName(file),
                            IMUtils.formatFileSize(info.fsize), url);
                    writeMine(msg);
                    post(msg);
                } catch (Exception e) {
                    error("发送失败：" + e.getMessage());
                }
            };
        }.start();
        
    }
}
