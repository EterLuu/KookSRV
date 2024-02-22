package ink.ziip.kooksrv;

import ink.ziip.kooksrv.config.Config;
import ink.ziip.kooksrv.config.ConfigurationManager;
import ink.ziip.kooksrv.kookbc.listener.KookBCListener;
import ink.ziip.kooksrv.listener.PlayerListener;
import lombok.Getter;
import org.bukkit.Bukkit;
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
    private BukkitScheduler bukkitScheduler;
    private ConfigurationManager configurationManager;
    private PlayerListener playerListener;


    @Override
    public void onEnable() {
        // Plugin startup logic

        instance = this;

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            getLogger().warning("Could not find PlaceholderAPI!");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        bukkitScheduler = Bukkit.getServer().getScheduler();

        configurationManager = new ConfigurationManager(instance);
        configurationManager.load();

        if (Config.TOKEN.isEmpty()) {
            getLogger().warning("Could not found token in config file!");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        CoreImpl core = new CoreImpl();
        JKook.setCore(core);
        YamlConfiguration config = new YamlConfiguration();
        kbcClient = new KBCClient(core, config, null, Config.TOKEN);
        kbcClient.start();

        kbcClient.getCore().getEventManager().registerHandlers(kbcClient.getInternalPlugin(), new KookBCListener());

        playerListener = new PlayerListener(instance);
        playerListener.register();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        if (playerListener != null)
            playerListener.unRegister();

        if (kbcClient != null) {
            kbcClient.getCore().getEventManager().unregisterAllHandlers(kbcClient.getInternalPlugin());
            kbcClient.shutdown();
        }
    }
}
