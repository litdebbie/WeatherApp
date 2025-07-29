import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

// Retreive weather data from API - this backend logic will fetch the latest weather
// data from external API and return it. 
// The GUI will display this data to the user

public class WeatherApp {
    // fetch weather data for searched location
    public static JSONObject getWeatherData(String locationName) {
        // get location coordinates using the geolocation API
        JSONArray locationData = getLocationData(locationName);

        // extract latitude and longitude data
        JSONObject location = (JSONObject) locationData.get(0);
        double latitude = (double) location.get("latitude");
        double longitude = (double) location.get("longitude");

        // build API request URL with location coordinates
        String urlString = "https://api.open-meteo.com/v1/forecast?" +
                "latitude=" + latitude + "&longitude=" + longitude +
                "&hourly=temperature_2m,relativehumidity_2m,weathercode,windspeed_10m&timezone=America%2FChicago";

        try {
            // call API and get response
            HttpURLConnection conn = fetchApiResponse(urlString);

            // check for response status
            // 200 means successful connection
            if (conn.getResponseCode() != 200) {
                System.out.println("Error: Could not connect to API!");
                return null;
            } else {
                // store the API results
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());

                // read and store the resulting json data into our string builder
                while(scanner.hasNext()) {
                    resultJson.append(scanner.nextLine());
                }

                // close scanner
                scanner.close();

                // close url connection
                conn.disconnect();

                // parse the JSON string into a JSON obj
                JSONParser parser = new JSONParser();
                JSONObject resultJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));
                
                // retrieve hourly data
                JSONObject hourly = (JSONObject) resultJsonObj.get("hourly");

                // want to get the current hour's data
                // need to get the index of our current hour
                JSONArray time = (JSONArray) hourly.get("time");
                int index = findIndexOfCurrentTime(time);

                // get temperature
                JSONArray temperatureData = (JSONArray) hourly.get("temperature_2m");
                double temperature = (double) temperatureData.get(index);

                // get weather code
                JSONArray weatherCode = (JSONArray) hourly.get("weather_code");
                String weatherCondition = convertWeatherCode((long) weatherCode.get(index));

                // get humidity
                JSONArray relativeHumidity = (JSONArray) hourly.get("relativehumidity_2m");
                long humidity = (long) relativeHumidity.get(index);

                // get windspeed
                JSONArray windspeedData = (JSONArray) hourly.get("windspeed_10m");
                double windspeed = (double) windspeedData.get(index);

                // build the weather json data object that will be accessed in frontend
                JSONObject weatherData = new JSONObject();
                weatherData.put("temperature", temperature);
                weatherData.put("weather_condition", weatherCondition);
                weatherData.put("humidity", humidity);
                weatherData.put("windspeed", windspeed);

                return weatherData;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // retrieves geographic coordinates for given location name
    public static JSONArray getLocationData(String locationName) {
        // replace any whitespace in location name to + to adhere to API's request format
        locationName = locationName.replaceAll(" ", "+");

        // build API url with location parameter
        String urlString = "https://geocoding-api.open-meteo.com/v1/search?name=" + locationName + "&count=10&language=en&format=json";

        try {
            // call API and get a response
            HttpURLConnection conn = fetchApiResponse(urlString);

            // check response status
            // 200 means successful connection
            if(conn.getResponseCode() != 200) {
                System.out.println("Error: Could not connect to API!");
                return null;
            } else {
                // store the API results
                StringBuilder resultJson = new StringBuilder();
                Scanner scanner = new Scanner(conn.getInputStream());

                // read and store the resulting json data into our string builder
                while(scanner.hasNext()) {
                    resultJson.append(scanner.nextLine());
                }

                // close scanner
                scanner.close();

                // close url connection
                conn.disconnect();

                // parse the JSON string into a JSON obj
                JSONParser parser = new JSONParser();
                JSONObject resultsJsonObj = (JSONObject) parser.parse(String.valueOf(resultJson));
                
                // get the list of location data the API generated from the location name
                JSONArray locationData = (JSONArray) resultsJsonObj.get("results");
                return locationData;
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

        // could not find location
        return null;
    }

    private static HttpURLConnection fetchApiResponse(String urlString) {
        try {
            // attempt to create connection
            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            // set request method to get
            conn.setRequestMethod("GET");

            // connect to our API
            conn.connect();
            return conn;
        } catch(IOException e) {
            e.printStackTrace();
        }

        // could not make connection
        return null;
    }

    private static int findIndexOfCurrentTime(JSONArray timeList) {
        String currentTime = getCurrentTime();

        // iterate through the time list and see which one matches our current time
        for (int i = 0; i < timeList.size(); i++){
            String time = (String) timeList.get(i);
            if(time.equalsIgnoreCase(currentTime)) return i;
        }

        return 0;
    }

    public static String getCurrentTime() {
        // get current date and time
        LocalDateTime currentDateTime = LocalDateTime.now();

        // format date to be 2025-09-02T00:00 (this is how is is read in the API)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH':00'");

        // format and print the current date and time
        String formattedDateTime = currentDateTime.format(formatter);

        return formattedDateTime;
    }

    // convert the weather code ot something more readable
    private static String convertWeatherCode(long weatherCode) {
        String weatherCondition = "";

        // clear
        if(weatherCode == 0L) weatherCondition = "Clear";
        // cloudy
        else if(weatherCode <= 3L && weatherCode > 0L) weatherCondition = "Cloudy";
        // foggy
        else if (weatherCode == 45L || weatherCode == 48L) weatherCondition = "Foggy";
        // light drizzle
        else if(weatherCode == 51L || weatherCode == 53L || weatherCode == 55L) weatherCondition = "Light Drizzle";
        // freezing drizzle
        else if(weatherCode == 56L || weatherCode == 57L) weatherCondition = "Freezing Drizzle";
        // rain
        else if(weatherCode == 61L || weatherCode == 63L || weatherCode == 65L) weatherCondition = "Rain";
        // freezing rain
        else if(weatherCode == 66L || weatherCode == 67L) weatherCondition = "Freezing Rain";
        // snow fall
        else if(weatherCode == 71L || weatherCode == 73L || weatherCode == 75L || weatherCode == 77L) weatherCondition = "Snow Fall";
        // rain showers
        else if(weatherCode == 80L || weatherCode == 81L || weatherCode == 82L) weatherCondition = "Rain Showers";
        // snow showers
        else if(weatherCode == 85L || weatherCode == 86L) weatherCondition = "Snow Showers";
        // thunderstorm  
        else if(weatherCode == 95L || weatherCode == 96L || weatherCode == 99L) weatherCondition = "Thunderstorms";

        return weatherCondition;
    }
}
