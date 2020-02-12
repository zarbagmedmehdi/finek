package com.example.finek.map;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.finek.R;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static android.app.Notification.DEFAULT_SOUND;
import static android.app.Notification.DEFAULT_VIBRATE;

/**
 * Created by NgocTri on 8/9/2016.
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = "Finek: Notification";


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        //FirebaseMessaging.getInstance().subscribeToTopic("lala");

        Log.d(TAG, "FROM:" + remoteMessage.getFrom());

        //Check if the message contains data
        if(remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data: " + remoteMessage.getData());
            Map<String, String> data = remoteMessage.getData();
            sendNotification(data.get("body"));
        }

        //Check if the message contains notification

        if(remoteMessage.getNotification() != null) {
            Log.d(TAG, "Mesage body:" + remoteMessage.getNotification().getBody());
            sendNotification(remoteMessage.getNotification().getBody());
        }
    }

    /**
     * Dispay the notification
     * @param body
     */
    private void sendNotification(String body) {

        Intent intent = new Intent(this, LastLocation.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        //Set sound of notification
        Uri notificationSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);








        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "1")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Finek: Notification")
                .setContentText(body)
                //    .setContentIntent(pendingIntent)
                .setDefaults(DEFAULT_SOUND | DEFAULT_VIBRATE) //Important for heads-up notification
                .setPriority(Notification.PRIORITY_MAX)//Important for heads-up notification
                .setContentIntent(pendingIntent)
                .setSound(notificationSound);

        Notification buildNotification = mBuilder.build();
        int notifyId = (int) System.currentTimeMillis(); //For each push the older one will not be replaced for this unique id

        // Since android Oreo notification channel is needed.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "1";
            String description = "1";
            int importance = NotificationManager.IMPORTANCE_HIGH; //Important for heads-up notification
            NotificationChannel channel = new NotificationChannel("1",
                    name,
                    importance);
            channel.setDescription(description);
            channel.setShowBadge(true);
            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
                notificationManager.notify(notifyId, buildNotification);
            }
        }else{

            NotificationManager mNotifyMgr = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            if (mNotifyMgr != null) {
                mNotifyMgr.notify(notifyId, buildNotification);
            }
        }
    }
    }

