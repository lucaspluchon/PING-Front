package com.app.ping.weather;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Timer;
import java.util.TimerTask;

import com.app.ping.Language;
import com.app.ping.PingApp;
import com.app.ping.controller.CSS;
import javafx.scene.paint.Color;
import javafx.util.Pair;
import org.json.JSONObject;

import static com.app.ping.Controller.*;


public class WeatherManager
{
    /**
     * OpenWeatherMap api key
     */
    private static final String OPEN_WEATHER_MAP_API_KEY = "ea57dfd61e2a2140837dcef81165fb74";
    public static String backColor;
    public static String secondColor;

    public static Timer timer;

    public static String apiLink;

    public static  WeatherReport lastWeather = new WeatherReport(0, 0);

    public static String getWeatherConfig()
    {
        Path path = null;
        try
        {
            path = Path.of(System.getProperty("user.home"),"config.json");
            if (!path.toFile().exists())
                return null;
        }
        catch (Exception e)
        {
            return null;
        }

        JSONObject config;
        try
        {
            config = new JSONObject(Files.readString(path));
        }
        catch (Exception e)
        {
            return null;
        }

        try
        {
            return config.getString("city");
        }
        catch (Exception e)
        {
            return null;
        }
    }


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
     * @return the JSON body representing the weather condition
     * @throws IOException if the request fail
     */
    static public JSONObject getWeather() throws IOException
    {
        return request(apiLink);
    }

    static public String getIP()
    {
        try
        {
            JSONObject ip = request("https://api.ipify.org?format=json");
            return ip.get("ip").toString();
        } catch (IOException e)
        {
            System.err.println("Unable to get ip");
        }
        return null;
    }

    static public Pair<String, String> getLocation(String ip)
    {
        try
        {
            JSONObject res = request("https://ipapi.co/" + ip + "/json/");
            System.out.println("Location for " + ip + " " + res.get("city") + " " + res.get("country"));
            return new Pair<>(res.get("latitude").toString(), res.get("longitude").toString());

        } catch (IOException e)
        {
            System.err.println("Unable to get location");
        }
        return null;

    }

    public static int getCloud(JSONObject data)
    {
        JSONObject clouds = (JSONObject) data.get("clouds");
        return (int) clouds.get("all");
    }

    public static double getRain(JSONObject data)
    {
        if (!data.keySet().contains("rain"))
            return 0;
        JSONObject clouds = (JSONObject) data.get("rain");
        return (double) clouds.get("1h");
    }

    public static Color buildColor(int rain, int clouds)
    {
        int beginRed = 255;
        int beginGreen = 197;
        int beginBlue = 0;

        int endRed = 183;
        int endGreen = 166;
        int endBlue = 108;

        if (rain <= 15)
        {
            return Color.rgb(
                    (int) (beginRed + ((int) (clouds * (endRed - beginRed) / 100))),//RED
                    (int) (beginGreen + ((int) (clouds * (endGreen - beginGreen) / 100))), // GREEN
                    (int) (beginBlue + ((int) (clouds * (endBlue - beginBlue) / 100)))// BLUE
            );
        }

        beginRed = 0;
        beginGreen = 112;
        beginBlue = 255;

        endRed = 98;
        endGreen = 125;
        endBlue = 158;

        return Color.rgb(
                (int) (beginRed + ((int) (clouds * (endRed - beginRed) / 100))),//RED
                (int) (beginGreen + ((int) (clouds * (endGreen - beginGreen) / 100))), // GREEN
                (int) (beginBlue + ((int) (clouds * (endBlue - beginBlue) / 100)))// BLUE
        );
    }

    /**
     * Get a weather report at the current location
     *
     * @return {@link WeatherReport} containing the weather If one of the request fail the function returns null
     */
    public static WeatherReport getWeatherReport()
    {
        try
        {
            JSONObject weather = getWeather();
            int rain = (int) ((getRain(weather) / 5) * 100);
            if (rain > 100)
                rain = 100;
            return new WeatherReport(getCloud(weather), rain);
        } catch (IOException e)
        {
            return null;
        }
    }

    public static String toHex(Color color)
    {
        return "#" + color.toString().substring(2,8);
    }

    private static double clamp(double value)
    {
        return Math.max(0, Math.min(1, value));
    }
    public static Boolean isBlue()
    {
        if (lastWeather != null)
            return lastWeather.rain() > 15;
        return false;
    }

    public static void adaptWeather() throws IOException {
        System.out.println("Adapting");
        System.out.println(apiLink);
        WeatherReport weather = getWeatherReport();
        WeatherManager.lastWeather = weather;

        System.out.println(weather);
        if (weather == null)
        {
            System.err.println("Unable to get weather");
            return;
        }

        Color themeColor = buildColor(weather.rain(), weather.clouds());
        backColor = toHex(themeColor);
        Color secondThemeColor = new Color(clamp(themeColor.getRed() * 1.20), clamp(themeColor.getGreen() * 1.20), clamp(themeColor.getBlue() * 1.20), 1);
        secondColor = toHex(secondThemeColor);

        CSS.setStyle(_resultTab, ".tab-header-background", String.format("-fx-background-color: %s;", backColor));
        CSS.setStyle(_resultTab, ".tab-pane .tab:selected", String.format("-fx-background-color: %s;", secondColor));
        CSS.setStyle(_consoleResult, ".text-area", String.format("-fx-control-inner-background: %s; -fx-background-color: %s", backColor, backColor));
    }


    public static void startTimer() throws IOException {
        if (PingApp.city == null)
        {
            String ip = getIP();
            if (ip == null)
                return;
            Pair<String, String> coord = getLocation(ip);
            if (coord == null)
                return;
            apiLink = "https://api.openweathermap.org/data/2.5/weather?lat=" + coord.getValue() + "&lon=" + coord.getKey() + "&appid=" + OPEN_WEATHER_MAP_API_KEY;
        }
        else
        {
            apiLink = "https://api.openweathermap.org/data/2.5/weather?q=" + PingApp.city + "&appid=" + OPEN_WEATHER_MAP_API_KEY;
        }

        WeatherManager.timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask()
        {
            @Override
            public void run()
            {
                try {
                    adaptWeather();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }, 0, 300000);

    }

}
