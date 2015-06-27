package android.geeps.util;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.geeps.R;
import android.geeps.activities.ActBarActivity;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

/**
 * Lança uma notificação com a logo do Geeps.
 */
public class GeepsNotification {

    /**
     * Create and show a simple notification containing the received GCM message.
     *
     * @param message GCM message received.
     */
    public static void sendNotification(Context context, String message) {
        Intent intent = new Intent(context, ActBarActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.icon_grayscale)
                .setContentTitle("GEEPS")
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);
        //.setStyle(new NotificationCompat.BigTextStyle()
        //      .bigText(message));

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // TODO Tem que ver esse id aqui, não sei como funciona esse esquema :x
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

}
