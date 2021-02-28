package de.obey.traxfight.objects;

/*

        (TraxFight-PotPvP)
  This Class was created by Obey
        20.02.2021 | 07:51

*/

public enum Liga {

    BRONZE4(0, "§eBronze IV", "§eIV", 0),
    BRONZE3(50, "§eBronze III", "§eIII", 1),
    BRONZE2(100, "§eBronze II", "§eII", 2),
    BRONZE1(150, "§eBronze I", "§eI", 3),
    SILBER4(300, "§fSilber IV", "§fIV", 4),
    SILBER3(400, "§fSilber III", "§fIII", 5),
    SILBER2(500, "§fSilber II", "§fII", 6),
    SILBER1(600, "§fSilber I", "§fI", 7),
    DIAMANT3(1000, "§bDiamant III", "§bIII", 8),
    DIAMANT2(1400, "§bDiamant II", "§bII", 9),
    DIAMANT1(1800, "§bDiamant I", "§bI", 10),
    PLATIN3(2500, "§3Platin III", "§3III", 11),
    PLATIN2(3000, "§3Platin II", "§3II", 12),
    PLATIN1(3500, "§3Platin I", "§3I", 13),
    MASTER(6000, "§cMaster", "§c✮", 14),
    GRANDMASTER(20000, "§4Grandmaster", "§4✮", 15);

    private int elo;
    private int id;
    private String prefix;
    private String smallPrefix;

    Liga(int elo, String prefix, String smallPrefix, int id){
        this.elo = elo;
        this.prefix = prefix;
        this.smallPrefix = smallPrefix;
        this.id = id;
    }

    public int getElo() {
        return elo;
    }

    public int getId() {
        return id;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSmallPrefix() {
        return smallPrefix;
    }
}
