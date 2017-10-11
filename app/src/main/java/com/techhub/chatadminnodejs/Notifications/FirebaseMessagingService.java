package com.techhub.chatadminnodejs.Notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.techhub.chatadminnodejs.ChatActivity;
import com.techhub.chatadminnodejs.R;

/**
 * Created by NTN on 10/10/2017.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {


    NotificationCompat.Builder mBuilder;
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);


        String notification_title=remoteMessage.getNotification().getTitle();
        String notification_message=remoteMessage.getNotification().getBody();

        String click_action=remoteMessage.getNotification().getClickAction();
        String from_user_id=remoteMessage.getData().get("from_user_id");

        //thong bao khi app dang run
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(notification_title)
                        .setContentText(notification_message)
                        .setAutoCancel(true);



        //start inten
        Intent resultIntent = new Intent(click_action);
        resultIntent.putExtra("from_user_id",from_user_id);


        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);


        int mNotificationId = (int)System.currentTimeMillis();
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        mNotifyMgr.notify(mNotificationId, mBuilder.build());

    }
}
