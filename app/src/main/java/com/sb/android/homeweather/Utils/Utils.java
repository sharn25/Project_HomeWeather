package com.sb.android.homeweather.Utils;

import com.sb.android.homeweather.logger.log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by singhsh on 9/17/2019.
 */

public class Utils {
    public static JSONObject getJSON(String murl){
        try {
            log.d("MainActivity","EnterJSONObject fecther from URL");
            URL url = new URL(murl);
            log.d("MainActivity","1");
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestProperty("content-type", "application/json;  charset=utf-8");
            con.setRequestProperty("Content-Language", "en-US");
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(false);


            int responseCode = con.getResponseCode();
            log.d("MainActivity","2");
            BufferedReader in =new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            log.d("MainActivity","3");
            String inputLine;
            StringBuffer json = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                json.append(inputLine);
            } in .close();
            JSONObject data = new JSONObject(json.toString());
            log.d("MainActivity","o" + json.toString());
            // This value will be 404 if the request was not
            // successful

            return data;
        }catch(Exception e){
            log.d("MainActivity","error(JSON get) : " +e);
            return null;
        }


    }
}
