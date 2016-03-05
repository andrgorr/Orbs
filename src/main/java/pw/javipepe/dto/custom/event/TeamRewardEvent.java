package pw.javipepe.dto.custom.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import pw.javipepe.dto.team.Team;

/**
 * Created by javipepe on 3/09/15.
 */
public class TeamRewardEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    Team t;
    int from;
    int to;
    int awarded;

    public TeamRewardEvent(Team t, int from, int to, int awarded){
        this.t = t;
        this.from = from;
        this.to = to;
        this.awarded = awarded;
    }

    public Team getTeam(){return t;}

    public int getFrom(){return from;}

    public int getTo(){return to;}

    public int getAwarded(){return awarded;}


    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
