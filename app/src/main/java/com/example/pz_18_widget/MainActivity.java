package com.example.pz_18_widget;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler =new Handler();
        updateWeatherData("Orenburg");
    }
    private void updateWeatherData(final String city){
        new Thread(){
            public void run(){
                final JSONObject json=ConnectFetch.getJSON(MainActivity.this, city);
                if(json==
                     null){ handler.post(new Runnable(){
                         public void run()
                         { Toast.makeText(MainActivity.this,
                                    city+"-информация не найдена",
                                    Toast.LENGTH_LONG).show(); } });
                } else{
                    handler.post(new Runnable(){
                        public void
                        run(){ renderWeather(json);
                        }
                    });
                }
            }
        }.start();
    }
    private void renderWeather(JSONObject json) {
        try {
            JSONObject details = json.getJSONArray("weather").getJSONObject(0);
            ((TextView) findViewById(R.id.weather)).setText(details.getString("description").toUpperCase());
        } catch (Exception e) {
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }

}