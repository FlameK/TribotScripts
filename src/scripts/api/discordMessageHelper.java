package scripts.api;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class discordMessageHelper {
    public static void sendDiscordMessage(final String message) {
        String jsonContent = "{ \"content\": \"" + message + "\"}";
        JsonObject json = new JsonParser().parse(jsonContent).getAsJsonObject();
        URL webhookUrl;
        try {
            webhookUrl = new URL("https://discordapp.com/api/webhooks/724621245622255679/8HamqjHI3BYF9yRr074xUAmqn06EmLAvF3mRojzWrcSiW4yuwOs_29Ac0McvNPZY3yGs");
            HttpURLConnection con = (HttpURLConnection) webhookUrl.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.addRequestProperty("Content-Type", "application/json");
            con.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(con.getOutputStream()));
            writer.write(json.toString());
            writer.close();

            StringBuilder sb = new StringBuilder();
            Scanner scanner = new Scanner(con.getInputStream());
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine());
            }
            scanner.close();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            //TODO handle errors
        } catch (IOException e) {
            e.printStackTrace();
            //TODO handle errors
        }
    }
}
