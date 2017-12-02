package utils.dialogflow.action;

import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;

import java.util.*;

public abstract class DialogFlowAction {
    public abstract JsonNode run(JsonNode root);

    protected HashMap<String,String> getRequestParams(JsonNode root){
        HashMap<String,String> ret = new HashMap<String,String>();

        Iterator<Map.Entry<String, JsonNode>> fields = root.get("result").get("parameters").fields();
        while(fields.hasNext()){
            Map.Entry<String, JsonNode> next = fields.next();
            if("location".equals(next.getKey())){
                // 更に深掘りする。
                Iterator<Map.Entry<String,JsonNode>> location = next.getValue().fields();
                while(location.hasNext()){
                    Map.Entry<String,JsonNode> loc = location.next();
                    ret.put("location." + loc.getKey(),loc.getValue().asText());
                }
            }else {
                ret.put(next.getKey(), next.getValue().asText());
            }
        }

        return ret;
    }
    protected JsonNode makeReply(String speech, String displayText){
//        List parameters = new ArrayList();
//        parameters.add(new BasicNameValuePair("speech", speech));
//        parameters.add(new BasicNameValuePair("displayText", displayText));
        HashMap<String,String> ret = new HashMap<>();
        ret.put("speech",speech);
        ret.put("displayText",displayText);

        return Json.toJson(ret);
    }

    protected JsonNode makeReply(String speech){
        return this.makeReply(speech,speech);
    }

    protected String chooseOne(String[] ary){
        Random rand = new Random();
        return ary[rand.nextInt(ary.length-1)];
    }
}