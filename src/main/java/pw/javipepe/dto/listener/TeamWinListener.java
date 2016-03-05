package pw.javipepe.dto.listener;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import pw.javipepe.dto.DestroyTheOrb;
import pw.javipepe.dto.Orb;
import pw.javipepe.dto.custom.event.TeamRewardEvent;
import pw.javipepe.dto.team.Team;
import pw.javipepe.dto.team.TeamAssist;

/**
 * Created by javipepe on 3/09/15.
 */
public class TeamWinListener implements Listener {

    @EventHandler
    public void onReward(TeamRewardEvent e){
        if(e.getTo() >= DestroyTheOrb.pointstowin){
            DestroyTheOrb.setWinner(e.getTeam());
            for(Player online : Bukkit.getOnlinePlayers()){
                online.playSound(online.getLocation(), Sound.WITHER_DEATH, 1.0F, 1.0F);
            }
            for(Orb o : DestroyTheOrb.orbs){
                o.getLocation().getBlock().setType(o.getMaterial());
            }

            Team obs = null;
            for(Team teams : TeamAssist.getTeams()){
                if(teams.getName().equalsIgnoreCase("Observers")) obs = teams;
            }
            if(obs != null) {
                for(Player observer : obs.getPlayers()){
                    for(Player p: Bukkit.getOnlinePlayers()){
                        p.showPlayer(observer);
                    }

                }
            }
        }
    }
}
