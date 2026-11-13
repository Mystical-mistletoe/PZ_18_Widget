package com.example.pz_18_widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.AppWidgetTarget;
import static com.example.pz_18_widget.ConnectFetch.getIconUrl;
import static com.example.pz_18_widget.StaticWeatherAnalyze.getTemperatureField;
import org.json.JSONObject;

import java.util.Arrays;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {
    final String LOG_TAG = "myLogs";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        SharedPreferences sp = context.getSharedPreferences(ConfigActivity.WIDGET_PREF, Context.MODE_PRIVATE);
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, sp, appWidgetManager, appWidgetId);
        }
        Log.d(LOG_TAG, "onUpdate " + Arrays.toString(appWidgetIds));
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        SharedPreferences.Editor editor = context.getSharedPreferences(ConfigActivity.WIDGET_PREF,
                Context.MODE_PRIVATE).edit();
        for (int widgetID : appWidgetIds) {
            editor.remove(ConfigActivity.WIDGET_CITY + widgetID);
        }
        editor.apply();
        Log.d(LOG_TAG, "onDeleted " + Arrays.toString(appWidgetIds));
    }

    @Override
    public void onEnabled(Context context) {
        Log.d(LOG_TAG, "onEnabled");
    }

    @Override
    public void onDisabled(Context context) {
        Log.d(LOG_TAG, "onDisabled");
    }

    static void updateAppWidget(final Context context, SharedPreferences sharedPreferences,
                                AppWidgetManager appWidgetManager, final int appWidgetId) {
        // Читаем параметры Preferences
        String widgetCity = sharedPreferences.getString(ConfigActivity.WIDGET_CITY + appWidgetId, null);
        if (widgetCity == null) return;

        final RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        remoteViews.setTextViewText(R.id.city_field, widgetCity);

        new ConnectFetch(context, widgetCity, new ConnectFetch.OnConnectionCompleteListener() {
            @Override
            public void onSuccess(JSONObject response) {
                renderWeather(response, context, remoteViews, appWidgetId);
            }

            @Override
            public void onFail(String message) {
            }
        });
        appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
    }

    public static void renderWeather(JSONObject json, Context context, RemoteViews remoteViews, int appWidgetId) {
        try {
            AppWidgetTarget appWidgetTarget = new AppWidgetTarget(context, R.id.weather_icon, remoteViews, appWidgetId);

            Glide.with(context.getApplicationContext())
                    .asBitmap()
                    .load(ConnectFetch.getIconUrl(json))
                    .into(appWidgetTarget);

            remoteViews.setTextViewText(R.id.details_field, StaticWeatherAnalyze.getTemperatureField(json));
            pushWidgetUpdate(context, remoteViews);
        } catch (Exception e) {
            Log.e("SimpleWeather", "One or more fields not found in the JSON data");
        }
    }
    public static void pushWidgetUpdate(Context context, RemoteViews rv) {
        ComponentName myWidget = new ComponentName(context, AppWidget.class);
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        manager.updateAppWidget(myWidget, rv);
    }
}