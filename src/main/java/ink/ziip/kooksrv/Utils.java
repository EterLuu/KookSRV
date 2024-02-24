package ink.ziip.kooksrv;

import ink.ziip.kooksrv.config.Config;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    private Utils() {
    }

    public static String translateColorCodes(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder();
            for (char c : ch) {
                builder.append("&").append(c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendMessageToAllPlayers(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(message);
        }

        KookSRV.getInstance().getLogger().log(Level.INFO, ChatColor.stripColor(message));
    }

    public static String getOnlinePlayerList() {

        List<String> onlinePlayers = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            onlinePlayers.add(player.getName());
        }

        if (onlinePlayers.isEmpty())
            return Config.MESSAGE_NO_ONLINE_PLAYER;

        String message = Config.MESSAGE_ONLINE_PLAYER_LIST
                .replace("%online_players%", String.valueOf(onlinePlayers.size()))
                .replace("%list%", String.join("，", onlinePlayers));

        for (Player player : Bukkit.getOnlinePlayers()) {
            message = PlaceholderAPI.setPlaceholders(player, message);
            break;
        }

        return message;
    }

    public static String getPlayerHeadImageUrl(Player player) {
        return "https://cravatar.eu/helmavatar/%player_name%/256.png".replace("%player_name%", player.getName());
    }
}
