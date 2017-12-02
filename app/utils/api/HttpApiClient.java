package utils.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import play.Logger;

import java.io.IOException;
import java.util.List;

public abstract class HttpApiClient {
    private Config config = null;

    protected HttpApiClient(){
       config =  ConfigFactory.load();
    }

    protected final Config config(){
        return config;
    }

    protected JsonNode getJson(String url){
        return getJson(url,null);
    }
    protected JsonNode getJson(String url, List parameters){

        CloseableHttpClient client = HttpClients.createDefault();

        HttpGet get = null;
        if(parameters != null) {
            String query = URLEncodedUtils.format(parameters, "UTF-8");
            get = new HttpGet(url + "?" + query);
        }
        else{
            get = new HttpGet(url);
        }
        try {
            CloseableHttpResponse response = client.execute(get);
            int sc = response.getStatusLine().getStatusCode();
            if(sc != 200){
                Logger.error("API : " + url + "\n" + "\t" + "The request has not been completed by " + sc);
                return null;
            }
            HttpEntity entity = response.getEntity();
            String jsonText = EntityUtils.toString(entity, "UTF-8");
            ObjectMapper mapper = new ObjectMapper();
            JsonNode json = mapper.readTree(jsonText);
            return json;
        }catch(IOException e){
            Logger.error("API : " + url + "\n\t" + "The request has not been competed.",e);
            return null;
        }
    }
}
