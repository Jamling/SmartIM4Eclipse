package cn.ieclipse.smartim.console;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SenderLinkTest {
    public String regex = "^\\d{2}:\\d{2}:\\d{2} [^:]+: ";
    
    @Before
    public void setUp() throws Exception {
    }
    
    @After
    public void tearDown() throws Exception {
    }
    
    @Test
    public void testGetLinkText() {
        String input = "16:17:28 Send1: SmartQQ支持发送文件吗？";
        input += "\n" + "16:17:38 Send 2: 支持的";
        input += "\n" + "16:17:46 发送者: 你是什么版本？";
        Pattern p = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher m = p.matcher(input);
        List<String> list = new ArrayList<>();
        while (m.find()) {
            list.add(m.group());
        }
        assertEquals("Send1", getLinkText(list.get(0)));
        assertEquals("Send 2", getLinkText(list.get(1)));
        assertEquals("发送者", getLinkText(list.get(2)));
    }
    
    private String getLinkText(String line) {
        int linkStart = 9;
        int linkEnd = line.indexOf(':', linkStart);
        return line.substring(linkStart, linkEnd);
    }
}
