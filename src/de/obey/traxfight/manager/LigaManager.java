package de.obey.traxfight.manager;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        20.02.2021 | 07:51

*/

import de.obey.traxfight.objects.Liga;
import de.obey.traxfight.usermanager.User;

import java.util.ArrayList;

public class LigaManager {

    private final ArrayList<Liga> ligen = new ArrayList<>();

    public LigaManager(){

        ligen.add(Liga.BRONZE4);
        ligen.add(Liga.BRONZE3);
        ligen.add(Liga.BRONZE2);
        ligen.add(Liga.BRONZE1);
        ligen.add(Liga.SILBER4);
        ligen.add(Liga.SILBER3);
        ligen.add(Liga.SILBER2);
        ligen.add(Liga.SILBER1);
        ligen.add(Liga.DIAMANT3);
        ligen.add(Liga.DIAMANT2);
        ligen.add(Liga.DIAMANT1);
        ligen.add(Liga.PLATIN3);
        ligen.add(Liga.PLATIN2);
        ligen.add(Liga.PLATIN1);
        ligen.add(Liga.MASTER);
        ligen.add(Liga.GRANDMASTER);

    }

    public Liga getLigaFromPoints(int amount){
        if(amount >= Liga.GRANDMASTER.getElo())
            return Liga.GRANDMASTER;

        if(amount >= Liga.MASTER.getElo())
            return Liga.MASTER;

        if(amount >= Liga.PLATIN1.getElo())
            return Liga.PLATIN1;

        if(amount >= Liga.PLATIN2.getElo())
            return Liga.PLATIN2;

        if(amount >= Liga.PLATIN3.getElo())
            return Liga.PLATIN3;

        if(amount >= Liga.DIAMANT1.getElo())
            return Liga.DIAMANT1;

        if(amount >= Liga.DIAMANT2.getElo())
            return Liga.DIAMANT2;

        if(amount >= Liga.DIAMANT3.getElo())
            return Liga.DIAMANT3;

        if(amount >= Liga.SILBER1.getElo())
            return Liga.SILBER1;

        if(amount >= Liga.SILBER2.getElo())
            return Liga.SILBER2;

        if(amount >= Liga.SILBER3.getElo())
            return Liga.SILBER3;

        if(amount >= Liga.SILBER4.getElo())
            return Liga.SILBER1;

        if(amount >= Liga.SILBER4.getElo())
            return Liga.SILBER4;

        if(amount >= Liga.BRONZE1.getElo())
            return Liga.BRONZE1;

        if(amount >= Liga.BRONZE2.getElo())
            return Liga.BRONZE2;

        if(amount >= Liga.BRONZE3.getElo())
            return Liga.BRONZE3;

        if(amount >= Liga.BRONZE4.getElo())
            return Liga.BRONZE4;

        return null;
    }

    public boolean checkForUprank(User user, int amount){
        Liga newRank = getLigaFromPoints(user.getInteger("ligapoints") + amount);
        Liga oldRank = getLigaFromPoints(user.getInteger("ligapoints"));

        if(newRank.getId() > oldRank.getId())
            return true;

        return false;
    }

    public boolean checkForDerank(User user, int amount){
        Liga newRank = getLigaFromPoints(user.getInteger("ligapoints") - amount);
        Liga oldRank = getLigaFromPoints(user.getInteger("ligapoints"));

        if(newRank.getId() < oldRank.getId())
            return true;

        return false;
    }

    public ArrayList<Liga> getLigen() {
        return ligen;
    }
}
