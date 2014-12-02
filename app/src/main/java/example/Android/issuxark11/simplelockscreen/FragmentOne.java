package example.Android.issuxark11.simplelockscreen;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

// News Fragment
public class FragmentOne extends Fragment {
    int[] array;
    TextView NewsTitle;
    WebView NewsImgWV;
    Button NewsBtn;
    ListView screen;
    ArrayList<String> arrList,arrList2, arrList4;
    ArrayAdapter<String> adapter;

    String urlStr,tv,readLine;
    String tagName,title,link,imgurl = null;
    int eventType, index;
    XmlPullParser xpp;
    XmlPullParserFactory factory;
    StringBuffer sb;
    Random rd;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        //Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_one, container, false);
        index=0;
        array=new int[1000];
        NewsBtn=(Button)view.findViewById(R.id.NewsBtn);

        screen=(ListView)view.findViewById(R.id.screen);
        arrList = new ArrayList<String>();   // tile
        arrList2 = new ArrayList<String>();  // url
        arrList4 = new ArrayList<String>();  // jpg
        adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,arrList);
        screen.setAdapter(adapter);
        adapter.clear();
        arrList.clear();
        arrList2.clear();
        arrList4.clear();
        adapter.notifyDataSetChanged();

        rd=new Random();
        int sel=rd.nextInt(12)%4;

        switch(sel)
        {
            case 1: urlStr = "http://rss.donga.com/politics.xml";break;
            case 2: urlStr = "http://rss.donga.com/national.xml";break;
            case 3: urlStr = "http://rss.donga.com/economy.xml";break;
            case 0: urlStr = "http://rss.donga.com/culture.xml";break;
        }

        NetworkThread thread = new NetworkThread();
        thread.setDaemon(true);
        thread.start();

        //Log.d("fureun", "ÀÛµ¿Áß");
        try{
            thread.join();
        }catch(Exception e){

        }

        int j=0;
        for(int i=0; i<arrList4.size(); i++)
        {
            if((arrList4.get(i)!=null))
            {
                array[j]=i;
                j++;
            }
        }
        index=rd.nextInt(100)%j;

        NewsTitle=(TextView)view.findViewById(R.id.newstitle);
        NewsTitle.setText(arrList.get(array[index]).toString());

        NewsImgWV = (WebView) view.findViewById(R.id.webViewNews);
        NewsImgWV.loadUrl(arrList4.get(array[index]).toString());
/*
        NewsImgWV.post(new Runnable() {
            @Override
            public void run() {
                NewsImgWV.setHorizontalScrollBarEnabled(false); // 세로 scroll 제거
                NewsImgWV.setVerticalScrollBarEnabled(false); // 가로 scroll 제거
                NewsImgWV.getSettings().setUseWideViewPort(true);
                NewsImgWV.setInitialScale(1);
                NewsImgWV.loadUrl(HumorURL);
            }
        });
*/
       /*
        NewsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, Webview.class);
                i.putExtra("url", arrList2.get(index).toString());
                startActivity(i);
            }
        });
        */
        return view;
    }

    class NetworkThread extends Thread {
        public void run() {
            stream();
        }
    }

    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            if(msg.what ==0){
                Log.e("fureun", "list size: "+adapter.getCount());
                adapter.notifyDataSetChanged();
            }
        }
    };

    public void parsing(){
        try{
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            xpp = factory.newPullParser();
            xpp.setInput(new StringReader(tv.trim()));
            eventType = xpp.getEventType();
            int num=0;
            while ((eventType != XmlPullParser.END_DOCUMENT)&&(num<100)){
                if(eventType == XmlPullParser.START_TAG){
                    String tagName2 = xpp.getName();

                    if(tagName2.equals("item")){
                        while ((eventType != XmlPullParser.END_DOCUMENT)&&(num<100)){
                            if(eventType == XmlPullParser.START_TAG){
                                tagName = xpp.getName();
                            }
                            else if(eventType == XmlPullParser.TEXT){
                                if(tagName!=null){
                                    if(tagName.equals("title")){
                                        title = xpp.getText().trim();
                                        if (title.length()>0) {
                                            arrList.add(title);
                                        }
                                      //  Log.d("fureun", title+"("+title.length()+")");
                                    }if(tagName.equals("link")){
                                        link = xpp.getText().trim();
                                        if(link.length()>0){
                                            arrList2.add(link);
                                        }
                                       // Log.d("fureun",link);
                                    }
                                    if(tagName.equals("description")){
                                        imgurl = xpp.getText().trim();

                                        if(imgurl.length()>0){
                                            if(imgurl.indexOf("jpg")!=-1)
                                            {
                                                String imgurl3=imgurl.substring(10,imgurl.indexOf("jpg")+3);
                                                arrList4.add(imgurl3);
                                            }
                                            else
                                            {
                                                arrList4.add(null);

                                            }

                                 //           Log.d("fureun",imgurl);
                                        }
                                    }
                                    num++;
                               //     Log.d("fureun", ""+num);
                                }
                            }
                            eventType = xpp.next();
                        }
                        //Log.d("fureun","End document");
                    }
                }
                eventType = xpp.next();
            }
        } catch (Exception e){
            Log.e("fureun",e.toString());
        }
    }

    public void stream(){
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(urlStr);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(in);
            BufferedReader buf = new BufferedReader(isr);
            sb = new StringBuffer();

            while (true) {
                readLine = buf.readLine();
                if (readLine == null)
                    break;
                sb.append(readLine);
                sb.append("\n");
            }
            tv = sb.toString();
            parsing();

            handler.sendEmptyMessage(0);
            try{
                Thread.sleep(1000);
            }catch (Exception e){}
        }
        catch (Exception e) {
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }
}