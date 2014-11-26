package example.Android.issuxark11.simplelockscreen;

import android.annotation.SuppressLint;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.MotionEvent;
import java.io.*;
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
import android.widget.Toast;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;

@SuppressLint({ "ClickableViewAccessibility", "NewApi" })

public class LockScreenActivity extends Activity {
    int i=0;
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

        // Slide Start
        final ImageView SetPosImg = (ImageView)findViewById(R.id.SetPosImg);
        SetPosImg.setClickable(true);

        // for Customizing
        String STRSAVEPATH ="/sdcard/TEST/";
        byte[] getcontent;

        File dir = makeDirectory(STRSAVEPATH);
        final File file = makeFile(dir, STRSAVEPATH+"MyFile.txt");

        final int g_c, gi_like, gi_hate;
        final String g_like, g_hate;

        getcontent=readFile(file);
        String ct=new String(getcontent);
        g_c=findMiddle(ct);
        g_like=ct.substring(0, g_c);
        g_hate=ct.substring(g_c+1, ct.length());
        gi_like=Integer.parseInt(g_like);
        gi_hate=Integer.parseInt(g_hate);

        Toast.makeText(getApplicationContext(), g_like, Toast.LENGTH_SHORT).show();
        Toast.makeText(getApplicationContext(), g_hate, Toast.LENGTH_SHORT).show();

        final int[] location= new int[2];
        SetPosImg.getLocationOnScreen(location);

        SetPosImg.setOnTouchListener(new View.OnTouchListener()
        {
            int viewX=location[0]+187;

            @Override
            public boolean onTouch(View v, MotionEvent event) {


                if(event.getX()<viewX+40 && event.getX()>viewX-40)
                    i=1;
                else if(event.getX()>=viewX+40 && event.getX()<viewX+70 && i==1)
                    i=2;
                else if(event.getX()>=viewX+70 && event.getX()<viewX+100 && i==2)
                    i=3;
                else if(event.getX()>=viewX+100 && event.getX()<viewX+120 && i==3)
                {
                    int ch_like=gi_like+1, ch_hate=gi_hate;
                    String s_like, s_hate;
                    s_like=String.valueOf(ch_like);
                    s_hate=String.valueOf(ch_hate);

                    String content = s_like+"c"+s_hate;
                    writeFile(file , content.getBytes());

                    //SystemClock.sleep(1000);
                    moveTaskToBack(true);
                    finish();
                    System.exit(0);

                    i=0;
                }
                else if(event.getX()<=viewX-40 && event.getX()>viewX-70 && i==1)
                    i=-2;
                else if( event.getX()<=viewX-70 &&event.getX()>viewX-100 && i==-2)
                    i=-3;
                else if(event.getX()<=viewX-100 &&event.getX()>viewX-120 && i==-3)
                {
                    int ch_like=gi_like, ch_hate=gi_hate+1;
                    String s_like, s_hate;
                    s_like=String.valueOf(ch_like);
                    s_hate=String.valueOf(ch_hate);

                    String content = s_like+"c"+s_hate;
                    writeFile(file , content.getBytes());

                    SystemClock.sleep(1000);
                    moveTaskToBack(true);
                    finish();
                    System.exit(0);

                    i=0;
                }
                return true;
            }

        });
        // Slide End
    }

    private int findMiddle(String ct)
    {
        for(int i=0; i<ct.length(); i++)
        {
            if(ct.charAt(i)=='c')
                return i;
        }
        return -1;
    }
    private File makeDirectory(String dir_path){
        File dir = new File(dir_path);
        if (!dir.exists())
        {
            dir.mkdirs();
            Toast.makeText(getApplicationContext(), "Success folder", Toast.LENGTH_SHORT).show();
        }

        return dir;
    }

    private File makeFile(File dir, String file_path){
        File file = null;
        if(dir.isDirectory()){
            file = new File(file_path);
            if(file!=null&&!file.exists()){
                try {
                    file.createNewFile();
                    Toast.makeText(this, "Success text", Toast.LENGTH_SHORT).show();
                    String content_first = "0c0";
                    writeFile(file , content_first.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }


    private boolean writeFile(File file , byte[] file_content){
        boolean result;
        FileOutputStream fos;
        if(file!=null&&file.exists()&&file_content!=null){
            try {
                fos = new FileOutputStream(file);
                try {
                    fos.write(file_content);
                    fos.flush();
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            result = true;
        }else{
            result = false;
        }
        return result;
    }

    private byte[] readFile(File file){
        int readcount=0;
        if(file!=null&&file.exists()){
            try {
                FileInputStream fis = new FileInputStream(file);
                readcount = (int)file.length();
                byte[] buffer = new byte[readcount];
                fis.read(buffer);
                fis.close();
                return buffer;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public boolean onKeyDown(int keyCode,KeyEvent event){
        if((keyCode==KeyEvent.KEYCODE_BACK)){
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
        } else if (curHour.equals("24") || curHour.equals("01") || curHour.equals("2")){
            fr = new FragmentThree();
        }
        else {
            fr = new FragmentOne();
        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_main, fr);
        fragmentTransaction.commit();
    }
}