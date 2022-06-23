package com.app.ping.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javafx.util.Pair;
import org.json.JSONObject;


public class WeatherManager
{
    static public JSONObject request(String url) throws IOException
    {
        URL urli = new URL(url);
        HttpURLConnection http = (HttpURLConnection)urli.openConnection();
        http.setRequestProperty("Accept", "application/json");
        StringBuilder content = new StringBuilder();
        String line;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(http.getInputStream()));

        // reading from the urlconnection using the bufferedreader
        while ((line = bufferedReader.readLine()) != null)
        {
            content.append(line).append("\n");
        }
        bufferedReader.close();
        http.disconnect();
        return new JSONObject(content.toString());
    }

    static public JSONObject getWeather(String lon, String lat) throws IOException
    {
        return request("https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=ea57dfd61e2a2140837dcef81165fb74");
    }

    static public String getIP()
    {
        try
        {
            JSONObject ip = request("https://api.ipify.org?format=json");
            return ip.get("ip").toString();
        } catch (IOException e)
        {
            System.out.println("Unable to get ip");
        }
        return null;
    }

    static public Pair<String, String> getLocation(String ip)
    {
        try
        {
            JSONObject res = request("https://ipapi.co/" + ip + "/json/");
            return new Pair<>(res.get("latitude").toString(), res.get("longitude").toString());

        } catch (IOException e)
        {
            System.out.println("Unable to get location");
        }
        return null;

    }

    public static int getCloud(JSONObject data)
    {
        JSONObject clouds = (JSONObject) data.get("clouds");
        return (int) clouds.get("all");
    }


    public static void main(String[] args)
    {

        String ip = getIP();
        Pair<String, String> coord = getLocation(ip);
        assert coord != null;
        try
        {
            JSONObject weather = getWeather(coord.getValue(), coord.getKey());
            System.out.println(getCloud(weather));
        } catch (IOException e)
        {
            System.out.println("Error");
        }


    }
}
