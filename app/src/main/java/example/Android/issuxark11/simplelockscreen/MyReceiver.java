package example.Android.issuxark11.simplelockscreen;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver{
    public static boolean screenoff;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if(intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            screenoff = true;

            Intent changeIntent = new Intent(context, LockScreenActivity.class);
            //changeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //context.startActivity(changeIntent);

            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, changeIntent, 0);

            try {
                pendingIntent.send();
            } catch (Exception ex) {
                ;
            }


        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)){
            screenoff = false;
        }
    }
}
