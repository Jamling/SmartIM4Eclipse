package cn.ieclipse.smartim.console;

import java.util.Arrays;
import java.util.List;
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
        String regex = "(https?|ftp|file)://(([\\w-~]+).)+([\\w-~\\/])+(((?!\\.)(\\S))+(\\.\\w+(\\?(\\w+=\\S&?)*)?)?)?";
        String msg = "qhttps://www.baidu.com/test/中%DE%DE%DE我的.html";
        List<String> tests = Arrays.asList(msg, "http://wab.com/",
                "http://abc.com", "http://t.cn/dfjdkf", "sftp://t.cn/我的",
                "sftp://t.cn/我的 gogo", "sftp://t.cn/我的.abc",
                "sftp://t.cn/我的.txt中", "ftp://t.cn/我的?n=我去", "ftp://t.cn/?n=我去",
                "ftp://t.cn/我的?n=我去&b=33");
        for (String str : tests) {
            Matcher m = Pattern.compile(regex).matcher(str);
            if (m.find()) {
                System.out.println(str.substring(m.start(), m.end()));
            }
            else {
                System.err.println(str);
            }
        }
    }
}
