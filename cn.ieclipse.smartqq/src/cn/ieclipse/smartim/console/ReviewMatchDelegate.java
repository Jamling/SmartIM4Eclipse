package cn.ieclipse.smartim.console;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.ui.console.IHyperlink;
import org.eclipse.ui.console.IPatternMatchListenerDelegate;
import org.eclipse.ui.console.PatternMatchEvent;
import org.eclipse.ui.console.TextConsole;

public class ReviewMatchDelegate implements IPatternMatchListenerDelegate {
    private TextConsole fConsole;
    
    @Override
    public void connect(TextConsole console) {
        this.fConsole = console;
    }
    
    @Override
    public void disconnect() {
        this.fConsole = null;
    }
    
    public TextConsole getConsole() {
        return fConsole;
    }
    
    @Override
    public void matchFound(PatternMatchEvent event) {
        try {
            int offset = event.getOffset();
            int length = event.getLength();
            IHyperlink link = new ReviewLink(fConsole);
            // Code: \S:\d+
            fConsole.addHyperlink(link, offset + 6, length - 6);
        } catch (BadLocationException e) {
        }
    }
}
