package cn.ieclipse.smartqq.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import cn.ieclipse.smartqq.preferences.HotKeyFieldEditor;

public class HotKeyTest extends Shell {
    private Text text;
    private Text text_1;
    
    /**
     * Launch the application.
     * 
     * @param args
     */
    public static void main(String args[]) {
        try {
            Display display = Display.getDefault();
            HotKeyTest shell = new HotKeyTest(display);
            shell.open();
            shell.layout();
            while (!shell.isDisposed()) {
                if (!display.readAndDispatch()) {
                    display.sleep();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Create the shell.
     * 
     * @param display
     */
    public HotKeyTest(Display display) {
        super(display, SWT.SHELL_TRIM);
        
        text = new Text(this, SWT.BORDER);
        text.setBounds(10, 10, 188, 23);
        text.addKeyListener(new KeyListener() {
            
            @Override
            public void keyReleased(KeyEvent e) {
            
            }
            
            @Override
            public void keyPressed(KeyEvent e) {
                e.doit = false;
                text.setText(HotKeyFieldEditor.keyEvent2String(e));
            }
        });
        
        text_1 = new Text(this, SWT.BORDER);
        text_1.setBounds(10, 39, 188, 23);
        
        text_1.addKeyListener(new KeyListener() {
            
            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void keyPressed(KeyEvent e) {
                if (text.getText()
                        .equals(HotKeyFieldEditor.keyEvent2String(e))) {
                    e.doit = false;
                    text_1.setText("");
                }
            }
        });
        
        createContents();
    }
    
    /**
     * Create contents of the shell.
     */
    protected void createContents() {
        setText("SWT Application");
        setSize(450, 300);
        
    }
    
    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }
}
