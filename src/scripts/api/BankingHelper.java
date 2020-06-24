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

    /** closes the bank with the escape option
     * @return
     */
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

    /** checks to see if the withdraw as noted option is enabled
     * @return
     */
    public static boolean isNotedOn() {
        if (Banking.isBankScreenOpen()) {
            RSInterface itemInterfaceChild = Interfaces.get(BANK_MASTER_ID, WITHDRAW_AS_ITEM_BACKGROUND_ID, 0);
            if (itemInterfaceChild != null) {
                return itemInterfaceChild.getTextureID() == 1141;
            }
        }
        return false;
    }

    /**
     * If noted is not enabled and returns false, use this to enabled withdraw as noted
     * @param noted
     * @return
     */
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

    /**
     * Equips an item in players inventory while the bank is open, uses ABC interaction delays.
     * @param itemName
     * @return
     */
    public static boolean equipItemWithBankOpen(String itemName) {

        RSItem[] i = Inventory.find(itemName);
        if (Banking.isBankScreenOpen()) {

            if (i != null && i.length > 0) {
                General.println(bankHelperText + "Attempting to equip: " + itemName);
                if (i[0].click("Wear") || i[0].click("Wield")) {
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

    /**
     * Drinks one does of stamina potion and then deposits it in the bank.
     * @param
     * @return
     */
    public static boolean drinkStaminaPotionWithBankOpenAndDeposit() {
        String[] staminaPotion = {"Stamina potion(1)", "Stamina potion(2)", "Stamina potion(3)", "Stamina potion(4)"};
        RSItem[] i = Inventory.find(staminaPotion);
        int staminaVarbit = 25;
        int staminaPotionInEffect = 1;
        int staminaPotionNoEffect = 0;
        int amount = Inventory.getCount(staminaPotion);

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
        if (Inventory.find(staminaPotion).length > 0) {
            depositItem(-1,staminaPotion[0]);
        }

        return false;
    }

    /**
     * Deposits a set amount of a specific item into the bank, utilising ABC interaction delays
     * @param amount
     * @param itemName
     * @return
     */

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
     * Withdraws an item from the bank in noted form. To withdraw all items set amount to -1.
     */

    public static boolean withdrawNoted(int amount, String itemName) {

        if (Banking.isBankScreenOpen() && Banking.isBankLoaded()) {
            if (!isNotedOn()) {
                General.println(bankHelperText + "Withdraw as noted is not enabled, enabling withdraw as noted.");
                setNoted(true);
            }

            int startCount = Inventory.getCount(itemName);

            if (amount == -1) {
                General.println(bankHelperText + "Attempting to withdraw all of item " + itemName);
                if (Banking.withdraw(0, itemName)) {
                    Antiban.waitItemInteractionDelay();
                    Timing.waitCondition(() -> {
                        General.sleep(General.randomSD(regularSleep, regularSD));
                        return Inventory.getCount(itemName) > startCount;
                    }, General.random(3000, 4000));
                }
            } else if (amount != -1) {
                General.println(bankHelperText + "Attempting to withdraw " + itemName + "as noted form");
                if (Banking.withdraw(amount, itemName)) {
                    Antiban.waitItemInteractionDelay();
                    Timing.waitCondition(() -> {
                        General.sleep(General.randomSD(regularSleep, regularSD));
                        return Inventory.getCount(itemName) > startCount;
                    }, General.random(3000, 4000));
                }
            }
        }
        return false;
    }


    /**
     * Ideas for more banking features:
     * Withdraw noted (quantity and name)
     * Deposit items (amount, name, antiban measure etc)
     * Withdraw items (amount, name, noted or not, antiban measures etc)
     * Withdraw potion, always withdraw the lowest one first.
     */
}
