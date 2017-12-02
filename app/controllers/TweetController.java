package controllers;

import models.Context;
import play.mvc.Controller;
import play.mvc.Result;
import play.libs.Json;

import java.util.*;
import java.util.Random;
import utils.api.TwitterClient;
import utils.context.TweetGenerator;

import utils.word2vector.ContextAnalysisSingleton;

import javax.inject.Inject;

public class TweetController extends Controller{
    Random rand = new Random();
    private final int MAX_STR_SIZE = 100;

    public Result tweetByKeyword(String keyword){
        TweetGenerator generator = new TweetGenerator();
        String ret_tweet = generator.reply(keyword);
        
        TwitterClient client = new TwitterClient();
        client.tweet(ret_tweet);

        //List<Context> list = Context.find.query().setMaxRows(50).findList();
        return ok(Json.toJson(ret_tweet));
    }

    public Result tweet() {
        TweetGenerator generator = new TweetGenerator();
        String ret_tweet = generator.tweet();
        TwitterClient client = new TwitterClient();
        client.tweet(ret_tweet);

        //List<Context> list = Context.find.query().setMaxRows(50).findList();
		return ok(Json.toJson(ret_tweet));
	}
}