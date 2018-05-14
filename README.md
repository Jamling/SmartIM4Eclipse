[![GitHub release](https://img.shields.io/github/release/jamling/SmartQQ4Eclipse.svg?maxAge=3600)](https://github.com/Jamling/SmartQQ4Eclipse)
[![Eclipse Marketplace](https://img.shields.io/eclipse-marketplace/v/SmartQQ.svg)](https://marketplace.eclipse.org/content/smartqq)
[![Eclipse Marketplace](https://img.shields.io/eclipse-marketplace/dt/SmartQQ.svg)](https://marketplace.eclipse.org/content/smartqq)

## SmartQQ4Eclipse

![screenshot](https://raw.githubusercontent.com/Jamling/SmartQQ4Eclipse/master/screenshot.gif)

Eclipse上的SmartQQ/微信插件，防Boss利器，让你可以在eclipse中使用QQ/微信进行聊天

Intellij IDEA上的插件请移步这里: https://github.com/Jamling/SmartQQ4IntelliJ, 支持所有的Intellij IDE哦，包含Android Studio, WebStrom等

## 功能

- [x] 收发文本消息
- [x] 收发图片
- [x] 收发文件
- [x] 发送工程中的文件
- [x] Code Review 发送代码位置及评语
- [x] 超链接点击
- [x] 热键设定
- [x] 一键关闭
- [x] 支持图灵机器人接入

**SmartQQ官方协议不支持收发图片和文件，扩展的文件收发由[第三方实现](http://api.ieclipse.cn/smartqq)**

|Feature           |Swing      | Eclipse    | IntelliJ    |
| ---------------- |:---------:|:----------:|:-----------:|
| 文本消息          | Y         |          Y | Y           |
| 聊天记录          | Y         |          Y | Y           |
| 消息提醒          | N         |          Y | O           |
| 接收表情          | O         |          Y |           X |
| 图灵机器人        | Y         |          Y |           X |
| 发送文件          | Y         |          Y |           Y |
| 消息群发          | Y         |          Y |           Y |
| 代码评审          | X         |          Y |           Y |

## 安装

从3.0开始，本插件依赖于[Eclipse Explorer]插件，如果安装时提示缺失[Eclipse Explorer]插件，如下示例错误
```
Cannot complete the install because one or more required items could not be found.
Software being installed: SmartQQ Feature 3.1.0.201803020925 (cn.ieclipse.smartqq.feature.feature.group 3.1.0.201803020925)
Missing requirement: SmartQQ Feature 3.1.0.201803020925 (cn.ieclipse.smartqq.feature.feature.group 3.1.0.201803020925)
requires 'cn.ieclipse.pde.explorer 4.1.0' but it could not be found
```
请先安装[Eclipse Explorer]插件或手动添加http://dl.ieclipse.cn/updates 更新站点后再安装SmartQQ

### Eclipse Marketplace

1. 点击Eclipse->Help->Eclipse Marketplace...打开eclipse插件市场
2. 输入SmartQQ搜索
3. 点击Install安装

**推荐使用Eclipse Martplace安装**

### Install New Software

1. 点击Eclipse->Help->Install New Software...搜索插件
2. 在Work with后面的输入框中输入http://dl.ieclipse.cn/updates/ 并回车
3. 选中SmartQQ，并取消勾选"Contact all update site during install to find required software"
4. 点击底部Next按钮继续安装

## 使用

1. 点击Windows->Show view->Others，找到SmartIM下的SmartQQ或Wechat双击打开SmartQQ或Wechat视图
2. 点击视图工具栏或菜单栏中的同步图标进行登录
3. 使用手机QQ/手机微信扫描二维码（SmartQQ视图使用手机QQ，Wechat视图使用手机微信扫一扫）
4. 验证成功后，等待拉取最近消息，好友及群组列表
5. 双击SmartIM视图中的好友或群，打开聊天窗口
6. 输入聊天内容，并按快捷键（默认为Enter）发送聊天信息

### 微信
1. 如果微信无法生成二维码图片，提示javax.net.ssl.SSLProtocolException: handshake alert:  unrecognized_name，请给eclipse加上`-Djsse.enableSNIExtension=false`参数，可以在eclipse.ini中加，也可以在eclipse快捷方式目标位置中添加

## 进阶使用
1. 文件发送
 - 点击聊天Console菜单（就是那个倒三角）选择 Send File 或 Send Project File 选择文件后发送
 - 对要发送的文件复制，然后粘贴到聊天窗口，再点击发送超链接，确认发送文件
2. 消息群发
 - 在SmartIM视图中，点击Broadcast工具图标（就是那个发射塔的图标），输入内容并选择要发送的好友，群及讨论群，再点击确定
3. 代码评审（未来接入Gerrit）
 - 在编辑器中右键菜单中点击`Code Review`，输入对代码的评审注释，选择发送的对象，点击确定发送
 - 对方接收到代码评审消息，点击代码位置，直接跳转到本地相同的代码位置，然后修改吧。

## 快捷键

注：在eclipse中，CR表示Enter键

- 发送，默认CR (Enter键)，发送消息
- 隐藏聊天，默认Alt + M，隐藏Contact视图
- 关闭聊天，默认Alt + C，关闭当前聊天窗口

注：快捷键有可能与eclipse中的热键冲突，请点击？打开首选项重新设置

## 感谢

- SmartQQ Java API: https://github.com/ScienJus/smartqq
- Wechat Java API: https://github.com/biezhi/wechat-bot-api
- SmartIM library: https://github.com/Jamling/SmartIM

## 问题提交

提交问题前，请参考[常见问题](https://github.com/Jamling/SmartIM/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98)

任何问题包括建议均可以在[Issue](https://github.com/Jamling/SmartQQ4Eclipse/issues)中提交

如果为Issue，建议带上eclipse版本及本插件版本信息（可以在Preference->SmartQQ中查看并复制版本信息）

## 开发

如果您对本项目感兴趣，请fork本项目，源代码下载完成之后，会有编译错误，需要将依赖的smartim-core等三个工程从build path中移除，并将libs下的core-x.x.x.jar, smartqq-x.x.x.jar, wechat.x.x.x.jar添加到build path.

[Eclipse Explorer]: https://github.com/Jamling/eclipse-explorer
