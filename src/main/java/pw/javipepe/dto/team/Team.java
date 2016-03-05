package pw.javipepe.dto.team;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pw.javipepe.dto.DestroyTheOrb;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by javipepe on 31/08/15.
 */
public class Team {

    ChatColor color;
    String name;
    Location spawn;
    List<ItemStack> loadout;
    ArrayList<Player> players;
    int points = 0;

    public Team(ChatColor color, String name, Location spawn, ArrayList<ItemStack> loadout){
        this.color = color;
        this.name = name;
        this.spawn = spawn;
        this.loadout = loadout;
        players = new ArrayList<Player>();
    }

    public ChatColor getColor(){
        return color;
    }

    public String getName(){
        return name;
    }

    public Location getSpawn(){
        return spawn;
    }

    public ArrayList<ItemStack> getLoadout(){
        return (ArrayList<ItemStack>)loadout;
    }

    public List<Player> getPlayers(){
        return players;
    }

    public void addPlayer(Player p){
        if(!getPlayers().contains(p)){
            if(TeamAssist.getTeamOfAPlayer(p) != null) {
                TeamAssist.getTeamOfAPlayer(p).removePlayer(p);
            }
            getPlayers().add(p);
            DestroyTheOrb.refreshName(p);
        }
    }

    public void removePlayer(Player p){
        if(getPlayers().contains(p))getPlayers().remove(p);
    }

    public void addPoints(int points){
        this.points = this.points + points;
    }

    public int getPoints() {return points;}
}
