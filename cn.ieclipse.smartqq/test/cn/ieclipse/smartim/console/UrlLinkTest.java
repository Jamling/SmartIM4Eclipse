package cn.ieclipse.smartim.console;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
                
                String chstr = "(.+?)(" + UCS_CHAR + "+$)";
                System.out.println(url.replaceAll(chstr, "$1"));
            }
            else {
                fail(str + " Not matches");
            }
        }
    }
    
    public static final String UCS_CHAR = "[" + "\u00A0-\uD7FF"
            + "\uF900-\uFDCF" + "\uFDF0-\uFFEF" + "\uD800\uDC00-\uD83F\uDFFD"
            + "\uD840\uDC00-\uD87F\uDFFD" + "\uD880\uDC00-\uD8BF\uDFFD"
            + "\uD8C0\uDC00-\uD8FF\uDFFD" + "\uD900\uDC00-\uD93F\uDFFD"
            + "\uD940\uDC00-\uD97F\uDFFD" + "\uD980\uDC00-\uD9BF\uDFFD"
            + "\uD9C0\uDC00-\uD9FF\uDFFD" + "\uDA00\uDC00-\uDA3F\uDFFD"
            + "\uDA40\uDC00-\uDA7F\uDFFD" + "\uDA80\uDC00-\uDABF\uDFFD"
            + "\uDAC0\uDC00-\uDAFF\uDFFD" + "\uDB00\uDC00-\uDB3F\uDFFD"
            + "\uDB44\uDC00-\uDB7F\uDFFD"
            + "&&[^\u00A0[\u2000-\u200A]\u2028\u2029\u202F\u3000]]";
}
