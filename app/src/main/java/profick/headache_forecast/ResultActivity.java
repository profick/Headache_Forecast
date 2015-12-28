package profick.headache_forecast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ResultActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String HEADACHE_PERCENT = "headache_percent";
    private static final String PREFERENCES = "HeadachePreferences";

    private Button weatherActivityButton;
    private TextView headacheTextView;
    private TextView sovetTextView;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        weatherActivityButton = (Button) findViewById(R.id.weatherActivityButton); weatherActivityButton.setOnClickListener(this);
        headacheTextView = (TextView) findViewById(R.id.headacheTextView);
        sovetTextView = (TextView) findViewById(R.id.sovetTextView);

        sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        int headachePercent = sharedPreferences.getInt(HEADACHE_PERCENT, -1);
        if (headachePercent == -1) {
            headacheTextView.setText("Нет данных.");
            sovetTextView.setText("Пожалуйста, пройдите анкетирование.");
        } else {
            if (0 <= headachePercent && headachePercent <= 15) {
                headacheTextView.setText("Крайне мала");
                sovetTextView.setText(R.string.sovet1);
            } else if (16 <= headachePercent && headachePercent <= 36) {
                headacheTextView.setText("Ниже среднего");
                sovetTextView.setText(R.string.sovet2);
            } else if (37 <= headachePercent && headachePercent <= 63) {
                headacheTextView.setText("Средняя");
                sovetTextView.setText(R.string.sovet3);
            } else if (64 <= headachePercent && headachePercent <= 76) {
                headacheTextView.setText("Выше среднего");
                sovetTextView.setText(R.string.sovet4);
            } else {
                headacheTextView.setText("Крайне высокая");
                sovetTextView.setText(R.string.sovet5);
            }
        }

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, WeatherActivity.class);
        startActivity(intent);
    }
}
