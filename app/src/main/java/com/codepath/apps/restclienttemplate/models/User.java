package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import com.codepath.apps.restclienttemplate.MeeperClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class User {

    public String name;
    public String screenName;
    public String publicImageUrl = "";
    public String id;
    MeeperClient client;

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

}
