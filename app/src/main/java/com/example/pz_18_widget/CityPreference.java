package com.example.pz_18_widget;

import android.app.Activity;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

public class CityPreference {
    SharedPreferences prefs;

    public CityPreference(AppCompatActivity activity) {
        prefs = activity.getPreferences(Activity.MODE_PRIVATE);
    }

    public String getCity() {
        return prefs.getString("city", "Orenburg");
    }

    public void setCity(String city) {
        prefs.edit().putString("city", city).commit();
    }
}
