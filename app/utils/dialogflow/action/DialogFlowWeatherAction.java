package utils.dialogflow.action;

import com.fasterxml.jackson.databind.JsonNode;
import utils.api.WeatherApiClient;
import utils.api.GoogleMapApiClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class DialogFlowWeatherAction extends DialogFlowAction{
    public JsonNode run(JsonNode root){

        HashMap<String,String> params = getRequestParams(root);
        // 日付をDate型に変換する。
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date convertedCurrentDate = sdf.parse(params.get("date"));
            date = convertedCurrentDate;
        }catch (Exception e){

        }
        // 住所をGeolocationに変換
        GoogleMapApiClient googleMap = new GoogleMapApiClient();
        GoogleMapApiClient.GeoLocation locate = googleMap.requestGeocodingApi(params.get("geo-city"));
        if(locate != null) {
            WeatherApiClient weatherApiClient = new WeatherApiClient();
            ArrayList<WeatherApiClient.Weather> list = weatherApiClient.request5DaysForecast(locate.latitude, locate.longitude);
            list.removeIf(weather -> !(weather.dt_txt.startsWith(params.get("date")))); // TODO これですが、時差を考慮する。

            if (list.size() > 0) {
                // ここは更に詳しくしていく
                return makeReply(tweet(list.get(0)));//.weatherDescription);
            }
        }
        return makeReply("ごめんね、、実はその日時の天気私にはわからないんだ…。");
    }

    private String tweet(WeatherApiClient.Weather result){
        String ret = chooseOne(new String[]{"天気は「"+  result.weatherDescription +"」だよ！","その日は"+ result.weatherDescription +"みたい。"});
        if(Double.parseDouble(result.maxTemperature) < 10){
            ret += "\n" + chooseOne(new String[]{"冷え込むからお気をつけて。","めっちゃ寒いよね！"});
        }
        if(Math.abs(Double.parseDouble(result.maxTemperature) - Double.parseDouble(result.minTemperature)) > 10){
            ret += "\n" + chooseOne(new String[]{"気温差が激しいので、体調壊さないように。。","気温差あるから、風邪引かないでよね？"});
        }
        if(Double.parseDouble(result.pressure) < 1000){
            ret += "\n" + chooseOne(new String[]{"","","気圧低くなるかもだから、、頭痛薬必須やお！！"});
        }
        return ret;
    }
}