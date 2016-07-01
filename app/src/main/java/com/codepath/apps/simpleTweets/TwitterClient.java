package com.codepath.apps.simpleTweets;

import android.content.Context;

import com.codepath.apps.simpleTweets.models.Tweet;
import com.codepath.oauth.OAuthBaseClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.scribe.builder.api.Api;
import org.scribe.builder.api.TwitterApi;

/*
 * 
 * This is the object responsible for communicating with a REST API. 
 * Specify the constants below to change the API being communicated with.
 * See a full list of supported API classes: 
 *   https://github.com/fernandezpablo85/scribe-java/tree/master/src/main/java/org/scribe/builder/api
 * Key and Secret are provided by the developer site for the given API i.e dev.twitter.com
 * Add methods for each relevant endpoint in the API.
 * 
 * NOTE: You may want to rename this object based on the service i.e TwitterClient or FlickrClient
 * 
 */
public class TwitterClient extends OAuthBaseClient {
	public static final Class<? extends Api> REST_API_CLASS = TwitterApi.class; // Change this
	public static final String REST_URL = "https://api.twitter.com/1.1"; // Change this, base API URL
	public static final String REST_CONSUMER_KEY = "A9o0nyZDkX3Yh6uic7WQk2TU2";       // Change this
	public static final String REST_CONSUMER_SECRET = "Kfd9Ip2zo6i83XMo8hNfdWrgHspoluwmQYfiS3Cj1ASiMTSGVz"; // Change this
	public static final String REST_CALLBACK_URL = "oauth://cpsimpletweets"; // Change this (here and in manifest)



	public TwitterClient(Context context) {
		super(context, REST_API_CLASS, REST_URL, REST_CONSUMER_KEY, REST_CONSUMER_SECRET, REST_CALLBACK_URL);
	}


	public void  getHomeTimeline(AsyncHttpResponseHandler handler){
		String apiURL = getApiUrl("statuses/home_timeline.json");

		//specify params
		RequestParams params = new RequestParams();

		params.put("count", 25);
		params.put("since_id",1);
		//Execute request:

		getClient().get(apiURL,params,handler);

	}

	public void composeTweet(String tweet, JsonHttpResponseHandler jsonHttpResponseHandler){
        String apiURL = getApiUrl("statuses/update.json");
        RequestParams params = new RequestParams();
        params.put("status", tweet);

        getClient().post(apiURL,params,jsonHttpResponseHandler);
	}

	public void getMentionsTimeline(JsonHttpResponseHandler jsonHttpResponseHandler) {

		String apiURL = getApiUrl("statuses/mentions_timeline.json");

		//specify params
		RequestParams params = new RequestParams();
		params.put("count", 25);
		//Execute request:

		getClient().get(apiURL,params,jsonHttpResponseHandler);

	}

	public void getUserTimeline(String screenName, AsyncHttpResponseHandler handler){
		String apiURL = getApiUrl("statuses/user_timeline.json");
        RequestParams params = new RequestParams();
        params.put("screen_name", screenName);
        params.put("count", 25);
        getClient().get(apiURL,params,handler);
	}

    public void getLikesTimeline(String screenname, AsyncHttpResponseHandler handler){
        String apiURL = getApiUrl("favorites/list.json");
        RequestParams params = new RequestParams();
        params.put("screen_name", screenname);
        getClient().get(apiURL,params,handler);
    }

	public void getMyInfo(AsyncHttpResponseHandler handler) {
        String apiURL = getApiUrl("account/verify_credentials.json");
        getClient().get(apiURL, null, handler);
    }

    public void getUserInfo(String screenname, AsyncHttpResponseHandler handler){
        String apiURL = getApiUrl("users/show.json");
        RequestParams params = new RequestParams();
        params.put("screen_name", screenname);
        getClient().get(apiURL,params,handler);
    }

	public void createRetweet(Tweet tweet, JsonHttpResponseHandler jsonHttpResponseHandler){
        String apiURL = getApiUrl("statuses/retweet.json");
        RequestParams params = new RequestParams();
        params.put("id", tweet.getUid());
        getClient().post(apiURL,params,jsonHttpResponseHandler);
    }

    public void unRetweet(Tweet tweet, JsonHttpResponseHandler jsonHttpResponseHandler){
        String apiURL = getApiUrl("statuses/unretweet.json");
        RequestParams params = new RequestParams();
        params.put("id", tweet.getUid());
        getClient().post(apiURL,params,jsonHttpResponseHandler);
    }

    public void favorite(Tweet tweet, JsonHttpResponseHandler jsonHttpResponseHandler){
        String apiURL = getApiUrl("favorites/create.json");
        RequestParams params = new RequestParams();
        params.put("id", tweet.getUid());
        getClient().post(apiURL,params,jsonHttpResponseHandler);
    }

    public void unFavorite(Tweet tweet, JsonHttpResponseHandler jsonHttpResponseHandler){
        String apiURL = getApiUrl("favorites/destroy.json");
        RequestParams params = new RequestParams();
        params.put("id", tweet.getUid());
        getClient().post(apiURL,params,jsonHttpResponseHandler);
    }


	//compose tweet: later...

	/* 1. Define the endpoint URL with getApiUrl and pass a relative path to the endpoint
	 * 	  i.e getApiUrl("statuses/home_timeline.json");
	 * 2. Define the parameters to pass to the request (query or body)
	 *    i.e RequestParams params = new RequestParams("foo", "bar");
	 * 3. Define the request method and make a call to the client
	 *    i.e client.get(apiUrl, params, handler);
	 *    i.e client.post(apiUrl, params, handler);
	 */
}