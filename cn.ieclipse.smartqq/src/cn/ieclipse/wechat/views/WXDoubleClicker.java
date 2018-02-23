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
package cn.ieclipse.wechat.views;

import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.views.IMContactDoubleClicker;
import cn.ieclipse.smartim.views.IMContactView;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年10月14日
 *       
 */
public class WXDoubleClicker extends IMContactDoubleClicker {
    
    public WXDoubleClicker(IMContactView view) {
        super(view);
    }
    
    @Override
    public void click(Object obj) {
        if (obj instanceof IContact) {
            open((IContact) obj);
        }
    }
    
    private void open(IContact contact) {
        //IMPlugin.getDefault().findConsole(WXChatConsole.class, contact, true);
    }
}
