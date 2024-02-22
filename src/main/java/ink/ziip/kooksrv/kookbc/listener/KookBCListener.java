package ink.ziip.kooksrv.kookbc.listener;

import ink.ziip.kooksrv.Utils;
import ink.ziip.kooksrv.config.Config;
import snw.jkook.entity.channel.Channel;
import snw.jkook.event.EventHandler;
import snw.jkook.event.Listener;
import snw.jkook.event.channel.ChannelMessageEvent;
import snw.jkook.message.Message;
import snw.jkook.message.component.TextComponent;

public class KookBCListener implements Listener {
    @EventHandler
    public void onKookMessage(ChannelMessageEvent event) {
        Message message = event.getMessage();
        Channel channel = event.getChannel();
        if (message.getSender().isBot())
            return;

        if (!channel.getId().equals(Config.CHANNEL))
            return;

        if (message.getComponent() instanceof TextComponent textComponent) {
            String bukkitMessage = Config.MESSAGE_KOOK_2_MINECRAFT
                    .replace("%player%", message.getSender().getNickName(channel.getGuild()))
                    .replace("%message%", message.getComponent().toString());
            Utils.sendMessageToAllPlayers(bukkitMessage);
        }
    }

}
