package cn.ieclipse.smartim.console;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.browser.BrowserFunction;

public class JSBridge extends BrowserFunction {
    
    public JSBridge(Browser browser, String name) {
        super(browser, name);
    }
    
    @Override
    public Object function(Object[] arguments) {
        Object obj = super.function(arguments);
        if (callback != null) {
            return callback.onFunction(arguments);
        }
        return obj;
    }
    
    private Callback callback;
    
    public void setCallback(Callback callback) {
        this.callback = callback;
    }
    
    public interface Callback {
        Object onFunction(Object[] args);
    }
}
