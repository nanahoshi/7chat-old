package utils.dialogflow.action;

import com.fasterxml.jackson.databind.JsonNode;
import utils.api.MecabApiClient;
import utils.api.WeatherApiClient;
import utils.api.GoogleMapApiClient;
import utils.context.TweetGenerator;
import models.Conversation;

import java.text.SimpleDateFormat;
import java.util.*;

public class DialogFlowChatAction extends DialogFlowAction{

    Random rand = new Random();
    public JsonNode run(JsonNode root){
        String context = root.get("result").get("resolvedQuery").asText();

        TweetGenerator tweet = new TweetGenerator();

        MecabApiClient mecab = new MecabApiClient();
        JsonNode parseNode = mecab.parse(context);//.get("results");
        if(parseNode == null){
            String ret = tweet.tweet();
            writeLog(root,ret);
            return makeReply(ret);
        }
        parseNode = parseNode.get("results");
        List<String> suggest = new ArrayList<String>();

        for(int i = 0;i<parseNode.size();++i){
            JsonNode result = parseNode.get(i);
            String part = result.get("品詞").asText();
            if(part.equals("名詞") || part.equals("形容詞") || part.equals("感動詞") || part.equals("動詞")){
                String sentence = result.get("原型").asText();
                if(sentence.equals("*")){
                    continue;
                }
                suggest.add(sentence);
            }
        }
        if(suggest.size()!=0){
            String ret = tweet.reply(suggest.get(rand.nextInt(suggest.size())));
            writeLog(root,ret);
            return makeReply(ret);
        }
        String ret = tweet.tweet();
        writeLog(root,ret);
        return makeReply(ret);
    }

    private void writeLog(JsonNode root, String reply){
        Conversation conversation = new Conversation();
        if(root.get("originalRequest") == null){
            return;
        }
        JsonNode data = root.get("originalRequest").get("data");

        conversation.channel = root.get("originalRequest").get("source").asText();
        switch(conversation.channel){
            case "line":
                conversation.userId = data.get("source").get("userId").asText();
                conversation.conversationId = data.get("message").get("id").asText();
            break;
            case "twitter":
                conversation.userId = data.get("user").get("id_str").asText();
                conversation.conversationId = data.get("id_str").asText();
            break;
        }
        conversation.content = root.get("result").get("resolvedQuery").asText();
        conversation.reply = reply;
        conversation.save();
    }
}