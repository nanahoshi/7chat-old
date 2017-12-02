package utils.api;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
public class MecabApiClient extends HttpApiClient {
    public MecabApiClient(){
        super();
    }

    public JsonNode parse(String context){
        final String url = "https://ai.nanahoshi.org/mecab/";//?address=1600+Amphitheatre+Parkway,+Mountain+View,+CA&key=YOUR_API_KEY"

        try{
            JsonNode json = getJson(url + URLEncoder.encode(context,"UTF-8"));
            return json;
        }catch(UnsupportedEncodingException e){

            return null;
        }
    }
}
