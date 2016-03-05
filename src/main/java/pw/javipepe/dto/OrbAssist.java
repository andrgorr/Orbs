package pw.javipepe.dto;

import org.bukkit.Location;
import org.bukkit.Material;
import pw.javipepe.dto.team.Team;

/**
 * Created by javipepe on 31/08/15.
 */
public class OrbAssist {

    public static void register(Location l, long buildup, Material m, Team t, int pointstoadd){
        Orb o = new Orb(l, buildup, m, t, pointstoadd);
        DestroyTheOrb.orbs.add(o);
    }

    public static void unregister(Orb o){
        if(DestroyTheOrb.orbs.contains(o))DestroyTheOrb.orbs.remove(o);
    }


    public static boolean isLocationOrb(Location l){
        for(Orb o : DestroyTheOrb.orbs){
            if(o.getLocation().getX() == l.getX() && o.getLocation().getY() == l.getY() && o.getLocation().getZ() == l.getZ())return true;
        }
        return false;
    }

    public static boolean isOrbFromTeam(Orb o, Team t){
        for(Orb orb :DestroyTheOrb.orbs){
            if(orb == o){
                if(o.getOwnerTeam() == t)return true;
            }
        }
        return false;
    }

    public static Orb getOrbAtLocation(Location l){
        for (Orb o: DestroyTheOrb.orbs){
            if(o.getLocation().getX() == l.getX() && o.getLocation().getY() == l.getY() && o.getLocation().getZ() == l.getZ()){
                return o;
            }
        }
        return null;
    }


}
