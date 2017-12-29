package cn.ieclipse.smartqq.console;

import java.io.File;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.console.IConsole;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.scienjus.smartqq.QNUploader;
import com.scienjus.smartqq.QNUploader.UploadInfo;
import com.scienjus.smartqq.client.SmartQQClient;
import com.scienjus.smartqq.handler.msg.DiscussMessageHandler;
import com.scienjus.smartqq.handler.msg.FriendMessageHandler;
import com.scienjus.smartqq.handler.msg.GroupMessageHandler;
import com.scienjus.smartqq.model.Discuss;
import com.scienjus.smartqq.model.DiscussInfo;
import com.scienjus.smartqq.model.Friend;
import com.scienjus.smartqq.model.Group;
import com.scienjus.smartqq.model.GroupInfo;
import com.scienjus.smartqq.model.QQMessage;
import com.scienjus.smartqq.model.UserInfo;

import cn.ieclipse.smartim.IMClientFactory;
import cn.ieclipse.smartim.IMPlugin;
import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.console.IMChatConsole;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.model.impl.AbstractFrom;
import cn.ieclipse.smartim.preferences.QiniuPerferencePage;
import cn.ieclipse.util.FileUtils;

public class QQChatConsole extends IMChatConsole {
    
    private static ImageDescriptor icon = IMPlugin
            .getImageDescriptor("icons/QQ.png");
            
    public QQChatConsole(String id, String name, String uin) {
        super(id, name, uin);
        setImageDescriptor(icon);
    }
    
    public QQChatConsole(IContact contact) {
        super(contact);
        setImageDescriptor(icon);
    }
    
    @Override
    public SmartQQClient getClient() {
        return (SmartQQClient) IMClientFactory.getInstance().getQQClient();
    }
    
    @Override
    public void loadHistory(String raw) {
        if (IMUtils.isMySendMsg(raw)) {
            write(raw);
            return;
        }
        JsonObject obj = new JsonParser().parse(raw).getAsJsonObject();
        QQMessage m = null;
        if (obj.has("group_code")) {
            m = (QQMessage) new GroupMessageHandler().handle(obj);
        }
        else if (obj.has("did")) {
            m = (QQMessage) new DiscussMessageHandler().handle(obj);
        }
        else {
            m = (QQMessage) new FriendMessageHandler().handle(obj);
        }
        
        AbstractFrom from = getClient().parseFrom(m);
        String name = from == null ? "未知用户" : from.getName();
        String msg = IMUtils.formatMsg(m.getTime(), name, m.getContent());
        write(msg);
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
        SmartQQClient client = (SmartQQClient) getClient();
        if (this.contact != null) {
            QQMessage m = client.createMessage(msg, contact);
            client.sendMessage(m, this.contact);
        }
    }
    
    private boolean isGroupChat() {
        return (contact instanceof Group) || (contact instanceof GroupInfo)
                || (contact instanceof Discuss)
                || (contact instanceof DiscussInfo);
    }
    
    @Override
    public boolean hideMyInput() {
        return super.hideMyInput() && isGroupChat();
    }
    
    public void sendFile(final String file) {
        final File f = new File(file);
        new Thread() {
            public void run() {
                uploadLock = true;
                try {
                    if (f.length() > (1 << 10)) {
                        write(String.format("%s 上传中，请稍候……", f.getName()));
                    }
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
                uploadLock = false;
            };
        }.start();
        
    }
}
