package pw.javipepe.dto.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import pw.javipepe.dto.DestroyTheOrb;
import pw.javipepe.dto.scoreboard.Scoreboard;
import pw.javipepe.dto.team.Team;
import pw.javipepe.dto.team.TeamAssist;


/**
 * Created by javipepe on 3/09/15.
 */
public class JoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent e){

        Team obs = null;
        for(Team t: TeamAssist.getTeams()){
            if(t.getName().equalsIgnoreCase("Observers"))obs = t;
        }
        if(obs != null){
            obs.addPlayer(e.getPlayer());
            e.getPlayer().getInventory().clear();
            for(ItemStack i : obs.getLoadout()){
                e.getPlayer().getInventory().addItem(i);
            }
            e.getPlayer().teleport(obs.getSpawn());
        }

        DestroyTheOrb.refreshName(e.getPlayer());

        e.getPlayer().setGameMode(GameMode.CREATIVE);

        for(Player p : Bukkit.getOnlinePlayers()){
            Scoreboard.refreshScoreboard(p);
        }

        e.setJoinMessage(e.getPlayer().getDisplayName() + ChatColor.YELLOW + " has joined the game");

    }
}
