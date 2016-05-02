package com.example.pallavi.shareit;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

public class MainSplashScreen extends AppCompatActivity {

    private boolean isLogged;
    private String userEmail;
    User currentUserObj;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_NoActionBar);
        setContentView(R.layout.activity_main_splash_screen);

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.alpha);
        anim.reset();
        LinearLayout l=(LinearLayout) findViewById(R.id.lin_lay);
        l.clearAnimation();
        l.startAnimation(anim);

        anim = AnimationUtils.loadAnimation(this, R.anim.translate);
        anim.reset();
        ImageView iv = (ImageView) findViewById(R.id.splash);
        iv.clearAnimation();
        iv.startAnimation(anim);
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs",MODE_PRIVATE);
        isLogged = sharedPreferences.getBoolean("isLogged", false);
        userEmail = sharedPreferences.getString("userEmail", "");
        //Toast.makeText(MainSplashScreen.this, "is", Toast.LENGTH_SHORT).show();

        Thread timerThread = new Thread(){
            public void run(){
                try{
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally{
                    if(isLogged)
                    {
                        getLoggedUserDetails(userEmail);
                    }
                    else
                    {
                        Intent intent = new Intent(MainSplashScreen.this,UserSignup.class);
                        startActivity(intent);
                    }

                }
            }
        };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        finish();
    }

    public void getLoggedUserDetails(String userEmail)
    {
        Firebase.setAndroidContext(this);
        Firebase myFirebaseRefForUser = new Firebase("https://blazing-inferno-9452.firebaseio.com/users");

        Query queryRef = myFirebaseRefForUser.orderByChild("userEmail").equalTo(userEmail);
        queryRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                    currentUserObj = userSnapshot.getValue(User.class);
                    //Toast.makeText(MainSplashScreen.this, "User Id" + currentUserObj.getFirstName(), Toast.LENGTH_LONG).show();
                    //Toast.makeText(UserLogin.this, "Authentication Success", Toast.LENGTH_SHORT).show();
                    Intent userLoginIntent = new Intent(MainSplashScreen.this, MenuCurtain.class);
                    userLoginIntent.putExtra("loggedInUser", currentUserObj);
                    startActivity(userLoginIntent);

                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
