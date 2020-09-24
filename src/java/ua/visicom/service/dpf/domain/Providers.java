package ua.visicom.service.dpf.domain;

import com.google.gson.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author kosogon
 */
public class Providers {
     private static final String API_TOKEN = "150524c4bd5f90610b756e6f24b67a5a";

//    public static Map getWeatherData(Map model) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
    
       public static HashMap<String, Object> getWeatherData(String city) {
        String[] argDes = {"main", "description"};
        String[] argTemp = {"temp"};

        Map map;
         try {
             map = getJsonData(city);
         } catch (IOException ex) {
             Logger.getLogger(Providers.class.getName()).log(Level.SEVERE, null, ex);
             throw new UnsupportedOperationException("Not supported");
         }
        
        String[] desription = getDataFromObject(map.get("weather").toString(), argDes);
        String[] temp = getDataFromObject(map.get("main").toString(), argTemp);

        HashMap<String, Object> data = new HashMap<String, Object>() {
            {

                put("main", desription[0]);
                put("desc", firstToUppetCase(desription[1]));
                put("temp", Math.round(Double.parseDouble(temp[0].trim()) - 273));

            }
        };
        return data;
    }

    private static String[] getDataFromObject(String str, String[] arg0) {
        String[] array = str.substring(1, str.length() - 1).split(",");
        String[] response = new String[arg0.length];
        for (int i = 0; i < arg0.length; i++) {
            for (String item : array) {
                String[] tempArr = item.trim().split("=");
                if (tempArr[0].equals(arg0[i])) {
                    response[i] = tempArr[1];
                }
            }
        }

        return response;
    }

    private static Map getJsonData(String city) throws MalformedURLException, IOException {
       

        String url = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_TOKEN;

        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        connection.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        Gson gs = new GsonBuilder().create();

        Map<String, Object> data = gs.fromJson(response.toString(), Map.class);

        return data;

    }
    
    public static String firstToUppetCase(String str){
        return str.substring(0,1).toUpperCase() + str.substring(1,str.length());
    }

    
    
}
