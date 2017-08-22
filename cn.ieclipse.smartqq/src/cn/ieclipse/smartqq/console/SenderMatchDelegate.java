package cn.ieclipse.smartqq.console;

import java.util.regex.Pattern;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.ui.console.IHyperlink;
import org.eclipse.ui.console.IPatternMatchListenerDelegate;
import org.eclipse.ui.console.PatternMatchEvent;
import org.eclipse.ui.console.TextConsole;

import cn.ieclipse.smartqq.Utils;

public class SenderMatchDelegate implements IPatternMatchListenerDelegate {
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
            IHyperlink link = new SenderLink(fConsole);
            fConsole.addHyperlink(link, offset + 9, length - 10);
        } catch (BadLocationException e) {
        }
    }
    
    public static void main(String[] args) {
        String regex = "\\d{2}:\\d{2}:\\d{2} (\\S+): ";
        String msg = Utils.formatMsg(0, "明", " fdqdfdf");
        System.out.println(Pattern.compile(regex).matcher(msg).find());
    }
}
