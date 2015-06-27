package android.geeps.services;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.geeps.R;
import android.geeps.activities.ActBarActivity;
import android.geeps.util.GeepsNotification;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GcmListenerService;

/**
 * Service do GCM, faz com que receba mensagens do gcm em background.
 */
public class MyGcmListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {
        String message = data.getString("PEDIDO_NOTIFICATION");
        Log.d("MyGcmListenerService", "From: " + from);
        Log.d("MyGcmListenerService", "Message: " + message);

        /**
         * In some cases it may be useful to show a notification indicating to the user
         * that a message was received.
         */
        GeepsNotification.sendNotification(this, message);
    }
}
