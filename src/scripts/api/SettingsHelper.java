package scripts.api;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Game;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.types.RSVarBit;

import java.awt.event.KeyEvent;

public class SettingsHelper {

    public static String settingHelperText = "[Settings Helper]: ";
    public static int regularSleep = 850;
    public static int shortSleep = 750;
    public static int longSleep = 1050;

    public static int cameraAngle = 100;

    private static SettingsHelper settingsHelper;
    public static SettingsHelper get() {
        return settingsHelper == null? settingsHelper = new SettingsHelper() : settingsHelper;
    }

    public static void enableHelpfulSettings() {
        setShiftDropMethod();
        enableShiftDrop();
        enableEscToClose();
        turnOffGameSounds();
        openInventory();
        setCameraAngle();
        // setZoom();
    }

    //sets dropping method to shift drop and top to bottom zigzag
    public static void setShiftDropMethod() {
        Inventory.setDroppingMethod(Inventory.DROPPING_METHOD.SHIFT);
        General.println(settingHelperText + "Drop method has been set to shift drop");
        Inventory.setDroppingPattern(Inventory.DROPPING_PATTERN.TOP_TO_BOTTOM_ZIGZAG);
        General.println(settingHelperText + "Drop pattern has been set to top to bottom zigzag");
    }

    //enables shift dropping
    public static int shiftDropOn = -2147343104;
    public static int shiftDropOff = -2147474176;
    public static void enableShiftDrop(){
        if (Game.getSetting(1055) == shiftDropOff) {
            General.println(settingHelperText + "Shift drop is not enabled, enabling shift dropping.");
            if (!GameTab.TABS.OPTIONS.isOpen()) {
                GameTab.TABS.OPTIONS.open();
                Timing.waitCondition(() -> {
                    General.sleep(General.randomSD(regularSleep, 75));
                    return GameTab.TABS.OPTIONS.isOpen();
                }, General.random(4000, 6000));
            }
            if (Interface.controlsInterfaceOpen() == null) {
                if (Interface.controlsInterfaceOpen().click()) {
                    Timing.waitCondition(() -> {
                        General.sleep(General.randomSD(regularSleep, 75));
                        return Interface.isShiftDropInterfaceValid();
                    }, General.random(4000, 5000));
                }
            }
            if (Interface.isShiftDropInterfaceValid()) {
                Interface.shiftDropInterfaceIsOpen().click();
                Timing.waitCondition(() -> {
                    General.sleep(General.randomSD(regularSleep, 75));
                    return Game.getSetting(1055) == shiftDropOn;
                }, General.random(4000, 5000));
            }
        }
        else if (Game.getSetting(1055) == shiftDropOn) {
            General.println(settingHelperText + "Shift drop is already enabled.");
        }
    }

    //enables clicking escape to close interface
    public static RSVarBit escToCloseVaribit = RSVarBit.get(4681);
    public static int escToCloseOn = 1;
    public static int escToCloseOff = 0;
    public static void enableEscToClose() {
        if (escToCloseVaribit.getValue() == escToCloseOff) {
            General.println(settingHelperText + "Press esc to close current interface is not enabled.");
            if (!GameTab.TABS.OPTIONS.isOpen()) {
                GameTab.TABS.OPTIONS.open();
                Timing.waitCondition(() -> {
                    General.sleep(General.randomSD(regularSleep, 75));
                    return GameTab.TABS.OPTIONS.isOpen();
                }, General.random(4000, 6000));
            }
            if (Interface.controlsInterfaceOpen() == null) {
                if (Interface.controlsInterfaceOpen().click()) {
                    Timing.waitCondition(() -> {
                        General.sleep(General.randomSD(regularSleep, 75));
                        return Interface.isKeyBindingInterfaceButtonValid();
                    }, General.random(4000, 5000));
                }
            }
        }
        if (Interface.isKeyBindingInterfaceButtonValid()) {
            if (Interface.keyBindingInterfaceButton().click()) {
                Timing.waitCondition(() -> {
                    General.sleep(General.randomSD(regularSleep, 75));
                    return Interface.isKeyBindingInterfaceOpen();
                }, General.random(5000, 6000));
            }
        }
        if (Interface.isKeyBindingInterfaceOpen()) {
            Interface.keyBindingInterfaceEscapeToClose().click();
            Timing.waitCondition(() -> {
                General.sleep(General.randomSD(regularSleep, 75));
                return escToCloseVaribit.getValue() == escToCloseOn;
            }, General.random(5000, 6000));
            General.println(settingHelperText + "Successfully enabled Esc to close current interface.");
            Keyboard.pressKeys(KeyEvent.VK_ESCAPE);
        } else if (escToCloseVaribit.getValue() == escToCloseOn) {
            General.println(settingHelperText + "Esc to close current interface already enabled.");
        }
    }

    public static void setCameraAngle() {
        if (Camera.getCameraAngle() != cameraAngle) {
            General.println("Current Camera Rotation is: " + Camera.getCameraRotation());
            Camera.setCameraAngle(cameraAngle);
            General.println("[CAMERA]: Rotating camera to optimal angle. Camera angle is " + Camera.getCameraAngle());
        }
    }
   /* public static void setZoom() {
        Mouse.scroll(false, 10);
    } */

    // turns off game sounds
    public static int gameVolumeSetting = 168;
    public static int soundsEffectVolumeSetting = 169;
    public static int areaSoundVolumeSetting = 872;
    public static int soundOff = 4;

    public static void turnOffGameSounds() {
        if (Game.getSetting(gameVolumeSetting) != soundOff || Game.getSetting(soundsEffectVolumeSetting) != soundOff || Game.getSetting(areaSoundVolumeSetting) != soundOff) {
            General.println(settingHelperText + "Sounds are enabled, disabling sounds");
            if (!GameTab.TABS.OPTIONS.isOpen()) {
                GameTab.TABS.OPTIONS.open();
                Timing.waitCondition(() -> {
                    General.sleep(General.randomSD(regularSleep, 75));
                    return GameTab.TABS.OPTIONS.isOpen();
                }, General.random(4000, 6000));
            }
            if (GameTab.TABS.OPTIONS.isOpen()) {
                if (Interface.audioInterfaceIsOpen().click()) {
                    Timing.waitCondition(() -> {
                        General.sleep(General.randomSD(regularSleep, 75));
                        return Interface.adjustMusicVolumeIsValid();
                    }, General.random(4000, 5000));
                }
            }
            if (Game.getSetting(gameVolumeSetting) != soundOff) {
                if (Interface.adjustMusicVolume().click()) {
                    Timing.waitCondition(() -> {
                        General.println(settingHelperText + "Attempting to turn of Game Volume.");
                        General.sleep(General.randomSD(regularSleep, 75));
                        return Game.getSetting(gameVolumeSetting) == soundOff;
                    }, General.random(4000, 5000));
                    General.println(settingHelperText + "Successfully turned off Game Volume.");
                }
            }
            if (Game.getSetting(soundsEffectVolumeSetting) != soundOff) {
                if (Interface.adjustSoundEffectVolume().click()) {
                    Timing.waitCondition(() -> {
                        General.println(settingHelperText + "Attempting to turn of Sound Effects.");
                        General.sleep(General.randomSD(regularSleep, 75));
                        return Game.getSetting(soundsEffectVolumeSetting) == soundOff;
                    }, General.random(4000, 5000));
                    General.println(settingHelperText + "Successfully turned off Sounds Effects.");
                }
            }
            if (Game.getSetting(areaSoundVolumeSetting) != soundOff) {
                if (Interface.adjustAreaSoundVolume().click()) {
                    Timing.waitCondition(() -> {
                        General.println(settingHelperText + "Attempting to turn of Area Sound Effects.");
                        General.sleep(General.randomSD(regularSleep, 75));
                        return Game.getSetting(areaSoundVolumeSetting) == soundOff;
                    }, General.random(4000, 5000));
                    General.println(settingHelperText + "Successfully turned off Area Sound Effects.");
                }
            }
        }
        else if (Game.getSetting(gameVolumeSetting) == soundOff && Game.getSetting(soundsEffectVolumeSetting) == soundOff && Game.getSetting(areaSoundVolumeSetting) == soundOff) {
            General.println(settingHelperText + "Sounds have already been disabled.");
        }
    }
    public static void openInventory() {
        if (!GameTab.TABS.INVENTORY.isOpen()) {
            if (GameTab.TABS.INVENTORY.open()) {
                Timing.waitCondition(() -> {
                    General.sleep(General.randomSD(regularSleep, 75));
                    return GameTab.TABS.INVENTORY.isOpen();
                }, General.random(4000, 5000));
            }
            General.println(settingHelperText + "Returned to inventory tab.");
        }
    }
}
