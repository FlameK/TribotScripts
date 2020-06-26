package scripts.teleTabMaker.graphicalUserInterface;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import org.tribot.api.General;
import scripts.teleTabMaker.data.UserSettings;

import java.net.URL;
import java.util.ResourceBundle;

public class GUIController extends AbstractGUIController {

    @FXML
    private TextField hostName;

    @FXML
    private Button startScriptButton;

    @FXML
    private CheckBox muleToHostTick;

    @FXML TextField muleName;

    @FXML
    public void setMuleToHostSelected() {
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void startButtonPressed() {

        //Setting the host preference
        String nameOfHost = hostName.getText();
        General.println("The host name is: " + nameOfHost);
        UserSettings.HOST_NAME = nameOfHost;

        //Setting the mule Name
        if (muleToHostTick.isSelected()) {
            String nameOfMule = hostName.getText();
            General.println("The name of the mule is: " + nameOfMule);
            UserSettings.MULE_NAME = nameOfMule;
        } else {
            String nameOfMule = muleName.getText();
            General.println("The mule is not the host. The mule you're using is: " + nameOfMule);
            UserSettings.MULE_NAME = nameOfMule;
        }

        //Setting the restocking preferences

        this.getGUI().close();
    }

}
