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

import org.eclipse.jface.preference.FieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年6月21日
 *       
 */
public class CompositeFieldEditor extends FieldEditor {
    private Composite fComposite;
    
    public CompositeFieldEditor(String labelText, Composite parent) {
        super(CompositeFieldEditor.class.getName(), labelText, parent);
    }
    
    @Override
    protected void adjustForNumColumns(int numColumns) {
        GridData gd = (GridData) fComposite.getLayoutData();
        gd.horizontalSpan = numColumns - 1;
        // We only grab excess space if we have to
        // If another field editor has more columns then
        // we assume it is setting the width.
        gd.grabExcessHorizontalSpace = gd.horizontalSpan == 1;
    }
    
    @Override
    protected void doFillIntoGrid(Composite parent, int numColumns) {
        getLabelControl(parent);
        
        fComposite = getCompositeControl(parent);
        GridData gd = new GridData();
        gd.horizontalSpan = numColumns - 1;
        gd.horizontalAlignment = GridData.FILL;
        gd.grabExcessHorizontalSpace = true;
        
        fComposite.setLayoutData(gd);
    }
    
    @Override
    protected void doLoad() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    protected void doLoadDefault() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    protected void doStore() {
        // TODO Auto-generated method stub
        
    }
    
    @Override
    public int getNumberOfControls() {
        // TODO Auto-generated method stub
        return 0;
    }
    
    public Composite getCompositeControl(Composite parent) {
        if (fComposite == null) {
            fComposite = new Composite(parent, SWT.NONE);
            
            fComposite.addDisposeListener(new DisposeListener() {
                @Override
                public void widgetDisposed(DisposeEvent event) {
                    fComposite = null;
                }
            });
        }
        else {
            checkParent(fComposite, parent);
        }
        return fComposite;
    }
}
