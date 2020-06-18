package scripts.teleTabMaker.data;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSObject;

import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.ObjectEntity;


public class Constants {

    // Inventory Objects
    public static int softClayNoted = 1762;
    public static int softClayUnnoted = 1761;
    public static int lawRune = 563;
    public static int teleportToHouse = 8013;

    // Interactiable Objects
    public static int insidePortalID = 4525;
    public static RSObject[] insidePortal = Objects.find(10, insidePortalID);

    public static boolean lecternIsVisible() {
        RSObject lecternVisible = Entities.find(ObjectEntity::new).nameEquals("Lectern").actionsEquals("Study")
                .sortByDistance()
                .getFirstResult();
        return lecternVisible != null;
    }

    public static boolean insideHousePortalIsVisible() {
        RSObject portalVisible = Entities.find(ObjectEntity::new).nameEquals("Portal").actionsEquals("Enter")
                .sortByDistance()
                .getFirstResult();
        return portalVisible != null;
    }
    // Teleport to Grand Exchange
    public static boolean interactWithRing() {
        if (GameTab.open(GameTab.TABS.EQUIPMENT)) { //open the gametab so we can see the ring if we have it..
            RSInterfaceChild ringSlot = Interfaces.get(387, 23); //grab the ring slot interface child..
            if (ringSlot != null && ringSlot.click("Grand Exchange")) { //check to see if it is there and we can click using the option, Grand Exchange.
                Timing.waitCondition(() -> {
                    while (Player.getAnimation() != -1) {
                        General.sleep(General.randomSD(1200, 70));
                    }
                    return Location.grandExchangeArea.contains(Player.getPosition()); //return if we have successfully returned to Grand Exchange.
                }, General.random(2000, 3000));
            }
        }
        return false;
    }

}
