[![GitHub release](https://img.shields.io/github/release/jamling/SmartQQ4Eclipse.svg?maxAge=3600)](https://github.com/Jamling/SmartQQ4Eclipse)

## SmartQQ4Eclipse

![screenshot](https://raw.githubusercontent.com/Jamling/SmartQQ4Eclipse/master/main.png)
![screenshot](https://raw.githubusercontent.com/Jamling/SmartQQ4Eclipse/master/chat.png)

Eclipse上的SmartQQ插件，防Boss利器，让你可以在eclipse中使用QQ进行聊天

Intellij IDEA上的插件请移步这里: https://github.com/Jamling/SmartQQ4IntelliJ, 支持所有的Intellij IDE哦，包含Android Studio, WebStrom等

## 功能
- 收发文本消息
- 热键设定
- 一键关闭
- 支持图灵机器人接入
- 支持发送文件（v1.0.6）

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
