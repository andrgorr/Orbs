package pw.javipepe.dto.listener;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import pw.javipepe.dto.team.Team;
import pw.javipepe.dto.team.TeamAssist;

/**
 * Created by javipepe on 4/09/15.
 */
public class RespawnListener implements Listener {

    @EventHandler
    public void onRespawn(PlayerRespawnEvent e){
        Player p = e.getPlayer();
        Team t = TeamAssist.getTeamOfAPlayer(p);

        if(t == null)return;

        //teleports player
        p.teleport(t.getSpawn());
        //gives stuff
        p.getInventory().clear();
        p.setGameMode(GameMode.SURVIVAL);
        p.setAllowFlight(false);
        p.setFlying(false);
        p.setHealth(20);
        p.setFoodLevel(20);
        for(ItemStack item : t.getLoadout()){
            p.getInventory().addItem(item);
        }

        Team obs = null;
        for(Team teams : TeamAssist.getTeams()){
            if(teams.getName().equalsIgnoreCase("Observers")) obs = teams;
        }
        if(obs != null) {
            for(Player observer : obs.getPlayers()){
                p.hidePlayer(observer);

            }
        }
    }
}
