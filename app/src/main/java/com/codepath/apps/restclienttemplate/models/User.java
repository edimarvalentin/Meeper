package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.versionedparcelable.NonParcelField;

import com.codepath.apps.restclienttemplate.MeeperClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;
@Parcel
public class User {

    private static final String TAG = "User";

    public String name;
    public String screenName;
    public String publicImageUrl = "";
    public String id;

    static MeeperClient client;

    public User(String id, @NonNull MeeperClient client){
        this.id = id;
        this.client = client;
        client.getUserInfo(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try {
                    JSONObject data = json.jsonObject.getJSONObject("data");
                    Log.i(TAG, "onSuccess on getting user INFO: " + data.toString());
                    name = data.getString("name");
                    screenName = data.getString("username");
                    publicImageUrl = getProfileUrl();
                } catch (JSONException e) {
                    Log.e(TAG, "Couldn't get user data from JSON object: " + json.toString());
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure on getUserInfo", throwable);
            }
        }, id);
    }

    public User() {}

    public static User fromJson(JSONObject jsonObject, MeeperClient client) throws JSONException {
       User user = new User();
       user.name = jsonObject.getString("name");
       user.screenName = jsonObject.getString("username");
       user.id = jsonObject.getString("id");
       user.client = client;
       return user;
    }

    public static List<User> fromJsonArray(JSONArray jsonArray, MeeperClient client) throws JSONException {
        List<User> users = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                users.add(fromJson(jsonArray.getJSONObject(i), client));
            }
        }catch(NullPointerException e){
            Log.i("User", "Null Pointer");
        }
        return users;
    }

    private void getProfileUrlRequest(){
        client.getProfileURL(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                try{
                    Log.i("User", "onSuccess JSON response: " + json.toString());
                    publicImageUrl = json.jsonObject.getJSONObject("data").getString("profile_image_url");
                    name = json.jsonObject.getJSONObject("data").getString("name");
                    screenName = json.jsonObject.getJSONObject("data").getString("username");
                }catch(JSONException e){
                    Log.i("User", "Couldn't get profile url");
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i("User", "onFailure on getProfileURL");
            }
        }, this.id);
    }

    public String getProfileUrl(){
        if(publicImageUrl.isEmpty()){
            getProfileUrlRequest();
        }
        return publicImageUrl;
    }

    private void setName(String name){
        this.name = name;
    }

    private void setId(String id){
        this.id = id;
    }

    private void setUsername(String username){
        this.screenName = username;
    }

    public String getName(){
        if(name == null){
            return "Me";
        }
        return name;
    }

    public String getUsername(){
        if(screenName == null){
            return "";
        }
        return screenName;
    }

}
