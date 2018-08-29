package br.com.ericsson.smartnotification.route.utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.ericsson.smartnotification.domain.api.PushNotification;
import br.com.ericsson.smartnotification.domain.api.ResponseNotification;
import br.com.ericsson.smartnotification.utils.JsonUtil;

@Component
public class PushFirebaseApp {
    
    @Value("${google.apis.url_google_apis_fcm}")
    private String urlGoogleApisFcm;

    public ResponseNotification sendMessage(String appAuthorizationKey, String title, String message, String token) throws IOException {      
        URL url = new URL(urlGoogleApisFcm);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("Authorization", "key=" + appAuthorizationKey);
        
        // Send post request
        con.setDoOutput(true);
        DataOutputStream  wr = new DataOutputStream(con.getOutputStream());

        wr.writeBytes(JsonUtil.parseToJsonString(new PushNotification(title, message, token)));
        wr.flush();
        wr.close();
        if(con.getResponseCode() != 200) {
            throw new IOException("Error " + urlGoogleApisFcm + " : " + con.getResponseCode());
        }
        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return JsonUtil.getGson().fromJson(response.toString(), ResponseNotification.class);
    }
    
}
