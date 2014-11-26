package example.Android.issuxark11.simplelockscreen;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LockScreenActivity extends Activity {

    private Button ConfBtn;     // 설정 버튼 -> 나중에 없애기!!

    private TextView cityText;  // 날씨 정보
    private TextView weatherCondition;
    private TextView temp;
    private ImageView imgView;

    long now = System.currentTimeMillis();  // 현재시간
    Date date = new Date(now);
    SimpleDateFormat CurHourFormat = new SimpleDateFormat("HH");
    String strCurHour = CurHourFormat.format(date);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // LockScreen Start
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON |WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        // 순정 잠금 화면 없애기
        setContentView(R.layout.activity_lock_screen);

        ConfBtn = (Button) findViewById(R.id.Configbtn);
        ConfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LockScreenActivity.this, ConfigActivity.class);
                startActivity(intent);
            }
        });
        // LockScreen End

        selectFrag(strCurHour);  // 시간에 따라 Fragment 선택

        // 날씨 정보 Start
        String city = "Seoul,KOR";
        cityText = (TextView) findViewById(R.id.cityText);
        weatherCondition = (TextView) findViewById(R.id.weatherCondition);
        temp = (TextView) findViewById(R.id.temp);
        imgView = (ImageView) findViewById(R.id.weatherIcon);

        JSONWeatherTask task = new JSONWeatherTask();
        task.execute(new String[]{city});
        // 날씨 정보 End
    }

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {
        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = ((new WeatherHttpClient()).getWeatherData(params[0]));

            try {
                weather = JSONWeatherParser.getWeather(data);

                // Let's retrieve the icon
                weather.iconData = ((new WeatherHttpClient()).getImage(weather.currentCondition.getIcon()));

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;
        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);

            if (weather.iconData != null && weather.iconData.length > 0) {
                Bitmap img = BitmapFactory.decodeByteArray(weather.iconData, 0, weather.iconData.length);
                imgView.setImageBitmap(img);
            }

            cityText.setText(weather.location.getCity() + "," + weather.location.getCountry());
            weatherCondition.setText(weather.currentCondition.getCondition() + "(" + weather.currentCondition.getWeatherdescribe() + ")");
            temp.setText("" + Math.round((weather.temperature.getTemperature() - 273.15)) + "℃");
        }
    }

    public void selectFrag(String curHour) {     // Fragment Select
        Fragment fr;

        if(curHour.equals("20") || curHour.equals("21") || curHour.equals("22") || curHour.equals("23")){
            fr = new FragmentTwo();
        }else {
            fr = new FragmentOne();
        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_main, fr);
        fragmentTransaction.commit();
    }
}