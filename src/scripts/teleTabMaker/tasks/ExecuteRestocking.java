package scripts.teleTabMaker.tasks;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Login;
import org.tribot.api2007.Player;

import org.tribot.api2007.types.RSObject;
import scripts.entityselector.Entities;

import scripts.entityselector.finders.prefabs.ObjectEntity;
import scripts.teleTabMaker.Main;
import scripts.teleTabMaker.data.*;
import scripts.teleTabMaker.framework.Priority;
import scripts.teleTabMaker.framework.Task;

import scripts.wastedbro.api.rsitem_services.GrandExchange;

public class ExecuteRestocking implements Task {

    public static String teleportToHouse = "Teleport to house";
    public static String lawRune = "Law rune";
    public static String softClay = "Soft clay";
    public static int buyQuanity = 4000;
    public static int amountOfTabsToSell = Inventory.getCount(teleportToHouse) - 2;

    @Override
    public Priority priority() {
        return Priority.HIGH;
    }

    @Override
    public String toString() {
        return "Restocking " + "Status: " + getTaskState();
    }

    @Override
    public boolean validate() {
        return (Inventory.find(Constants.lawRune).length == 0 || (Inventory.find(Constants.softClayNoted).length == 0 &&
                Inventory.find(Constants.softClayUnnoted).length == 0))
                || Location.grandExchangeArea.contains(Player.getPosition());
    }

    @Override
    public void execute() {
        switch (getTaskState()) {

            case TELEPORT_TO_GE:

                if (Constants.interactWithRing()) {
                    General.println("Successfully teleported to Grand Exchange.");
                }
                break;

            case OPEN_GRAND_EXCHANGE:

                RSObject grandExchangeBooth = Entities.find(ObjectEntity::new).nameEquals("Grand Exchange booth").actionsEquals("Exchange")
                        .sortByDistance()
                        .getFirstResult();

                if (grandExchangeBooth != null) {
                    if (!grandExchangeBooth.isClickable() && grandExchangeBooth.adjustCameraTo()) {
                        Timing.waitCondition(() -> {
                            General.sleep(General.randomSD(750, 50));
                            return grandExchangeBooth.isClickable();
                        }, General.random(2000, 3000));
                    }
                    if (DynamicClicking.clickRSObject(grandExchangeBooth, "Exchange")) {
                        General.println("Interacting with Grand Exchange Clerk");
                        General.sleep(General.randomSD(650, 50));
                        Timing.waitCondition(() -> {
                            while (Player.isMoving() || !Interface.grandExchangeIsValid()) {
                                General.sleep(General.randomSD(800, 60));
                            }
                            return Interface.grandExchangeIsValid();
                        }, General.random(3000, 4000));
                    }
                }
                if (Interface.grandExchangeIsValid()) {
                    General.println("Grand Exchange opened successfully.");
                }
                break;

            case SELL_ITEMS:

                PriceCalculations.costOfTeleportToHouse = GrandExchange.getPrice(PriceCalculations.teleportToHouseID);
                General.println("The most recent price of a Teleport to house tab is: " + PriceCalculations.costOfTeleportToHouse);

                if (GrandExchange.offer(true, teleportToHouse, PriceCalculations.costOfTeleportToHouse, amountOfTabsToSell, true)) {
                    General.println("Creating sell offer for teleport tabs.");
                    Timing.waitCondition(() -> {
                        General.sleep(General.randomSD(750, 50));
                        return Interface.grandExchangeIsValid();
                    }, General.random(8000, 12000));
                }
                break;

            case BUY_LAW_RUNE:
                PriceCalculations.costOfLawRune = GrandExchange.getPrice(PriceCalculations.lawRuneID);
                General.println("The most recent price of a Law rune is: " + PriceCalculations.costOfLawRune);

                if (!GrandExchange.getOffers().equals(lawRune) && Inventory.getCount(lawRune) < buyQuanity) {
                    General.println("Creating buy offer for " + buyQuanity + " " + lawRune);
                    if (GrandExchange.offer(true, lawRune, PriceCalculations.costOfLawRune, buyQuanity, false)) {
                        Timing.waitCondition(() -> {
                            General.sleep(General.randomSD(1200, 50));
                            return GrandExchange.getOffers().equals(lawRune);
                        }, General.random(8000, 12000));
                    }
                }
                break;

            case BUY_SOFT_CLAY:
                PriceCalculations.costOfSoftClay = GrandExchange.getPrice(PriceCalculations.softClayID);
                General.println("The most recent price of Soft clay is: " + PriceCalculations.costOfSoftClay);

              if (!GrandExchange.getOffers().equals(softClay) && Inventory.getCount(softClay) < buyQuanity) {
                    General.println("Creating buy offer for " + buyQuanity + " " + softClay);
                    if (GrandExchange.offer(true, softClay, PriceCalculations.costOfSoftClay, buyQuanity, false)) {
                        Timing.waitCondition(() -> {
                            General.sleep(General.randomSD(1200, 50));
                            return GrandExchange.getOffers().equals(softClay);
                        }, General.random(8000, 12000));
                    }
              }
                break;

            case IDLE_OFFLINE:
                    General.println("Waiting for offers to buy. Logging out.");
                    GrandExchange.close();
                    Login.logout();
                    General.sleep(General.random(120000, 180000));
                    Login.login();
                }
        }
    private enum TaskState {
        TELEPORT_TO_GE, OPEN_GRAND_EXCHANGE, SELL_ITEMS, BUY_LAW_RUNE, BUY_SOFT_CLAY, IDLE_OFFLINE, RETURN_TO_HOUSE
    }

    private TaskState getTaskState() {
        if (!Location.grandExchangeArea.contains(Player.getPosition())) {
            return TaskState.TELEPORT_TO_GE;
        }
        if (Location.grandExchangeArea.contains(Player.getPosition()) && !Interface.grandExchangeIsValid()) {
            return TaskState.OPEN_GRAND_EXCHANGE;
        }
        if (Interface.grandExchangeIsValid() && Inventory.getCount(teleportToHouse) > 2) {
            return TaskState.SELL_ITEMS;
        }
        if (Interface.grandExchangeIsValid() && (Inventory.getCount(lawRune) < buyQuanity &&
                !GrandExchangeHelper.isLawRuneOfferPresent())) {
            return TaskState.BUY_LAW_RUNE;
        }
        if (Interface.grandExchangeIsValid() && Inventory.getCount(softClay) < buyQuanity &&
                !GrandExchangeHelper.isSoftClayOfferPresent()) {
            return TaskState.BUY_SOFT_CLAY;
        }
        if ((Inventory.getCount(softClay) != buyQuanity && GrandExchange.getOffers().equals(softClay)) ||
                (Inventory.getCount(lawRune) != buyQuanity && GrandExchange.getOffers().equals(lawRune))) {
            return TaskState.IDLE_OFFLINE;
        }
        return TaskState.RETURN_TO_HOUSE;
    }
}
