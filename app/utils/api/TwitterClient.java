package utils.api;

import play.*;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterClient{
    public Boolean tweet(String text){
        try{
        ConfigurationBuilder configInfo = new ConfigurationBuilder();
        configInfo.setDebugEnabled(true)
            .setOAuthConsumerKey(Play.application().configuration().getString("twitter.consumerKey"))
            .setOAuthConsumerSecret(Play.application().configuration().getString("twitter.consumerKeySecret"))
            .setOAuthAccessToken(Play.application().configuration().getString("twitter.accessToken"))
            .setOAuthAccessTokenSecret(Play.application().configuration().getString("twitter.accessTokenSecret"));

        TwitterFactory tf = new TwitterFactory(configInfo.build());
        
        Twitter twitter = tf.getInstance();
        twitter.updateStatus(text);

        }catch(TwitterException e){
            play.Logger.error("Twitter Client has some error.Please see the below.",e);
            return false;
        }
        finally{
            return true;
        }
    }
}