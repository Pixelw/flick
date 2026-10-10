# Flick

Flick 是一个开发中的 Android 应用，当前主要提供 BanG Dream! 歌曲浏览与在线播放，以及 Bandori Station 房间信息展示。

## 主要技术栈

| 方向 | 当前使用的技术 |
| --- | --- |
| 开发语言与构建 | AGP 内置 Kotlin 2.4.21、Gradle 9.6.0、Android Gradle Plugin 9.4.1、Gradle Kotlin DSL |
| 界面 | Jetpack Compose（BOM 2026.09.00）、Material 3，结合 Fragment、XML 布局与 Data Binding |
| 状态与架构 | ViewModel、LiveData、StateFlow、Kotlin Coroutines 1.11.0，按功能组织的 Repository 数据层 |
| 页面导航 | AndroidX Navigation 2.10.2，使用 Fragment 导航图承载页面 |
| 音乐播放 | AndroidX Media3 1.11.1：ExoPlayer、MediaSessionService、MediaController |
| 网络通信 | Retrofit 3.0.0、OkHttp 5.5.0、Cronet 500.1.0；WebSocket 使用 OkHttp |
| JSON 解析 | Moshi 1.15.2，配合 KSP 2.3.12 生成适配器 |
| 图片加载 | Coil 3.6.3，用于歌曲封面和用户头像，启用磁盘缓存 |
| 偏好存储 | Preferences DataStore 1.2.1，已接入基础设施 |
| 调试与测试 | LeakCanary 2.14（Debug）、JUnit 4、AndroidX JUnit、Espresso、Compose UI Test |

## 已实现功能

- **歌曲列表**：从接口加载 BanG Dream! 歌曲，展示封面、歌曲名称等信息，并缓存列表数据。
- **在线播放**：选择歌曲播放，支持播放／暂停、上一首／下一首、进度显示与拖动定位。
- **后台播放**：通过 MediaSessionService 承载播放，并接入系统媒体会话。
- **底部播放栏**：展示当前歌曲，支持滑动切歌和进入播放页面。
- **Bandori Station**：通过 WebSocket 获取房间列表，展示房间号、说明、用户头像与时间，并处理后续推送。
- **页面切换与主题**：侧边抽屉切换车站和音乐页面，支持跟随系统的深浅色主题及 Android 12+ 动态配色。
- **资源源站配置**：启动时获取远程源站配置，统一生成音乐文件和图片资源地址。

当前尚未完成车站登录、聊天发送、房间复制与跳转，以及 Project SEKAI 等其他入口。

## 构建

使用 JDK 25（可使用 Android Studio 自带 JBR）和 Android SDK 37；最低支持 Android 8.0（API 26），目标 SDK 为 37。Java/Kotlin 字节码目标为 11。

Android 17 兼容验证应覆盖前台开始播放后锁屏、通知恢复播放、音频焦点中断恢复，以及平板横屏／分屏。页面已处理系统栏安全间距，后台播放由 MediaSessionService 管理；仍需在 Android 17 设备上验证实际行为。

在 Android Studio 中将项目 Gradle JDK 指向 JDK 25，并将命令行的 `JAVA_HOME` 设置为同一个 JDK。机器路径仅保存在本机配置中。

```bash
./gradlew :app:assembleDebug --no-daemon
```

APK 输出位置：`app/build/outputs/apk/debug/app-debug.apk`。

项目代码位于 `app/src/main/java/tech/pixelw/flick/`，贡献约定见 [AGENTS.md](AGENTS.md)。
