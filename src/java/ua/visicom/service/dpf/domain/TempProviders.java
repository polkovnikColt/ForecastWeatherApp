package ua.visicom.service.dpf.domain;

import com.google.gson.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class TempProviders {

    public static Map getDataFromHTML(String city) throws MalformedURLException, IOException {
        String url = "https://www.ventusky.com/" + city;

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

        String[] fullHtmlLines = response.toString().replaceAll("\\s+", "").replaceAll("<", " ").split(" ");

        String JSON = getJsonFromHTML(fullHtmlLines).replaceAll("\'", "");

        if (JSON != null) {
            Gson gs = new GsonBuilder().create();
            Map<String, int[]> data = gs.fromJson(JSON, Map.class);
            Map m = makeDataMap(data);
            return m;

        }
        return null;
    }

    private static String getJsonFromHTML(String[] fullHtmlLines) {
        String oneLine = "";
        for (String point : fullHtmlLines) {
            if (point.length() >= 21) {
                oneLine = point.substring(0, 21);
            }
            if (oneLine.equals("forecastdata-forecast")) {
                return point.substring(22, point.length() - 3);
            }
        }
        return null;
    }

    private static Map<String, String> makeDataMap(Map json) {
        Map<String, String> daysTemp = new TreeMap<>();
        for (int i = 1; i < json.size() - 2; i++) {
            String o = json.get("d_" + i).toString();
            daysTemp.put("d_" + i, getTemp(o));
        }

        return daysTemp;
    }

    private static String getTemp(String obj) {
        String s = obj.replaceAll("\\s+", "");
        String[] arr = s.split(",");
        ArrayList<Integer> allTemp = new ArrayList<>();
        for (String line : arr) {
            String[] tempArray = line.split("=");
            if (tempArray[0].equals("td")) {
                allTemp.add(Integer.parseInt(tempArray[1].substring(0, 2)));
            }
        }
        return minMax(allTemp);
    }

    private static String minMax(ArrayList<Integer> array) {
        Collections.sort(array);
        return array.get(0) + "/" + array.get(5);
    }

}
