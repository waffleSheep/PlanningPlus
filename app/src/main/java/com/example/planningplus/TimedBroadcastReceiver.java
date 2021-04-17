package com.example.planningplus;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class TimedBroadcastReceiver extends BroadcastReceiver {

    private static NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        final String title = intent.getStringExtra("title");
        final String description = intent.getStringExtra("description");
        final String username = intent.getStringExtra("username");
        FirebaseApp.initializeApp(context);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference documentReference = db.collection("users").document(username);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                user.notificationPPS.add(new NotificationPP(title, description, System.currentTimeMillis()));
                documentReference.set(user);
            }
        });
        initNotif(context);
        sendNotif(context, title, description);
    }

    private void sendNotif(Context context, String title, String text) {
        int notificationID = 101;
        String channelID ="com.example.planningplus.timed";
        Notification notification = new Notification.Builder(context,
                channelID).setContentTitle(title)
                .setContentText(text)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setChannelId(channelID)
                .build();
        notificationManager.notify(notificationID,notification);
    }

    private void initNotif(Context context) {
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel("com.example.planningplus.timed",
                "Timed Plan", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("Plan Triggered");
        channel.enableVibration(true);
        notificationManager.createNotificationChannel(channel);
    }
}
