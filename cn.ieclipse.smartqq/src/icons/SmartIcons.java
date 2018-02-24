/*
 * Copyright 2014-2017 ieclipse.cn.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package icons;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.ide.IDE;

import cn.ieclipse.smartim.IMPlugin;

/**
 * 类/接口描述
 * 
 * @author Jamling
 * @date 2017年11月2日
 *       
 */
public class SmartIcons {
    public static abstract class IconLoader {
        public static ImageDescriptor getIcon(String path) {
            return IMPlugin.getImageDescriptor(path);
        }
        public static ImageDescriptor getShare(String name) {
            return IMPlugin.getSharedImage(name);
        }
    }
    
    public static ImageDescriptor qq = IconLoader.getIcon("/icons/QQ.png");
    public static ImageDescriptor wechat = IconLoader.getIcon("/icons/wechat.png");
    
    public static ImageDescriptor signin = IconLoader.getIcon("/icons/qrcode.png");
    public static ImageDescriptor signout = IconLoader.getShare(ISharedImages.IMG_ELCL_STOP);
    public static ImageDescriptor test = IconLoader.getShare(ISharedImages.IMG_DEC_FIELD_WARNING);
    public static ImageDescriptor close = IconLoader.getIcon("/icons/close.png");
    public static ImageDescriptor show = IconLoader.getIcon("/icons/eye.png");
    public static ImageDescriptor hide = IconLoader.getIcon("/icons/eye-slash.png");
    public static ImageDescriptor broadcast = IconLoader.getIcon("/icons/broadcast.png");
    public static ImageDescriptor settings = IconLoader.getShare(ISharedImages.IMG_LCL_LINKTO_HELP);
    
    public static ImageDescriptor group = IconLoader.getIcon("/icons/user-circle.png");
    public static ImageDescriptor friend = IconLoader.getIcon("/icons/user.png");
    public static ImageDescriptor discuss = IconLoader.getIcon("/icons/user-o.png");
    
    public static ImageDescriptor file = IconLoader.getShare(ISharedImages.IMG_OBJ_FILE);
    public static ImageDescriptor image = IconLoader.getIcon("/icons/image.png");
    public static ImageDescriptor projectFile = IconLoader.getShare(IDE.SharedImages.IMG_OBJ_PROJECT);
    public static ImageDescriptor face = IconLoader.getIcon("/icons/face.png");
    public static ImageDescriptor lock = IconLoader.getIcon("/icons/lock_co.png");
    public static ImageDescriptor clear = IconLoader.getIcon("/icons/clear_co.png");
    
    public static void main(String[] args) {
        System.out.println(group);
    }
    
}
