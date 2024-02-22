package ink.ziip.kooksrv.config;

import ink.ziip.kooksrv.KookSRV;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

@Getter
public class Config extends BaseConfigurationFile {
    private final String fileName = "config.yml";
    private final String resourceName = "config.yml";

    public Config(@NotNull KookSRV plugin) {
        super(plugin);
    }

    @Override
    public int getLatestVersion() {
        return 0;
    }

    // Kook
    @ConfigOption(path = "token")
    public static String TOKEN;

    @ConfigOption(path = "channel")
    public static String CHANNEL;

    // Messages
    @ConfigOption(path = "messages.minecraft2kook")
    public static String MESSAGE_MINECRAFT_2_KOOK;

    @ConfigOption(path = "messages.kook2minecraft")
    public static String MESSAGE_KOOK_2_MINECRAFT;
}
