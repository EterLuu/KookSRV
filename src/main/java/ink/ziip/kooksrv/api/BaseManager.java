package ink.ziip.kooksrv.api;

import ink.ziip.kooksrv.KookSRV;

public abstract class BaseManager {
    protected final KookSRV plugin;

    public BaseManager(KookSRV kookSRV) {
        this.plugin = kookSRV;
    }

    public abstract void load();

    public abstract void unload();
}
