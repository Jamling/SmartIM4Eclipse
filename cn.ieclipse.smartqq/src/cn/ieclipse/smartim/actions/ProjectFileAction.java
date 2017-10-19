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
package cn.ieclipse.smartim.actions;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.dialogs.FilteredResourcesSelectionDialog;

import cn.ieclipse.smartim.IMPlugin;
import cn.ieclipse.smartim.console.IMChatConsole;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年8月22日
 *       
 */
public class ProjectFileAction extends Action {
    private IMChatConsole fConsole;
    
    public ProjectFileAction(IMChatConsole console) {
        this.fConsole = console;
        setText("Send Project File");
        setToolTipText(
                "Send your project(workspace) file to " + fConsole.getName());
        // setImageDescriptor(
        // LetterImageFactory.createDescriptor('P', SWT.COLOR_DARK_GRAY));
        setImageDescriptor(IMPlugin.getImageDescriptor("icons/project.png"));
    }
    
    @Override
    public void run() {
        if (fConsole != null) {
            ProjectFileDialog dialog = new ProjectFileDialog(new Shell(),
                    fConsole.getName());
            if (Window.OK == dialog.open()) {
                Object obj = dialog.getFirstResult();
                if (obj instanceof IFile) {
                    String file = ((IFile) obj).getLocation().toOSString();
                    if (file != null) {
                        fConsole.sendFile(file);
                    }
                }
            }
        }
    }
    
    private static class ProjectFileDialog
            extends FilteredResourcesSelectionDialog {
        public ProjectFileDialog(Shell shell, String name) {
            super(shell, false, ResourcesPlugin.getWorkspace().getRoot(),
                    IResource.FILE);
            setTitle("Select project file to send to " + name);
        }
    }
}
