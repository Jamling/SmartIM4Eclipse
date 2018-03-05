package cn.ieclipse.smartim.common;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wb.swt.SWTResourceManager;

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
            IMPlugin.getDefault().log(msg, e);
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
    
    public static boolean openInternalBrowser(String url, boolean external) {
        boolean ret = false;
        IWorkbenchBrowserSupport support = PlatformUI.getWorkbench()
                .getBrowserSupport();
        boolean ext = external || !support.isInternalWebBrowserAvailable();
        try {
            if (!ext) {
                int style = IWorkbenchBrowserSupport.LOCATION_BAR
                        | IWorkbenchBrowserSupport.NAVIGATION_BAR
                        | IWorkbenchBrowserSupport.AS_EDITOR;
                support.createBrowser(style, IMPlugin.PLUGIN_ID, null, null)
                        .openURL(new URL(url));
                ret = true;
            }
            else {
                support.getExternalBrowser().openURL(new URL(url));
                ret = true;
            }
        } catch (PartInitException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return ret;
    }
    
    public static Color getColor(int r, int g, int b) {
        return SWTResourceManager.getColor(r, g, b);
    }
    
    public static Color getPrefColor(String pluginId, String key) {
        IEclipsePreferences pref = InstanceScope.INSTANCE.getNode(pluginId);
        String c = pref.get(key, null);
        if (c != null) {
            String[] cs = c.split(",");
            if (cs.length == 3) {
                return getColor(Integer.parseInt(cs[0]),
                        Integer.parseInt(cs[1]), Integer.parseInt(cs[2]));
            }
        }
        return null;
    }
    
    public static Color getEditorBack() {
        Color bg = getPrefColor("org.eclipse.ui.editors",
                "AbstractTextEditor.Color.Background");
        return bg;
    }
    
    public static Color getEditorFore() {
        Color bg = getPrefColor("org.eclipse.ui.editors",
                "AbstractTextEditor.Color.Foreground");
        return bg;
    }
    
    public static String getHtmlColor(Color c) {
        String rgb = "";
        if (c != null) {
            rgb = "#" + dec2hex(c.getRed()) + dec2hex(c.getGreen())
                    + dec2hex(c.getBlue());
        }
        return rgb;
    }
    
    private static String dec2hex(int d) {
        int i = d & 0xff;
        String s = Integer.toHexString(i);
        if (s.length() < 2) {
            s = "0" + s;
        }
        return s;
    }
}
