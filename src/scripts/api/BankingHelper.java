package scripts.api;

import org.tribot.api.Clicking;
import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Keyboard;
import org.tribot.api.types.generic.Condition;
import org.tribot.api2007.*;
import org.tribot.api2007.types.RSInterface;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSItem;
import scripts.api.antiban.Antiban;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.RunescapeBank;

import java.awt.event.KeyEvent;

public class BankingHelper {

    public static String bankHelperText = "[Bank Helper]: ";
    public static int regularSleep = 850;
    public static int shortSleep = 750;
    public static int longSleep = 1050;
    private static final int BANK_MASTER_ID = 12,
            WITHDRAW_AS_ITEM_ID = 21,
            WITHDRAW_AS_NOTED_ID = 23,
            WITHDRAW_AS_ITEM_BACKGROUND_ID = 20;


    private static BankingHelper bankingHelper;

    public static BankingHelper get() {
        return bankingHelper == null ? bankingHelper = new BankingHelper() : bankingHelper;
    }

    // closes the bank with the escape option
    public static void closeBankWithEscape() {
        if (Banking.isBankScreenOpen() && Banking.isBankLoaded()) {
            Keyboard.sendPress(KeyEvent.CHAR_UNDEFINED, KeyEvent.VK_ESCAPE);
            General.println(bankHelperText + "Attempting to close bank with escape key.");
            Timing.waitCondition(() -> {
                General.sleep(General.randomSD(regularSleep, 75));
                return !Banking.isBankScreenOpen();
            }, General.random(5000, 6000));
        }
    }

    public static boolean isNotedOn() {
        if (Banking.isBankScreenOpen()) {
            RSInterface itemInterfaceChild = Interfaces.get(BANK_MASTER_ID, WITHDRAW_AS_ITEM_BACKGROUND_ID, 0);
            if (itemInterfaceChild != null) {
                return itemInterfaceChild.getTextureID() == 1141;
            }
        }
        return false;
    }

    public static boolean setNoted(boolean noted) {
        if (Banking.isBankScreenOpen()) {
            if (noted && !isNotedOn()) {
                RSInterfaceChild notedInterfaceChild = Interfaces.get(BANK_MASTER_ID, WITHDRAW_AS_NOTED_ID);
                if (notedInterfaceChild != null && notedInterfaceChild.click("Note")) {
                    General.println(bankHelperText + "Turning on 'Withdraw as noted' option.");
                    Timing.waitCondition(() -> {
                        General.sleep(10, 30);
                        return isNotedOn();
                    }, General.random(800, 1200));
                    return isNotedOn();
                }
            } else if (!noted && isNotedOn()) {
                RSInterfaceChild itemInterfaceChild = Interfaces.get(BANK_MASTER_ID, WITHDRAW_AS_ITEM_ID);
                if (itemInterfaceChild != null && itemInterfaceChild.click("Item")) {
                    Timing.waitCondition(() -> {
                        General.sleep(10, 30);
                        return !isNotedOn();
                    }, General.random(800, 1200));
                    return !isNotedOn();
                }
            }
        }
        return false;
    }

    public static boolean equipItemWithBankOpen(String item) {

        RSItem[] i = Inventory.find(item);
        if (Banking.isBankScreenOpen()) {

            if (i != null && i.length > 0) {
                if (i[0].click("Wear")) {
                    Antiban.waitItemInteractionDelay();
                    Timing.waitCondition(() -> {
                        General.sleep(General.randomSD(750, 75));
                        return Inventory.find(item).length == 0;
                    }, General.random(2000, 3000));
                }
                General.println("BANKING: Equipped item " + item);
            }
        }
        return false;
    }
}
