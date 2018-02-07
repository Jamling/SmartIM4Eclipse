package cn.ieclipse.smartim.htmlconsole;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Adapter;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.wb.swt.SWTResourceManager;

import cn.ieclipse.smartim.IMPlugin;
import cn.ieclipse.smartim.views.IMContactView;

public class ClosableTabHost extends CTabFolder {
    IMContactView imPanel;
    
    /**
     * Create the composite.
     * 
     * @param parent
     * @param style
     */
    public ClosableTabHost(Composite parent) {
        super(parent, SWT.CLOSE | SWT.FLAT);
        setUnselectedImageVisible(true);
        setUnselectedCloseVisible(false);
        // setSimple(false);
        setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
        addCTabFolder2Listener(new CTabFolder2Adapter() {
            @Override
            public void showList(CTabFolderEvent event) {
            
            }
        });
        addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                if (e.item instanceof IMChatConsole) {
                    IMChatConsole item = (IMChatConsole) e.item;
                    item.setImage(item.IMG_NORMAL);
                }
            }
        });
    }
    
    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
    
    public void bling(int index, String name) {
        int size = getItemCount();
        if (index >= 0 && index < size) {
            final CTabItem item = getItem(index);
            new Thread(new BlingThread(name, item)).start();
        }
    }
    
    public static class BlingThread implements Runnable {
        int count = 4;
        long interval = 300;
        String name;
        CTabItem item;
        Rectangle bounds;
        
        Runnable cmd = new Runnable() {
            @Override
            public void run() {
                if (item != null && !item.isDisposed()
                        && item instanceof IMChatConsole) {
                    // String text = item.getText();
                    // item.setText("".equals(text) ? name : "");
                    Image img = item.getImage();
                    Image bling = ((IMChatConsole) item).IMG_SELECTED;
                    item.setImage(img == bling ? img : bling);
                }
            }
        };
        
        public BlingThread(String name, CTabItem item) {
            this.item = item;
            this.name = name;
            bounds = item.getBounds();
        }
        
        @Override
        public void run() {
            while (count > 0) {
                try {
                    IMPlugin.runOnUI(cmd);
                    Thread.sleep(interval);
                    count--;
                } catch (InterruptedException e) {
                    return;
                }
            }
        }
    }
}
