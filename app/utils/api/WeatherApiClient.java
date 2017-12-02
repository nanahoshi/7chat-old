package utils.api;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class WeatherApiClient extends HttpApiClient{

    String openWeatherApiKey = "";

    public WeatherApiClient(){
        super();
        openWeatherApiKey = config().getString("openweather.api.key");
    }
    public class Weather{
        public Weather(){
        }
        public Weather(String dt_txt, String weather,String description,String minTemperture,String maxTemperture,String humidity,String pressure){
            this.dt_txt = dt_txt;
            this.weather = weather;
            this.weatherDescription = description;
            this.minTemperature = minTemperture;
            this.maxTemperature = maxTemperture;
            this.humidity = humidity;
            this.pressure = pressure;
        }
        public String dt_txt;

        public String weather;
        public String weatherDescription;

        public String minTemperature;
        public String maxTemperature;

        public String humidity;
        public String pressure;
    }

    public ArrayList<Weather> request5DaysForecast(String latitude, String longitude){
        final String url = "https://api.openweathermap.org/data/2.5/forecast";
        ArrayList<Weather> ret_list = new ArrayList<>();

        List parameters = new ArrayList();
        parameters.add(new BasicNameValuePair("APPID", openWeatherApiKey));
        parameters.add(new BasicNameValuePair("lat", latitude));
        parameters.add(new BasicNameValuePair("lon", longitude));

        parameters.add(new BasicNameValuePair("units", "metric"));
        parameters.add(new BasicNameValuePair("lang", "ja"));

        JsonNode root = getJson(url,parameters);
        for(int i = 0;i<root.get("list").size();++i){
            JsonNode leaf = root.get("list").get(i);
            Weather weather = new Weather();
            weather.dt_txt = leaf.get("dt_txt").asText();
            JsonNode weather_node = leaf.get("weather").get(0);
            if(weather_node != null){
                weather.weather = weather_node.get("main").asText();
                weather.weatherDescription = weather_node.get("description").asText();
            }
            JsonNode main_node = leaf.get("main");
            if(main_node != null){
                weather.minTemperature = main_node.get("temp_min").asText();
                weather.maxTemperature = main_node.get("temp_max").asText();
                weather.humidity = main_node.get("humidity").asText();
                weather.pressure = main_node.get("pressure").asText();
            }
            ret_list.add(weather);
        }
        return ret_list;
    }
}
