package com.example.pallavi.shareit;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

import io.filepicker.Filepicker;
import io.filepicker.FilepickerCallback;
import io.filepicker.models.FPFile;

public class EditProfile extends AppCompatActivity {

    ImageView profilePicture;
    private static int RESULT_LOAD_IMG = 1;
    String imgDecodableString;
    Button saveButton;
    Firebase myFirebaseRef=new Firebase("https://blazing-inferno-9452.firebaseio.com/users");
    TextView fname, lname, status;
    User loggedInUser,editUser;
    private static final String FILEPICKER_API_KEY = "AbR7Y2iuHTq2sE0E9v1pQz";
    private static final String PARENT_APP = "SampleApp";
    String uploadedURL;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        getSupportActionBar().setHomeButtonEnabled(true);
        Intent intent = getIntent();
        loggedInUser = (User) intent.getSerializableExtra("loggedInUser");

        Filepicker.setKey(FILEPICKER_API_KEY);
        Filepicker.setAppName(PARENT_APP);

        editUser= new User();
        fname = (TextView)findViewById(R.id.fname);
        lname = (TextView)findViewById(R.id.lname);
        status = (TextView)findViewById(R.id.status);

        uploadOrginalUserDetails();

        profilePicture = (ImageView) findViewById(R.id.profilePic);
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, RESULT_LOAD_IMG);
            }
        });

        saveButton = (Button) findViewById(R.id.saveProfileInfo);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String profileDp = uploadLocalFile(imgDecodableString);
                //Log.d("Uploaded URL",""+editUser.getDp());
                editUser.setFirstName(fname.getText().toString());
                editUser.setLastName(lname.getText().toString());
                editUser.setStatus(status.getText().toString());
                /*myFirebaseRef = new Firebase("https://blazing-inferno-9452.firebaseio.com/users");

                Firebase userRef = myFirebaseRef.child(loggedInUser.getUserID().toString());
                userRef.child("firstName").setValue(fname.getText().toString());
                userRef.child("lastName").setValue(lname.getText().toString());
                userRef.child("dp").setValue("hfgkdfgi");
                userRef.child("status").setValue(status.getText().toString());*/


            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // When an Image is picked
            if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK
                    && null != data) {
                // Get the Image from data

                Uri selectedImage = data.getData();
                String[] filePathColumn = { MediaStore.Images.Media.DATA };

                // Get the cursor
                Cursor cursor = getContentResolver().query(selectedImage,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                imgDecodableString = cursor.getString(columnIndex);
                cursor.close();
                Bitmap bitmapImage = BitmapFactory.decodeFile(imgDecodableString);
                //profilePicture.setImageBitmap(BitmapFactory
                 //       .decodeFile(imgDecodableString));
                profilePicture.setImageBitmap(bitmapImage);
                int nh = (int) ( bitmapImage.getHeight() * (512.0 / bitmapImage.getWidth()) );
                Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, 512, nh, true);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                scaled.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                FileOutputStream fo = null;
                try {
                    fo = new FileOutputStream(imgDecodableString);
                    fo.write(bytes.toByteArray());
                    fo.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Toast.makeText(this, "You haven't picked Image",
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Something went wrong"+e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }

    }
    public String uploadLocalFile(String filePath) {
        Log.d("FilePath", filePath);
        Uri myuri = getImageContentUri(EditProfile.this, new File(filePath));
        Log.d("Returned URI", myuri.toString());

        Filepicker.uploadLocalFile(myuri, this, new FilepickerCallback() {
            @Override
            public void onFileUploadSuccess(FPFile fpFile) {
                Log.d("On upload1", fpFile.getUrl());
                uploadedURL=fpFile.getUrl();
                editUser.setDp(uploadedURL);
                //myFirebaseRef = new Firebase("https://blazing-inferno-9452.firebaseio.com/users");

                Firebase userRef = myFirebaseRef.child(loggedInUser.getUserID().toString());
                userRef.child("firstName").setValue(fname.getText().toString());
                userRef.child("lastName").setValue(lname.getText().toString());
                userRef.child("dp").setValue(uploadedURL);
                userRef.child("status").setValue(status.getText().toString());
            }

            @Override
            public void onFileUploadError(Throwable error) {
                Log.e("ErrorMssg",error.toString());
            }

            @Override
            public void onFileUploadProgress(Uri uri, float progress) {
                Log.d("On upload3", uri.toString());

            }
        });
        return uploadedURL;
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

    public void uploadOrginalUserDetails()
    {
        myFirebaseRef.orderByChild("userID").equalTo(loggedInUser.getUserID()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {

                    editUser = userSnapshot.getValue(User.class);
                    fname.setText(editUser.getFirstName());
                    lname.setText(editUser.getLastName());
                    status.setText(editUser.getStatus());
                    if(editUser.getDp()!=null)
                    {
                        Picasso.with(EditProfile.this).load(editUser.getDp())
                                //.noFade().centerCrop().resize(50,50)
                                .into(profilePicture);
                    }
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

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
