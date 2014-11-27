package example.Android.issuxark11.simplelockscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ConfigActivity extends Activity {

    private Button onBtn, offBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_config);

        onBtn = (Button) findViewById(R.id.onBtn);
        offBtn = (Button) findViewById(R.id.offBtn);

        onBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfigActivity.this, MyService.class);
                startService(intent);
            }
        });

        offBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfigActivity.this, MyService.class);
                stopService(intent);
            }
        });
    }
}