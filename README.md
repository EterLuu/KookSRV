# KookSRV

KookSRV 把 Minecraft 服务器的聊天接到一个 KOOK 文字频道。玩家可以在游戏和 KOOK 之间对话，频道也能收到上下线通知并查询在线名单。

## 连接 KOOK 频道

安装 KookSRV 和 PlaceholderAPI。启动一次生成[配置](src/main/resources/config.yml)，然后在 `kook` 下填写机器人令牌和频道。用 `chat` 和 `online-players` 选择要转发的内容。安装 Floodgate 后，头像可以使用基岩版玩家名。

用 `/kooksrv status` 核对频道，修改文本或功能开关后用 `/kooksrv reload`。更换机器人令牌需要重启服务端。自行打包请参照[构建说明](docs/building.md)。
