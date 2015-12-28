package profick.headache_forecast;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import profick.headache_forecast.data.Channel;
import profick.headache_forecast.data.Item;
import profick.headache_forecast.service.WeatherService;
import profick.headache_forecast.service.WeatherServiceCallback;

public class WeatherActivity extends AppCompatActivity implements WeatherServiceCallback, OnClickListener {

    private static final String RISING = "rising";


    private ImageView weatherIconImageView;
    private TextView temperatureTextView;
    private TextView conditionTextView;
    private TextView locationTextView;

    private Button anketaButton;
    private Button resultButton;

    private WeatherService service;
    private ProgressDialog dialog;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        weatherIconImageView = (ImageView) findViewById(R.id.weatherIconImageView);
        temperatureTextView = (TextView) findViewById(R.id.temperatureTextView);
        conditionTextView = (TextView) findViewById(R.id.conditionTextView);
        locationTextView = (TextView) findViewById(R.id.locationTextView);
        anketaButton = (Button) findViewById(R.id.anketaButton); anketaButton.setOnClickListener(this);
        resultButton = (Button) findViewById(R.id.resultButton); resultButton.setOnClickListener(this);

        service = new WeatherService(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading...");
        dialog.show();

        service.refreshWeather("St. Petersburg, Russia");


    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.anketaButton:
                intent = new Intent(this, AnketaActivity.class);
                startActivity(intent);
                break;
            case R.id.resultButton:
                intent = new Intent(this, ResultActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void serviceSuccess(Channel channel) {
        dialog.hide();

        sharedPreferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(RISING, channel.getAtmosphere().getRising());
        editor.apply();


        Item item = channel.getItem();

        int resourceId = getResources().getIdentifier("drawable/icon_" + item.getCondition().getCode(), null, getPackageName());

        @SuppressWarnings("deprecation")
        Drawable weatherIconDrawable = getResources().getDrawable(resourceId);

        weatherIconImageView.setImageDrawable(weatherIconDrawable);


        temperatureTextView.setText(item.getCondition().getTemperature() + "\u00B0" + channel.getUnits().getTemperature());
        conditionTextView.setText(item.getCondition().getDescription());
        locationTextView.setText(service.getLocation());
        Toast.makeText(this, "Загрузка завершена", Toast.LENGTH_SHORT);
    }

    @Override
    public void serviceFailure(Exception exception) {
        dialog.hide();
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
    }
}
