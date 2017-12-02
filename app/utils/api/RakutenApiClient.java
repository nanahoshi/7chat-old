package utils.api;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.message.BasicNameValuePair;
import play.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RakutenApiClient extends HttpApiClient{
    private String rakutenApiKey = "";
    private String rakutenAffiliateKey ="";

    public class Hotel{
        public String name;
        public String url;
        public String imageUrl;
    }

    public RakutenApiClient() {
        super();
        rakutenApiKey = config().getString("rakuten.api.id");
        rakutenAffiliateKey = config().getString("rakuten.affiliate.id");
    }
    public Hotel requestHotelwithVacant(String latitude, String longitude, String periodFrom, String periodTo, Integer adultNum, Integer maxCharge) {
        final String url = "https://app.rakuten.co.jp/services/api/Travel/VacantHotelSearch/20170426";


        List parameters = new ArrayList();
        parameters.add(new BasicNameValuePair("format", "json"));

        parameters.add(new BasicNameValuePair("applicationId", rakutenApiKey));
        parameters.add(new BasicNameValuePair("affiliateId", rakutenAffiliateKey));

        parameters.add(new BasicNameValuePair("checkinDate", periodFrom));
        parameters.add(new BasicNameValuePair("checkoutDate", periodTo));

        parameters.add(new BasicNameValuePair("datumType", "1"));
        parameters.add(new BasicNameValuePair("latitude", latitude));
        parameters.add(new BasicNameValuePair("longitude", longitude));

        if(adultNum != null){
            parameters.add(new BasicNameValuePair("adultNum", adultNum.toString()));
        }
        if(maxCharge != null){
            parameters.add(new BasicNameValuePair("maxCharge", maxCharge.toString()));
        }
        JsonNode root = getJson(url,parameters);
        if(root.get("error") != null){
            Logger.info("Data was not found ",root.get("error").asText());
            return null;
        }
        JsonNode hotelAray = root.get("hotels");
        // 今回のホテルはここにするか
        Random rnd = new Random();
        int num = rnd.nextInt(hotelAray.size()-1);

        Hotel hotel = new Hotel();

        hotel.name = hotelAray.get(num).get("hotel").get(0).get("hotelBasicInfo").get("hotelName").asText();
        hotel.url  = hotelAray.get(num).get("hotel").get(0).get("hotelBasicInfo").get("planListUrl").asText();
        hotel.imageUrl = hotelAray.get(num).get("hotel").get(0).get("hotelBasicInfo").get("hotelImageUrl").asText();

        return hotel;
    }
}
