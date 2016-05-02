package com.example.pallavi.shareit;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.widget.Toast;


public class NotifyService extends Service {
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate(){

        //Toast.makeText(NotifyService.this, "In notify service", Toast.LENGTH_LONG).show();
        Uri sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager mNM = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(this.getApplicationContext(), MenuCurtain.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent1, 0);

        Notification mNotify = new Notification.Builder(this)
                .setContentTitle("Remider of Notify!")
                .setContentText("View Moment")
                .setSmallIcon(R.drawable.ic_menu_gallery)
                .setContentIntent(pIntent)
                .setSound(sound)
                .addAction(0, "Load Moment ", pIntent)
                .build();

        mNM.notify(1, mNotify);
    }
}
