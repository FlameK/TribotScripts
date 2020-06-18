package scripts.teleTabMaker.tasks;

import org.tribot.api.DynamicClicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Game;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.*;

import scripts.dax_api.api_lib.DaxWalker;
import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.NpcEntity;

import scripts.entityselector.finders.prefabs.ObjectEntity;
import scripts.teleTabMaker.antiban.Antiban;
import scripts.teleTabMaker.data.Constants;
import scripts.teleTabMaker.data.Interface;
import scripts.teleTabMaker.data.Location;
import scripts.teleTabMaker.framework.Priority;
import scripts.teleTabMaker.framework.Task;

public class ExecuteUnnotingClay implements Task {



    public static int coinsUsed;

    @Override
    public Priority priority() {
        return Priority.MEDIUM;
    }

    @Override
    public String toString() {
        return "Unnoting Clay " + "Status: " + getTaskState().name();
    }

    @Override
    public boolean validate() {
        return Inventory.find(Constants.softClayUnnoted).length == 0 && Inventory.find(Constants.lawRune).length > 0 &&
                Inventory.find(Constants.softClayNoted).length > 0;
    }

    @Override
    public void execute() {
        switch (getTaskState()) {

            case LEAVE_HOUSE:

                RSObject insideHousePortal = Entities.find(ObjectEntity::new).idEquals(4525)
                        .sortByDistance()
                        .getFirstResult();

                if (insideHousePortal != null) {
                    if (!insideHousePortal.isClickable() && insideHousePortal.adjustCameraTo()) {
                        General.println("Adjusting camera so portal is visible.");
                        Timing.waitCondition(() -> {
                            General.sleep(General.randomSD(600, 50));
                            return insideHousePortal.isClickable() && insideHousePortal.isOnScreen();
                        }, General.random(2000, 3000));
                    }
                }
                if (insideHousePortal != null) {
                    if (insideHousePortal.isClickable()) {
                        if (DynamicClicking.clickRSObject(insideHousePortal, "Enter")) {
                            Timing.waitCondition(() -> {
                                General.sleep(General.randomSD(1300, 60));
                                return Location.outsidePortal.contains((Player.getPosition()));
                            }, General.random(2000, 3000));
                        }
                    }
                }
                break;

            case UNNOTE_CLAY:

                RSItem[] softClay = Inventory.find(Constants.softClayNoted);
                RSNPC phials = Entities.find(NpcEntity::new).nameEquals("Phials")
                        .sortByDistance()
                        .getFirstResult();

                if (phials != null) {
                    if (phials.isOnScreen()) {
                        if (softClay != null) {
                            if (softClay[0].click("Use")) {
                                General.println("Using soft clay on phials.");
                                Antiban.waitItemInteractionDelay();
                                if (Game.getUptext().contains("Use Soft clay")) {
                                    if (phials.click("Use Soft clay -> " + phials.getDefinition().getName())) {
                                        Timing.waitCondition(() -> {
                                            General.sleep(General.randomSD(3700, 50));
                                            return Interface.isPhialsInterfaceValid();
                                        }, General.random(5000, 6000));
                                    }
                                }
                            }
                        }
                    }
                }
                if (Interface.isPhialsInterfaceValid()) {
                    RSInterfaceComponent exchangeAllSoftClay = Interface.unnoteAllSoftClay();
                    if (exchangeAllSoftClay.click()) {
                        coinsUsed += 120;
                        General.println("Unnoting all soft clay.");
                        Timing.waitCondition(() -> {
                            General.sleep(600, 50);
                            return Inventory.find(Constants.softClayUnnoted).length > 0;
                        }, General.random(2000, 3000));
                    }
                }
                break;


        }
    }

    private enum TaskState {

        LEAVE_HOUSE, UNNOTE_CLAY
    }

    private TaskState getTaskState() {

        if (Constants.insideHousePortalIsVisible() && Inventory.find(Constants.softClayUnnoted).length == 0 &&
                !Location.generalOutsidePortalArea.contains(Player.getPosition())) {
            return TaskState.LEAVE_HOUSE;
        }
        if (Inventory.find(Constants.softClayUnnoted).length == 0 && Location.generalOutsidePortalArea.contains(Player.getPosition())) {
            return TaskState.UNNOTE_CLAY;
        }
        return TaskState.LEAVE_HOUSE;
    }
}


