package com.codepath.apps.simpleTweets;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpleTweets.models.Tweet;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.List;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

/**
 * Created by gbelton on 6/27/16.
 * apply view holder pattern
 */
public class TweetsArrayAdapter extends ArrayAdapter<Tweet>{

    TwitterClient client = TwitterApplication.getRestClient();



    public TweetsArrayAdapter(Context context, List<Tweet> tweets) {
        super(context, R.layout.item_tweet, tweets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){



        final Tweet tweet = getItem(position);
        //User user = tweet.getUser();

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_tweet,parent,false);
        }
        TextView tvScreenName = (TextView) convertView.findViewById(R.id.tvScreenName);
        ImageView ivProfile = (ImageView) convertView.findViewById(R.id.ivProfileImage);
        TextView tvUserName = (TextView) convertView.findViewById(R.id.tvUsername);
        TextView tvBody = (TextView) convertView.findViewById(R.id.tvBody);

        TextView tvTimeStamp = (TextView) convertView.findViewById(R.id.tvTimeStamp);
        tvBody.setText(tweet.getBody());
        final Button btnRetweet = (Button) convertView.findViewById(R.id.btnRetweet);
        final Button btnFav = (Button) convertView.findViewById(R.id.btnFav);

        //set font
        Typeface helv = Typeface.createFromAsset(getContext().getAssets(), "fonts/HelveticaNeue-Regular.ttf");
        Typeface helv_bold = Typeface.createFromAsset(getContext().getAssets(), "fonts/helvetica-neue-bold.ttf");
        tvScreenName.setTypeface(helv);
        tvBody.setTypeface(helv);

        tvTimeStamp.setTypeface(helv);
        tvUserName.setTypeface(helv_bold);


        //time stamp
        tvTimeStamp.setText(TimeFormatter.getTimeDifference(tweet.getCreatedAt()));

        //assign text and images
        tvScreenName.setText(tweet.getUser().getScreenname());
        tvUserName.setText(tweet.getUser().getName());
        ivProfile.setImageResource(android.R.color.transparent); //clear the old image for a recycle view
        Picasso.with(getContext()).load(tweet.getUser().getProfileImageUrl()).transform(new RoundedCornersTransformation(3, 3)).into(ivProfile);

        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                launchProfileView(tweet);
            }
        });

        if (tweet.isRT()){
            btnRetweet.setBackgroundResource(R.drawable.btn_retweeted);
        }
        else{
            btnRetweet.setBackgroundResource(R.drawable.retweet_btn);
        }

        if (tweet.isFav()){
            btnFav.setBackgroundResource(R.drawable.heart_true_btn);
        }
        else{
            btnFav.setBackgroundResource(R.drawable.heart_false_btn);
        }

        Button btnReply = (Button) convertView.findViewById(R.id.btnReply);

        btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                launchComposeView(tweet);
            }
        });



        btnRetweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                if (!tweet.isRT()) {
                    client.createRetweet(tweet, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d("DEBUG", "DID THIS WORK?");
                            //HomeTimelineFragment home = (HomeTimelineFragment) adapterViewPager.getRegisteredFragment(0);
                            //home.appendTweet(tweet);
                            btnRetweet.setBackgroundResource(R.drawable.btn_retweeted);
                            tweet.isRT = true;


                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("DEBUG", errorResponse.toString());
                        }
                    });
                }
                else{
                        client.unRetweet(tweet, new JsonHttpResponseHandler() {
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                Log.d("DEBUG", "DID THIS WORK?");
                                //HomeTimelineFragment home = (HomeTimelineFragment) adapterViewPager.getRegisteredFragment(0);
                                //home.appendTweet(tweet);
                                btnRetweet.setBackgroundResource(R.drawable.retweet_btn);
                                tweet.isRT = false;
                            }

                            @Override
                            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                                Log.d("DEBUG", errorResponse.toString());
                            }
                        });
                }
            }
        });


        btnFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

                if (!tweet.isFav()) {
                    client.favorite(tweet, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d("DEBUG", "DID THIS WORK?");
                            //HomeTimelineFragment home = (HomeTimelineFragment) adapterViewPager.getRegisteredFragment(0);
                            //home.appendTweet(tweet);
                            btnFav.setBackgroundResource(R.drawable.heart_true_btn);
                            tweet.isFav = true;


                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("DEBUG", errorResponse.toString());
                        }
                    });
                }
                else{
                    client.unFavorite(tweet, new JsonHttpResponseHandler() {
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            Log.d("DEBUG", "DID THIS WORK?");
                            //HomeTimelineFragment home = (HomeTimelineFragment) adapterViewPager.getRegisteredFragment(0);
                            //home.appendTweet(tweet);
                            btnFav.setBackgroundResource(R.drawable.heart_false_btn);
                            tweet.isFav = false;
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                            Log.d("DEBUG", errorResponse.toString());
                        }
                    });
                }
            }
        });




        return convertView;
    }

    public void launchProfileView(Tweet tweet){
        Intent i = new Intent(getContext(),ProfileActivity.class);
        i.putExtra("user", tweet.getUser());
        i.putExtra("screen_name",tweet.getUser().getScreenname());
        getContext().startActivity(i);
    }

    public void launchComposeView(Tweet tweet){
        Intent i = new Intent(getContext(),ComposeActivity.class);
        i.putExtra("screen_name",tweet.getUser().getScreenname());
        getContext().startActivity(i);
    }

/*
    public void retweet(){
        client.createRetweet(tweet,new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, PreferenceActivity.Header[] headers, JSONObject response) {
                //Log.d("DEBUG", "DID THIS WORK?");
                HomeTimelineFragment home = (HomeTimelineFragment) adapterViewPager.getRegisteredFragment(0);
                home.appendTweet(tweet);
            }

            @Override
            public void onFailure(int statusCode, PreferenceActivity.Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //Log.d("DEBUG", errorResponse.toString());
            }
        });
    }
    */


}

