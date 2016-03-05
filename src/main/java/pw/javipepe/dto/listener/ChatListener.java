package pw.javipepe.dto.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

/**
 * Created by javipepe on 4/09/15.
 */
public class ChatListener implements Listener {

    @EventHandler
    public void onChat(PlayerChatEvent e){
        e.setCancelled(true);

        Bukkit.broadcastMessage(ChatColor.WHITE + "<" + e.getPlayer().getDisplayName() + ChatColor.WHITE + ">: " + e.getMessage());
    }
}
