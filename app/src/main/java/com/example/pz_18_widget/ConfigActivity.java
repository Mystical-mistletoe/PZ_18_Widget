package com.example.pz_18_widget;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public class ConfigActivity extends AppCompatActivity {
    int widgetID = AppWidgetManager.INVALID_APPWIDGET_ID;
    Intent resultValue;
    public final static String WIDGET_PREF = "widget_pref";
    public final static String WIDGET_CITY = "widget_city_";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);  // извлекаем ID конфигурируемого виджета
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            widgetID = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }  // и проверяем его корректность
        if (widgetID == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
        }  // формируем intent ответа
        resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID); // отрицательный ответ
        setResult(RESULT_CANCELED, resultValue);
        setContentView(R.layout.activity_config);
    }
    public void setCity(View view) {
        EditText etText = (EditText) findViewById(R.id.city);
        // запись значения с экрана
        SharedPreferences sp = getSharedPreferences(WIDGET_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(WIDGET_CITY + widgetID, etText.getText().toString());
        editor.apply();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        AppWidget.updateAppWidget(this, sp, appWidgetManager, widgetID);

        // Положительный ответ и закрытие экрана
        setResult(RESULT_OK, resultValue);
        finish();
    }

}