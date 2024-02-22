package ink.ziip.kooksrv.config;

import ink.ziip.kooksrv.KookSRV;
import ink.ziip.kooksrv.api.BaseManager;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Modified under <a href="https://github.com/AlessioDP/ADP-Core">ADP-Core</a>
 *
 * @author AlessioDP
 */
@Getter
public abstract class BaseConfigurationManager extends BaseManager {
    private final List<BaseConfigurationFile> configs = new ArrayList<>();

    public BaseConfigurationManager(@NotNull KookSRV plugin) {
        super(plugin);
    }

    @Override
    public void load() {
        reload();
    }

    @Override
    public void unload() {
    }

    public void reload() {
        // Load defaults
        for (BaseConfigurationFile baseConfigurationFile : configs) {
            baseConfigurationFile.initializeConfiguration(Paths.get(plugin.getDataFolder().getAbsolutePath()));
        }

        // Check versions
        for (BaseConfigurationFile baseConfigurationFile : configs) {
            if (baseConfigurationFile.exists()) {
                baseConfigurationFile.checkVersion(isAutoUpgradeEnabled());
            }
        }

    }

    /**
     * Is the automatic upgrade of configs enabled?
     *
     * @return true if enabled
     */
    protected abstract boolean isAutoUpgradeEnabled();
}