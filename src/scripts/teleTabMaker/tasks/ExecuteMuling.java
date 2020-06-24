package scripts.teleTabMaker.tasks;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Player;
import org.tribot.api2007.types.RSInterfaceComponent;
import org.tribot.api2007.types.RSItem;
import org.tribot.api2007.types.RSObject;
import scripts.entityselector.Entities;
import scripts.entityselector.finders.prefabs.ObjectEntity;
import scripts.teleTabMaker.data.Constants;
import scripts.teleTabMaker.data.Interface;
import scripts.teleTabMaker.data.Location;
import scripts.teleTabMaker.data.UserSettings;
import scripts.teleTabMaker.framework.Priority;
import scripts.teleTabMaker.framework.Task;

import static scripts.api.discordMessageHelper.sendDiscordMessage;


public class ExecuteMuling implements Task {

    public int totalCoins = 0;

    @Override
    public Priority priority() {
        return Priority.HIGH;
    }

    @Override
    public String toString() {
        return "Muling";
    }

    @Override
    public boolean validate() {
        return UserSettings.activateMuling();
    }

    @Override
    public void execute() {
        switch (getTaskState()) {

            case RETURN_TO_HOUSE:
                if (Location.generalOutsidePortalArea.contains(Player.getPosition())) {

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
                                                Keyboard.typeString(UserSettings.hostName);
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
                                            Keyboard.typeString(UserSettings.hostName);
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
                        else {
                            RSItem[] teleportToHouse = Inventory.find(Constants.teleportToHouse);
                            if (!(teleportToHouse[0] == null)) {

                            }
                        }
                        break;
                }

            case TRADE_MULE:

                sendDiscordMessage("Successfully completed muling with +" + Player.getRSPlayer().getName() +
                        "The total amount of gold received from the trade was: " + totalCoins);

        }

    }

    private enum TaskState {

        RETURN_TO_HOUSE, WAITING_FOR_MULE, TRADE_MULE

    }

    private TaskState getTaskState() {
        return TaskState.WAITING_FOR_MULE;
    }
}
