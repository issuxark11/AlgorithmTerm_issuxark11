package example.Android.issuxark11.simplelockscreen;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

public class FragmentThree extends Fragment {

    private static String HumorURL = "http://algorithmic.pe.hu/algo.html";
    WebView webview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_three, container, false);
        webview = (WebView) view.findViewById(R.id.webViewHumor);
        webview.post(new Runnable() {
            @Override
            public void run() {
                webview.setHorizontalScrollBarEnabled(false); // 세로 scroll 제거
                webview.setVerticalScrollBarEnabled(false); // 가로 scroll 제거
                webview.getSettings().setUseWideViewPort(true);
                webview.setInitialScale(1);
                webview.loadUrl(HumorURL);
            }
        });
        return view;
    }
}