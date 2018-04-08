package cn.ieclipse.smartqq.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import cn.ieclipse.smartim.common.LetterImageFactory;

public class LetterImageTest extends Shell {
    
    /**
     * Launch the application.
     * 
     * @param args
     */
    public static void main(String args[]) {
        try {
            Display display = Display.getDefault();
            LetterImageTest shell = new LetterImageTest(display);
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
    public LetterImageTest(Display display) {
        super(display, SWT.SHELL_TRIM);
        setLayout(new GridLayout(20, true));
        int start = 0x0;
        for (int i = start; i < start + 256; i++) {
            Label lblNewLabel = new Label(this, SWT.NONE);
            lblNewLabel.setImage(
                    LetterImageFactory.create((char) i, SWT.COLOR_RED));
        }
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
