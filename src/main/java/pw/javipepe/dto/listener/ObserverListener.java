package pw.javipepe.dto.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import pw.javipepe.dto.DestroyTheOrb;
import pw.javipepe.dto.team.Team;
import pw.javipepe.dto.team.TeamAssist;

/**
 * Created by javipepe on 3/09/15.
 */
public class ObserverListener implements Listener {

    @EventHandler
    public void onPlace(BlockPlaceEvent e){
        //hasstarted
        if(!DestroyTheOrb.hasStarted()){e.setCancelled(true);}
        //hasended
        if(DestroyTheOrb.hasEnded()){e.setCancelled(true);}

        Player p = e.getPlayer();
        Team teamofp = TeamAssist.getTeamOfAPlayer(p);

        //spectator

        if(teamofp.getName().equalsIgnoreCase("Observers")){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        //hasstarted
        if(!DestroyTheOrb.hasStarted()){e.setCancelled(true);}
        //hasended
        if(DestroyTheOrb.hasEnded()){e.setCancelled(true);}

        Player p = e.getPlayer();
        Team teamofp = TeamAssist.getTeamOfAPlayer(p);

        //spectator

        if(teamofp.getName().equalsIgnoreCase("Observers")){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        //hasstarted
        if(!DestroyTheOrb.hasStarted()){e.setCancelled(true);}
        //hasended
        if(DestroyTheOrb.hasEnded()){e.setCancelled(true);}

        Player p = e.getPlayer();
        Team teamofp = TeamAssist.getTeamOfAPlayer(p);

        //spectator

        if(teamofp.getName().equalsIgnoreCase("Observers")){
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onPvp(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player){
            Player p = (Player)e.getDamager();

            if(TeamAssist.getTeamOfAPlayer(p).getName().equalsIgnoreCase("Observers") || !DestroyTheOrb.hasStarted() || DestroyTheOrb.hasEnded()){
                e.setCancelled(true);
            }
        }
    }
}
