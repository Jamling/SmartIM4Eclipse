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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.IHyperlink;
import org.eclipse.ui.console.TextConsole;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import cn.ieclipse.smartim.IMPlugin;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年8月21日
 *       
 */
public class ReviewLink implements IHyperlink {
    private IMChatConsole console;
    
    public ReviewLink(TextConsole console) {
        this.console = (IMChatConsole) console;
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
            String linkText = getLinkText();
            int pos = linkText.lastIndexOf(':');
            String file = linkText.substring(0, pos);
            int line = Integer.parseInt(linkText.substring(pos + 1).trim());
            if (line > 0) {
                line--;
            }
            open(file, line);
        } catch (CoreException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    private void open(String file, int lineNumber) {
        try {
            IPath path = new Path(file);
            String prj = path.segment(0);
            IProject project = ResourcesPlugin.getWorkspace().getRoot()
                    .getProject(prj);
                    
            IFile f = project.getFile(path.removeFirstSegments(1));
            IEditorPart editorPart = IDE.openEditor(PlatformUI.getWorkbench()
                    .getActiveWorkbenchWindow().getActivePage(), f);
            if (editorPart instanceof ITextEditor && lineNumber >= 0) {
                ITextEditor textEditor = (ITextEditor) editorPart;
                IDocumentProvider provider = textEditor.getDocumentProvider();
                provider.connect(editorPart.getEditorInput());
                IDocument document = provider
                        .getDocument(editorPart.getEditorInput());
                try {
                    IRegion line = document.getLineInformation(lineNumber);
                    textEditor.selectAndReveal(line.getOffset(),
                            line.getLength());
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
                provider.disconnect(editorPart.getEditorInput());
            }
        } catch (Exception e) {
            String msg = String.format("unable to open %s", file);
            IMPlugin.getDefault().log(msg);
            Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
                    .getShell();
            MessageDialog.openWarning(shell, null,
                    msg + "\n exception: " + e.toString());
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
            
            return line.substring(regionOffsetInLine,
                    regionOffsetInLine + region.getLength());
        } catch (BadLocationException e) {
            IStatus status = new Status(IStatus.ERROR, IMPlugin.PLUGIN_ID, 0,
                    "unable to retrive hyper link text", e);
            throw new CoreException(status);
        }
    }
}
