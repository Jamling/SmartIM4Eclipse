package cn.ieclipse.smartim.actions;

import cn.ieclipse.smartim.htmlconsole.IMChatConsole;
import icons.SmartIcons;

public class SendImageAction extends SendFileAction {
    
    public SendImageAction(IMChatConsole console) {
        super(console);
        setText("图片");
        this.setToolTipText("发送图片");
        setImageDescriptor(SmartIcons.image);
        filterNames = new String[] { "Image Files", "All Files (*)" };
        filterExtensions = new String[] { "*.gif;*.png;*.jpg;*.jpeg;*.bmp",
                "*" };
    }
    
}
