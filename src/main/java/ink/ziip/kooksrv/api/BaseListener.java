package ink.ziip.kooksrv.api;

import ink.ziip.kooksrv.KookSRV;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.IllegalPluginAccessException;

public abstract class BaseListener implements Listener {
    protected final KookSRV plugin;

    protected BaseListener(KookSRV plugin) {
        this.plugin = plugin;
    }

    public void register() {
        try {
            Bukkit.getPluginManager().registerEvents(this, KookSRV.getInstance());
        } catch (IllegalPluginAccessException ignored) {
        }
    }

    public void unRegister() {
        HandlerList.unregisterAll(this);
    }
}