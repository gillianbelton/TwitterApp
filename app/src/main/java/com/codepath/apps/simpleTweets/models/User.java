package com.codepath.apps.simpleTweets.models;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Created by gbelton on 6/27/16.
 */
public class User implements Serializable {
    private String name;

    public String getTagline() {
        return tagLine;
    }

    public int getFollowersCount() {
        return followersCount;
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
    private int followersCount;
    private int followingCount;
    private String coverPhotoUrl;


    public static User fromJSON(JSONObject jsonObject){
        User u = new User();

        try {
            u.name = jsonObject.getString("name");
            u.profileImageUrl = jsonObject.getString("profile_image_url");
            u.uid = jsonObject.getLong("id");
            u.coverPhotoUrl =jsonObject.getString("profile_banner_url");
            u.screenname = jsonObject.getString("screen_name");
            u.tagLine = jsonObject.getString("description");
            u.followersCount = jsonObject.getInt("followers_count");
            u.followingCount = jsonObject.getInt("friends_count");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return u;
    }


}
