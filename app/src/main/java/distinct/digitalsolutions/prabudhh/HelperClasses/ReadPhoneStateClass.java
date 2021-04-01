package distinct.digitalsolutions.prabudhh.HelperClasses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class ReadPhoneStateClass extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub


        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

        if (state != null) {

            if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                // Phone is ringing
                Toast.makeText(context, "recied", Toast.LENGTH_SHORT).show();

            } else if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
                // Call received


            } else if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {
                // Call Dropped or rejected

            }

        }

    }

}