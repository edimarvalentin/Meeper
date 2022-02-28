package com.codepath.apps.restclienttemplate.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

@Parcel
public class Meep {
    public String body;
    public int likes;
    public int remeeps;
    public String id;
    public User user;

    public static Meep fromJson(JSONObject jsonObject, User user) throws JSONException {
        Meep meep = new Meep();
        meep.body = jsonObject.getString("text");
        meep.id = jsonObject.getString("id");

        if(jsonObject.has("public_metrics")) {
            meep.likes = jsonObject.getJSONObject("public_metrics").getInt("like_count");
            meep.remeeps = jsonObject.getJSONObject("public_metrics").getInt("retweet_count");
        }

        meep.user = user;
        //meep.createdAt = jsonObject.getString("created_at");
       // meep.user = User.fromJson(jsonObject.getJSONObject("user"));
        return meep;
    }

    public Meep(){}

    public static List<Meep> fromJsonArray(JSONArray jsonArray, User user) throws JSONException {
        List<Meep> meeps = new ArrayList<>();
        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                meeps.add(fromJson(jsonArray.getJSONObject(i), user));
            }
        }catch(NullPointerException e){
            Log.i("Meep", "Null Pointer");
        }
        return meeps;
    }



}
