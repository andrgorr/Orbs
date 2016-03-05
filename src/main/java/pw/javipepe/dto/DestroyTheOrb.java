package pw.javipepe.dto;

import com.sk89q.bukkit.util.CommandsManagerRegistration;
import com.sk89q.minecraft.util.commands.*;
import org.bukkit.*;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pw.javipepe.dto.command.GameCommands;
import pw.javipepe.dto.listener.*;
import pw.javipepe.dto.team.Team;
import pw.javipepe.dto.team.TeamAssist;

import java.io.File;
import java.util.*;

/**
 * Created by javipepe on 31/08/15.
 */
public class DestroyTheOrb extends JavaPlugin {

    public static List<Orb> orbs = new ArrayList<Orb>();
    public static int pointstowin;
    private static boolean hasended = false;
    public static boolean hasstarted = false;
    private CommandsManager<CommandSender> commands;
    static File ranks = new File(Bukkit.getPluginManager().getPlugin("DTO").getDataFolder(), "ranks.yml");
    public static FileConfiguration ranksyml = YamlConfiguration.loadConfiguration(ranks);

    @Override
    public void onEnable(){
        createObservers();
        registerListeners();
        setupCommands();

        //
        ArrayList<ItemStack> kit = new ArrayList<ItemStack>();
        ItemStack shovel = new ItemStack(Material.DIAMOND_SPADE);
        kit.add(shovel);
        Location red = new Location(Bukkit.getWorld("dto"), -589.4, 10.0, -98.5);
        Location blue = new Location(Bukkit.getWorld("dto"), -589.4, 10.0, -94.5);
        TeamAssist.createTeam(ChatColor.DARK_RED, "Red Team", red, kit);
        TeamAssist.createTeam(ChatColor.BLUE, "Blue Team", blue, kit);

        Location orbred1 = new Location(Bukkit.getWorld("dto"), -590, 10, -101);
        Location orbred2 = new Location(Bukkit.getWorld("dto"), -590, 10, -102);
        Location orbblue1 = new Location(Bukkit.getWorld("dto"), -590, 10, -92);
        Location orbblue2 = new Location(Bukkit.getWorld("dto"), -590, 10, -93);
        Team redt = null;
        Team bluet = null;
        for(Team t: TeamAssist.getTeams()){
            if(t.getName().equalsIgnoreCase("Red Team"))redt = t;
            if(t.getName().equalsIgnoreCase("Blue Team"))bluet = t;
        }

        OrbAssist.register(orbred1, 10, Material.DIRT, redt, 2);
        OrbAssist.register(orbred2, 10, Material.DIRT, redt, 2);
        OrbAssist.register(orbblue1, 10, Material.DIRT, bluet, 2);
        OrbAssist.register(orbblue2, 10, Material.DIRT, bluet, 2);

        //points to win
        pointstowin = 10;


        //messages for scores

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                if (!hasended) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(ChatColor.YELLOW + " Scores: " + ChatColor.DARK_GRAY + "- ");
                    for (Team t : TeamAssist.getTeams()) {
                        if (!t.getName().equalsIgnoreCase("Observers")) {
                            sb.append(t.getColor() + t.getName() + " " + ChatColor.DARK_AQUA + "(" + t.getColor() + t.getPoints() + ChatColor.DARK_AQUA + ") " + ChatColor.DARK_GRAY + " - " + ChatColor.RESET);
                            //red + Red Team (4) B
                        }
                    }

                    Bukkit.broadcastMessage(sb.toString().trim());
                }
            }
        }, 0L, 3 * 60 * 20 /*3mins*/);
    }


    private void setupCommands() {
        this.commands = new CommandsManager<CommandSender>() {
            @Override
            public boolean hasPermission(CommandSender sender, String perm) {
                return sender instanceof ConsoleCommandSender || sender.hasPermission(perm);
            }
        };
        CommandsManagerRegistration cmdRegister = new CommandsManagerRegistration(this, this.commands);

        cmdRegister.register(GameCommands.class);

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        try {
            this.commands.execute(cmd.getName(), args, sender, sender);
        } catch (CommandPermissionsException e) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to perform that command.");
        } catch (MissingNestedCommandException e) {
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (CommandUsageException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
            sender.sendMessage(ChatColor.RED + e.getUsage());
        } catch (WrappedCommandException e) {
            if (e.getCause() instanceof NumberFormatException) {
                sender.sendMessage(ChatColor.RED + "Number expected, text received instead.");
            } else {
                sender.sendMessage(ChatColor.RED + "There was an internal error while performing this command.");
                e.printStackTrace();
            }
        } catch (CommandException e) {
            sender.sendMessage(ChatColor.RED + e.getMessage());
        }

        return true;
    }

    private void registerListeners(){
        PluginManager pm = Bukkit.getServer().getPluginManager();

        pm.registerEvents(new OrbBreakListener(), this);
        pm.registerEvents(new JoinListener(), this);
        pm.registerEvents(new ObserverListener(), this);
        pm.registerEvents(new TeamWinListener(), this);
        pm.registerEvents(new ChatListener(), this);
        pm.registerEvents(new RespawnListener(), this);
    }

    private void createObservers(){
        ArrayList<ItemStack> obskit = new ArrayList<ItemStack>();
        ItemStack compass = new ItemStack(Material.COMPASS);
        compass.getItemMeta().setDisplayName(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Teleporter");
        obskit.add(compass);
        Location l = new Location(Bukkit.getWorld("dto"), -577.650, 6, -96.355, 90,90);

        TeamAssist.createTeam(ChatColor.AQUA, "Observers", l, obskit);
    }

    public static void setWinner(Team t){
        if(!hasended){

            hasended = true;
            if(t.getName().equalsIgnoreCase("no-result")){
                Bukkit.broadcastMessage(" ");
                Bukkit.broadcastMessage(ChatColor.RED + "Match ended without a winner");
                Bukkit.broadcastMessage(" ");
                return;
            }
            Bukkit.broadcastMessage(" ");
            Bukkit.broadcastMessage(t.getColor() + t.getName() + ChatColor.WHITE + " won this game!");
            Bukkit.broadcastMessage(" ");
            for(Team team : TeamAssist.getTeams()) {
                if(team.getName().equals("Observers"))
                    continue;
                for(Player p : team.getPlayers()) {
                    p.setAllowFlight(true);
                    p.setGameMode(GameMode.CREATIVE);
                }
            }
        }
    }

    public static boolean hasEnded(){return hasended;}

    public static boolean hasStarted(){return hasstarted;}

    public static void refreshName(Player p){

        p.setDisplayName(getRanks(p) + TeamAssist.getTeamOfAPlayer(p).getColor() + p.getName());
        p.setPlayerListName(p.getDisplayName().replace(getRanks(p), ""));
    }

    public static String getRanks(Player p){
        /*if(p.getName().equalsIgnoreCase("KyloCode")){
            return ChatColor.YELLOW + "" + ChatColor.BOLD + "ADMIN " + ChatColor.DARK_GRAY + "| " + ChatColor.RESET;
        }*/
        if(p.getName().equalsIgnoreCase("javipepe")){
            return ChatColor.BLUE + "" + ChatColor.BOLD + "DEV " + ChatColor.DARK_GRAY + "| " + ChatColor.RESET;
        }

        for(String rank : ranksyml.getStringList("ranks")) {
            for(String players : ranksyml.getStringList("ranks." + rank)) {
                UUID u = UUID.fromString(players);
                String prefix = ranksyml.getString("ranks." + rank + ".prefix");
                if(p.getUniqueId() == u) {
                    return ChatColor.translateAlternateColorCodes('&', prefix) + " " + ChatColor.DARK_GRAY + "| " + ChatColor.RESET;
                }
            }
        }
        return "";
    }
}
