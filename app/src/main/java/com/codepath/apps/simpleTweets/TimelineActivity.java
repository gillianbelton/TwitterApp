package com.codepath.apps.simpleTweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.codepath.apps.simpleTweets.fragments.HomeTimelineFragment;
import com.codepath.apps.simpleTweets.fragments.MentionsTimelineFragment;
import com.codepath.apps.simpleTweets.fragments.TweetsListFragment;
import com.codepath.apps.simpleTweets.models.Tweet;
import com.codepath.apps.simpleTweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class TimelineActivity extends AppCompatActivity {

    private TweetsListFragment fragmentTweetsList;
    private int REQUEST_CODE = 60;
    SmartFragmentStatePagerAdapter adapterViewPager;
    TwitterClient client;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);

        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager);
        adapterViewPager = new TweetsPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        //PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs);
        //tabStrip.setViewPager(vpPager);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.ic_twitter_icon);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        client = TwitterApplication.getRestClient();

        client.getMyInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJSON(response);
                Log.d("DEBUG", "USER WAS NULL");
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                Log.d("DEBUG","went to on failure");
                Log.d("DEBUG",errorResponse.toString());
            }
        });

    }

    public void onProfileView(MenuItem item) {
        Intent i = new Intent(this, ProfileActivity.class);
        startActivity(i);

    }

    public void goToProfile(View view) {
        Intent i = new Intent(this, ProfileActivity.class);
        //String screenName =
        //i.putExtra("screen_name", screenName );
        startActivity(i);

    }

    /*
    public void retweet(View view){
        client.createRetweet(Tweet tweet,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Log.d("DEBUG", "DID THIS WORK?");
                HomeTimelineFragment home = (HomeTimelineFragment) adapterViewPager.getRegisteredFragment(0);
                home.appendTweet(tweet);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //Log.d("DEBUG", errorResponse.toString());
            }
        });
    }
    */

    public void onClickComposeTweet(MenuItem item) {
        Intent i = new Intent(this, ComposeActivity.class);
        i.putExtra("user", user);
        startActivityForResult(i, REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && RESULT_OK == resultCode){
            Tweet tweet = (Tweet) data.getSerializableExtra("tweet");
            HomeTimelineFragment home = (HomeTimelineFragment) adapterViewPager.getRegisteredFragment(0);
            home.appendTweet(tweet);
        }
        else{

        }

    }

    //return order of fragemtns in viewpager
    public class TweetsPagerAdapter extends SmartFragmentStatePagerAdapter{
        final int PAGE_COUNT = 2;
        private String tabTitles[] = {"Home","Mentions"};

        public TweetsPagerAdapter(FragmentManager fm){
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if (position ==0){
                return new HomeTimelineFragment();
            }
            else if (position == 1){
                return new MentionsTimelineFragment();
            }
            else {
                return null;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return tabTitles.length;
        }
    }

    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_timeline, menu);
        return true;
    }
}
