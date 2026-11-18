# KookSRV

KookSRV 将 Minecraft 与 KOOK 频道连接，支持双向聊天、玩家上下线通知、在线玩家查询卡片，
并可选使用 Floodgate 处理基岩版玩家头像。

## 环境

- Paper 26.3（`spigot-api 26.3`）
- Java 21+
- PlaceholderAPI 2.12.x
- 可选：Floodgate

## 构建

```bash
mvn clean package
```

产物：`target/KookSRV-1.2.0-26.3.jar`（已内嵌并重定位 KookBC/JKook 依赖）。

## 配置

所有设置与文本均在 `config.yml`：

- `kook.*`：token、频道、服务器名、头像 URL。
- `chat.*`：KOOK→Minecraft、Minecraft→KOOK、加入、退出消息开关。
- `online-players.*`：关键词、分隔符、开关。
- `cards.*`：卡片主题与尺寸。
- `messages.*`：全部语言文本。

`/kooksrv reload|status` 可热重载配置并查看当前频道/服务器名。频道、文本与功能开关立即生效；
更换 `kook.token` 需重启服务器，因为 KookBC 的 core 在每个 JVM 中只能初始化一次。

旧版平铺配置（`token`、`channel`、`messages.*`）会在自动升级时迁移到 `kook.*`
与 `messages.*` 新结构。
