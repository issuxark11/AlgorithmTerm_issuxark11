package example.Android.issuxark11.simplelockscreen;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
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

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.File;
import java.util.Random;

@SuppressLint({ "ClickableViewAccessibility", "NewApi" })

public class LockScreenActivity extends Activity {
    int i=0;   // Customizing
    private Button ConfBtn;     // 설정 버튼 -> 나중에 없애기!!

    private TextView dateText;  // 날짜 표시
    private TextView cityText;  // 날씨 정보
    private TextView weatherCondition;
    private TextView temp;
    private ImageView imgView;

    long now = System.currentTimeMillis();  // 현재시간
    Date date = new Date(now);
    SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
    SimpleDateFormat CurHourFormat = new SimpleDateFormat("HH");
    String strCurDate = CurDateFormat.format(date);
    String strCurHour = CurHourFormat.format(date);

    Random rd;
    public static int sel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // LockScreen Start
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        // 순정 잠금 화면 없애기
        setContentView(R.layout.activity_lock_screen);

        // LockScreen End

        dateText = (TextView) findViewById(R.id.dateText);
        dateText.setText(strCurDate);   // 오늘 날짜 출력
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

        int[] g_c = new int[7];
        final int po_gi_like, po_gi_hate, na_gi_like, na_gi_hate, ec_gi_like, ec_gi_hate, cu_gi_like, cu_gi_hate;
        final String po_g_like, po_g_hate, na_g_like, na_g_hate, ec_g_like, ec_g_hate, cu_g_like, cu_g_hate;

        getcontent=readFile(file);
        String ct=new String(getcontent);
        g_c = findMiddle(ct);
        po_g_like=ct.substring(0, g_c[0]);
        po_g_hate=ct.substring(g_c[0]+1, g_c[1]);
        na_g_like=ct.substring(0, g_c[0]);
        na_g_hate=ct.substring(g_c[0]+1, g_c[1]);
        ec_g_like=ct.substring(0, g_c[0]);
        ec_g_hate=ct.substring(g_c[0]+1, g_c[1]);
        cu_g_like=ct.substring(0, g_c[0]);
        cu_g_hate=ct.substring(g_c[0]+1, g_c[1]);
        po_gi_like=Integer.parseInt(po_g_like);
        po_gi_hate=Integer.parseInt(po_g_hate);
        na_gi_like=Integer.parseInt(na_g_like);
        na_gi_hate=Integer.parseInt(na_g_hate);
        ec_gi_like=Integer.parseInt(ec_g_like);
        ec_gi_hate=Integer.parseInt(ec_g_hate);
        cu_gi_like=Integer.parseInt(cu_g_like);
        cu_gi_hate=Integer.parseInt(cu_g_hate);

        int po_sel=po_gi_like-po_gi_hate+100;
        int na_sel=na_gi_like-na_gi_hate+100;
        int ec_sel=ec_gi_like-ec_gi_hate+100;
        int cu_sel=cu_gi_like-cu_gi_hate+100;

        if(po_sel<50)
            po_sel=50;
        if(po_sel>200)
            po_sel=200;
        if(na_sel<50)
            na_sel=50;
        if(na_sel>200)
            na_sel=200;
        if(ec_sel<50)
            ec_sel=50;
        if(ec_sel>200)
            ec_sel=200;
        if(cu_sel<50)
            cu_sel=50;
        if(cu_sel>200)
            cu_sel=200;

        rd=new Random();

        int ran_sum=rd.nextInt(po_sel+na_sel+ec_sel+cu_sel);
        if(ran_sum<=po_sel)
            sel=1;
        else if(ran_sum>po_sel && ran_sum<=(po_sel+na_sel))
            sel=2;
        else if(ran_sum>(po_sel+na_sel)&& ran_sum<=(po_sel+na_sel+ec_sel))
            sel=3;
        else
            sel=0;

        final int[] location= new int[2];
        SetPosImg.getLocationOnScreen(location);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        final int screenWidth = metrics.widthPixels;

        SetPosImg.setOnTouchListener(new View.OnTouchListener()
        {
            int viewX=location[0]+screenWidth/2;

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
                    int ch_like;
                    String s_like;
                    String content;
                    if(sel==1){
                        ch_like=po_gi_like+1;
                        s_like=String.valueOf(ch_like);
                        content = s_like+"c"+po_g_hate+"c"+na_g_like+"c"+na_g_hate+"c"+ec_g_like+"c"+
                                ec_g_hate+"c"+cu_g_like+"c"+cu_g_hate;
                    }else if(sel==2){
                        ch_like=na_gi_like+1;
                        s_like=String.valueOf(ch_like);
                        content = po_g_like+"c"+po_g_hate+"c"+s_like+"c"+na_g_hate+"c"+ec_g_like+"c"+
                                ec_g_hate+"c"+cu_g_like+"c"+cu_g_hate;
                    }else if(sel==3){
                        ch_like=ec_gi_like+1;
                        s_like=String.valueOf(ch_like);
                        content = po_g_like+"c"+po_g_hate+"c"+na_g_like+"c"+na_g_hate+"c"+s_like+"c"+
                                ec_g_hate+"c"+cu_g_like+"c"+cu_g_hate;
                    }else if(sel==0){
                        ch_like=cu_gi_like+1;
                        s_like=String.valueOf(ch_like);
                        content = po_g_like+"c"+po_g_hate+"c"+na_g_like+"c"+na_g_hate+"c"+ec_g_like+"c"+
                                ec_g_hate+"c"+s_like+"c"+cu_g_hate;
                    }
                    else{
                        content = po_g_like+"c"+po_g_hate+"c"+na_g_like+"c"+na_g_hate+"c"+ec_g_like+"c"+
                                ec_g_hate+"c"+cu_g_like+"c"+cu_g_hate;
                    }
                    writeFile(file , content.getBytes());

                    SystemClock.sleep(1000);
                    LockScreenActivity.this.finish();
                    /*
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    */
                    i=0;
                }
                else if(event.getX()<=viewX-40 && event.getX()>viewX-70 && i==1)
                    i=-2;
                else if( event.getX()<=viewX-70 &&event.getX()>viewX-100 && i==-2)
                    i=-3;
                else if(event.getX()<=viewX-100 &&event.getX()>viewX-120 && i==-3)
                {
                    int ch_hate;
                    String s_hate;
                    String content;
                    if(sel==1){
                        ch_hate=po_gi_hate+1;
                        s_hate=String.valueOf(ch_hate);
                        content = po_g_like+"c"+s_hate+"c"+na_g_like+"c"+na_g_hate+"c"+ec_g_like+"c"+
                                ec_g_hate+"c"+cu_g_like+"c"+cu_g_hate;
                    }else if(sel==2){
                        ch_hate=na_gi_hate+1;
                        s_hate=String.valueOf(ch_hate);
                        content = po_g_like+"c"+po_g_hate+"c"+na_g_like+"c"+s_hate+"c"+ec_g_like+"c"+
                                ec_g_hate+"c"+cu_g_like+"c"+cu_g_hate;
                    }else if(sel==3){
                        ch_hate=ec_gi_hate+1;
                        s_hate=String.valueOf(ch_hate);
                        content = po_g_like+"c"+po_g_hate+"c"+na_g_like+"c"+na_g_hate+"c"+ec_g_like+"c"+
                                s_hate+"c"+cu_g_like+"c"+cu_g_hate;
                    }else if(sel==0){
                        ch_hate=cu_gi_hate+1;
                        s_hate=String.valueOf(ch_hate);
                        content = po_g_like+"c"+po_g_hate+"c"+na_g_like+"c"+na_g_hate+"c"+ec_g_like+"c"+
                                ec_g_hate+"c"+cu_g_like+"c"+s_hate;
                    }else{
                        content = po_g_like+"c"+po_g_hate+"c"+na_g_like+"c"+na_g_hate+"c"+ec_g_like+"c"+
                                ec_g_hate+"c"+cu_g_like+"c"+cu_g_hate;
                    }
                    writeFile(file , content.getBytes());

                    SystemClock.sleep(1000);
                    LockScreenActivity.this.finish();
                    /*
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    */
                    i=0;
                }
                return true;
            }
        });
        // Slide End
    }

    private int[] findMiddle(String ct)
    {
        int[] r = new int[7];
        int j=0;
        for(int i=0; i<ct.length(); i++)
        {
            if(ct.charAt(i)=='c'){
                r[j]=i;
                j++;
            }
        }
        return r;
    }

    private File makeDirectory(String dir_path){
        File dir = new File(dir_path);
        if (!dir.exists())
        {
            dir.mkdirs();
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
                    String content_first = "0c0c0c0c0c0c0c0";
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

        if( curHour.equals("09") || curHour.equals("10") || curHour.equals("11") ||
                curHour.equals("14") || curHour.equals("15") || curHour.equals("16") || curHour.equals("17") || curHour.equals("18") ) {
            Bundle args = new Bundle();
            args.putInt("select", sel);
            fr = new FragmentOne();
            fr.setArguments(args);
        } else if ( curHour.equals("20") || curHour.equals("21") || curHour.equals("22")) {
            fr = new FragmentTwo();
        } else if ( curHour.equals("12") || curHour.equals("18") || curHour.equals("19") ) {
            fr = new FragmentThree();
        } else if ( curHour.equals("07") || curHour.equals("08")  || curHour.equals("13") || curHour.equals("14")  ) {
            fr = new FragmentFour();
        } else {
            fr = new FragmentFive();
        }

        FragmentManager fm = getFragmentManager();
        fm.popBackStack();
        FragmentTransaction fragmentTransaction = fm.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_main, fr);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}