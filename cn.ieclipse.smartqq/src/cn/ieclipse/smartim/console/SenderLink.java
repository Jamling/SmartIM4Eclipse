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
package cn.ieclipse.smartim.console;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.ui.console.IHyperlink;
import org.eclipse.ui.console.TextConsole;

import cn.ieclipse.smartim.IMPlugin;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年8月21日
 *       
 */
public class SenderLink implements IHyperlink {
    private IMChatConsole console;
    
    public SenderLink(TextConsole console) {
        this.console = (IMChatConsole) console;
    }
    
    Menu m;
    
    @Override
    public void linkEntered() {
        if (m == null) {
            m = new Menu(console.getPage().getControl());
            MenuItem mi = new MenuItem(m, SWT.PUSH);
            mi.setText("@");
        }
        // m.setVisible(true);
    }
    
    @Override
    public void linkExited() {
        if (m != null) {
            m.setVisible(false);
        }
    }
    
    @Override
    public void linkActivated() {
        String linkText;
        try {
            linkText = getLinkText();
            if (console != null) {
                console.activeInput("@" + linkText + " ");
            }
        } catch (CoreException e) {
            e.printStackTrace();
            return;
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
            
            int linkStart = 9;
            int linkEnd = line.indexOf(':', linkStart);
            return line.substring(linkStart, linkEnd);
            
            // return line.substring(linkStart == -1 ? 0 : linkStart + 1,
            // linkEnd + 1);
        } catch (BadLocationException e) {
            IStatus status = new Status(IStatus.ERROR, IMPlugin.PLUGIN_ID, 0,
                    "unable to retrive hyper link text", e);
            throw new CoreException(status);
        }
    }
}
