/*
 * Copyright 2014-2015 ieclipse.cn.
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
package cn.ieclipse.smartqq.console;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.ui.console.IHyperlink;
import org.eclipse.ui.console.TextConsole;

import cn.ieclipse.smartqq.QQPlugin;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年8月21日
 *       
 */
public class UrlLink implements IHyperlink {
    private ChatConsole console;
    
    public UrlLink(TextConsole console) {
        this.console = (ChatConsole) console;
    }
    
    @Override
    public void linkEntered() {
    }
    
    @Override
    public void linkExited() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public void linkActivated() {
        try {
            String url = getLinkText();
            open(url);
        } catch (CoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    private void open(String url) {
        if (java.awt.Desktop.isDesktopSupported()) {
            try {
                // 创建一个URI实例
                java.net.URI uri = java.net.URI.create(url);
                // 获取当前系统桌面扩展
                java.awt.Desktop dp = java.awt.Desktop.getDesktop();
                // 判断系统桌面是否支持要执行的功能
                if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
                    // 获取系统默认浏览器打开链接
                    dp.browse(uri);
                }
            } catch (java.lang.NullPointerException e) {
                // 此为uri为空时抛出异常
            } catch (java.io.IOException e) {
                // 此为无法获取系统默认浏览器
            }
        }
    }
    
    protected TextConsole getConsole() {
        return console;
    }
    
    /**
     * Returns this link's text
     * 
     * @exception CoreException
     *                if unable to retrieve the text
     */
    protected String getLinkText() throws CoreException {
        try {
            IDocument document = getConsole().getDocument();
            IRegion region = getConsole().getRegion(this);
            int regionOffset = region.getOffset();
            
            int lineNumber = document.getLineOfOffset(regionOffset);
            IRegion lineInformation = document.getLineInformation(lineNumber);
            int lineOffset = lineInformation.getOffset();
            String line = document.get(lineOffset, lineInformation.getLength());
            
            int regionOffsetInLine = regionOffset - lineOffset;
            
            return line.substring(regionOffsetInLine, regionOffsetInLine + region.getLength());
        } catch (BadLocationException e) {
            IStatus status = new Status(IStatus.ERROR, QQPlugin.PLUGIN_ID, 0,
                    "unable to retrive hyper link text", e);
            throw new CoreException(status);
        }
    }
}
