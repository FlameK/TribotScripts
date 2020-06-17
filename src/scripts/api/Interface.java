package scripts.api;

import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSInterfaceComponent;

public class Interface {

    //Control Interface Options
    // Checks to see if the Control Interface is open and enables a clickable interface
    public static RSInterfaceComponent controlsInterfaceOpen() {
        return (RSInterfaceComponent) Interfaces.get(261, 1, 6);
    }
    // Checks to see if the Control Interface is valid
    public static boolean isControlInterfaceValid() {
        RSInterfaceComponent controlInterface = controlsInterfaceOpen();
        return controlInterface != null;
    }
    // Shift Drop Interface Options
    // Checks to see if the Shift Drop interface is open and creates a clickable interface
    public static RSInterfaceChild shiftDropInterfaceIsOpen() {
        return Interfaces.get(261, 79);
    }
    // Checks to see if the Shift Drop Interface is valid
    public static boolean isShiftDropInterfaceValid() {
        RSInterfaceChild shiftDropInterface = shiftDropInterfaceIsOpen();
        return shiftDropInterface != null;
    }
    // Key Binding Interface Options
    // Checks to see if Key Binding Interface is open and creates a clickable interface
    public static RSInterface keyBindingInterfaceIsOpen() {
        return Interfaces.get(121);
    }
    // Checks to see if the Key Binding Interface is valid
    public static boolean isKeyBindingInterfaceOpen() {
        RSInterface keyBindingInterfaceIsOpen = keyBindingInterfaceIsOpen();
        return keyBindingInterfaceIsOpen != null;
    }
    // Checks to see if Key Binding Options is clickable and creates a clickable interface
    public static RSInterfaceChild keyBindingInterfaceButton() {
        return Interfaces.get(261, 77);
    }
    // Checks to see if Key Binding Options is valid
    public static boolean isKeyBindingInterfaceButtonValid() {
        RSInterfaceChild keyBindingInterface = keyBindingInterfaceButton();
        return keyBindingInterface != null;
    }
    // Checks to see if the Escape to close option is visible and creates a clickable option
    public static RSInterfaceChild keyBindingInterfaceEscapeToClose() {
        return Interfaces.get(121, 103);
    }

    //Control Interface Options
    // Checks to see if the Control Interface is open and enables a clickable interface
    public static RSInterfaceComponent audioInterfaceIsOpen() {
        return (RSInterfaceComponent) Interfaces.get(261, 1, 2);
    }
    // Checks to see if the Control Interface is valid
    public static boolean isAudioInterfacevalid() {
        RSInterfaceComponent audioInterface = audioInterfaceIsOpen();
        return audioInterface != null;
    }
    //Music Interface Options
    // Checks to see if the Game Volume Interface is open and enables a clickable interface
    public static RSInterfaceChild adjustMusicVolume() {
        return Interfaces.get(261, 38);
    }
    // Checks to see if the Game Volume Interface is valid
    public static boolean adjustMusicVolumeIsValid() {
        RSInterfaceChild adjustMusicVolume = adjustMusicVolume();
        return adjustMusicVolume != null;
    }
    // Checks to see if the Sound Effects Volume Interface is open and enables a clickable interface
    public static RSInterfaceChild adjustSoundEffectVolume() {
        return Interfaces.get(261, 44);
    }
    // Checks to see if the Sound Effects Volume Interface is valid
    public static boolean adjustSoundEffectVolumeIsValid() {
        RSInterfaceChild adjustSoundEffectVolume = adjustSoundEffectVolume();
        return adjustSoundEffectVolume != null;
    }
    // Checks to see if the Sound Effects Volume Interface is open and enables a clickable interface
    public static RSInterfaceChild adjustAreaSoundVolume() {
        return Interfaces.get(261, 50);
    }
    // Checks to see if the Sound Effects Volume Interface is valid
    public static boolean adjustAreaSoundVolumeIsValid() {
        RSInterfaceChild adjustSoundEffectVolume = adjustSoundEffectVolume();
        return adjustSoundEffectVolume != null;
    }

    //Teleport to house interface
    public static RSInterfaceChild teleportToHouseInterface() {
        return Interfaces.get(218, 28);
    }
    public static boolean isTeleportToHouseValid() {
        RSInterfaceChild teleportToHouseInterface = teleportToHouseInterface();
        return teleportToHouseInterface != null;
    }
    //Teleport to camelot
    public static RSInterfaceChild teleportToCamelotInterface() {
        return Interfaces.get(218, 31);
    }
    public static boolean isTeleportToCamelotValid() {
        RSInterfaceChild teleportToCamelot = teleportToCamelotInterface();
        return teleportToCamelot != null;
    }
    //Options menu interface
    public static RSInterfaceChild houseSettingsOptionsInterface() {
        return Interfaces.get(261, 100);
    }
    public static boolean isHouseSettingsOptionsValid() {
        RSInterfaceChild houseSettingsOption = houseSettingsOptionsInterface();
        return houseSettingsOption != null;
    }
    // Call Servant Interface
    public static RSInterfaceComponent callServantInterface() {
        return (RSInterfaceComponent) Interfaces.get(370,19,3);
    }
    public static boolean isCallServantValid() {
        RSInterfaceComponent callServant = callServantInterface();
        return callServant != null;
    }
    //Demon butler repeat last task
    public static RSInterfaceComponent repeatLastTask() {
        return (RSInterfaceComponent) Interfaces.get(219,1,1);
    }
    public static boolean isRepeatLastTaskInterfaceValid() {
        RSInterfaceComponent repeatLastTaskInterface = repeatLastTask();
        return repeatLastTaskInterface != null;
    }
    //Demon butler returned
    public static RSInterfaceChild butlerReturned() {
        return Interfaces.get(231, 3);
    }
    public static boolean isButlerReturnedInterfaceValid() {
        RSInterfaceChild butlerReturned = butlerReturned();
        return butlerReturned != null;
    }
    // Player Already in Trade
    public static RSInterfaceComponent tradeGameTextChat() {
        return (RSInterfaceComponent) Interfaces.get(162, 58, 0);
    }
    public static String inTradeMessage = "Other player is busy at the moment.";
    public static String sendingTradeMessage = "Sending trade offer...";
    public static String tradeCompleted = "Accepted trade.";
    public static String declinedTrade = "Other player declined trade.";

    // Trade Window Interface
    public static boolean isTradeWindowValid() {
        RSInterface tradeWindowOpen = Interfaces.get(335);
        return tradeWindowOpen != null;
    }
    // Trade Window Next Phase
    public static RSInterfaceChild nextTradeWidowText() {
        return Interfaces.get(335, 30);
    }
    public static String waitingForOtherPlayer = "Waiting for other player...";
    public static String otherPlayerHasAccepted = "Other player has accepted.";
}
