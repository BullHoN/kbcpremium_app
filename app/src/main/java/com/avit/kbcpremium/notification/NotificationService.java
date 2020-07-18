package com.avit.kbcpremium.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.room.FtsOptions;

import com.avit.kbcpremium.HomeActivity;
import com.avit.kbcpremium.R;
import com.avit.kbcpremium.SharedPrefNames;
import com.avit.kbcpremium.db.AppointmentItemRepository;
import com.avit.kbcpremium.db.OrderItemRepository;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.awt.font.TextAttribute;
import java.util.Map;

public class NotificationService extends FirebaseMessagingService {

    public static int NOTIFICATION_ID = 1;
    private SharedPreferences sharedPreferences;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);

        String dbName = SharedPrefNames.DB_NAME;
        sharedPreferences = getSharedPreferences(dbName, Context.MODE_PRIVATE);

        String socketIdName = SharedPrefNames.SOCKET_ID;
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(socketIdName,s);

        editor.apply();

    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Map<String,String> data = remoteMessage.getData();

        String title = data.get("title");
        String body = data.get("body");
        String orderId = data.get("orderId");

        Log.i("Notifcation","New notification arrived");
        AppointmentItemRepository repository = new AppointmentItemRepository(getApplication());
        OrderItemRepository repository1 = new OrderItemRepository(getApplication());


        if(title.contains("Booking")){
            NotificationReceiveData data1 = new NotificationReceiveData(orderId,1);
            repository.updateData(data1);
        }else {
            int status = Integer.parseInt(data.get("status"));

            String message;
            if(status == -1){
                message = data.get("message");
                Log.i("Notification",message);
            }else {
                message = "Order is Delivered";
            }

            NotificationReceiveData data1 = new NotificationReceiveData(orderId,status,message);
            repository1.updateData(data1);
        }

        generateNotification(title,body);

    }

    private void generateNotification(String title,String body){

        Bundle bundle = new Bundle();
        bundle.putString("order_id","asfasf_safasfsaf");

        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("notification",bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        String CHANNEL_ID = "KBC_PREMIUM_channel_01";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            CharSequence name = "KBC_PREMIUM_channel";
            String Description = "This is my KBC PREMIUM Channel";
            int importance = NotificationManager.IMPORTANCE_HIGH;


            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setDescription(Description);
            mChannel.setImportance(importance);
            mChannel.setShowBadge(false);
            mChannel.enableVibration(true);

            notificationManager.createNotificationChannel(mChannel);

        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent
                ,PendingIntent.FLAG_ONE_SHOT);

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,CHANNEL_ID)
                .setSmallIcon(R.drawable.app_logo)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);


        if(NOTIFICATION_ID > 1073741824){
            NOTIFICATION_ID = 0;
        }

        notificationManager.notify(NOTIFICATION_ID,notificationBuilder.build());
    }

}
