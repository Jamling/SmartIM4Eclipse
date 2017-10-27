[![GitHub release](https://img.shields.io/github/release/jamling/SmartQQ4Eclipse.svg?maxAge=3600)](https://github.com/Jamling/SmartQQ4Eclipse)

## SmartQQ4Eclipse

![screenshot](https://raw.githubusercontent.com/Jamling/SmartQQ4Eclipse/master/screenshot.gif)

Eclipse上的SmartQQ/微信插件，防Boss利器，让你可以在eclipse中使用QQ/微信进行聊天

Intellij IDEA上的插件请移步这里: https://github.com/Jamling/SmartQQ4IntelliJ, 支持所有的Intellij IDE哦，包含Android Studio, WebStrom等

## 功能

特色功能

- 热键设定
- 一键关闭
- 支持图灵机器人接入

|Feature           |Origin     | Eclipse    | IntelliJ    |
| ---------------- |:---------:|:----------:|:-----------:|
| 文本消息          | yes       |          X | X         |
| 聊天记录          | yes       |          Y | X         |
| 消息提醒          | O         |          Y | X         |
| 表情              | yes (系统表情)|   X| X |
| 图灵QQ机器人        | X       |          yes | X|
| [发送文件](http://api.ieclipse.cn/smartqq/)        | X       |          yes | O 不支工程文件快捷发送|
| 消息群发        | X       |          yes | X|
| 代码评审        | X       |          yes | X|

## 安装

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

1. 点击Windows->Show view，找到SmartQQ下的Smart双击打开Smart视图
2. 点击视图工具栏或菜单栏中的同步图标进行登录
3. 使用手机QQ扫描二维码
4. 验证成功后，等待拉取最近消息，好友及群组列表
5. 双击Smart视图中的好友或群，打开聊天窗口（聊天窗口为Console）
6. 使用快捷键或点击I图标，激活输入窗口（不建议直接在console中输入，会导致同步时间）
7. 输入聊天内容，并按快捷键（默认为Enter）发送聊天信息

### 微信
1. 如果微信无法生成二维码图片，提示javax.net.ssl.SSLProtocolException: handshake alert:  unrecognized_name，请给eclipse加上`-Djsse.enableSNIExtension=false`参数，可以在eclipse.ini中加，也可以在eclipse快捷方式目标位置中添加

## 进阶使用
1. 文件发送
 - 点击聊天Console菜单（就是那个倒三角）选择 Send File 或 Send Project File 选择文件后发送
 - 对要发送的文件复制，然后粘贴到聊天窗口，再点击发送超链接，确认发送文件
2. 消息群发
 - 在Smart视图中，点击Broadcast工具图标（就是那个发射塔的图标），输入内容并选择要发送的好友，群及讨论群，再点击确定
3. 代码评审（未来接入Gerrit）
 - 在编辑器中右键菜单中点击`Code Review`，输入对代码的评审注释，选择发送的对象，点击确定发送
 - 对方接收到代码评审消息，点击代码位置，直接跳转到本地相同的代码位置，然后修改吧。

## 快捷键

注：在eclipse中，CR表示Enter键

- 激活输入，默认CR，在console下面打开一个小窗口进行输入
- 发送，默认CR (Enter键)，发送消息
- 上/下一个聊天, 默认左/右箭头，也可以在console工具栏使用鼠标切换
- 隐藏聊天，默认Alt + M，隐藏Contact视图，清空当前聊天内容
- 关闭聊天，默认Alt + C，关闭Contact视图和所有的聊天窗口
- 退出输入，默认ESC

注：快捷键有可能与eclipse中的热键冲突，请点击？打开首选项重新设置

## 感谢

SmartQQ Java API: https://github.com/ScienJus/smartqq


## 问题提交

任何问题包括建议均可以在[Issue](https://github.com/Jamling/SmartQQ4Eclipse/issues)中提交

如果为Issue，建议带上eclipse版本及本插件版本信息（可以在Preference->SmartQQ中查看并复制版本信息）
