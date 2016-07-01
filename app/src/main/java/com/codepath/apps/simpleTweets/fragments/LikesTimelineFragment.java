package com.codepath.apps.simpleTweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

import com.codepath.apps.simpleTweets.TwitterApplication;
import com.codepath.apps.simpleTweets.TwitterClient;
import com.codepath.apps.simpleTweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by gbelton on 6/30/16.
 */
public class LikesTimelineFragment extends TweetsListFragment{
    TwitterClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        populateTimeline();
    }

    public static LikesTimelineFragment newInstance(String screen_name){

        LikesTimelineFragment likeFrag = new LikesTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name",screen_name);
        likeFrag.setArguments(args);
        return likeFrag;
    }


    protected void populateTimeline(){
        String screenName = getArguments().getString("screen_name");
        client.getLikesTimeline(screenName, new JsonHttpResponseHandler(){
            //success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                ArrayList<Tweet> tweets = Tweet.fromJSONArray(response);
                clear();
                addAll(tweets);
            }

            //failure
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG",errorResponse.toString());
            }
        });
    }
}
