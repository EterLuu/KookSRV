package ink.ziip.kooksrv;

import ink.ziip.kooksrv.command.KookSRVCommand;
import ink.ziip.kooksrv.config.Config;
import ink.ziip.kooksrv.config.ConfigurationManager;
import ink.ziip.kooksrv.integration.FloodgateManager;
import ink.ziip.kooksrv.kookbc.listener.KookBCListener;
import ink.ziip.kooksrv.listener.PlayerListener;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import snw.jkook.JKook;
import snw.jkook.config.file.YamlConfiguration;
import snw.kookbc.impl.CoreImpl;
import snw.kookbc.impl.KBCClient;

@Getter
public final class KookSRV extends JavaPlugin {
    @Getter
    private static KookSRV instance;
    private KBCClient kbcClient;
    /**
     * KookBC's CoreImpl may be initialised only once per JVM and JKook refuses a
     * second core registration, so the client is created at most once per
     * server session. Config reloads still apply channel/messages changes.
     */
    private boolean coreInitialised;
    private BukkitScheduler bukkitScheduler;
    private ConfigurationManager configurationManager;
    private PlayerListener playerListener;
    private FloodgateManager floodgateManager;

    public static KookSRV getInstance() { return instance; }
    public KBCClient getKbcClient() { return kbcClient; }
    public BukkitScheduler getBukkitScheduler() { return bukkitScheduler; }
    public ConfigurationManager getConfigurationManager() { return configurationManager; }
    public PlayerListener getPlayerListener() { return playerListener; }
    public FloodgateManager getFloodgateManager() { return floodgateManager; }

    @Override
    public void onEnable() {
        instance = this;
        bukkitScheduler = Bukkit.getServer().getScheduler();
        configurationManager = new ConfigurationManager(instance);
        configurationManager.load();

        PluginCommand command = getCommand("kooksrv");
        if (command != null) {
            KookSRVCommand executor = new KookSRVCommand(this);
            command.setExecutor(executor);
            command.setTabCompleter(executor);
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().warning("Could not find PlaceholderAPI!");
            return;
        }

        floodgateManager = new FloodgateManager(instance);
        floodgateManager.load();
        startBridge();
    }

    /** Starts or restarts all KOOK-related runtime services from current config. */
    public void startBridge() {
        if (coreInitialised) {
            // A KOOK client already exists (or its core was initialised); KookBC
            // cannot re-create it in-process. Channel/messages changes already
            // apply because they are read from Config on every use.
            if (Boolean.TRUE.equals(Config.DEBUG)) {
                getLogger().info("KOOK core already initialised; token changes require a server restart.");
            }
            return;
        }
        stopBridge();
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().warning("Could not find PlaceholderAPI; chat bridge is inactive.");
            return;
        }
        if (!Boolean.TRUE.equals(Config.SETTINGS_ENABLED)) {
            getLogger().info("KookSRV is disabled by settings.enabled.");
            return;
        }
        if (Config.TOKEN == null || Config.TOKEN.isBlank()) {
            getLogger().warning("Could not find a KOOK token in config.yml; chat bridge is inactive.");
            return;
        }

        playerListener = new PlayerListener(instance);
        playerListener.register();
        bukkitScheduler.runTaskAsynchronously(instance, this::initializeKookBC);
    }

    private void stopBridge() {
        if (playerListener != null) {
            playerListener.unRegister();
            playerListener = null;
        }
        if (kbcClient != null) {
            try {
                if (kbcClient.getInternalPlugin() != null) {
                    kbcClient.getCore().getEventManager().unregisterAllHandlers(kbcClient.getInternalPlugin());
                }
            } catch (RuntimeException exception) {
                if (Boolean.TRUE.equals(Config.DEBUG)) {
                    getLogger().warning("Could not unregister KOOK handlers: " + exception.getMessage());
                }
            }
            try {
                kbcClient.shutdown();
            } catch (RuntimeException exception) {
                getLogger().warning("Could not shut down KOOK client: " + exception.getMessage());
            }
            kbcClient = null;
        }
    }

    /**
     * Reloads YAML. Channel, message and feature switches apply immediately;
     * a changed token requires a server restart because KookBC cores are
     * single-use per JVM.
     */
    public void reloadBridge() {
        configurationManager.reload();
        if (!coreInitialised) {
            startBridge();
        }
    }

    @Override
    public void onDisable() {
        stopBridge();
    }

    private void initializeKookBC() {
        CoreImpl core;
        try {
            core = new CoreImpl();
            JKook.setCore(core);
            // From this point on JKook has a registered core for this JVM.
            coreInitialised = true;
            YamlConfiguration config = new YamlConfiguration();
            KBCClient client = new KBCClient(core, config, null, Config.TOKEN);
            client.start();
            client.getCore().getEventManager().registerHandlers(client.getInternalPlugin(), new KookBCListener());
            kbcClient = client;
            if (Boolean.TRUE.equals(Config.DEBUG)) {
                getLogger().info("Connected KookSRV to KOOK channel " + Config.CHANNEL + ".");
            }
        } catch (RuntimeException | Error exception) {
            getLogger().warning("Could not start the KOOK client: " + exception.getMessage());
            getLogger().warning("Fix plugins/KookSRV/config.yml (kook.token) and restart the server; "
                    + "KookBC cannot re-initialise its core in-process.");
        }
    }
}
