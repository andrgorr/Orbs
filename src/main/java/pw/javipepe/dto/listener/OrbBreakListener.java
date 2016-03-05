package pw.javipepe.dto.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import pw.javipepe.dto.Orb;
import pw.javipepe.dto.OrbAssist;
import pw.javipepe.dto.custom.event.TeamRewardEvent;
import pw.javipepe.dto.scoreboard.Scoreboard;
import pw.javipepe.dto.team.Team;
import pw.javipepe.dto.team.TeamAssist;

/**
 * Created by javipepe on 31/08/15.
 */
public class OrbBreakListener implements Listener{

    @EventHandler
    public void onBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        Team t = TeamAssist.getTeamOfAPlayer(p);
        Location l = e.getBlock().getLocation();
        //Bukkit.broadcastMessage(ChatColor.RED + p.getName() + " broke a " + e.getBlock().getType().toString().toLowerCase() + " block @ " + l.getX() + ", " + l.getY() + ", " + l.getZ());

        if(OrbAssist.isLocationOrb(l)){
            //Bukkit.broadcastMessage(ChatColor.RED + "It's an orb!");
            Orb o = OrbAssist.getOrbAtLocation(l);
            //Bukkit.broadcastMessage(ChatColor.RED + "It's " + ChatColor.RED + "orb of team " + o.getOwnerTeam().getColor() + o.getOwnerTeam().getName() + ChatColor.RED + " at " + o.getLocation().getX() + ", " + o.getLocation().getY() + ", " + o.getLocation().getZ() + " of material " + o.getMaterial().toString().toLowerCase());

            if(o.getOwnerTeam() == t && !o.getOwnerTeam().getName().equalsIgnoreCase("noowner")){
                e.setCancelled(true);
                p.sendMessage(ChatColor.RED + " You may not destroy your own orb.");
                return;
            }

            e.getBlock().getDrops().clear();
            int oldpoints = t.getPoints();
            Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "----------------------------------------------------");
            Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "-- " + TeamAssist.getTeamOfAPlayer(p).getColor() + p.getDisplayName() +  ChatColor.WHITE + " broke an orb of team " + t.getColor() + t.getName() + ChatColor.DARK_PURPLE + " --");
            Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "-- " + ChatColor.WHITE + "Regenerating in " + ChatColor.YELLOW + o.getBuildup() + ChatColor.WHITE + " seconds!" + ChatColor.DARK_PURPLE + " --");
            Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "-- " + ChatColor.GOLD + "+" + o.getPointsToAdd() + " points for " + t.getColor() + t.getName() + ChatColor.DARK_PURPLE + " --");
            Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "----------------------------------------------------");
            t.addPoints(o.getPointsToAdd());
            o.broken = true;
            for(Player online : Bukkit.getOnlinePlayers()){
                online.playSound(online.getLocation(), Sound.AMBIENCE_THUNDER, 1.9F, 1.0F);
                Scoreboard.refreshScoreboard(online);
            }
            o.setBroken(true);
            Bukkit.getPluginManager().callEvent(new TeamRewardEvent(t, oldpoints, oldpoints + o.getPointsToAdd(), o.getPointsToAdd()));


        }
    }
}
