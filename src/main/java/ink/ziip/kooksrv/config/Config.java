package ink.ziip.kooksrv.config;

import ink.ziip.kooksrv.KookSRV;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
public class Config extends BaseConfigurationFile {
    private final String fileName = "config.yml";
    private final String resourceName = "config.yml";

    public Config(@NotNull KookSRV plugin) {
        super(plugin);
    }

    @Override
    public int getLatestVersion() {
        return 2;
    }

    // Kook
    @ConfigOption(path = "token")
    public static String TOKEN;

    @ConfigOption(path = "channel")
    public static String CHANNEL;

    // Online players
    @ConfigOption(path = "online-players.keywords")
    public static List<String> ONLINE_PLAYERS_KEYWORDS;

    // Messages
    @ConfigOption(path = "messages.minecraft2kook")
    public static String MESSAGE_MINECRAFT_2_KOOK;

    @ConfigOption(path = "messages.kook2minecraft")
    public static String MESSAGE_KOOK_2_MINECRAFT;

    @ConfigOption(path = "messages.online-player-list")
    public static String MESSAGE_ONLINE_PLAYER_LIST;

    @ConfigOption(path = "messages.no-online-player")
    public static String MESSAGE_NO_ONLINE_PLAYER;

    @ConfigOption(path = "messages.player-join")
    public static String MESSAGE_PLAYER_JOIN;

    @ConfigOption(path = "messages.player-quit")
    public static String MESSAGE_PLAYER_QUIT;
}
