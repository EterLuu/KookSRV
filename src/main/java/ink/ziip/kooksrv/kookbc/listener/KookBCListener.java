package ink.ziip.kooksrv.kookbc.listener;

import ink.ziip.kooksrv.Utils;
import ink.ziip.kooksrv.config.Config;
import snw.jkook.entity.channel.TextChannel;
import snw.jkook.event.EventHandler;
import snw.jkook.event.Listener;
import snw.jkook.event.channel.ChannelMessageEvent;
import snw.jkook.message.Message;
import snw.jkook.message.component.TextComponent;

public class KookBCListener implements Listener {
    @EventHandler
    public void onKookMessage(ChannelMessageEvent event) {
        Message message = event.getMessage();
        TextChannel channel = event.getChannel();
        if (message.getSender().isBot())
            return;

        if (!channel.getId().equals(Config.CHANNEL))
            return;

        if (message.getComponent() instanceof TextComponent) {
            String stringMessage = message.getComponent().toString();
            if (Config.ONLINE_PLAYERS_KEYWORDS.contains(stringMessage)) {
                message.reply(Utils.getOnlinePlayerList());
                return;
            }

            String bukkitMessage = Config.MESSAGE_KOOK_2_MINECRAFT
                    .replace("%player%", message.getSender().getNickName(channel.getGuild()))
                    .replace("%message%", stringMessage);
            Utils.sendMessageToAllPlayers(bukkitMessage);
        }
    }

}
