package pw.javipepe.dto.team;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by javipepe on 31/08/15.
 */
public class TeamAssist {

    private static List<Team> teams = new ArrayList<Team>();

    public static void createTeam(ChatColor color, String name, Location spawn, ArrayList<ItemStack> loadout){
        Team t = new Team(color, name, spawn, loadout);
        teams.add(t);
    }

    public static void removeTeam(Team t){
        if(teams.contains(t))teams.remove(t);
    }

    public static Team getTeamOfAPlayer(Player p){
        for(Team t : teams){
            if(t.getPlayers().contains(p)){
                return t;
            }
        }
        return null;
    }

    public static List<Team> getTeams(){return teams;}




}
