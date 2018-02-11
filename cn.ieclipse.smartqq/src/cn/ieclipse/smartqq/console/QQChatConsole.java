package cn.ieclipse.smartqq.console;

import java.io.File;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;

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
import cn.ieclipse.smartim.common.LetterImageFactory;
import cn.ieclipse.smartim.htmlconsole.IMChatConsole;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.model.impl.AbstractFrom;
import cn.ieclipse.smartim.preferences.QiniuPerferencePage;
import cn.ieclipse.smartim.preferences.SettingsPerferencePage;
import cn.ieclipse.smartim.views.IMContactView;

public class QQChatConsole extends IMChatConsole {
    
    public QQChatConsole(IContact target, IMContactView imPanel) {
        super(target, imPanel);
        char ch = 'F';
        if (target instanceof Group || target instanceof GroupInfo) {
            ch = 'G';
        }
        else if (target instanceof Discuss || target instanceof DiscussInfo) {
            ch = 'D';
        }
        IMG_NORMAL = LetterImageFactory.create(ch, SWT.COLOR_BLACK);
        IMG_SELECTED = LetterImageFactory.create(ch, SWT.COLOR_RED);
        setImage(IMG_NORMAL);
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
        if (contact instanceof Friend) {
            return false;
        }
        boolean hide = IMPlugin.getDefault().getPreferenceStore()
                .getBoolean(SettingsPerferencePage.HIDE_MY_INPUT);
        return hide;
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
                    send(msg);
                } catch (Exception e) {
                    error("发送失败：" + e.getMessage());
                }
                uploadLock = false;
            };
        }.start();
    }
}
