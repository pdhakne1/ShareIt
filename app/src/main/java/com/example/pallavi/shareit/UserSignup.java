package com.example.pallavi.shareit;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UserSignup extends AppCompatActivity {

    EditText firstName;
    EditText lastName;
    EditText email;
    EditText password;
    Button submit;
    Pattern pattern;
    Matcher matcher;
    final String EMAIL_PATTERN = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    User userSignupObj = new User();
    Firebase myFirebaseRef;
    Boolean boolInsert = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_signup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Firebase.setAndroidContext(this);
        myFirebaseRef = new Firebase("https://blazing-inferno-9452.firebaseio.com/");


        firstName = (EditText) findViewById(R.id.firstName);
        lastName = (EditText) findViewById(R.id.lastName);
        email = (EditText) findViewById(R.id.emailID);
        password = (EditText) findViewById(R.id.password);
        submit = (Button) findViewById(R.id.userSignupSubmitBtn);

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(firstName.getText().toString().trim().equals(""))
                {
                    firstName.setError("First Name cannot be Blank");
                    return;
                }
                if(lastName.getText().toString().trim().equals(""))
                {
                    lastName.setError("Last Name cannot be Blank");
                    return;
                }

                if(email.getText().toString().trim().equals(""))
                {
                    email.setError("User Name cannot be Blank");
                    return;
                }

                pattern = Pattern.compile(EMAIL_PATTERN);
                matcher = pattern.matcher(email.getText());

                if(!matcher.matches())
                {
                    email.setError("Not Valid User Email");
                    return;
                }

                userSignupObj.setFirstName(firstName.getText().toString());
                userSignupObj.setLastName(lastName.getText().toString());
                userSignupObj.setUserEmail(email.getText().toString());
                userSignupObj.setPassword(password.getText().toString());


                myFirebaseRef.createUser(userSignupObj.getUserEmail(), userSignupObj.getPassword(), new Firebase.ValueResultHandler<Map<String, Object>>() {
                    @Override
                    public void onSuccess(Map<String, Object> stringObjectMap) {
                        boolInsert=true;
                        userSignupObj.setUserID(stringObjectMap.get("uid").toString());
                        userSignupObj.setFriendsList(null);
                        userSignupObj.setMomentsList(null);

                        myFirebaseRef.child("users").child(userSignupObj.getUserID()).setValue(userSignupObj);

                        SharedPreferences userSettings = getSharedPreferences("LoginPrefs",MODE_PRIVATE);
                        SharedPreferences.Editor editor = userSettings.edit();
                        editor.putBoolean("isLogged", true);
                        editor.putString("userEmail", email.getText().toString());
                        editor.commit();
                        AlertDialog alertDialog = new AlertDialog.Builder(UserSignup.this).create();
                        alertDialog.setTitle("Status");
                        alertDialog.setMessage("User Created Successfully");
                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent userLoginIntent = new Intent(UserSignup.this, UserLogin.class);
                                        startActivity(userLoginIntent);
                                    }
                                });
                        alertDialog.show();
                        return;
                    }

                    @Override
                    public void onError(FirebaseError firebaseError) {
                        boolInsert=false;
                        //Toast.makeText(UserSignup.this, "Email Id already Exists", Toast.LENGTH_SHORT).show();
                    }
                });



            }
        });

    }



}