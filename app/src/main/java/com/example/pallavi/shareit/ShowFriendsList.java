package com.example.pallavi.shareit;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.filepicker.Filepicker;
import io.filepicker.FilepickerCallback;
import io.filepicker.models.FPFile;

public class ShowFriendsList extends AppCompatActivity {

    User loggedInUser,friendDetails;
    ArrayList<FriendDetails> friendArrayList = new ArrayList<FriendDetails>();
    ArrayList<User> userList = new ArrayList<User>();
    FriendDetails myFriend;
    FriendsListAdapter fAdapter;
    ListView listView;
    Button shareButton;
    ArrayList<FriendDetails> selectedFriendList = new ArrayList<FriendDetails>();
    Moment momentDetails;
    private static final String FILEPICKER_API_KEY = "AbR7Y2iuHTq2sE0E9v1pQz";
    private static final String PARENT_APP = "SampleApp";
    String uploadedURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_friends_list);

        getSupportActionBar().setHomeButtonEnabled(true);
        Intent intent = getIntent();
        loggedInUser = (User) intent.getSerializableExtra("loggedInUser");
        momentDetails = (Moment) intent.getSerializableExtra("momentDetails");
        //Toast.makeText(ShowFriendsList.this, "user Id from intent"+loggedInUser.getUserID(), Toast.LENGTH_SHORT).show();
        Filepicker.setKey(FILEPICKER_API_KEY);
        Filepicker.setAppName(PARENT_APP);
        uploadLocalFile(momentDetails.getMomentPath());
        populateFriendList();
        listView = (ListView) findViewById(R.id.custom_friend_list);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(ShowFriendsList.this, "position" + position, Toast.LENGTH_SHORT).show();
                ((CheckBox)view.findViewById(R.id.friendListcheckBox)).setChecked(true);
                FriendDetails selectedFriend = (FriendDetails) listView.getAdapter().getItem(position);
                selectedFriendList.add(selectedFriend);
            }
        });

        shareButton= (Button) findViewById(R.id.shareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Firebase.setAndroidContext(ShowFriendsList.this);
                final Firebase mysaveMomentFirebaseRef = new Firebase("https://blazing-inferno-9452.firebaseio.com/moments");
                String momentId = Integer.toString(momentDetails.getMomentID());
                //momentDetails.setMomentPath(convertImage(momentDetails.getMomentPath()));
                Log.d("Before Firebase",momentDetails.toString());
                mysaveMomentFirebaseRef.child(momentId).setValue(momentDetails);
                for(FriendDetails f : selectedFriendList)
                {
                    //Toast.makeText(ShowFriendsList.this, "On click"+momentDetails.getMomentPath(), Toast.LENGTH_SHORT).show();
                    momentDetails.setFriendId(f.getFriendID());
                    addMoment(f);

                }
                AlertDialog alertDialog = new AlertDialog.Builder(ShowFriendsList.this).create();
                alertDialog.setTitle("Status");
                alertDialog.setMessage("Moment Shared Successfully");
                alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent userLoginIntent = new Intent(ShowFriendsList.this, MenuCurtain.class);
                                userLoginIntent.putExtra("loggedInUser", loggedInUser);
                                startActivity(userLoginIntent);
                            }
                        });
                alertDialog.show();
                //return;

            }
        });


    }

    public void populateFriendList()
    {


        Firebase.setAndroidContext(this);
        final Firebase myFirebaseRef = new Firebase("https://blazing-inferno-9452.firebaseio.com");



        myFirebaseRef.child("users/"+loggedInUser.getUserID()+"/friendsList").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                String groupKey = dataSnapshot.getKey();
                //Toast.makeText(ShowFriendsList.this, "On child added" + groupKey, Toast.LENGTH_SHORT).show();
                Log.d("On child added", groupKey);
                myFirebaseRef.child("users/" + groupKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {


                        friendDetails = dataSnapshot.getValue(User.class);
                        myFriend = new FriendDetails(friendDetails.getUserID(), friendDetails.getFirstName(), friendDetails.getLastName(), friendDetails.getUserEmail(), friendDetails.getStatus(), friendDetails.getDp(), false);
                        userList.add(friendDetails);
                        friendArrayList.add(myFriend);
                        if (fAdapter == null) {
                            fAdapter = new FriendsListAdapter(ShowFriendsList.this, R.layout.friendlist, friendArrayList);
                            listView.setAdapter(fAdapter);
                        }
                        //fAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

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
        });

    }

    public void addMoment(FriendDetails f)
    {
        //Toast.makeText(ShowFriendsList.this, "addMoment", Toast.LENGTH_SHORT).show();
        //Toast.makeText(ShowFriendsList.this, "Moment" + momentDetails.getMomentID(), Toast.LENGTH_SHORT).show();


        final Firebase mysaveUserMomentFirebaseRef = new Firebase("https://blazing-inferno-9452.firebaseio.com/users");
        //mysaveUserMomentFirebaseRef.child(f.getFriendID()).child("momentsList").child(Integer.toString(momentDetails.getMomentID())).setValue(momentDetails.getMomentName());
        mysaveUserMomentFirebaseRef.child(f.getFriendID()).child("momentsList").child(Integer.toString(momentDetails.getMomentID())).setValue("true");
    }
    public String convertImage(String path)
    {
        File myfile = new File(path);
        Toast.makeText(ShowFriendsList.this, "Path"+path, Toast.LENGTH_SHORT).show();
        Log.d("ImagePath",path);
        Bitmap bmp =  BitmapFactory.decodeFile(myfile.getAbsolutePath());//your image
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        bmp.recycle();
        byte[] byteArray = byteStream.toByteArray();
        String imageFile = Base64.encodeToString(byteArray, Base64.DEFAULT);

        return imageFile;

    }

    public void uploadLocalFile(String filePath) {
        //File imagePath = new File(Context.getFilesDir(), "images");
        Uri myuri = getImageContentUri(ShowFriendsList.this, new File(filePath));
        Log.d("Returned URI", myuri.toString());

        Filepicker.uploadLocalFile(myuri, this, new FilepickerCallback() {
            @Override
            public void onFileUploadSuccess(FPFile fpFile) {
                Log.d("On upload1", fpFile.getUrl());
                uploadedURL=fpFile.getUrl();
                momentDetails.setMomentPath(uploadedURL);
            }

            @Override
            public void onFileUploadError(Throwable error) {

            }

            @Override
            public void onFileUploadProgress(Uri uri, float progress) {
                Log.d("On upload3", uri.toString());

            }
        });
    }

    @Override
    protected void onDestroy() {
        Filepicker.unregistedLocalFileUploadCallbacks();
        super.onDestroy();
    }

    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID },
                MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
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
