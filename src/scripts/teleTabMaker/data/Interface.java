package scripts.teleTabMaker.data;

import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSInterfaceComponent;

public class Interface {

    public static RSInterfaceComponent houseTabInterface() {
        return (RSInterfaceComponent) Interfaces.get(79, 15, 0);
    }
    public static RSInterface makeTabInterface() {
        return Interfaces.get(79);
    }
    public static RSInterface phialsInterface() {
        return Interfaces.get(219);
    }
    public static RSInterfaceComponent unnoteAllSoftClay() {
        return (RSInterfaceComponent) Interfaces.get(219, 1, 3);
    }
    public static RSInterfaceChild friendsHouse() {
        return Interfaces.get(162, 44);
    }
    public static RSInterfaceComponent lastUsedFriendsHouse() {
        return (RSInterfaceComponent) Interfaces.get(162,40,0);
    }
    public static boolean isTabInterfaceValid() {
        RSInterface makeTabInterface = makeTabInterface(); // not correct just for use for now
        return makeTabInterface != null;
    }
    public static boolean isHouseInterfaceValid() {
        RSInterfaceComponent houseTabInterface = houseTabInterface();
        return houseTabInterface != null;
    }
    public static boolean isPhialsInterfaceValid() {
        RSInterface phialsInterface = phialsInterface();
        return phialsInterface != null;
    }
    public static boolean isFriendHouseInterfaceValid() {
        RSInterfaceChild friendsHouse = friendsHouse();
        return friendsHouse != null;
    }
    public static boolean isLastUsedFriendsHouseValid() {
        RSInterfaceComponent lastUsedFriendsHouse = lastUsedFriendsHouse();
        return lastUsedFriendsHouse != null;
    }
    public static boolean grandExchangeIsValid() {
        RSInterface grandExchangeWindow = Interfaces.get(465);
        return grandExchangeWindow != null;
    }

}
