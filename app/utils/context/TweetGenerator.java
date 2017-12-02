package utils.context;

import models.Context;
import play.libs.Json;
import utils.api.TwitterClient;
import utils.word2vector.ContextAnalysisSingleton;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class TweetGenerator {

    Random rand = new Random();
    private final int MAX_STR_SIZE = 50;

    public String reply(String context){
        ContextAnalysisSingleton vec = ContextAnalysisSingleton.getInstance();

        Collection<String> near = vec.getNearest(context,10);
        List<String> nearAry = new ArrayList(near);
        if(nearAry.size()<10){
            return tweet();
        }

        String ret_tweet = "";
        // 文章の起点を抽出する。
        List<Context> start = Context.find.query().where()
                .eq("is_able_to_begin",true)
                //.eq("previous_keyword",nearAry.get(rand.nextInt(nearAry.size())))
                .in("previous_keyword",nearAry)
                .orderBy().desc("frequency")
                .setMaxRows(50).findList();
        if(start.size()<1){
            return tweet();
        }
        // 試すのは最大3回にしましょう
        for(int i = 0;i < 3; ++i){
            int num = rand.nextInt(start.size());
            ret_tweet = start.get(num).previousKeyword + start.get(num).currentKeyword;

            String keyPreviousKeyword = start.get(num).currentKeyword;
            String keyPreviousKeywordType = start.get(num).currentKeywordType;
            String keyCurrentKeyword = start.get(num).afterKeyword;
            String keycurrentKeywordType = start.get(num).afterKeywordType;
            boolean isEndFlg = false;

            do{
                List<Context> node = Context.find.query().where()
                        .eq("previous_keyword",keyPreviousKeyword)
                        .eq("previous_keyword_type",keyPreviousKeywordType)
                        .eq("current_keyword",keyCurrentKeyword)
                        .eq("current_keyword_type",keycurrentKeywordType)
                        .orderBy().desc("frequency")
                        .setMaxRows(50).findList();
                if(node.isEmpty()){
                    isEndFlg = true;
                    break;
                }
                num = rand.nextInt(node.size());

                //ret_tweet = ret_tweet + node.get(num).previousKeyword + node.get(num).currentKeyword;
                ret_tweet = ret_tweet + node.get(num).afterKeyword;

                if(node.get(num).isEnd){

                    isEndFlg = true;
                    break;
                }
                keyPreviousKeyword = node.get(num).currentKeyword;
                keyPreviousKeywordType = node.get(num).currentKeywordType;
                keyCurrentKeyword = node.get(num).afterKeyword;
                keycurrentKeywordType = node.get(num).afterKeywordType;

            }while(ret_tweet.length() < MAX_STR_SIZE);
            if(isEndFlg){
                break;
            }
        }
        return ret_tweet;
    }

    public String tweet(){
        List<String> acceptable_type = new ArrayList<String>();
        acceptable_type.add("固有名詞");
        acceptable_type.add("サ変接続");
        acceptable_type.add("形容動詞語幹");
        acceptable_type.add("代名詞");

        String ret_tweet = "";
        // 文章の起点を抽出する。
        List<Context> start = Context.find.query().where()
                .eq("is_able_to_begin",true)
                .in("previous_keyword_type",acceptable_type)
                .orderBy().desc("updated_at")
                .setMaxRows(100).findList();

        // 試すのは最大3回にしましょう
        for(int i = 0;i < 3; ++i){
            int num = rand.nextInt(start.size());
            ret_tweet = start.get(num).previousKeyword + start.get(num).currentKeyword;

            String keyPreviousKeyword = start.get(num).currentKeyword;
            String keyPreviousKeywordType = start.get(num).currentKeywordType;
            String keyCurrentKeyword = start.get(num).afterKeyword;
            String keycurrentKeywordType = start.get(num).afterKeywordType;
            boolean isEndFlg = false;

            do{
                List<Context> node = Context.find.query().where()
                        .eq("previous_keyword",keyPreviousKeyword)
                        .eq("previous_keyword_type",keyPreviousKeywordType)
                        .eq("current_keyword",keyCurrentKeyword)
                        .eq("current_keyword_type",keycurrentKeywordType)
                        .orderBy().desc("frequency")
                        .setMaxRows(50).findList();
                if(node.isEmpty()){
                    isEndFlg = true;
                    break;
                }
                num = rand.nextInt(node.size());

                //ret_tweet = ret_tweet + node.get(num).previousKeyword + node.get(num).currentKeyword;
                ret_tweet = ret_tweet + node.get(num).afterKeyword;

                if(node.get(num).isEnd){

                    isEndFlg = true;
                    break;
                }
                keyPreviousKeyword = node.get(num).currentKeyword;
                keyPreviousKeywordType = node.get(num).currentKeywordType;
                keyCurrentKeyword = node.get(num).afterKeyword;
                keycurrentKeywordType = node.get(num).afterKeywordType;

            }while(ret_tweet.length() < MAX_STR_SIZE);
            if(isEndFlg){
                break;
            }
        }
        return ret_tweet;
    }
}
