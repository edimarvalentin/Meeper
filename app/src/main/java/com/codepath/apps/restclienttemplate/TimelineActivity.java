package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.restclienttemplate.models.Meep;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimelineActivity extends AppCompatActivity {

    public static final String TAG = "TimelineActivity";

    MeeperClient client;
    RecyclerView rvMeeps;
    List<Meep> meeps;
    List<User> users;
    MeepsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timeline);
        client = MeeperApp.getRestClient(this);

        rvMeeps = findViewById(R.id.rvMeeps);

        meeps = new ArrayList<>();
        users = new ArrayList<>();
        adapter = new MeepsAdapter(this, meeps);

        rvMeeps.setLayoutManager(new LinearLayoutManager(this));
        rvMeeps.setAdapter(adapter);

        getFollowingIds();
    }

    private void populateHomeTimeline() {
        for(int i = 0; i < users.size(); i++) {
            int finalI = i;
            client.getHomeTimeline(new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Headers headers, JSON json) {
                    Log.i(TAG, "Success!" + json.toString());
                    try {
                        JSONArray jsonArray = json.jsonObject.getJSONArray("data");
                        meeps.addAll(Meep.fromJsonArray(jsonArray, users.get(finalI)));
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        Log.e(TAG, "Json exception", e);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                    Log.i(TAG, "Failure! :" + response, throwable);
                }
            }, users.get(i).id);
        }
    }

    private void getFollowingIds(){
        client.getFollowing(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "Following: Success!" + json.toString());

                try {
                    JSONArray jsonArray = json.jsonObject.getJSONArray("data");
                    users.addAll(User.fromJsonArray(jsonArray, client));
                    if(users.size() > 0){
                        populateHomeTimeline();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "Json exception", e);
                    e.printStackTrace();
                }


            }
            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.i(TAG, "Following: Failure! :" + response, throwable);
            }
        });
    }

}