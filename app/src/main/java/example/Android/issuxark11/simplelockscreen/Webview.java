package example.Android.issuxark11.simplelockscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created by 영진 on 2014-12-02.
 */
public class Webview extends Activity {
    WebView WV;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        String url=intent.getStringExtra("url");
        setContentView(R.layout.activity_web);
        WV=(WebView)findViewById(R.id.webViewNew);
        WV.loadUrl(url);
    }
}
