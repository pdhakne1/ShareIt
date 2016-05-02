package com.example.pallavi.shareit;

/**
 * Created by pallavi on 3/6/16.
 */
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;


public class FirebaseBackgroundService extends Service {

    Query qref;
    private ChildEventListener handler;
    private ValueEventListener mListener;
    private String userId;
    private Moment momentDetails;
    private User userDetails;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle bName = intent.getExtras();
        if(bName!=null)
        {
            userId = (String) bName.getString("userId");
        }
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public void onCreate() {
        super.onCreate();

        Context context = getApplicationContext();
        Firebase.setAndroidContext(context);
        final Firebase f = new Firebase("https://blazing-inferno-9452.firebaseio.com");
        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs",MODE_PRIVATE);
        userId = sharedPreferences.getString("userId","");
        Log.d("on Create Firebase", userId);
        momentDetails=new Moment();
        userDetails= new User();

        handler = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.d("On data changed", dataSnapshot.getKey()+":"+f);
                //f= new Firebase("https://blazing-inferno-9452.firebaseio.com/moments");

                String groupKey = dataSnapshot.getKey();
                String childValue = dataSnapshot.getValue().toString();
                Log.d("childValue.",dataSnapshot.getChildrenCount()+":"+childValue);
                if(childValue.equalsIgnoreCase("true"))
                {

                    f.child("moments/" + groupKey).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            Log.d("FB DataSnapshot", dataSnapshot.toString());
                            momentDetails = dataSnapshot.getValue(Moment.class);
                            Log.d("FB Moment", momentDetails.toString());
                            final Firebase myFirebaseRef = new Firebase("https://blazing-inferno-9452.firebaseio.com/users");

                            qref = myFirebaseRef.orderByChild("userEmail").equalTo(momentDetails.getUserId());
                            Log.d("QRef", qref.toString());
                            mListener=qref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                        userDetails = userSnapshot.getValue(User.class);
                                        Log.d("FB User", userDetails.toString());
                                        postNotif(userDetails, momentDetails);
                                    }

                                }

                                @Override
                                public void onCancelled(FirebaseError firebaseError) {

                                }
                            });

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                }


            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //fAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };

        f.child("users").child(userId + "/momentsList").addChildEventListener(handler);
        //f.child("users").child(userId + "/momentsList").orderByKey().equalTo("true").addChildEventListener(handler);
    }

    private void postNotif(User notifMoment, Moment momentDetails) {
        Log.d("Notify", notifMoment.getUserEmail() + ":" + momentDetails.getMomentID() + ":" + momentDetails.getMomentName());
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int icon = R.drawable.icon_share_notification;
        /*Notification notification = new Notification(icon, "Firebase" + Math.random(), System.currentTimeMillis());
//		notification.flags |= Notification.FLAG_AUTO_CANCEL;
        Context context = getApplicationContext();
        CharSequence contentTitle = "Background" + Math.random();
        Intent notificationIntent = new Intent(context, ViewUserMoment.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        notification.setLatestEventInfo(context, contentTitle, notifString, contentIntent);
        mNotificationManager.notify(1, notification);*/
        Context context = getApplicationContext();
        Intent notificationIntent = new Intent(context, ViewUserMoment.class);
        notificationIntent.putExtra("selectedMoment", momentDetails);
        notificationIntent.putExtra("loggedUserId",userId);
        notificationIntent.putExtra("activityName", "FirebaseMoment");

        int id = (int) (Math.random() * 100);
        PendingIntent contentIntent = PendingIntent.getActivity(context, id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context)
                .setContentTitle("ShareIt")
                .setContentText(notifMoment.getFirstName() + " " + notifMoment.getLastName() + " has shared a moment with you.")
                .setSmallIcon(icon)
                .setColor(Color.parseColor("#F44336"))
                .setContentIntent(contentIntent)
                .setAutoCancel(true);

        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(id, notification.build());


    }
    public void cleanup() {


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("OnDestroy","Yes");
    }
}
