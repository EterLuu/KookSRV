package ink.ziip.kooksrv.command;

import ink.ziip.kooksrv.KookSRV;
import ink.ziip.kooksrv.config.Config;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public final class KookSRVCommand implements CommandExecutor, TabCompleter {

    private final KookSRV plugin;

    public KookSRVCommand(KookSRV plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(usage());
            return true;
        }
        switch (args[0].toLowerCase(Locale.ROOT)) {
            case "reload" -> {
                try {
                    plugin.reloadBridge();
                    sender.sendMessage(Config.MESSAGE_RELOAD_SUCCESS);
                } catch (RuntimeException exception) {
                    sender.sendMessage(Config.MESSAGE_RELOAD_FAILED.replace("%error%", String.valueOf(exception.getMessage())));
                }
            }
            case "status" -> sender.sendMessage(Config.MESSAGE_STATUS
                    .replace("%channel%", Config.CHANNEL)
                    .replace("%server_name%", Config.SERVER_NAME));
            default -> sender.sendMessage(usage());
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            String prefix = args[0].toLowerCase(Locale.ROOT);
            return List.of("reload", "status").stream().filter(value -> value.startsWith(prefix)).toList();
        }
        return List.of();
    }

    private String usage() {
        return Config.MESSAGE_USAGE == null ? "" : Config.MESSAGE_USAGE;
    }
}
