package scripts.api;

import org.tribot.api.General;
import org.tribot.api2007.Login;

public class LoginHelper {

    private static LoginHelper loginhelper;
    public static LoginHelper get() {
        return loginhelper == null? loginhelper = new LoginHelper() : loginhelper;
    }
    // General Login Check
    public static void loginSettings() {
        if (Login.getLoginState().equals(Login.STATE.LOGINSCREEN)) {
            Login.login();
            General.println("[LOGIN]: Logging in...");
            if (Login.getLoginState().equals(Login.STATE.INGAME)) {
                General.println("[LOGIN]: Successfully logged in.");
            }
        }
    }
}
