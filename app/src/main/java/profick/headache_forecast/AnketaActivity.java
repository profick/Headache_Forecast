package profick.headache_forecast;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import profick.headache_forecast.service.WeatherService;

public class AnketaActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String RISING = "rising";
    private static final String HEADACHE_PERCENT = "headache_percent";
    private static final String PREFERENCES = "HeadachePreferences";

    private SharedPreferences sharedPreferences;

    private CheckBox checkBoxes[] = new CheckBox[10];

    private Button finishedButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anketa);

        finishedButton = (Button) findViewById(R.id.finishedButton); finishedButton.setOnClickListener(this);

        for (int i = 1; i <= 10; i++) {
            checkBoxes[i - 1] = (CheckBox) findViewById(getResources().getIdentifier("checkBox" + String.valueOf(i), "id", getPackageName()));
        }

        checkBoxes[9].setOnClickListener(this);

        if (savedInstanceState != null) {
            for (int i = 0; i < 10; i++) {
                checkBoxes[i].setChecked(savedInstanceState.getBoolean("checkBox" + String.valueOf(i), false));
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        for (int i = 0; i < 10; i++) {
            outState.putBoolean("checkBox" + String.valueOf(i), checkBoxes[i].isChecked());
        }
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkBox9:
                for (int i = 0; i < 9; i++) {
                    checkBoxes[i].setChecked(false);
                }
                break;
            default:
                sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(HEADACHE_PERCENT, makeResult(checkBoxes));
                editor.apply();

                Toast.makeText(this, "Анкета заполнена", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(this, WeatherActivity.class);
                startActivity(intent);
                break;
        }

    }

    private int makeResult(CheckBox[] checkBoxes) {
        double sum = 0;
        for (int i = 0; i < 8; i++) {
            if (checkBoxes[i].isChecked()) {
                switch (i) {
                    case 0: sum+=10; break;
                    case 1: sum+=16; break;
                    case 2: sum+=2; break;
                    case 3: sum+=6; break;
                    case 4: sum+=4; break;
                    case 5: sum+=3; break;
                    case 6: sum+=5; break;
                    case 7: sum+=40; break;
                }
            }
        }
        sharedPreferences = getSharedPreferences(PREFERENCES, MODE_PRIVATE);
        boolean flag = checkBoxes[8].isChecked() || sharedPreferences.getInt(RISING, -1) == 2;

        if (flag) {
            sum += 1;
        }
        sum /= 87;
        if (flag) {
            sum += 1/45;
        }

        if (checkBoxes[9].isChecked()) {
            sum = 0;
        }

        sum *= 100;

        return (int) sum ;

    }


}
