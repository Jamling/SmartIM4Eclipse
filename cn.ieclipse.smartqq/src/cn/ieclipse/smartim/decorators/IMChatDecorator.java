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
package cn.ieclipse.smartim.decorators;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.IDecoration;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;

import cn.ieclipse.smartim.common.LetterImageFactory;
import cn.ieclipse.smartim.model.impl.AbstractContact;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年10月26日
 *       
 */
public class IMChatDecorator implements ILabelDecorator {
    
    @Override
    public void addListener(ILabelProviderListener listener) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void dispose() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public boolean isLabelProperty(Object element, String property) {
        // TODO Auto-generated method stub
        return false;
    }
    
    @Override
    public void removeListener(ILabelProviderListener listener) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public Image decorateImage(Image image, Object element) {
        if (image != null && element instanceof AbstractContact) {
            AbstractContact c = (AbstractContact) element;
            int ch = Math.min(c.getUnread(), 9) + (int) '0';
            ImageDescriptor id = LetterImageFactory.createDescriptor((char) ch,
                    SWT.COLOR_RED);
            return new IMCompositeImageDescriptor(image, id,
                    IDecoration.TOP_LEFT).createImage();
        }
        return null;
    }
    
    @Override
    public String decorateText(String text, Object element) {
        if (element instanceof AbstractContact) {
            AbstractContact c = (AbstractContact) element;
            CharSequence s = c.getLastMessage().getText();
            return text + "[" + s + "]";
        }
        return null;
    }
    
}
