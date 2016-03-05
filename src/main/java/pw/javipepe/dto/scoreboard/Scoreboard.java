package pw.javipepe.dto.scoreboard;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import pw.javipepe.dto.DestroyTheOrb;
import pw.javipepe.dto.Orb;
import pw.javipepe.dto.team.Team;
import pw.javipepe.dto.team.TeamAssist;
import sun.security.krb5.internal.crypto.Des;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by javipepe on 3/09/15.
 */
public class Scoreboard {

    public static void refreshScoreboard(Player p) {
        String indent = " ";

        ArrayList<String> orbsmessages = new ArrayList<String>();
        HashMap<Integer, Team> places = new HashMap<Integer, Team>();
        ArrayList<Orb> orbsofone = new ArrayList<Orb>();
        ArrayList<Orb> orbsoftwo = new ArrayList<Orb>();


        Score score;
        final org.bukkit.scoreboard.Scoreboard boardStats;
        final Objective oStats;
        boardStats = Bukkit.getServer().getScoreboardManager().getNewScoreboard();
        oStats = boardStats.registerNewObjective("score", "dummy");
        oStats.setDisplaySlot(DisplaySlot.SIDEBAR);






        int highest = 10;


        int teamcount = TeamAssist.getTeams().size()-/*obs*/1;


        if(teamcount == 2){
            Team teamone = TeamAssist.getTeams().get(1);
            Team teamtwo = TeamAssist.getTeams().get(2);
            for(Orb o: DestroyTheOrb.orbs){
                if(o.getOwnerTeam() == teamone){
                    orbsofone.add(o);
                }else if(o.getOwnerTeam() == teamtwo){
                    orbsoftwo.add(o);
                }
            }
            places.put(highest, teamone);
            places.put(highest - (orbsofone.size() + 2), teamtwo);

            oStats.setDisplayName(ChatColor.AQUA + "Orbs");

            score = oStats.getScore(Bukkit.getOfflinePlayer(ChatColor.RED + "-----MAX-----"));
            score.setScore(DestroyTheOrb.pointstowin);

            for(Team t : TeamAssist.getTeams()){
                if(!t.getName().equalsIgnoreCase("Observers")) {
                    score = oStats.getScore(Bukkit.getOfflinePlayer(t.getColor() + t.getName()));
                    score.setScore(t.getPoints());
                }
            }

            score = oStats.getScore("  ");
            score.setScore(-1);

            int i = -2;
            for(Orb o: DestroyTheOrb.orbs){
                String structure = indent + o.getOwnerTeam().getColor() + (o.isBroken() ? "➔ ":"◎ ") + ChatColor.WHITE + "Orb";
                if(orbsmessages.contains(structure)){
                    structure = structure + ChatColor.RESET;
                }
                score = oStats.getScore(Bukkit.getOfflinePlayer(structure));

                orbsmessages.add(structure);
                score.setScore(i);
                i--;
            }




            p.setScoreboard(boardStats);
        }

    }
}
