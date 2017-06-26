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
package cn.ieclipse.smartqq.preferences;

import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.browser.IWorkbenchBrowserSupport;
import org.osgi.framework.Bundle;

import cn.ieclipse.smartqq.QQPlugin;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年6月22日
 *       
 */
public class SmartQQPreferencePage extends PreferencePage
        implements IWorkbenchPreferencePage {
        
    public SmartQQPreferencePage() {
        setPreferenceStore(QQPlugin.getDefault().getPreferenceStore());
        setDescription(Messages.SmartQQPreferencePage_desc);
    }
    
    @Override
    public void init(IWorkbench workbench) {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    protected Control createContents(Composite parent) {
        Composite comp = new Composite(parent, SWT.NONE);
        comp.setLayout(new GridLayout(1, false));
        Label lbl = new Label(comp, SWT.BOLD);
        lbl.setText("Installation"); //$NON-NLS-1$
        
        Text text = new Text(comp, SWT.WRAP | SWT.READ_ONLY);
        text.setText(getBundlesInfo());
        
        Link l = new Link(comp, SWT.ITALIC);
        l.setText(""); //$NON-NLS-1$
        l.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                String url = e.text;
                if (url.indexOf("://") < 0) { //$NON-NLS-1$
                    url = "http://" + url; //$NON-NLS-1$
                }
                openBrowser(url);
            }
            
            private void openBrowser(String url) {
                IWorkbenchBrowserSupport support = PlatformUI.getWorkbench()
                        .getBrowserSupport();
                IWebBrowser browser;
                try {
                    browser = support.getExternalBrowser();
                    browser.openURL(new URL(url));
                } catch (PartInitException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });
        return comp;
    }
    
    private static String getBundlesInfo() {
        StringBuilder sb = new StringBuilder();
        Bundle bundle = Platform.getProduct().getDefiningBundle();
        sb.append("Platform:\n\u25cf "); //$NON-NLS-1$
        sb.append(getBundleInfo(bundle));
        bundle = Platform.getBundle(QQPlugin.PLUGIN_ID);
        sb.append("\nHost plugin:\n\u25cf "); //$NON-NLS-1$
        sb.append(getBundleInfo(bundle));
        return sb.toString();
    }
    
    private static String getBundleInfo(Bundle bundle) {
        String name = bundle.getHeaders().get("Bundle-Name"); //$NON-NLS-1$
        String ver = bundle.getVersion().toString();
        // UNINSTALLED,INSTALLED, RESOLVED, STARTING, STOPPING, ACTIVE.
        String stStr = "unknown"; //$NON-NLS-1$
        int st = bundle.getState();
        if (st == Bundle.UNINSTALLED) {
            stStr = "uninstalled"; //$NON-NLS-1$
        }
        else if (st == Bundle.INSTALLED) {
            stStr = "installed"; //$NON-NLS-1$
        }
        else if (st == Bundle.RESOLVED) {
            stStr = "resolved"; //$NON-NLS-1$
        }
        else if (st == Bundle.STARTING) {
            stStr = "starting"; //$NON-NLS-1$
        }
        else if (st == Bundle.STOPPING) {
            stStr = "stopping"; //$NON-NLS-1$
        }
        else if (st == Bundle.ACTIVE) {
            stStr = "active"; //$NON-NLS-1$
        }
        return String.format("%s(%s):%s", name, ver, stStr); //$NON-NLS-1$
    }
}
