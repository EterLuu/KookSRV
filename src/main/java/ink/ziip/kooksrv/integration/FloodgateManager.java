package ink.ziip.kooksrv.integration;

import ink.ziip.kooksrv.KookSRV;
import ink.ziip.kooksrv.api.BaseManager;
import org.bukkit.Bukkit;
import org.geysermc.floodgate.api.FloodgateApi;

import java.util.UUID;

public class FloodgateManager extends BaseManager {
    private FloodgateApi floodgateApi;
    private boolean enabled;

    public FloodgateManager(KookSRV kookSRV) {
        super(kookSRV);
        enabled = false;
    }

    @Override
    public void load() {
        if (Bukkit.getPluginManager().getPlugin("floodgate") == null) {
            enabled = false;
        } else {
            enabled = true;
            floodgateApi = FloodgateApi.getInstance();
        }
    }

    @Override
    public void unload() {

    }

    public boolean isFloodgatePlayer(UUID uuid) {
        if (!enabled) {
            return false;
        }
        return floodgateApi.isFloodgatePlayer(uuid);
    }
}
