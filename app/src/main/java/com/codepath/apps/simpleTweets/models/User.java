package com.codepath.apps.simpleTweets.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by gbelton on 6/27/16.
 */
public class User implements Serializable {
    private String name;

    public String getTagline() {
        return tagLine;
    }



    public int getFollowingCount() {
        return followingCount;
    }

    private long uid;

    public String getName() {
        return name;
    }

    public long getUid() {
        return uid;
    }

    public String getScreenname() {
        return screenname;
    }

    public String getCoverPhotoUrl() {
        return coverPhotoUrl;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    private String screenname;
    private String profileImageUrl;
    private String tagLine;
    private double followersCount;
    private int followingCount;
    private String coverPhotoUrl;


    public static User fromJSON(JSONObject jsonObject){
        User u = new User();

        try {
            u.name = jsonObject.getString("name");
            u.profileImageUrl = jsonObject.getString("profile_image_url");
            u.uid = jsonObject.getLong("id");
            u.coverPhotoUrl =jsonObject.optString("profile_banner_url");
            u.screenname = jsonObject.getString("screen_name");
            u.tagLine = jsonObject.getString("description");
            u.followersCount = jsonObject.getInt("followers_count");
            u.followingCount = jsonObject.getInt("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return u;
    }


    public String getFollowersCount() {

        if (followersCount< 1000) {
            int follow =(int) followersCount;
            return Integer.toString(follow);
        }
        else if (followersCount <10000){
            followersCount = round(followersCount/1000, 1);
            Double.toString(followersCount);
            return (followersCount + "k");
        }
        else if (followersCount < 100000){
            followersCount = round(followersCount/1000, 1);
            Double.toString(followersCount);
            return (followersCount + "k");
        }
        else if (followersCount <1000000){
            followersCount = round(followersCount/1000, 1);
            Double.toString(followersCount);
            return (followersCount + "k");
        }
        else if (followersCount <10000000){
            followersCount = round(followersCount/1000000, 2);
            Double.toString(followersCount);
            return (followersCount + "m");
        }
        else if (followersCount < 100000000){

            followersCount = round(followersCount/1000000, 1);
            Double.toString(followersCount);
            return (followersCount + "m");
        }
        else{
            return Double.toString(followersCount);
        }
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


}
