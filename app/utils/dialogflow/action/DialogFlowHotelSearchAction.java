package utils.dialogflow.action;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.primitives.Ints;
import utils.api.GoogleMapApiClient;
import utils.api.RakutenApiClient;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class DialogFlowHotelSearchAction extends DialogFlowAction {
    @Override
    public JsonNode run(JsonNode root) {
        HashMap<String,String> params = getRequestParams(root);
        // 緯度経度を取得する。
        // location には色んなパターンで入っているので細かそうな所から順番にあたる。
        String location_business_name = params.get("location.business-name");
        String location_street_address = params.get("location.street-address");
        String location_station = params.get("location.station");
        String location_subadmin = params.get("location.subadmin-area");
        String location_city = params.get("location.city");

        String location = "";
        if(location_city != null){
            location = location_city;
        }
        if(location_subadmin != null){
            location = location_subadmin;
        }
        if(location_station != null){
            location = location_city;
        }
        if(location_street_address != null){
            location = location_business_name;
        }
        if(location_business_name != null){
            location = location_business_name;
        }

        GoogleMapApiClient googleMap = new GoogleMapApiClient();
        GoogleMapApiClient.GeoLocation locate = googleMap.requestGeocodingApi(location);

        // 日にち
        String period = params.get("date-period");
        String[] term = period.split("/");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            //Date from = sdf.parse(term[0]);
            Calendar to = Calendar.getInstance();
            to.setTime(sdf.parse(term[1]));
            if(term[0].equals(term[1])) {
                to.add(Calendar.DATE, 1);
                term[1] = sdf.format(to.getTime());
            }
        }catch (Exception e){

        }
        Integer adultNum = null;
        adultNum = Integer.parseInt(params.get("adultNum"));
        Integer maxCharge = null;
        if(params.get("maxCharge") != null && params.get("maxCharge").length()>0)
        {
            maxCharge = Integer.parseInt(params.get("maxCharge"));
        }


        RakutenApiClient rakuten = new RakutenApiClient();
        RakutenApiClient.Hotel hotel = rakuten.requestHotelwithVacant(locate.latitude,locate.longitude,term[0],term[1],adultNum,maxCharge);
        if(hotel != null) {
            return makeReply("こことかどー？？  " + hotel.name + "  " + hotel.url + "  " + hotel.imageUrl);
        }else{
            return makeReply("ごめん！探したんだけどその日時でホテル空いていないみたい。違う場所とかはないかな？");
        }
    }
}
