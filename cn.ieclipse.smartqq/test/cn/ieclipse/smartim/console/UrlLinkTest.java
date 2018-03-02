package cn.ieclipse.smartim.console;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.util.Patterns;

public class UrlLinkTest {
    @Before
    public void setUp() throws Exception {
    }
    
    @After
    public void tearDown() throws Exception {
    }
    
    String regex = "(https?|ftp|file)://(([\\w-~]+)\\.)+([\\w-~\\/])+(((?!\\.)(\\S))+(\\.\\w+(\\?(\\w+=\\S&?)*)?)?)?";
    
    @Test
    public void test() {
        String msg = "qhttps://www.baidu.com/test/中%DE%DE%DE我的.html";
        List<String> tests = Arrays.asList(msg, "http://wab.com/",
                "一段http://abc.com中文", "http://t.cn/abc def", "https://t.cn/我的",
                "https://t.cn/我的 gogo", "http://t.cn/我的.abc",
                "https://t.cn/我的.txt 中", "http://t.cn/我的?n=我去",
                "ftp://t.cn/?n=我去", "https://t.cn/我的?n=我去&b=33",
                "http://abc.com/2/f1.0.2(9)-release.apk dd");
        for (String str : tests) {
            Matcher m = Patterns.WEB_URL.matcher(str);
            if (m.find()) {
                String url = m.group();
                System.out.println(url);
                assertTrue("Matches", true);
                
                String chstr = "(.+?)(" + IMUtils.UCS_CHAR + "+$)";
                System.out.println(url.replaceAll(chstr, "$1"));
            }
            else {
                fail(str + " Not matches");
            }
        }
    }
}
