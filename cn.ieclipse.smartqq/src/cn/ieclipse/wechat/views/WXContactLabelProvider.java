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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

import cn.ieclipse.smartim.common.LetterImageFactory;
import cn.ieclipse.smartim.views.IMContactLabelProvider;
import cn.ieclipse.smartim.views.IMContactView;
import cn.ieclipse.wechat.WXUtils;
import io.github.biezhi.wechat.model.Contact;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年10月16日
 *       
 */
public class WXContactLabelProvider extends IMContactLabelProvider {
    
    public WXContactLabelProvider(IMContactView view) {
        super(view);
    }
    
    @Override
    public Image getContactImage(Object obj) {
        if (obj instanceof Contact) {
            Contact c = (Contact) obj;
            char ch = WXUtils.getContactChar(c);
            if (ch > 0) {
                if (ch == 'F') {
                    return LetterImageFactory.create(ch, SWT.COLOR_DARK_GREEN);
                }
                else if (ch == 'G') {
                    return LetterImageFactory.create(ch, SWT.COLOR_DARK_BLUE);
                }
                else if (ch == 'P') {
                    return LetterImageFactory.create(ch, SWT.COLOR_DARK_CYAN);
                }
                else if (ch == 'S') {
                    return LetterImageFactory.create(ch, SWT.COLOR_DARK_RED);
                }
            }
        }
        return null;
    }
}
