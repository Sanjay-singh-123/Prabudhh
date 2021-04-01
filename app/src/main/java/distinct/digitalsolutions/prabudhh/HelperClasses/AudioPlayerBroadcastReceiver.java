package distinct.digitalsolutions.prabudhh.HelperClasses;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.Toast;

import com.google.android.exoplayer2.ui.PlayerNotificationManager;

import distinct.digitalsolutions.prabudhh.Interfaces.NotificationInterface;


public class AudioPlayerBroadcastReceiver extends BroadcastReceiver {

    private NotificationInterface notificationInterface;

    public AudioPlayerBroadcastReceiver(NotificationInterface notificationInterface) {

        this.notificationInterface = notificationInterface;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if (action != null) {

            if (action.equalsIgnoreCase(PlayerNotificationManager.ACTION_PAUSE)){

                notificationInterface.notificationEvents(0,1);

            }else if (action.equalsIgnoreCase(PlayerNotificationManager.ACTION_PLAY)){

                notificationInterface.notificationEvents(1,0);

            }

        }
    }
}
