package profick.headache_forecast;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import profick.headache_forecast.data.Channel;
import profick.headache_forecast.data.Item;
import profick.headache_forecast.service.WeatherService;
import profick.headache_forecast.service.WeatherServiceCallback;

public class WeatherActivity extends AppCompatActivity implements WeatherServiceCallback, OnClickListener {

    private static final String RISING = "rising";
    private static final String RESOURCE_ID = "resourceId";
    private static final String TEMPERATURE = "temperature";
    private static final String CONDITION = "condition";
    private static final String LOCATION = "location";
    private static final String CITY = "city";
    private static final String LAST_UPDATE = "lastUpdate";
    private static final String PREFERENCES = "HeadachePreferences";

    private ImageView weatherIconImageView;
    private TextView temperatureTextView;
    private TextView conditionTextView;
    private TextView locationTextView;
    private TextView lastUpdateTextView;

    private EditText cityEditText;

    private Button anketaActivityButton;
    private Button resultActivityButton;
    private Button updateButton;

    private WeatherService service;
    private ProgressDialog dialog;
    private SharedPreferences sharedPreferences;

    private int resourceId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherIconImageView = (ImageView) findViewById(R.id.weatherIconImageView);

        temperatureTextView = (TextView) findViewById(R.id.temperatureTextView);
        conditionTextView = (TextView) findViewById(R.id.conditionTextView);
        locationTextView = (TextView) findViewById(R.id.locationTextView);
        lastUpdateTextView = (TextView) findViewById(R.id.lastUpdateTextView);

        cityEditText = (EditText) findViewById(R.id.cityEditText); cityEditText.setOnClickListener(this);

        anketaActivityButton = (Button) findViewById(R.id.anketaActivityButton); anketaActivityButton.setOnClickListener(this);
        resultActivityButton = (Button) findViewById(R.id.resultActivityButton); resultActivityButton.setOnClickListener(this);
        updateButton = (Button) findViewById(R.id.updateButton); updateButton.setOnClickListener(this);


        if (savedInstanceState != null) {
            resourceId = savedInstanceState.getInt(RESOURCE_ID, 0);
            if (resourceId != 0) {
                @SuppressWarnings("deprecation")
                Drawable weatherIconDrawable = getResources().getDrawable(resourceId);
                weatherIconImageView.setImageDrawable(weatherIconDrawable);
            }
            temperatureTextView.setText(savedInstanceState.getString(TEMPERATURE));
            conditionTextView.setText(savedInstanceState.getString(CONDITION));
            locationTextView.setText(savedInstanceState.getString(LOCATION));
            cityEditText.setText(savedInstanceState.getString(CITY));
            lastUpdateTextView.setText(savedInstanceState.getString(LAST_UPDATE));
        } else {
            sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
            resourceId = sharedPreferences.getInt(RESOURCE_ID, 0);
            if (resourceId != 0) {
                @SuppressWarnings("deprecation")
                Drawable weatherIconDrawable = getResources().getDrawable(resourceId);
                weatherIconImageView.setImageDrawable(weatherIconDrawable);
            }
            temperatureTextView.setText(sharedPreferences.getString(TEMPERATURE, "Температура"));
            conditionTextView.setText(sharedPreferences.getString(CONDITION, "Температура"));
            locationTextView.setText(sharedPreferences.getString(LOCATION, "Температура"));
            lastUpdateTextView.setText(sharedPreferences.getString(LAST_UPDATE, "Температура"));
        }



    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.cityEditText:
                cityEditText.setText("");
                break;
            case R.id.anketaActivityButton:
                intent = new Intent(this, AnketaActivity.class);
                startActivity(intent);
                break;
            case R.id.resultActivityButton:
                intent = new Intent(this, ResultActivity.class);
                startActivity(intent);
                break;
            case R.id.updateButton:
                service = new WeatherService(this);
                dialog = new ProgressDialog(this);
                dialog.setMessage("Loading...");
                dialog.show();

                if (cityEditText.getText().toString().equals("Введите название города (на английском)")) {
                    cityEditText.setText("St. Petersburg");
                }
                service.refreshWeather(cityEditText.getText().toString());
                break;
            default:
                break;
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (resourceId != 0){
            outState.putInt(RESOURCE_ID, resourceId);
        }
        outState.putString(TEMPERATURE, temperatureTextView.getText().toString());
        outState.putString(CONDITION, conditionTextView.getText().toString());
        outState.putString(LOCATION, locationTextView.getText().toString());
        outState.putString(CITY, cityEditText.getText().toString());
        outState.putString(LAST_UPDATE, lastUpdateTextView.getText().toString());
    }

    @Override
    public void serviceSuccess(Channel channel) {
        dialog.hide();

        Item item = channel.getItem();

        resourceId = getResources().getIdentifier("drawable/icon_" + item.getCondition().getCode(), null, getPackageName());

        @SuppressWarnings("deprecation")
        Drawable weatherIconDrawable = getResources().getDrawable(resourceId);
        weatherIconImageView.setImageDrawable(weatherIconDrawable);


        temperatureTextView.setText(item.getCondition().getTemperature() + "\u00B0" + channel.getUnits().getTemperature());
        conditionTextView.setText(item.getCondition().getDescription());
        locationTextView.setText(channel.getLocation().getCity() + ", " + channel.getLocation().getCountry());
        lastUpdateTextView.setText("Последнее обновление: " + channel.getLastBuildDate());


        sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(RISING, channel.getAtmosphere().getRising());
        editor.putString(TEMPERATURE, temperatureTextView.getText().toString());
        editor.putInt(RESOURCE_ID, resourceId);
        editor.putString(CONDITION, conditionTextView.getText().toString());
        editor.putString(LOCATION, locationTextView.getText().toString());
        editor.putString(LAST_UPDATE, lastUpdateTextView.getText().toString());
        editor.apply();

        Toast.makeText(this, "Загрузка завершена", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void serviceFailure(Exception exception) {
        dialog.hide();
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}
