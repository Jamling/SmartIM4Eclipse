/*
 * Copyright 2014-2017 ieclipse.cn.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.ieclipse.wechat;

import java.io.File;
import java.net.URLConnection;
import java.util.Arrays;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;

import cn.ieclipse.smartim.IMClientFactory;
import cn.ieclipse.smartim.IMHistoryManager;
import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.common.LetterImageFactory;
import cn.ieclipse.smartim.console.IMChatConsole;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.model.impl.AbstractFrom;
import cn.ieclipse.smartim.views.IMContactView;
import cn.ieclipse.util.FileUtils;
import cn.ieclipse.util.StringUtils;
import io.github.biezhi.wechat.api.WechatClient;
import io.github.biezhi.wechat.model.Contact;
import io.github.biezhi.wechat.model.UploadInfo;
import io.github.biezhi.wechat.model.WechatMessage;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年10月16日
 *       
 */
public class WXChatConsole extends IMChatConsole {
    
    public WXChatConsole(IContact target, IMContactView imPanel) {
        super(target, imPanel);
        char ch = WXUtils.getContactChar(target);
        if (ch > 0) {
            IMG_NORMAL = LetterImageFactory.create(ch, SWT.COLOR_BLACK);
            IMG_SELECTED = LetterImageFactory.create(ch, SWT.COLOR_RED);
            setImage(IMG_NORMAL);
        }
        if (target instanceof Contact) {
            setText(WXUtils.getPureName(target.getName()));
        }
    }
    
    @Override
    public WechatClient getClient() {
        return (WechatClient) IMClientFactory.getInstance().getWechatClient();
    }
    
    @Override
    public void loadHistory(String raw) {
        if (IMUtils.isMySendMsg(raw)) {
            write(raw);
            return;
        }
        // unreachable code
        WechatMessage m = (WechatMessage) getClient().handleMessage(raw);
        AbstractFrom from = getClient().getFrom(m);
        write(WXUtils.formatHtmlIncoming(m, from));
    }
    
     @Override
    protected String formatInput(String name, String input) {
        return WXUtils.formatHtmlOutgoing(name, input, true);
    }
    
    @Override
    public boolean hideMyInput() {
        return false;
    }
    
    @Override
    public void post(String msg) {
        WechatClient client = getClient();
        if (client.isLogin() && contact != null) {
            WechatMessage m = client.createMessage(0, msg, contact);
            client.sendMessage(m, contact);
        }
        else {
            error("发送失败，客户端异常（可能已断开连接或找不到联系人）");
        }
    }
    
    @Override
    protected boolean hyperlinkActivated(String desc) {
        if (desc.startsWith("weixin://")) {
            MessageDialog.openWarning(getParent().getShell(), "不支持的协议",
                    desc + "为微信专用协议，请使用手机微信打开");
            return false;
        }
        return super.hyperlinkActivated(desc);
    }
    
    @Override
    public void sendFileInternal(final String file) {
        // error("暂不支持，敬请关注 https://github.com/Jamling/SmartIM 或
        // https://github.com/Jamling/SmartQQ4IntelliJ 最新动态");
        final File f = new File(file);
        final WechatClient client = getClient();
        if (!checkClient(client)) {
            return;
        }
        
        String ext = FileUtils.getExtension(f.getPath()).toLowerCase();
        String mimeType = URLConnection.guessContentTypeFromName(f.getName());
        String media = "pic";
        int type = WechatMessage.MSGTYPE_IMAGE;
        String content = "";
        if (Arrays.asList("png", "jpg", "jpeg", "bmp").contains(ext)) {
            type = WechatMessage.MSGTYPE_IMAGE;
            media = "pic";
        }
        else if ("gif".equals(ext)) {
            type = WechatMessage.MSGTYPE_EMOTICON;
            media = "doc";
        }
        else {
            type = WechatMessage.MSGTYPE_FILE;
            media = "doc";
        }
        
        final UploadInfo uploadInfo = client.uploadMedia(f, mimeType, media);
        
        if (uploadInfo == null) {
            error("上传失败");
            return;
        }
        String link = StringUtils.file2url(file);
        String label = file.replace('\\', '/');
        String input = null;
        if (type == WechatMessage.MSGTYPE_EMOTICON
                || type == WechatMessage.MSGTYPE_IMAGE) {
            input = String.format("<img src=\"%s\" border=\"0\" alt=\"%s\"",
                    link, label);
            if (uploadInfo.CDNThumbImgWidth > 0) {
                input += " width=\"" + uploadInfo.CDNThumbImgWidth + "\"";
            }
            if (uploadInfo.CDNThumbImgHeight > 0) {
                input += " height=\"" + uploadInfo.CDNThumbImgHeight + "\"";
            }
            input = String.format("<a href=\"%s\" title=\"%s\">%s</a>", link,
                    link, input);
        }
        else {
            input = String.format("<a href=\"%s\" title=\"%s\">%s</a>", link,
                    label, label);
            content = client.createFileMsgContent(f, uploadInfo.MediaId);
        }
        
        final WechatMessage m = client.createMessage(type, content, contact);
        m.text = input;
        m.MediaId = uploadInfo.MediaId;
        
        client.sendMessage(m, contact);
        
        if (!hideMyInput()) {
            String name = client.getAccount().getName();
            String msg = WXUtils.formatHtmlOutgoing(name, m.text, false);
            insertDocument(msg);
            IMHistoryManager.getInstance().save(getHistoryDir(), getHistoryFile(), msg);
        }
    }
}
