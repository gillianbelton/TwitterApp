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
 * Created by gbelton on 6/28/16.
 */
public class UserTimelineFragment extends TweetsListFragment {

    private TwitterClient client;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();
        populateTimeline();
    }


    public static UserTimelineFragment newInstance(String screen_name){

        UserTimelineFragment userFragment = new UserTimelineFragment();
        Bundle args = new Bundle();
        args.putString("screen_name",screen_name);
        userFragment.setArguments(args);
        return userFragment;
    }

    protected void populateTimeline(){
        String screenName = getArguments().getString("screen_name");
        client.getUserTimeline(screenName, new JsonHttpResponseHandler(){
            //success
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                Log.d("DEBUG", response.toString());
                ArrayList<Tweet> tweets = Tweet.fromJSONArray(response);
                addAll(tweets);
                //aTweets.addAll(tweets);
            }

            //failure
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG",errorResponse.toString());
            }
        });
    }

}
