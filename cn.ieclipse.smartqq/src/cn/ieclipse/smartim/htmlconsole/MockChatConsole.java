package cn.ieclipse.smartim.htmlconsole;

import org.eclipse.swt.SWT;

import cn.ieclipse.smartim.SmartClient;
import cn.ieclipse.smartim.common.IMUtils;
import cn.ieclipse.smartim.common.LetterImageFactory;
import cn.ieclipse.smartim.model.IContact;
import cn.ieclipse.smartim.views.IMContactView;

/**
 * Created by Jamling on 2018/1/3.
 */
public class MockChatConsole extends IMChatConsole {
    public MockChatConsole(IContact target, IMContactView imPanel) {
        super(target, imPanel);
        IMG_NORMAL = LetterImageFactory.create('F', SWT.COLOR_BLACK);
        IMG_SELECTED = LetterImageFactory.create('F', SWT.COLOR_RED);
        setImage(IMG_NORMAL);
    }
    
    @Override
    public void loadHistory(String raw) {
    
    }
    
    @Override
    public void post(String msg) {
    
    }
    
    @Override
    public SmartClient getClient() {
        return null;
    }
    
    @Override
    public void loadHistories() {
    
    }
    
    @Override
    public void send(String input) {
        String msg = IMUtils.formatHtmlMyMsg(System.currentTimeMillis(), "Me",
                input);
        if (!hideMyInput()) {
            insertDocument(msg);
        }
    }
}
