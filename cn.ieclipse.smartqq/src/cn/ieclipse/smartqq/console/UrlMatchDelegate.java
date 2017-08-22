package cn.ieclipse.smartqq.console;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.ui.console.IHyperlink;
import org.eclipse.ui.console.IPatternMatchListenerDelegate;
import org.eclipse.ui.console.PatternMatchEvent;
import org.eclipse.ui.console.TextConsole;

public class UrlMatchDelegate implements IPatternMatchListenerDelegate {
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
            IHyperlink link = new UrlLink(fConsole);
            fConsole.addHyperlink(link, offset, length);
        } catch (BadLocationException e) {
        }
    }
    
    public static void main(String[] args) {
        String regex = "(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        String msg = "qhttps://www.baidu.com/aa.apk?dfd=11中%DE%DE%DE我的";
        Matcher m = Pattern.compile(regex).matcher(msg);
        if (m.find()) {
            System.out.println(msg.substring(m.start(), m.end()));
        }
    }
}
