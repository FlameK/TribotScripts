package scripts.teleTabMaker.tasks;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Objects;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSNPC;
import org.tribot.api2007.types.RSObject;

import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.NpcEntity;
import scripts.entityselector.finders.prefabs.ObjectEntity;

import scripts.teleTabMaker.antiban.Antiban;
import scripts.teleTabMaker.data.Constants;
import scripts.teleTabMaker.data.Interface;
import scripts.teleTabMaker.data.Location;
import scripts.teleTabMaker.framework.Priority;
import scripts.teleTabMaker.framework.Task;

public class ExecuteMakingTabs implements Task {

    public RSObject lectern = Entities.find(ObjectEntity::new).nameEquals("Lectern").actionsEquals("Study")
            .sortByDistance()
            .getFirstResult();

    public RSNPC phials = Entities.find(NpcEntity::new).nameEquals("Phials")
            .sortByDistance()
            .getFirstResult();

    public static int tabsMade;
    public static int lawRunesUsed;
    public static int softClayUsed;

    @Override
    public Priority priority() {
        return Priority.MEDIUM;
    }

    @Override
    public String toString() {
        return "Making Tablets " + "Status: " + getTaskState().name();
    }

    @Override
    public boolean validate() {
        return Inventory.find(Constants.softClayUnnoted).length > 0 && Inventory.find(Constants.lawRune).length > 0 &&
                !Location.generalOutsidePortalArea.contains(Player.getPosition()) &&
                (Constants.lecternIsVisible());
    }

    @Override
    public void execute() {
        switch (getTaskState()) {

            case OPEN_INTERFACE:

                if (!Interface.isTabInterfaceValid()) {
                    RSObject[] lectern = Objects.findNearest(30, "Lectern");
                    if (lectern[0] != null) {
                        if (Clicking.click("Study", lectern[0])) {
                            General.println("Attempting to interact with Lectern.");
                            Timing.waitCondition(() -> {
                                General.sleep(General.randomSD(750, 75));
                                return Interface.isTabInterfaceValid();
                            }, General.random(6000, 7000));
                        }
                    }
                }
                break;

            case SELECT_TABLET:
                if (Interface.isTabInterfaceValid()) {
                    if (Interface.isHouseInterfaceValid()) {
                        if (Interface.houseTabInterface().click()) {
                            General.println("Attempting to make tablets.");
                            Timing.waitCondition(() -> {
                                General.sleep(General.randomSD(750, 75));
                                return !Interface.isHouseInterfaceValid();
                            }, General.random(2000, 3000));
                            tabsMade += 24;
                            lawRunesUsed += 24;
                            softClayUsed += 24;
                        }
                    }
                }
                break;

            case MAKING_TABLET:
                if (Player.getAnimation() != -1 || !Interface.isTabInterfaceValid()) {
                    General.println("Player is making tabs, sleeping until this is finished.");
                    Timing.waitCondition(() -> {
                        General.sleep(General.randomSD(1200, 50));
                        Antiban.timedActions();
                        return Player.getAnimation() == -1 || Inventory.find(Constants.softClayUnnoted).length == 0;
                    }, General.random(2000, 3000));
                }
                break;
        }
    }
        private enum TaskState {

        OPEN_INTERFACE, SELECT_TABLET, MAKING_TABLET


        }

        private TaskState getTaskState() {

        if (Constants.lecternIsVisible() && !Interface.isTabInterfaceValid() && Player.getAnimation() == -1) {
            return TaskState.OPEN_INTERFACE;
        }
        if (Interface.isHouseInterfaceValid()) {
            return TaskState.SELECT_TABLET;
        }
            return TaskState.MAKING_TABLET;
        }

}
