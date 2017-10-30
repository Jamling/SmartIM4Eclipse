package cn.ieclipse.smartim.console;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UrlLinkTest {
    
    @Before
    public void setUp() throws Exception {
    }
    
    @After
    public void tearDown() throws Exception {
    }
    
    String regex = "(https?|ftp|file)://(([\\w-~]+).)+([\\w-~\\/])+(((?!\\.)(\\S))+(\\.\\w+(\\?(\\w+=\\S&?)*)?)?)?";
    
    @Test
    public void test() {
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
                assertTrue("Matches", true);
            }
            else {
                fail(str + " Not matches");
            }
        }
    }
    
}
