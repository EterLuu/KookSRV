# KookSRV

KookSRV 将 Minecraft 与 KOOK 频道连接，支持双向聊天、玩家上下线通知、在线玩家查询卡片，
并可选使用 Floodgate 处理基岩版玩家头像。

## 环境

- Paper 26.3 或 Paper 1.21.11（兼容整个 Paper 1.21.x 系列）
- Java 21+
- PlaceholderAPI 2.12.x
- 可选：Floodgate

## 构建

```bash
bash scripts/build.sh                     # Paper 26.3，默认
PAPER_API_VERSION=1.21.11 bash scripts/build.sh
```

产物写入 `dist/KookSRV-1.2.0-<Paper版本>.jar`（已内嵌并重定位 KookBC/JKook
依赖）。1.21.x 产物声明 `api-version: 1.21`，可通用于 1.21.1 至 1.21.11；26.3
产物声明 `api-version: 26.3`。直接执行 `mvn clean package` 仍会构建默认的
Paper 26.3 版本。

依赖说明：

- KookBC `0.33.0`，从 Maven Central 引入（`io.github.snwcreations:kookbc:0.33.0`），
  其底层为 JKook `0.54.2`。
- KookBC 0.33 的 JSON 实现由 Gson 切换到 Jackson，Jackson（`com.fasterxml.jackson`）
  已随插件内嵌并重定位，避免与服务端自带版本冲突；`META-INF/services` 通过
  `ServicesResourceTransformer` 同步重定位。
- KookBC 0.33 默认在虚拟线程上并行派发事件；本插件已关闭并行事件处理以保持消息顺序，
  所有 Bukkit API 调用都会回到主线程执行。

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
