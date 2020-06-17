package scripts.api;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.ChooseOption;
import org.tribot.api2007.GameTab;
import org.tribot.api2007.Interfaces;
import org.tribot.api2007.types.RSGroundItem;
import org.tribot.api2007.types.RSInterfaceChild;
import org.tribot.api2007.types.RSItem;

import java.awt.*;

public class InventoryHelper {

    public static boolean dragItemToSlot(RSItem item, int row, int col, int... dev){
        int x; int y;
        RSInterfaceChild i = Interfaces.get(149,0);
        if (i == null)
            return false;

        x = (int)i.getAbsoluteBounds().getX()+42*(col-1)+16+ General.randomSD(-16, 16, 0, dev.length == 0 ? 6 : dev[0]);
        y = (int)i.getAbsoluteBounds().getY()+36*(row-1)+16+General.randomSD(-16, 16, 0, dev.length == 0 ? 6 : dev[0]);

        item.hover();
        Mouse.drag(Mouse.getPos(),new Point(x, y),1);
        return item.getIndex() == row*4+col;
    }
    private static boolean inventoryItemFoundAndValid (RSItem[] inventoryItems) {
        return inventoryItems.length > 0 && inventoryItems[0].isClickable();
    }

    private static boolean groundItemFoundAndValid (RSGroundItem[] groundItems) {
        return groundItems.length > 0 && groundItems[0].isClickable();
    }

    private static boolean rightClickInventoryItem (RSItem item, String optionToSelect) {
        if (item.hover()) {
            Mouse.click(GlobalConstants.RIGHT_CLICK);
            String optionToSelectFullString = optionToSelect + " " + item.getDefinition().getName();
            if (ChooseOption.isOptionValid(optionToSelectFullString)) {
                return ChooseOption.select(optionToSelectFullString);
            } else if (ChooseOption.isOptionValid("Cancel")) {
                if (ChooseOption.select("Cancel")) {
                    return rightClickInventoryItem(item, optionToSelect);
                }
            }
        }
        return false;
    }

    private static boolean rightClickGroundItem (RSGroundItem item, String optionToSelect) {
        if (item.hover()) {
            Mouse.click(GlobalConstants.RIGHT_CLICK);
            String optionToSelectFullString = optionToSelect + " " + item.getDefinition().getName();
            if (ChooseOption.isOptionValid(optionToSelectFullString)) {
                return ChooseOption.select(optionToSelectFullString);
            } else if (ChooseOption.isOptionValid("Cancel")) {
                ChooseOption.select("Cancel");
            }
        }
        return false;
    }

    public static boolean clickOnInventoryItem (RSItem[] inventoryItems, String optionToSelect) {
        boolean inventoryOpenedSuccessfully = TabsHelper.openTab(GameTab.TABS.INVENTORY) &&
                Timing.waitCondition(() -> {
                    General.sleep(500);
                    return GameTab.TABS.INVENTORY.isOpen();
                }, General.random(2000, 5000));

        if (inventoryOpenedSuccessfully && inventoryItemFoundAndValid(inventoryItems)) {
            RSItem inventoryItem = inventoryItems[0];
            boolean successfullyClicked = (optionToSelect == "") ? inventoryItem.click() : rightClickInventoryItem(inventoryItem, optionToSelect);
            return successfullyClicked;
        }
        return false;
    }

    public static boolean clickOnGroundItem (RSGroundItem[] groundItems, String optionToSelect) {
        if (groundItemFoundAndValid(groundItems)) {
            RSGroundItem groundItem = groundItems[0];
            boolean successfullyClicked = (optionToSelect == "") ? groundItem.click() : rightClickGroundItem(groundItem, optionToSelect);
            return successfullyClicked;
        }
        return false;
    }

    public static boolean clickOnInventoryItem (RSItem[] inventoryItems) {
        return clickOnInventoryItem(inventoryItems, "");
    }

    public static boolean clickOnGroundItem (RSGroundItem[] groundItems) {
        return clickOnGroundItem(groundItems, "");
    }
}
