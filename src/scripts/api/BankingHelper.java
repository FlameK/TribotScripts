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
import org.tribot.api2007.types.RSVarBit;
import scripts.api.antiban.Antiban;
import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.RunescapeBank;

import java.awt.event.KeyEvent;

public class BankingHelper {

    public static String bankHelperText = "[Bank Helper]: ";
    public static int regularSleep = 850, shortSleep = 750, longSleep = 1050;
    public static int regularSD = 75, shortSD = 50, longSD = 100;

    private static final int BANK_MASTER_ID = 12, WITHDRAW_AS_ITEM_ID = 21, WITHDRAW_AS_NOTED_ID = 23, WITHDRAW_AS_ITEM_BACKGROUND_ID = 20;

    private static BankingHelper bankingHelper;

    public static BankingHelper get() {
        return bankingHelper == null ? bankingHelper = new BankingHelper() : bankingHelper;
    }

    // closes the bank with the escape option
    public static boolean closeBankWithEscape() {
        if (Banking.isBankScreenOpen() && Banking.isBankLoaded()) {
            Keyboard.sendPress(KeyEvent.CHAR_UNDEFINED, KeyEvent.VK_ESCAPE);
            General.println(bankHelperText + "Attempting to close bank with escape key.");
            Timing.waitCondition(() -> {
                General.sleep(General.randomSD(regularSleep, regularSD));
                return !Banking.isBankScreenOpen();
            }, General.random(5000, 6000));
        }
        return false;
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

    public static boolean equipItemWithBankOpen(String itemName) {

        RSItem[] i = Inventory.find(itemName);
        if (Banking.isBankScreenOpen()) {

            if (i != null && i.length > 0) {
                General.println(bankHelperText + "Attempting to equip: " + itemName);
                if (i[0].click("Wear")) {
                    Antiban.waitItemInteractionDelay();
                    Timing.waitCondition(() -> {
                        General.sleep(General.randomSD(regularSleep, regularSD));
                        return Inventory.find(itemName).length == 0;
                    }, General.random(2000, 3000));
                }
                General.println(bankHelperText + "Equipped item " + itemName);
            }
        }
        return false;
    }

    public static boolean drinkStaminaPotionWithBankOpenAndDeposit(String itemName) {
        RSItem[] i = Inventory.find(itemName);
        int staminaVarbit = 0;
        int staminaPotionInEffect = 1;
        int staminaPotionNoEffect = 0;
        int amount = Inventory.getCount(itemName);

        if (Banking.isBankScreenOpen() && Game.getVarBit(staminaVarbit) == staminaPotionNoEffect) {
            if (i[0].click("Drink")) {
                Antiban.waitItemInteractionDelay();
                Timing.waitCondition(() -> {
                    General.sleep(General.randomSD(regularSleep, regularSD));
                    return Game.getVarBit(staminaVarbit) == staminaPotionInEffect;
                }, General.random(3000, 4000));
            }
        }
        else if (Game.getVarBit(staminaVarbit) == staminaPotionInEffect) {
            General.println(bankHelperText + "Stamina potion is already activated, attempting to deposit item");
        }
        depositItem(amount, itemName);
        return false;
    }

    public static boolean depositItem(int amount, String itemName) {
        RSItem[] i = Inventory.find(itemName);

        if (Banking.isBankScreenOpen() && Banking.isBankLoaded()) {
            General.println(bankHelperText + "Bank is open, attempting to deposit " + amount + " " + itemName);
            if (Inventory.find(itemName).length > 0) {
                Banking.deposit(amount, itemName);
                Antiban.waitItemInteractionDelay();
                Timing.waitCondition(() -> {
                    General.sleep(General.randomSD(regularSleep, regularSD));
                    return Inventory.find(itemName).length == 0;
                }, General.random(3000, 4000));
                General.println(bankHelperText + "Successfully deposited " + amount + " " + itemName);
            }
        }
        return false;
    }

    /**
     * Ideas for more banking features:
     * Withdraw noted (quantity and name)
     * Deposit items (amount, name, antiban measure etc)
     * Withdraw items (amount, name, noted or not, antiban measures etc)
     */
}
