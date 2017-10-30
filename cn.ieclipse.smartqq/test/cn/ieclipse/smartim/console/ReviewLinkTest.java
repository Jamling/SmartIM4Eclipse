package cn.ieclipse.smartim.console;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ReviewLinkTest {
    
    String regex = "Code: [\\S ]+:[\\d]+ ?";
    
    @Before
    public void setUp() throws Exception {
    }
    
    @After
    public void tearDown() throws Exception {
    }
    
    @Test
    public void testGetLinkText() {
        
        String input = "16:17:28 Send1: Code: /var/path/file:33";
        input += "\n" + "16:17:38 Send 2: Code: \\C:\\\\path\\file:do.java:33 ";
        input += "\n" + "16:17:46 发送者: Code: /var/path path2/src.:33 ";
        Pattern p = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher m = p.matcher(input);
        List<String> list = new ArrayList<>();
        while (m.find()) {
            list.add(m.group());
        }
        System.out.println(list);
        assertEquals("Code: /var/path/file:33", getLinkText(list.get(0)));
        assertEquals("Code: \\C:\\\\path\\file:do.java:33 ", getLinkText(list.get(1)));
        assertEquals("Code: /var/path path2/src.:33 ", getLinkText(list.get(2)));
    }
    
    private Object getLinkText(String string) {
        return string;
    }
    
}
