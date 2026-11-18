package ink.ziip.kooksrv.config;

import ink.ziip.kooksrv.KookSRV;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.List;

@Getter
public class Config extends BaseConfigurationFile {
    private final String fileName = "config.yml";
    private final String resourceName = "config.yml";

    public Config(@NotNull KookSRV plugin) {
        super(plugin);
    }

    @Override public String getFileName() { return fileName; }
    @Override public String getResourceName() { return resourceName; }
    @Override public int getLatestVersion() { return 3; }

    /**
     * Migrates legacy flat keys before typed options are read, so an old
     * config never logs a wall of "null value" errors on first start.
     */
    @Override
    public void loadFileOptions() {
        migrateLegacyKeys();
        attachBundledDefaults();
        super.loadFileOptions();
    }

    /** Makes every key in the bundled resource available as a fallback. */
    private void attachBundledDefaults() {
        try (var stream = plugin.getResource(getResourceName())) {
            if (stream == null) return;
            YamlConfiguration defaults = new YamlConfiguration();
            defaults.loadFromString(new String(stream.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8));
            configuration.setDefaults(defaults);
            configuration.options().copyDefaults(true);
        } catch (Exception ignored) {
        }
    }

    /** Loads old flat keys into the nested structure, preserving values. */
    public void migrateLegacyKeys() {
        var old = this.configuration;
        if (old == null) return;
        boolean changed = false;
        if (old.contains("token") && !old.contains("kook.token")) {
            old.set("kook.token", old.getString("token"));
            old.set("token", null);
            changed = true;
        }
        if (old.contains("channel") && !old.contains("kook.channel")) {
            old.set("kook.channel", old.getString("channel"));
            old.set("channel", null);
            changed = true;
        }
        // Add missing defaults for options introduced after the last version bump.
        // Values come from the bundled config.yml so no text is hardcoded in Java.
        for (String path : DEFAULT_PATHS) {
            addDefault(path, defaultFromResource(path));
        }
        if (changed || changedByDefaults) {
            saveConfiguration();
            changedByDefaults = false;
        }
    }

    private static final String[] DEFAULT_PATHS = {
            "settings.enabled",
            "kook.server-name",
            "kook.avatar-url",
            "chat.kook-to-minecraft.enabled",
            "chat.minecraft-to-kook.enabled",
            "chat.player-join.enabled",
            "chat.player-quit.enabled",
            "online-players.enabled",
            "online-players.separator",
            "cards.join-theme",
            "cards.quit-theme",
            "cards.size"
    };

    /** Reads a default value from the bundled resource config.yml. */
    private Object defaultFromResource(String path) {
        try (var stream = plugin.getResource(getResourceName())) {
            if (stream == null) return null;
            YamlConfiguration defaults = new YamlConfiguration();
            defaults.loadFromString(new String(stream.readAllBytes(), java.nio.charset.StandardCharsets.UTF_8));
            return defaults.get(path);
        } catch (Exception exception) {
            return null;
        }
    }

    private void addDefault(String path, Object value) {
        if (!configuration.contains(path) && value != null) {
            configuration.set(path, value);
            changedByDefaults = true;
        }
    }

    private boolean changedByDefaults = false;

    @Override
    public void loadFromOutdatedConfiguration(@NotNull YamlConfiguration outdated) {
        if (outdated.contains("token")) outdated.set("kook.token", outdated.getString("token"));
        if (outdated.contains("channel")) outdated.set("kook.channel", outdated.getString("channel"));
        super.loadFromOutdatedConfiguration(outdated);
    }

    public void saveConfiguration() {
        try {
            configuration.save(configurationPath.toFile());
        } catch (Exception ignored) {
        }
    }

    @ConfigOption(path = "settings.enabled") public static Boolean SETTINGS_ENABLED;
    @ConfigOption(path = "settings.debug") public static Boolean DEBUG;

    @ConfigOption(path = "kook.token") public static String TOKEN;
    @ConfigOption(path = "kook.channel") public static String CHANNEL;
    @ConfigOption(path = "kook.server-name") public static String SERVER_NAME;
    @ConfigOption(path = "kook.avatar-url") public static String AVATAR_URL;

    @ConfigOption(path = "chat.kook-to-minecraft.enabled") public static Boolean CHAT_KOOK_TO_MINECRAFT;
    @ConfigOption(path = "chat.minecraft-to-kook.enabled") public static Boolean CHAT_MINECRAFT_TO_KOOK;
    @ConfigOption(path = "chat.player-join.enabled") public static Boolean PLAYER_JOIN_ENABLED;
    @ConfigOption(path = "chat.player-quit.enabled") public static Boolean PLAYER_QUIT_ENABLED;

    @ConfigOption(path = "online-players.enabled") public static Boolean ONLINE_PLAYERS_ENABLED;
    @ConfigOption(path = "online-players.keywords") public static List<String> ONLINE_PLAYERS_KEYWORDS;
    @ConfigOption(path = "online-players.separator") public static String ONLINE_PLAYERS_SEPARATOR;

    @ConfigOption(path = "cards.join-theme") public static String JOIN_CARD_THEME;
    @ConfigOption(path = "cards.quit-theme") public static String QUIT_CARD_THEME;
    @ConfigOption(path = "cards.size") public static String CARD_SIZE;

    @ConfigOption(path = "messages.minecraft2kook") public static String MESSAGE_MINECRAFT_2_KOOK;
    @ConfigOption(path = "messages.kook2minecraft") public static String MESSAGE_KOOK_2_MINECRAFT;
    @ConfigOption(path = "messages.online-player-list") public static String MESSAGE_ONLINE_PLAYER_LIST;
    @ConfigOption(path = "messages.no-online-player") public static String MESSAGE_NO_ONLINE_PLAYER;
    @ConfigOption(path = "messages.player-join") public static String MESSAGE_PLAYER_JOIN;
    @ConfigOption(path = "messages.player-quit") public static String MESSAGE_PLAYER_QUIT;
    @ConfigOption(path = "messages.command-usage") public static String MESSAGE_USAGE;
    @ConfigOption(path = "messages.command-reload-success") public static String MESSAGE_RELOAD_SUCCESS;
    @ConfigOption(path = "messages.command-reload-failed") public static String MESSAGE_RELOAD_FAILED;
    @ConfigOption(path = "messages.command-status") public static String MESSAGE_STATUS;
}
