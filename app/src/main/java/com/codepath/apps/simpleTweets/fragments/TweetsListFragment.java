package com.codepath.apps.simpleTweets.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.codepath.apps.simpleTweets.R;
import com.codepath.apps.simpleTweets.TweetsArrayAdapter;
import com.codepath.apps.simpleTweets.TwitterApplication;
import com.codepath.apps.simpleTweets.TwitterClient;
import com.codepath.apps.simpleTweets.models.Tweet;

import java.util.ArrayList;
import java.util.List;

public abstract class TweetsListFragment extends Fragment {

    private TweetsArrayAdapter aTweets;
    private ArrayList<Tweet> tweets;
    private ListView lvTweets;
    protected TwitterClient client;
    private SwipeRefreshLayout swipeContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_tweets_list,parent,false);
        lvTweets = (ListView) v.findViewById(R.id.lvTweets);
        //connect adapter to listview
        lvTweets.setAdapter(aTweets);

        // Lookup the swipe container view
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                populateTimeline();
                swipeContainer.setRefreshing(false);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        return v;
    }

    public void appendTweet(Tweet tweet){
        tweets.add(0,tweet);
        aTweets.notifyDataSetChanged();
        lvTweets.setSelection(0);

    }

    protected abstract void populateTimeline();


    public void clear(){
        aTweets.clear();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        client = TwitterApplication.getRestClient();

        // create the a
        // rray list data source
        tweets = new ArrayList<>();

        //construct the adapter
        aTweets = new TweetsArrayAdapter(getActivity(), tweets);
    }

    public void addAll(List<Tweet> tweets){
        aTweets.addAll(tweets);
        //aTweets.notifyDataSetChanged();
    }





}


