package example.Android.issuxark11.simplelockscreen;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class FragmentThree extends Fragment {

    private static String HumorURL = "http://algorithmic.pe.hu/algo.html";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_three, container, false);
        if (HumorURL != null) {
            WebView webview = (WebView) view.findViewById(R.id.webViewHumor);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.setWebViewClient(new webClient());
            webview.loadUrl(HumorURL);
        }
        return view;
    }

    private class webClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }
}