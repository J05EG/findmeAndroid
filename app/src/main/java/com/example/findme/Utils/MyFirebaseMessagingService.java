package com.example.findme.Utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.findme.MenuSlideActivity;
import com.example.findme.R;
import com.example.findme.Utils.ConstantSQLite;
import com.example.findme.connections.ConectionSQLite;
import com.example.findme.connections.ConnectionBackend;
import com.example.findme.models.User;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    String  TAG = "FIREBAE";
    ConnectionBackend conn = new ConnectionBackend();
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // …

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "HEEEERMANO" + remoteMessage.getFrom());


        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {

            if(String.valueOf(remoteMessage.getData().get("type")).equals("1")){
                Log.d(TAG, "LA DAAATA" + String.valueOf(remoteMessage.getData()));
                Log.d(TAG, "LA DAAATA" + String.valueOf(remoteMessage.getData().get("name")));
                String mens = String.format(String.valueOf(remoteMessage.getData().get("name"))+" "+String.valueOf(remoteMessage.getData().get("lastName"))+" ha salido de se zona segura");
                mostrarNotificacion(remoteMessage.getNotification().getTitle(),mens);
            }else if(String.valueOf(remoteMessage.getData().get("type")).equals("2")){
                String mens = String.format(String.valueOf(remoteMessage.getData().get("name"))+" "+String.valueOf(remoteMessage.getData().get("lastName"))+" Se ha desactivado su Beacon");
                mostrarNotificacion(remoteMessage.getNotification().getTitle(),mens);
            };
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG,   remoteMessage.getNotification().getBody());


        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }



    private void mostrarNotificacion(String title, String body) {
        Intent i = new Intent(this, MenuSlideActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pi = PendingIntent.getActivity(this,
                0 /* Request code */,
                i,
                PendingIntent.FLAG_ONE_SHOT);

        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this,
                getString(R.string.default_notification_channel_id))
                .setSmallIcon(R.drawable.logo_findme)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(sound)
                .setContentIntent(pi);

        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(0, builder.build());


    }


    @Override
    public void onNewToken(String token) {
        Log.d("Token", "Refreshed token: " + token);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        try{
            if(ConstantSQLite.ConsultarDatosUser(getApplicationContext()).getToken() != null) {
                String userId = ConstantSQLite.ConsultarDatosUser(getApplicationContext()).getId();
                conn.updateSmartPhoneToken(userId,token);
                ConstantSQLite.UpdateTokenUser(getApplicationContext(),token,userId);
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
