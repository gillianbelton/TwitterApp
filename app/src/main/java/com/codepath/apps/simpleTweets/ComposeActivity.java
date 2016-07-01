package com.codepath.apps.simpleTweets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.codepath.apps.simpleTweets.models.Tweet;
import com.codepath.apps.simpleTweets.models.User;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;
import jp.wasabeef.picasso.transformations.RoundedCornersTransformation;

public class ComposeActivity extends AppCompatActivity {

    TwitterClient client;
    User user;
    TextView tvCharCount;
    EditText etTweet;
    ImageView ivProfilePic;
    String screenname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);
        client = TwitterApplication.getRestClient();
        user = (User) getIntent().getSerializableExtra("user");
        screenname = getIntent().getStringExtra("screen_name");


        client = TwitterApplication.getRestClient();

        client.getMyInfo(new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                user = User.fromJSON(response);
                etTweet = (EditText) findViewById(R.id.etTweet);


                if (screenname != null){
                    etTweet.setText("@"+screenname + " ");
                    etTweet.setSelection(etTweet.length());
                }
                tvCharCount = (TextView) findViewById(R.id.tvCharLimit);
                ivProfilePic = (ImageView) findViewById(R.id.ivProfilePic);
                Picasso.with(getApplicationContext()).load(user.getProfileImageUrl()).transform(new RoundedCornersTransformation(3, 3)).into(ivProfilePic);
                etTweet.addTextChangedListener(mTextEditorWatcher);

                //etTweet.addTextChangedListener(mTextEditorWatcher);
            }
        });



        //getSupportActionBar().setDisplayShowTitleEnabled(false);

    }


    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            tvCharCount.setText(String.valueOf(140-s.length()));
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            tvCharCount.setText(String.valueOf(140-s.length()));
        }

        public void afterTextChanged(Editable s) {
            tvCharCount.setText(String.valueOf(140-s.length()));
        }
    };

    public void onCancelCompose(View view){
        Intent i = new Intent();
        setResult(RESULT_CANCELED,i);
        finish();
    }

    public void composeTweet(View view) {
        //Log.d("DEBUG", "DID THIS WORK COMPOSE!?");
        EditText etTweet = (EditText) findViewById(R.id.etTweet);
        String tweet = etTweet.getText().toString();
        client.composeTweet(tweet, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Log.d("DEBUG", "DID THIS WORK?");
                Tweet tweet = Tweet.fromJSON(response);
                Intent i = new Intent();
                i.putExtra("tweet", tweet);
                setResult(RESULT_OK,i);
                finish();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                //Log.d("DEBUG", errorResponse.toString());
            }
        });
    }
}
