package ink.ziip.kooksrv.listener;

import ink.ziip.kooksrv.KookSRV;
import ink.ziip.kooksrv.Utils;
import ink.ziip.kooksrv.api.BaseListener;
import ink.ziip.kooksrv.config.Config;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import snw.jkook.entity.abilities.Accessory;
import snw.jkook.entity.channel.Channel;
import snw.jkook.entity.channel.TextChannel;
import snw.jkook.message.component.card.CardBuilder;
import snw.jkook.message.component.card.MultipleCardComponent;
import snw.jkook.message.component.card.Size;
import snw.jkook.message.component.card.Theme;
import snw.jkook.message.component.card.element.ImageElement;
import snw.jkook.message.component.card.element.MarkdownElement;
import snw.jkook.message.component.card.module.SectionModule;
import snw.kookbc.impl.KBCClient;

public class PlayerListener extends BaseListener {

    public PlayerListener(KookSRV plugin) {
        super(plugin);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        if (!Boolean.TRUE.equals(Config.SETTINGS_ENABLED) || !Config.CHAT_MINECRAFT_TO_KOOK) return;
        plugin.getBukkitScheduler().runTaskAsynchronously(plugin, () -> {
            Player player = event.getPlayer();

            KBCClient kbcClient = plugin.getKbcClient();
            if (kbcClient == null) return;
            Channel channel = kbcClient.getCore().getHttpAPI().getChannel(Config.CHANNEL);
            if (channel instanceof TextChannel textChannel) {
                String message = Config.MESSAGE_MINECRAFT_2_KOOK
                        .replace("%server_name%", Config.SERVER_NAME)
                        .replace("%player%", player.getName())
                        .replace("%message%", event.getMessage());

                message = PlaceholderAPI.setPlaceholders(player, message);
                textChannel.sendComponent(ChatColor.stripColor(message));
            }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!Boolean.TRUE.equals(Config.SETTINGS_ENABLED) || !Config.PLAYER_JOIN_ENABLED) return;
        plugin.getBukkitScheduler().runTaskAsynchronously(plugin, () -> {
            Player player = event.getPlayer();

            KBCClient kbcClient = plugin.getKbcClient();
            if (kbcClient == null) return;
            Channel channel = kbcClient.getCore().getHttpAPI().getChannel(Config.CHANNEL);
            if (channel instanceof TextChannel textChannel) {
                String message = Config.MESSAGE_PLAYER_JOIN
                        .replace("%server_name%", Config.SERVER_NAME)
                        .replace("%player%", player.getName());
                message = PlaceholderAPI.setPlaceholders((OfflinePlayer) player, message);
                textChannel.sendComponent(createPlayerInfoMessageCard(player, message, theme(Config.JOIN_CARD_THEME, Theme.INFO)));
            }
        });
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!Boolean.TRUE.equals(Config.SETTINGS_ENABLED) || !Config.PLAYER_QUIT_ENABLED) return;
        plugin.getBukkitScheduler().runTaskAsynchronously(plugin, () -> {
            Player player = event.getPlayer();

            KBCClient kbcClient = plugin.getKbcClient();
            if (kbcClient == null) return;
            Channel channel = kbcClient.getCore().getHttpAPI().getChannel(Config.CHANNEL);
            if (channel instanceof TextChannel textChannel) {
                String message = Config.MESSAGE_PLAYER_QUIT
                        .replace("%server_name%", Config.SERVER_NAME)
                        .replace("%player%", player.getName());
                message = PlaceholderAPI.setPlaceholders((OfflinePlayer) player, message);
                textChannel.sendComponent(createPlayerInfoMessageCard(player, message, theme(Config.QUIT_CARD_THEME, Theme.DANGER)));
            }
        });
    }

    public MultipleCardComponent createPlayerInfoMessageCard(Player player, String message, Theme theme) {
        CardBuilder cardBuilder = new CardBuilder();
        cardBuilder.setTheme(theme);
        cardBuilder.setSize(size(Config.CARD_SIZE));

        MarkdownElement markdownElement = new MarkdownElement(Utils.escapeMarkdown(message));
        ImageElement imageElement = new ImageElement(Utils.getPlayerHeadImageUrl(player), "", Size.SM, false);

        SectionModule sectionModule = new SectionModule(markdownElement, imageElement, Accessory.Mode.LEFT);

        cardBuilder.addModule(sectionModule);

        return cardBuilder.build();
    }

    private Theme theme(String configured, Theme fallback) {
        try {
            return Theme.valueOf(configured.toUpperCase(java.util.Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            return fallback;
        }
    }

    private Size size(String configured) {
        try {
            return Size.valueOf(configured.toUpperCase(java.util.Locale.ROOT));
        } catch (IllegalArgumentException exception) {
            return Size.SM;
        }
    }
}
