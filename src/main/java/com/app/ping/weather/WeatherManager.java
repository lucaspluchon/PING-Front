package com.app.ping.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

import javafx.util.Pair;
import org.json.JSONObject;


public class WeatherManager
{
    /// OpenWeatherMap api key
    private static final String OPEN_WEATHER_MAP_API_KEY = "ea57dfd61e2a2140837dcef81165fb74";

    /**
     * Make a url request using JSON body type
     *
     * @param url url of the request
     * @return the JSON body of the http response
     * @throws IOException If the request fail
     */
    static public JSONObject request(String url) throws IOException
    {
        URL urli = new URL(url);
        HttpURLConnection http = (HttpURLConnection) urli.openConnection();
        http.setRequestProperty("Accept", "application/json");
        StringBuilder content = new StringBuilder();
        String line;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(http.getInputStream()));

        while ((line = bufferedReader.readLine()) != null)
        {
            content.append(line).append("\n");
        }
        bufferedReader.close();
        http.disconnect();
        return new JSONObject(content.toString());
    }

    /**
     * Make a request to get weather at a given location using OpenWeatherMap API
     *
     * @param lon longitude for the coord
     * @param lat latitude for the coord
     * @return the JSON body representing the weather condition
     * @throws IOException if the request fail
     */
    static public JSONObject getWeather(String lon, String lat) throws IOException
    {
        return request("https://api.openweathermap.org/data/2.5/weather?lat=" + lat + "&lon=" + lon + "&appid=" + OPEN_WEATHER_MAP_API_KEY);
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

    public static float getRain(JSONObject data)
    {
        if (!data.keySet().contains("rain"))
            return 0;
        JSONObject clouds = (JSONObject) data.get("rain");
        return (float) clouds.get("1h");
    }

    /**
     * Get a weather report at the current location
     *
     * @return {@link WeatherReport} containing the weather If one of the request fail the function returns null
     */
    public static WeatherReport getWeatherReport()
    {
        String ip = getIP();
        Pair<String, String> coord = getLocation(ip);
        assert coord != null;
        try
        {
            JSONObject weather = getWeather(coord.getValue(), coord.getKey());
            return new WeatherReport(getCloud(weather), getRain(weather));
        } catch (IOException e)
        {
            return null;
        }
    }

    public static void setTimer()
    {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println(WeatherManager.getWeatherReport());
            }
        }, 0, 300000);

    }

}
