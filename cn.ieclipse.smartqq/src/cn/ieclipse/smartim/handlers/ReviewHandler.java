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
package cn.ieclipse.smartim.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;

import cn.ieclipse.smartim.dialogs.ReviewDialog;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年8月21日
 *       
 */
public class ReviewHandler extends AbstractHandler {
    
    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        IEditorPart editor = PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getActivePage().getActiveEditor();
        if (editor.getEditorInput() instanceof IFileEditorInput) {
            IFileEditorInput input = (IFileEditorInput) editor.getEditorInput();
            IFile file = input.getFile();
            ISelection sel = editor.getEditorSite().getSelectionProvider()
                    .getSelection();
            if (sel instanceof TextSelection) {
                TextSelection ts = (TextSelection) sel;
                ReviewDialog dialog = new ReviewDialog(editor.getSite().getShell());
                dialog.setData(file, ts);
                dialog.open();
            }
        }
        return null;
    }
    
}
