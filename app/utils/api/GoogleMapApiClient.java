package utils.api;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class GoogleMapApiClient extends HttpApiClient {
    private String GoogleMapApiKey = "";
    public GoogleMapApiClient(){
        super();
        GoogleMapApiKey = config().getString("google.map.api.key");
    }

    public class GeoLocation{
        public GeoLocation(String latitude, String longitude){
            this.latitude = latitude;
            this.longitude = longitude;
        }
        public String latitude = "";
        public String longitude = "";
    }

    public GeoLocation requestGeocodingApi(String where){
        final String url = "https://maps.googleapis.com/maps/api/geocode/json";//?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=YOUR_API_KEY"

        List parameters = new ArrayList();
        parameters.add(new BasicNameValuePair("address", where));
        parameters.add(new BasicNameValuePair("key", GoogleMapApiKey));

        JsonNode json = getJson(url,parameters);
        JsonNode geo = json.get("results").get(0).get("geometry").get("location");
        return new GeoLocation(geo.get("lat").asText(),geo.get("lng").asText());
    }
}
