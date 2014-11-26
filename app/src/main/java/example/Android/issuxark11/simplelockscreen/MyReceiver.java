package example.Android.issuxark11.simplelockscreen;

import android.app.KeyguardManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class MyReceiver extends BroadcastReceiver {
    public static boolean screenoff;

    private TelephonyManager telephonyManager = null;
    private boolean isPhoneIdle = true;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            screenoff = true;

            if (telephonyManager == null) {
                telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                telephonyManager.listen(phoneListener, PhoneStateListener.LISTEN_CALL_STATE);
            }

            if (isPhoneIdle) {
                Intent changeIntent = new Intent(context, LockScreenActivity.class);
                //changeIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //context.startActivity(changeIntent);

                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, changeIntent, 0);

                try {
                    pendingIntent.send();
                } catch (Exception ex) {

                }

            } else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                screenoff = false;
            }
        }
    }

    private PhoneStateListener phoneListener = new PhoneStateListener() {
        @Override
        public void onCallStateChanged(int state, String incomingNumber)
        {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE:
                    isPhoneIdle = true;
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    isPhoneIdle = false;
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    isPhoneIdle = false;
                    break;
            }
        }
    };
}