package com.codepath.apps.simpleTweets;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.codepath.apps.simpleTweets.fragments.LikesTimelineFragment;
import com.codepath.apps.simpleTweets.fragments.MentionsTimelineFragment;
import com.codepath.apps.simpleTweets.fragments.UserTimelineFragment;
import com.codepath.apps.simpleTweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;


    //public boolean isMe;

public class ProfileActivity extends AppCompatActivity {
    TwitterClient client;
    User user;
    SmartFragmentStatePagerAdapter adapterViewPager;
    String screenname;
    String defaultUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        defaultUrl = "https://s.graphiq.com/sites/default/files/2307/media/images/t2/Twitter_Blue_1409382.png";


        client = TwitterApplication.getRestClient();
        user = (User) getIntent().getSerializableExtra("user");


        //get the screenname from activity that launches this

        screenname = getIntent().getStringExtra("screen_name");

        loadUserInfo(screenname);

        ViewPager vpPager = (ViewPager) findViewById(R.id.viewpager2);
        adapterViewPager = new TweetsPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
        PagerSlidingTabStrip tabStrip = (PagerSlidingTabStrip) findViewById(R.id.tabs2);
        tabStrip.setViewPager(vpPager);
    /*
        if (savedInstanceState == null){
            //create user timeline fragment
            LikesTimelineFragment likesFragment = LikesTimelineFragment.newInstance(screenname);
            //display user fragment within this activity (dynamic)
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flContainer, likesFragment);
            ft.commit();
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        //if (id == R.id.action_settings){
        //    return true;
        //}
        return super.onOptionsItemSelected(item);
    }


    public void populateProfileHeader(User user){
        TextView tvName = (TextView) findViewById(R.id.tvName);
        TextView tvTagline = (TextView) findViewById(R.id.tvTagline);
        TextView tvFollowers = (TextView) findViewById(R.id.tvFollowers);
        TextView tvFollowing = (TextView) findViewById(R.id.tvFollowing);
        //TextView tvScreenName = (TextView) findViewById(R.id.tvScreenName);
        ImageView ivProfileImage = (ImageView) findViewById(R.id.ivProfileImage);
        ImageView ivCoverImage = (ImageView) findViewById(R.id.ivCoverPic);

        tvName.setText(user.getName());
        //tvScreenName.setText("@"+user.getScreenname());

        tvTagline.setText(user.getTagline());
        tvFollowers.setText(user.getFollowersCount() + " Followers");
        tvFollowing.setText(user.getFollowingCount() + " Following");

        if (user.getCoverPhotoUrl() != null && !user.getCoverPhotoUrl().isEmpty()) {
            Picasso.with(this).load(user.getCoverPhotoUrl()).into(ivCoverImage);
        }
        else{
            //Picasso.with(this).load(defaultUrl).into(ivCoverImage);
            ivCoverImage.setBackgroundColor(Color.rgb(41,156,231));
        }
        Picasso.with(this).load(user.getProfileImageUrl()).transform(new RoundedCornersTransformation(3, 3)).into(ivProfileImage);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return true;
    }

    public class TweetsPagerAdapter extends SmartFragmentStatePagerAdapter{
        final int PAGE_COUNT = 3;

        //MAKE THIS SPECIFIC FOR YOURSELF OR NOT:
        //if(isMe){
        //    String i;
        //}

        private String tabTitles[] = {"Tweets","Likes","Mentions"};

        public TweetsPagerAdapter(FragmentManager fm){
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            if (position ==0){
                return UserTimelineFragment.newInstance(screenname);
            }
            else if (position == 1){
                return LikesTimelineFragment.newInstance(screenname);
            }
            else if (position == 2){
                return new MentionsTimelineFragment();
            }
            else {
                //String hello;
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

    public void loadUserInfo(final String screenname2){

        if(screenname2 != null && !screenname2.isEmpty()){
            client.getUserInfo(screenname2, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    user = User.fromJSON(response);
                    populateProfileHeader(user);

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("DEBUG","went to on failure");
                    Log.d("DEBUG",errorResponse.toString());
                }
                });
        }
        else{
            client.getMyInfo(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    if (user == null) {
                        user = User.fromJSON(response);
                        //isMe = true;
                        screenname = user.getScreenname();
                        Log.d("DEBUG", "USER WAS NULL");
                    } else {
                        Log.d("DEBUG", user.toString());
                    }
                    populateProfileHeader(user);

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                    Log.d("DEBUG","went to on failure");
                    Log.d("DEBUG",errorResponse.toString());
                }
            });
        }
    }

}
