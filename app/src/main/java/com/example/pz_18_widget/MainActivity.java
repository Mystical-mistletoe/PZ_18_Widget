package com.example.pz_18_widget;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


import com.bumptech.glide.Glide;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    Handler handler;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setInfo();
    }
    public void changeCityDialog(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Введите город");
        final EditText input = new EditText(MainActivity.this);
        input.setText(new CityPreference(this).getCity()); // Показываем текущий город
        builder.setView(input);
        builder.setPositiveButton("ОК", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String newCity = input.getText().toString();
                changeCity(newCity); // Вызываем метод сохранения и обновления
            }
        });
        builder.setNegativeButton("Отмена", null);
        builder.show();
    }
    public void changeCity(String city) {
        new CityPreference(this).setCity(city);
        setInfo();
    }

    public void setCity(View view) {
        showInputDialog();
    }

    private void showInputDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Измените город:");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);
        builder.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changeCity(input.getText().toString());
            }
        });
        builder.show();
    }
    private void setInfo() {
        new ConnectFetch(this, new CityPreference(this).getCity(),
                new ConnectFetch.OnConnectionCompleteListener() {
            @Override
            public void onSuccess(JSONObject response) {
                renderWeather(response);
            }

            @Override
            public void onFail(String message) {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            }
        });
    }
    private void renderWeather(JSONObject json) {
        try {
            Glide.with(this)
                    .load(ConnectFetch.getIconUrl(json))
                    .into((ImageView) findViewById(R.id.weather_icon));

            ((TextView)findViewById(R.id.city_field)).setText(StaticWeatherAnalyze.getCityField(json));
            ((TextView)findViewById(R.id.updated_field)).setText(StaticWeatherAnalyze.getLastUpdateTime(json));
            ((TextView)findViewById(R.id.details_field)).setText(StaticWeatherAnalyze.getDetailsField(json));
            ((TextView)findViewById(R.id.current_temperature_field)).setText(StaticWeatherAnalyze.getTemperatureField(json));

        } catch (Exception e) {
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }

}