package ink.ziip.kooksrv.listener;

import ink.ziip.kooksrv.KookSRV;
import ink.ziip.kooksrv.api.BaseListener;
import ink.ziip.kooksrv.config.Config;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import snw.jkook.entity.channel.Channel;
import snw.jkook.entity.channel.TextChannel;
import snw.kookbc.impl.KBCClient;

public class PlayerListener extends BaseListener {

    public PlayerListener(KookSRV plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        plugin.getBukkitScheduler().runTaskAsynchronously(plugin, () -> {
            Player player = event.getPlayer();

            KBCClient kbcClient = plugin.getKbcClient();
            Channel channel = kbcClient.getCore().getHttpAPI().getChannel(Config.CHANNEL);
            if (channel instanceof TextChannel textChannel) {
                String message = Config.MESSAGE_MINECRAFT_2_KOOK;

                message = message.replace("%player%", player.getName())
                        .replace("%message%", event.getMessage());

                message = PlaceholderAPI.setPlaceholders(player, message);
                textChannel.sendComponent(ChatColor.stripColor(message));
            }
        });
    }
}
