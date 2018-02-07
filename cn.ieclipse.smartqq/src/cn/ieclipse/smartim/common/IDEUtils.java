package cn.ieclipse.smartim.common;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import cn.ieclipse.smartim.IMPlugin;

public class IDEUtils {
    public static void open(String file, int lineNumber) {
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
    
    public static void toggleViewPart(String viewId, boolean show) {
        IWorkbenchPage page = PlatformUI.getWorkbench()
                .getActiveWorkbenchWindow().getActivePage();
        if (show) {
            try {
                page.showView(viewId);
            } catch (PartInitException e) {
                IMPlugin.getDefault().log("无法打开" + viewId, e);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        IViewPart view = page.findView(viewId);
        if (view != null) {
            page.hideView(view);
        }
    }
}
