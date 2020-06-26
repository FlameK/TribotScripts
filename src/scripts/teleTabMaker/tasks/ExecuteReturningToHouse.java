package scripts.teleTabMaker.tasks;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSInterfaceComponent;
import org.tribot.api2007.types.RSObject;
import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.ObjectEntity;
import scripts.teleTabMaker.data.Constants;
import scripts.teleTabMaker.data.Interface;
import scripts.teleTabMaker.data.Location;
import scripts.teleTabMaker.data.UserSettings;
import scripts.teleTabMaker.framework.Priority;
import scripts.teleTabMaker.framework.Task;

public class ExecuteReturningToHouse implements Task {


    @Override
    public Priority priority() {
        return Priority.MEDIUM;
    }

    @Override
    public String toString() {
        return "Returning to house " + getTaskState().name();
    }

    @Override
    public boolean validate() {
        return !Constants.lecternIsVisible() && Inventory.find(Constants.softClayUnnoted).length > 0 &&
                Inventory.find(Constants.lawRune).length > 0 && Location.generalOutsidePortalArea.contains(Player.getPosition());
    }

    @Override
    public void execute() {
        switch (getTaskState()) {
             case RETURN_TO_HOUSE:

                RSObject housePortal = Entities.find(ObjectEntity::new).idEquals(15478)
                        .sortByDistance()
                        .getFirstResult();
                RSObject lectern = Entities.find(ObjectEntity::new).nameEquals("Lectern").actionsEquals("Study")
                        .sortByDistance()
                        .getFirstResult();

                if (housePortal != null) {
                    if (Clicking.click("Friend's house", housePortal)) {
                        General.sleep(General.randomSD(750, 20));
                        General.println("Returning to hosts house.");
                        Timing.waitCondition(() -> {
                            while (Player.isMoving() || !Interface.isFriendHouseInterfaceValid()) {
                                General.sleep(General.randomSD(1300, 50));
                            }
                            return Interface.isFriendHouseInterfaceValid();
                        }, General.random(2000, 3000));
                    }
                    if (Interface.friendsHouse() != null) {
                        if (Interface.friendsHouse() != null) {
                            if (Interfaces.isInterfaceValid(162)) {
                                RSInterfaceComponent lastVisitedFriendsHouse = (RSInterfaceComponent) Interfaces.get(162, 40, 0);
                                if (lastVisitedFriendsHouse != null) {
                                    if (Interface.lastUsedFriendsHouse().getText().contains("PlatinumR0se")) {
                                        General.println("Host name detected, pressing enter.");
                                        General.sleep(General.randomSD(600, 50));
                                        Keyboard.pressEnter();
                                        Timing.waitCondition(() -> {
                                            General.sleep(General.randomSD(1500, 75));
                                            return lectern != null;
                                        }, General.random(2000, 3000));
                                    } else if (!Interface.lastUsedFriendsHouse().getText().contains("PlatinumR0se")) {
                                        General.println("Host name does not match required name, entering new host name.");
                                        General.println("Typing in hosts name.");
                                        Keyboard.typeString(UserSettings.HOST_NAME);
                                        Keyboard.pressEnter();
                                        Timing.waitCondition(() -> {
                                            General.sleep(General.randomSD(1500, 75));
                                            return lectern != null;
                                        }, General.random(2000, 3000));
                                    }
                                }
                                else if (lastVisitedFriendsHouse == null) {
                                    if (Player.isMoving()) {
                                        Timing.waitCondition(() -> {
                                            General.sleep(General.randomSD(1200, 60));
                                            return !Player.isMoving();
                                        }, General.random(2000, 3000));
                                    }
                                    General.println("Typing in hosts name.");
                                    Keyboard.typeString(UserSettings.HOST_NAME);
                                    Keyboard.pressEnter();
                                    Timing.waitCondition(() -> {
                                        General.sleep(General.randomSD(1500, 60));
                                        return lectern != null;
                                        }, General.random(2000, 3000));
                                    }
                                }
                            }
                        }
                    }
                break;
        }
    }

    private enum TaskState {
        RETURN_TO_HOUSE
    }

    private static TaskState getTaskState() {
        return TaskState.RETURN_TO_HOUSE;
    }
}
