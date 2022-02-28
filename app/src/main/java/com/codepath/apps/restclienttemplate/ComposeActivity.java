package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.ColorSpace;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.apps.restclienttemplate.models.Meep;
import com.codepath.apps.restclienttemplate.models.User;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

public class ComposeActivity extends AppCompatActivity {

    public static final int MAX_MEEP_LENGTH = 140;
    public static final String TAG = "ComposeActivity";

    MeeperClient client = MeeperApp.getRestClient(this);

    EditText etCompose;
    Button btnMeep;
    TextView tvCharCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compose);

        etCompose = findViewById(R.id.etCompose);
        btnMeep = findViewById(R.id.btnCompose);
        tvCharCount = findViewById(R.id.tvCharCount);

        etCompose.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int count = editable.toString().length();
                tvCharCount.setText(count+ "/" + MAX_MEEP_LENGTH);
                if(count > MAX_MEEP_LENGTH){
                    tvCharCount.setTextColor(Color.RED);
                }else{
                    tvCharCount.setTextColor(Color.rgb(0, 0, 0));
                }
            }
        });


        //Click Listener
        btnMeep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String meepContent = etCompose.getText().toString();
                if(meepContent.isEmpty()){
                    Toast.makeText(ComposeActivity.this, "You haven't typed anything", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(meepContent.length() > MAX_MEEP_LENGTH){
                    Toast.makeText(ComposeActivity.this, "Your meep is too long", Toast.LENGTH_SHORT);
                    return;
                }
                // Post Meep;
                client.setMeep(new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Headers headers, JSON json) {
                        Log.e(TAG, "onSuccess to publish meep: " + json.toString());
                        try {
                            JSONObject data = json.jsonObject.getJSONObject("data");
                            Meep meep = Meep.fromJson(data, new User("1495539038404685828", client));
                            Intent intent = new Intent();
                            intent.putExtra("meep", Parcels.wrap(meep));
                            setResult(RESULT_OK, intent);
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                        Log.e(TAG, "onFailure to publish meep: " + response, throwable);
                    }
                },meepContent);
            }
        });
    }
}