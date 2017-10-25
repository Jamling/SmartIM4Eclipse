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

import cn.ieclipse.smartim.IMHistoryManager;
import cn.ieclipse.smartim.IMSendCallback;
import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.wechat.views.WXContactView;
import io.github.biezhi.wechat.api.WechatClient;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年10月25日
 *       
 */
public class WXSendCallback extends IMSendCallback {
    WXContactView fContactView;
    
    public WXSendCallback(WXContactView fContactView) {
        this.fContactView = fContactView;
    }
    
    @Override
    protected void onSuccess(int type, String targetId, CharSequence msg) {
        super.onSuccess(type, targetId, msg);
        WechatClient client = fContactView.getClient();
        String name = client.getAccount().getName();
        IMHistoryManager.getInstance().save(client, targetId,
                IMUtils.formatMsg(System.currentTimeMillis(), name, msg));
    }
}
