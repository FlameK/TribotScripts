package scripts.teleTabMaker;

import org.tribot.api.General;
import org.tribot.api.Timing;
import org.tribot.api.input.Mouse;
import org.tribot.api2007.Camera;
import org.tribot.api2007.Inventory;
import org.tribot.api2007.Login;
import org.tribot.script.Script;
import org.tribot.script.ScriptManifest;
import org.tribot.script.interfaces.Arguments;
import org.tribot.script.interfaces.Painting;

import scripts.dax_api.api_lib.DaxWalker;
import scripts.dax_api.api_lib.models.DaxCredentials;
import scripts.dax_api.api_lib.models.DaxCredentialsProvider;

import scripts.teleTabMaker.antiban.Antiban;
import scripts.teleTabMaker.antiban.PersistantABCUtil;
import scripts.teleTabMaker.data.MyDaxCredentials;
import scripts.teleTabMaker.data.PriceCalculations;
import scripts.teleTabMaker.data.Vars;
import scripts.teleTabMaker.framework.Task;
import scripts.teleTabMaker.graphicalUserInterface.GUI;
import scripts.teleTabMaker.tasks.ExecuteMakingTabs;
import scripts.teleTabMaker.tasks.ExecuteRestocking;
import scripts.teleTabMaker.tasks.ExecuteReturningToHouse;
import scripts.teleTabMaker.tasks.ExecuteUnnotingClay;

import scripts.wastedbro.api.rsitem_services.GrandExchange;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

@ScriptManifest(category = "Money Making", name = "Elliott's HouseTabber", authors = "Elliott")
public class Main extends Script implements Painting, Arguments {

    private ArrayList<Task> tasks = new ArrayList<>();
    private long startTime;
    public static int startTeleportTabCount = 0;
    Font font = new Font("Verdana", Font.PLAIN, 12);

    public String MULE_NAME = "";
    public String TAB_TO_MAKE = "Teleport to house";

    private URL fxml;
    private GUI gui;

    @Override
    public void run() {

        try {
            fxml = new URL("https://pastebin.com/raw/JDwNd48e"); //path goes here
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        gui = new GUI(fxml);
        gui.show();
        while (gui.isOpen()) {
            General.println("GUI is open, please complete.");
            sleep(500);
        }

        General.useAntiBanCompliance(true);
        Antiban.create();
        Antiban.setPrintDebug(true);
        PriceCalculations.costOfTeleportToHouse = GrandExchange.getPrice(PriceCalculations.teleportToHouseID);
        General.println("The price of a Teleport to house tab is: " + PriceCalculations.costOfTeleportToHouse);
        PriceCalculations.costOfLawRune = GrandExchange.getPrice(PriceCalculations.lawRuneID);
        General.println("The price of a Law rune is: " + PriceCalculations.costOfLawRune);
        PriceCalculations.costOfSoftClay = GrandExchange.getPrice(PriceCalculations.softClayID);
        General.println("The price of Soft clay is: " + PriceCalculations.costOfSoftClay);
        startTime = Timing.currentTimeMillis();
        Camera.setRotationMethod(Camera.ROTATION_METHOD.ONLY_MOUSE);
        Camera.setCamera(General.random(87, 100), 100);

        PersistantABCUtil.generateRunPercentage();
        PersistantABCUtil.activateRun();

        General.println("Welcome to Elliott's Teleport Tab Maker.");
        General.println("If you find any bugs please let me know. Happy botting!");

        DaxWalker.setCredentials(new DaxCredentialsProvider() {
            @Override
            public DaxCredentials getDaxCredentials() {
                return new DaxCredentials(MyDaxCredentials.myDaxCredentials, "PUBLIC-KEY");
            }
        });

        if (Login.getLoginState().equals(Login.STATE.INGAME)) {
            General.println("[LOGIN]: Successfully logged in.");
            startTeleportTabCount = Inventory.getCount(PriceCalculations.teleportToHouseID);
            General.println("Start Teleport Tab count is: " + startTeleportTabCount);
            Camera.setCameraAngle(General.random(73, 111));

        }

        addTasks();

        while (Vars.get().shouldRun) {

            handleTasks();
        }

    }

    private void addTasks() {

        tasks.add(new ExecuteMakingTabs());
        tasks.add(new ExecuteUnnotingClay());
        tasks.add(new ExecuteReturningToHouse());
    }

    private void handleTasks() {
        for (Task t : tasks) {
            if (t.validate()) {
                Vars.get().status = t.toString();
                t.execute();
                General.sleep(100, 250);
                break;
            }
        }
    }

    public static void stopScript(String reason) {
        General.println("Script stopped : " + reason);
        Vars.get().shouldRun = false;
        Antiban.destroy();
    }

    @Override
    public void onPaint(Graphics g) {

        double timeRan = System.currentTimeMillis() - startTime;

        int tabsUsed = ExecuteMakingTabs.tabsMade;
        int totalCostOfTabs = PriceCalculations.costOfTeleportToHouse * tabsUsed;
        int currentTabs = Inventory.getCount(PriceCalculations.teleportToHouseID);
        int tabsMade = currentTabs - startTeleportTabCount;
        int tabsPerHour = (int) (tabsMade * (3600000 / timeRan));

        int lawRunesUsed = ExecuteMakingTabs.lawRunesUsed;
        int totalCostOfLawRunes = PriceCalculations.costOfLawRune * lawRunesUsed;

        int softClayUsed = ExecuteMakingTabs.softClayUsed;
        int totalCostOfSoftClay = PriceCalculations.costOfSoftClay * softClayUsed;

        int coinsUsed = ExecuteUnnotingClay.coinsUsed;
        int totalCostOfCoins = PriceCalculations.costOfCoins * coinsUsed;

        int totalCosts = totalCostOfLawRunes + totalCostOfSoftClay + totalCostOfCoins;
        int profitGained = (totalCostOfTabs) - totalCosts;
        int profitPerHour = (int) (profitGained * (3600000 / timeRan));

        g.setColor(Color.BLACK);
        g.fillRect(7, 234, 278, 98);

        g.setColor(Color.WHITE);
        g.setFont(font);

        g.drawString("Elliott's Tab Maker Version 1", 10, 250);
        g.drawString("Version:  1.01", 10, 265);
        g.drawString("Task: " + Vars.get().status, 10, 280);
        g.drawString("Runtime: " + Timing.msToString(this.getRunningTime()), 10, 295);
        g.drawString("Tabs Made: " + tabsMade + " (" + tabsPerHour + ")", 10, 310);
        g.drawString("Profit: " + profitGained + "(" + profitPerHour + ")", 10, 325);

        Point mP = Mouse.getPos();
        g.drawLine(mP.x, 0, mP.x, 500);
        g.drawLine(0, mP.y, 800, mP.y);
    }

    @Override
    public void passArguments(HashMap<String, String> arg0) {
        String scriptSelect = arg0.get("custom_input");
        String clientStarter = arg0.get("autostart");
        String input = clientStarter != null ? clientStarter : scriptSelect;
            for(String arg:input.split(";")){
                try{
                    if(arg.startsWith("tablet")){
                        TAB_TO_MAKE = arg.split(":")[1];
                    } else if(arg.startsWith("mule")){
                        MULE_NAME = arg.split(":")[1];
                    }
                } catch(Exception e){
                    println("Error in arguments. Please define in the following order: 'tablet:tabletName';'mule:muleName'.");
                }
            }
        }
}

