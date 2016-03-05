package pw.javipepe.dto.command;

import com.sk89q.minecraft.util.commands.Command;
import com.sk89q.minecraft.util.commands.CommandContext;
import com.sk89q.minecraft.util.commands.CommandException;
import com.sk89q.minecraft.util.commands.CommandPermissions;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import pw.javipepe.dto.DestroyTheOrb;
import pw.javipepe.dto.Orb;
import pw.javipepe.dto.team.Team;
import pw.javipepe.dto.team.TeamAssist;


/**
 * Created by javipepe on 3/09/15.
 */
public class GameCommands {

    @Command(aliases = {"orbs"}, desc = "test", usage = "/command", min = 0, max = 0)
    public static void orbs(final CommandContext args, CommandSender sender) throws Exception {
        for (Orb o : DestroyTheOrb.orbs) {
            sender.sendMessage(ChatColor.RED + "Orb of team " + o.getOwnerTeam().getColor() + o.getOwnerTeam().getName() + ChatColor.RED + " at " + o.getLocation().getX() + ", " + o.getLocation().getY() + ", " + o.getLocation().getZ() + " of material " + o.getMaterial().toString().toLowerCase());
        }
    }

    @Command(aliases = {"join"}, desc = "Join a team", usage = "<team>", min = 1)
    public static void join(final CommandContext args, CommandSender sender) throws Exception {
        if (DestroyTheOrb.hasEnded() || DestroyTheOrb.hasStarted())
            throw new CommandException("Can't join at this point of the match.");

        String teamreceived = args.getJoinedStrings(0);

        Team target = null;
        for (Team t : TeamAssist.getTeams()) {
            if (t.getName().contains(teamreceived))
                target = t;
        }

        if (target == null) throw new CommandException("No team called like that found.");

        if (TeamAssist.getTeamOfAPlayer((Player) sender) == target)
            throw new CommandException("You are already on that team.");

        target.addPlayer((Player) sender);
        sender.sendMessage(ChatColor.GRAY + "Joined " + target.getColor() + target.getName());
    }

    @Command(aliases = {"start"}, desc = "Start the match", usage = "/command", min = 0, max = 0)
    @CommandPermissions("dto.start")
    public static void start(final CommandContext cmd, CommandSender sender) throws CommandException {


        if (!DestroyTheOrb.hasEnded() && !DestroyTheOrb.hasStarted()) {
            DestroyTheOrb.hasstarted = true;
            for (Team t : TeamAssist.getTeams()) {
                if (!t.getName().equalsIgnoreCase("Observers")) {
                    for (Player p : t.getPlayers()) {
                        //teleports player
                        p.teleport(t.getSpawn());
                        //gives stuff
                        p.getInventory().clear();
                        p.setGameMode(GameMode.SURVIVAL);
                        p.setAllowFlight(false);
                        p.setFlying(false);
                        p.setHealth(20);
                        p.setFoodLevel(20);
                        for (ItemStack item : t.getLoadout()) {
                            p.getInventory().addItem(item);
                        }

                        Team obs = null;
                        for (Team teams : TeamAssist.getTeams()) {
                            if (teams.getName().equalsIgnoreCase("Observers")) obs = teams;
                        }
                        if (obs != null) {
                            for (Player observer : obs.getPlayers()) {
                                p.hidePlayer(observer);

                            }
                        }


                        //message
                        Bukkit.broadcastMessage(" ");
                        Bukkit.broadcastMessage(ChatColor.YELLOW + "Match has started. " + ChatColor.WHITE + "Good luck!");
                        Bukkit.broadcastMessage(" ");
                    }
                }
            }
        } else throw new CommandException("Can't start at this point");
    }

    @Command(aliases = {"end"}, desc = "End the match", usage = "/command", max = 0)
    @CommandPermissions("dto.end")
    public static void end(final CommandContext cmd, CommandSender sender) throws CommandException {
        if (!DestroyTheOrb.hasEnded() && DestroyTheOrb.hasStarted()) {

            Team t = new Team(ChatColor.BLACK, "no-result", null, null);
            DestroyTheOrb.setWinner(t);
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.setAllowFlight(true);
                Team obs = null;
                for (Team teams : TeamAssist.getTeams()) {
                    if (t.getName().equalsIgnoreCase("Observers")) obs = t;
                }
                if (obs != null) {
                    p.getInventory().clear();
                    for (ItemStack i : obs.getLoadout()) {
                        p.getInventory().addItem(i);
                    }
                }
            }
        } else throw new CommandException("Can't end at this point");
    }


}



