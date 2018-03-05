package com.upmc.tomo.tp_alt_mozaigram.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;
import java.util.ArrayList;
/**
 * Created by Tomo on 22/02/2018.
 */

public class User {

    String username;
    String sessionKey;

    List<String> imageURLs;
    List<String> friendsUsername;
    List<String> friendRequests;

    public User(String username, String sessionKey, JSONArray imageURLs, JSONArray friendsUsername, JSONArray friendRequests) throws JSONException {
        Log.d("USER", "creating new user...");
        this.username = username;
        this.sessionKey = sessionKey;
        this.imageURLs = new ArrayList<String>();
        this.friendsUsername = new ArrayList<String>();
        this.friendRequests = new ArrayList<String>();

        for(int i = 0; i < imageURLs.length(); i++) {
            this.imageURLs.add(imageURLs.getString(i));
            Log.d("USER", imageURLs.getString(i));
        }
        for(int i = 0; i < imageURLs.length(); i++)
            this.friendsUsername.add(imageURLs.getString(i));
        for(int i = 0; i < imageURLs.length(); i++)
            this.friendRequests.add(imageURLs.getString(i));
    }

    public String getUsername() { return this.username; }
    public String getSessionKey() { return this.sessionKey; }
    public List<String> getImageURLs() { return this.imageURLs; }
    public List<String> getFriendsUsername() { return this.friendsUsername; }
    public List<String> getFriendRequests() { return this.friendRequests; }

    public void setUsername(String username) { this.username = username; }
    public void setSessionKey(String sessionKey) { this.sessionKey = sessionKey; }
    public void setImageURLs(List<String> imageURLs) { this.imageURLs = imageURLs; }
    public void setFriendsUsername(List<String> friendsUsername) { this.friendsUsername = friendsUsername; }
    public void setFriendRequests(List<String> friendRequests) { this.friendRequests = friendRequests; }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", sessionKey='" + sessionKey + '\'' +
                ", imageURLs=" + imageURLs +
                ", friendsUsername=" + friendsUsername +
                ", friendRequests=" + friendRequests +
                '}';
    }
}
