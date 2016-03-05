package pw.javipepe.dto;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import pw.javipepe.dto.scoreboard.Scoreboard;
import pw.javipepe.dto.team.Team;

/**
 * Created by javipepe on 31/08/15.
 */
public class Orb {

    Location l;
    long buildup;
    Material m;
    Team t;
    public boolean broken;
    int pointstoadd;

    public Orb(Location l, long buildup, Material m, Team t, int pointstoadd){
        this.l = l;
        this.buildup = buildup;
        this.m = m;
        this.t = t;
        this.pointstoadd = pointstoadd;
    }

    public Location getLocation(){
        return l;
    }

    public long getBuildup(){
        return buildup;
    }

    public Material getMaterial(){
        return m;
    }

    public Team getOwnerTeam(){
        return t;
    }

    public boolean isBroken() { return broken;}

    public int getPointsToAdd() { return pointstoadd;}

    public void setBroken(boolean b){
        //if(!isBroken() == b){
            if(b && !DestroyTheOrb.hasEnded()){
                //broken = true;
                Bukkit.getScheduler().runTaskLater(Bukkit.getPluginManager().getPlugin("DTO"), new Runnable() {
                    public void run() {
                        if(!DestroyTheOrb.hasEnded()) {
                            setBroken(false);
                            for (Player online : Bukkit.getOnlinePlayers()) {
                                Scoreboard.refreshScoreboard(online);
                            }
                            getLocation().getBlock().setType(getMaterial());
                            Bukkit.broadcastMessage("Orb at " + ChatColor.GREEN + l.getBlockX() + ", " + l.getBlockY() + ", " + l.getBlockZ() + ChatColor.WHITE + " of team " + t.getColor() + t.getName() + ChatColor.WHITE + " has respawned!");

                        }

                    }
                }, getBuildup() *20);
            }else broken = false;
        //}
    }

    public String toString(){
        return isBroken() ? ChatColor.AQUA + "➔ " + ChatColor.GRAY + "Orb" : ChatColor.GREEN + "✓ " + ChatColor.WHITE + "Orb";
    }
}
