package scripts.teleTabMaker.data;

import scripts.teleTabMaker.tasks.ExecuteMakingTabs;
import scripts.teleTabMaker.tasks.ExecuteUnnotingClay;

public class PriceCalculations {

    public static int teleportToHouseID = 8013;
    public static int lawRuneID = 563;
    public static int softClayID = 1761;
    public static int coinsID = 995;

    public static int costOfTeleportToHouse = 0;
    public static int tabsUsed = ExecuteMakingTabs.tabsMade;
    public static int totalCostOfTabs = costOfTeleportToHouse * tabsUsed;

    public static int costOfLawRune = 0;
    public static int lawRunesUsed = ExecuteMakingTabs.lawRunesUsed;
    public static int totalCostOfLawRunes = costOfLawRune * lawRunesUsed;

    public static int costOfSoftClay = 0;
    public static int softClayUsed = ExecuteMakingTabs.softClayUsed;
    public static int totalCostOfSoftClay = costOfSoftClay * softClayUsed;

    public static int costOfCoins = 1;
    public static int coinsUsed = ExecuteUnnotingClay.coinsUsed;
    public static int totalCostOfCoins = costOfCoins * coinsUsed;

    public static int totalCosts = totalCostOfTabs + totalCostOfLawRunes + totalCostOfSoftClay;
}
