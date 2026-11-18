package ink.ziip.kooksrv.config;

import ink.ziip.kooksrv.KookSRV;

public class ConfigurationManager extends BaseConfigurationManager {
    public ConfigurationManager(KookSRV plugin) {
        super(plugin);
        getConfigs().add(new Config(plugin));
    }

    @Override
    public void load() {
        super.load();
    }

    @Override
    protected boolean isAutoUpgradeEnabled() {
        return true;
    }
}
