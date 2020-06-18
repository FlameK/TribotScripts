package scripts.teleTabMaker.data;

import org.tribot.api.General;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterface;

public class GrandExchangeHelper {

    public static boolean isLawRuneOfferPresent() {
        for (int i = 7; i < 15; i++) {
            RSInterface item = Interfaces.get(465, i, 18);
            if (item != null && !item.isHidden()) {
                if (item.getComponentItem() == PriceCalculations.lawRuneID) {
                    General.println("We already have an offer for the following item:" + " Law rune");
                }
            }
        }
        return true;
    }

    public static boolean isSoftClayOfferPresent() {
        for (int i = 7; i < 15; i++) {
            RSInterface item = Interfaces.get(465, i, 18);
            if (item != null && !item.isHidden()) {
                if (item.getComponentItem() == PriceCalculations.softClayID) {
                    General.println("We already have an offer for the following item:" + " Soft clay");
                }
            }
        }
        return true;
    }

    public static boolean isTeleportToHouseOfferPresent() {
        for (int i = 7; i < 15; i++) {
            RSInterface item = Interfaces.get(465, i, 18);
            if (item != null && !item.isHidden()) {
                if (item.getComponentItem() == PriceCalculations.teleportToHouseID) {
                    General.println("We already have an offer for the following item:" + " Teleport to house.");
                }
            }
        }
        return true;
    }
}