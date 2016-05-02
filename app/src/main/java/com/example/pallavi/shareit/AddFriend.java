package com.example.pallavi.shareit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class AddFriend extends AppCompatActivity {

    ImageView friendProfilePicture;
    EditText friendEmail;
    TextView friendName;
    Button searchButton;
    Button saveFriend;
    StringBuffer sb = new StringBuffer();
    User friendDetails,loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);

        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent = getIntent();
        loggedInUser = (User) intent.getSerializableExtra("loggedInUser");

        //Toast.makeText(AddFriend.this, "UserID Add Friend "+loggedInUser.getUserID(), Toast.LENGTH_SHORT).show();

        Firebase.setAndroidContext(this);
        final Firebase myFirebaseRef = new Firebase("https://blazing-inferno-9452.firebaseio.com/users");

        friendEmail = (EditText) findViewById(R.id.editFriendEmail);
        friendProfilePicture = (ImageView) findViewById(R.id.friendProfilePicture);
        friendName = (TextView) findViewById(R.id.friendName);

        saveFriend = (Button) findViewById(R.id.saveFriend);
        saveFriend.setVisibility(View.INVISIBLE);

        friendProfilePicture.setVisibility(View.INVISIBLE);
        friendName.setVisibility(View.INVISIBLE);


        searchButton = (Button) findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String friendEmailValue = friendEmail.getText().toString();
                //Toast.makeText(AddFriend.this, "Email: "+ friendEmailValue, Toast.LENGTH_SHORT).show();
                Query queryRef = myFirebaseRef.orderByChild("userEmail").equalTo(friendEmailValue);
                if(queryRef==null)
                {
                    //Toast.makeText(AddFriend.this, "Null value returned from query", Toast.LENGTH_SHORT).show();
                    /*AlertDialog alertDialog = new AlertDialog.Builder(AddFriend.this).create();
                    alertDialog.setTitle("Add Friend Status");
                    alertDialog.setMessage("Could not find a friend with the email id. Please Try again!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                }
                            });
                    alertDialog.show();
                    return;*/
                }
                else
                {
                    queryRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if(dataSnapshot.getChildrenCount()==0)
                            {
                                AlertDialog alertDialog = new AlertDialog.Builder(AddFriend.this).create();
                                alertDialog.setTitle("Add Friend Status");
                                alertDialog.setMessage("Could not find a friend with the email id. Please Try again!");
                                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();

                                            }
                                        });
                                alertDialog.show();
                                return;
                            }

                            for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                                friendDetails = userSnapshot.getValue(User.class);
                                //Toast.makeText(AddFriend.this, "User ID: "+ friendDetails.getUserID(), Toast.LENGTH_SHORT).show();

                                try {
                                     if(friendDetails.getDp()!=null)
                                     {
                                         /*byte[] decodedByte = com.firebase.client.utilities.Base64.decode(friendDetails.getDp());
                                         Bitmap myImage = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
                                         friendProfilePicture.setImageBitmap(myImage);*/
                                         Picasso.with(AddFriend.this).load(friendDetails.getDp())
                                                 //.noFade().centerCrop().resize(50,50)
                                                 .into(friendProfilePicture);
                                     }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                ((TextView)findViewById(R.id.friendName)).setText(friendDetails.getFirstName());

                                /*for(String s:friendDetails.getFriendsList().values())
                                {
                                    Toast.makeText(AddFriend.this, "Friend Value:"+s, Toast.LENGTH_SHORT).show();
                                }*/

                                //Log.d("User frindlist 1", friendDetails.getFriendsList().toString());
                                //Log.d("User frindlist 2",friendDetails.getFriendsList().get(1).toString());

                                friendProfilePicture.setVisibility(View.VISIBLE);
                                friendName.setVisibility(View.VISIBLE);
                                saveFriend.setVisibility(View.VISIBLE);
                                saveFriend.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        myFirebaseRef.child(loggedInUser.getUserID()).child("friendsList").child(friendDetails.getUserID()).setValue(friendDetails.getUserEmail());
                                    }
                                });


                            }

                        }

                        @Override
                        public void onCancelled(FirebaseError firebaseError) {

                        }
                    });
                    /*((TextView)findViewById(R.id.friendName)).setText(friendDetails.getFirstName());

                    friendProfilePicture.setVisibility(View.VISIBLE);
                    friendName.setVisibility(View.VISIBLE);
                    saveFriend.setVisibility(View.VISIBLE);
                    saveFriend.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            myFirebaseRef.child(loggedInUser.getUserID()).child("friendsList").setValue(friendDetails.getUserEmail());
                        }
                    });*/
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
