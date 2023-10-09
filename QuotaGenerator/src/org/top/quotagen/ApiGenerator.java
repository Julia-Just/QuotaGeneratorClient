package org.top.quotagen;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class ApiGenerator implements IGenerator {
    @Override
    public String getRandomQuota() {
        try {
            URL url = new URL("https://api.whatdoestrumpthink.com/api/v1/quotes/random");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("accept", "application/json");
            InputStream responseStream = connection.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(responseStream));
           // return in.readLine();

            Object obj = new JSONParser().parse(in);
            JSONObject jo = (JSONObject) obj;  // Уточнить, что это значит?
            String quoteText = (String) jo.get("message");
            return quoteText;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}