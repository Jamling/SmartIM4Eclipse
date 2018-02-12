package cn.ieclipse.smartim.htmlconsole;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Sash;

public class Splitter extends Composite {
    
    public Splitter(Composite parent, Control ctrlA, Control ctrlB,
            int splitDirection) {
        super(parent, SWT.NONE);
        
        setLocation(0, 0);
        setSize(parent.getSize());
        setVisible(true);
        boolean result = ctrlA.setParent(this);
        assert result;
        result = ctrlB.setParent(this);
        assert result;
        
        final Sash sash = new Sash(this, splitDirection);
        final FormLayout form = new FormLayout();
        setLayout(form);
        
        FormData ctrlAFormData = new FormData();
        ctrlAFormData.left = new FormAttachment(0, 0);
        ctrlAFormData.top = new FormAttachment(0, 0);
        
        if (splitDirection == SWT.VERTICAL) {
            ctrlAFormData.right = new FormAttachment(sash, 0);
            ctrlAFormData.bottom = new FormAttachment(100, 0);
        }
        else if (splitDirection == SWT.HORIZONTAL) {
            ctrlAFormData.right = new FormAttachment(100, 0);
            ctrlAFormData.bottom = new FormAttachment(sash, 0);
        }
        
        ctrlA.setLayoutData(ctrlAFormData);
        
        final int limit = 20;
        final FormData sashData = new FormData();
        
        if (splitDirection == SWT.VERTICAL) {
            // sashData.width = 10;
            final int percent = 30;
            sashData.left = new FormAttachment(percent, 0);
            sashData.top = new FormAttachment(0, 0);
            sashData.bottom = new FormAttachment(100, 0);
            sash.setLayoutData(sashData);
            
            sash.addListener(SWT.Selection, new Listener() {
                public void handleEvent(Event e) {
                    Rectangle sashRect = sash.getBounds();
                    Rectangle shellRect = sash.getParent().getClientArea();
                    int right = shellRect.width - sashRect.width - limit;
                    e.x = Math.max(Math.min(e.x, right), limit);
                    if (e.x != sashRect.x) {
                        sashData.left = new FormAttachment(0, e.x);
                        sash.getParent().layout();
                    }
                }
            });
        }
        else if (splitDirection == SWT.HORIZONTAL) {
            // sashData.width = 10;
            final int percent = 70;
            sashData.left = new FormAttachment(0, 0);
            sashData.top = new FormAttachment(percent, 0);
            sashData.right = new FormAttachment(100, 0);
            sash.setLayoutData(sashData);
            
            sash.addListener(SWT.Selection, new Listener() {
                public void handleEvent(Event e) {
                    Rectangle sashRect = sash.getBounds();
                    Rectangle shellRect = sash.getParent().getClientArea();
                    // Rectangle shellRect = getClientArea();
                    int right = shellRect.height - sashRect.height - limit;
                    e.y = Math.max(Math.min(e.y, right), limit);
                    if (e.y != sashRect.y) {
                        sashData.top = new FormAttachment(0, e.y);
                        sash.getParent().layout();
                    }
                }
            });
        }
        
        FormData ctrlBFormData = new FormData();
        ctrlBFormData.right = new FormAttachment(100, 0);
        ctrlBFormData.bottom = new FormAttachment(100, 0);
        if (splitDirection == SWT.VERTICAL) {
            ctrlBFormData.left = new FormAttachment(sash, 0);
            ctrlBFormData.top = new FormAttachment(0, 0);
        }
        else if (splitDirection == SWT.HORIZONTAL) {
            ctrlBFormData.left = new FormAttachment(0, 0);
            ctrlBFormData.top = new FormAttachment(sash, 0);
        }
        ctrlB.setLayoutData(ctrlBFormData);
        
        layout();
    }
    
}