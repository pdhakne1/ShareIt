package com.example.pallavi.shareit;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.app.Activity;
import android.os.SystemClock;


import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.w3c.dom.Text;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UserLogin extends AppCompatActivity  {

    EditText userEmail, userPsswd;
    Button userSignUp, userLogin;
    User currentUserObj;
    Pattern pattern;
    Matcher matcher;
    final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Firebase myFirebaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://blazing-inferno-9452.firebaseio.com/");


        userEmail = (EditText) findViewById(R.id.userLoginUserName);
        userPsswd = (EditText) findViewById(R.id.userLoginPassword);



        userSignUp = (Button) findViewById(R.id.userSignup);
        userSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent userSignUp = new Intent(UserLogin.this, UserSignup.class);
                startActivity(userSignUp);
            }
        });
        userLogin = (Button) findViewById(R.id.userLoginBtn);
        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(userEmail.getText().toString().trim().equals(""))
                {
                    userEmail.setError("User Name cannot be Blank");
                    return;
                }

                pattern = Pattern.compile(EMAIL_PATTERN);
                matcher = pattern.matcher(userEmail.getText());

                if(!matcher.matches())
                {
                    userEmail.setError("Not Valid User Email");
                    return;
                }


                Login(userEmail.getText().toString(), userPsswd.getText().toString());


            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Login(String email, String password){
        //Toast.makeText(UserLogin.this, "Within Login function", Toast.LENGTH_SHORT).show();
        myFirebaseRef.authWithPassword(email, password, new MyAuthResultHandler());
    }

    class MyAuthResultHandler implements Firebase.AuthResultHandler{

        @Override
        public void onAuthenticated(AuthData authData) {

            //Toast.makeText(UserLogin.this, "Auth Id:"+authData.getUid(), Toast.LENGTH_SHORT).show();
            Firebase myFirebaseRefForUser = new Firebase("https://blazing-inferno-9452.firebaseio.com/users");

            Query queryRef = myFirebaseRefForUser.orderByChild("userID").equalTo(authData.getUid());
            queryRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                        currentUserObj = userSnapshot.getValue(User.class);
                        //Toast.makeText(UserLogin.this, "User Id" + currentUserObj.getUserID(), Toast.LENGTH_SHORT).show();
                        //Toast.makeText(UserLogin.this, "Authentication Success", Toast.LENGTH_SHORT).show();
                        Intent userLoginIntent = new Intent(UserLogin.this, MenuCurtain.class);
                        userLoginIntent.putExtra("loggedInUser", currentUserObj);
                        startActivity(userLoginIntent);

                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });


        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {

            AlertDialog alertDialog = new AlertDialog.Builder(UserLogin.this).create();
            alertDialog.setTitle("Status");
            alertDialog.setMessage("Incorrect Login Credentials");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();
            return;

        }
    }






}

