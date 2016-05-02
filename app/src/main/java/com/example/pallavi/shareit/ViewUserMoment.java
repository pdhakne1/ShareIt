package com.example.pallavi.shareit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewUserMoment extends AppCompatActivity {

    private Moment selectedMoment;
    private String activityName;
    private TextView momentHeading,momentName;
    private ImageView momentImage;
    private Button save;
    private int momentId;
    private String userEmail;
    File mCurrentPhotoPath;
    private User friendDetails;
    String loggedUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_moment);

        getSupportActionBar().setHomeButtonEnabled(true);

        SharedPreferences sharedPreferences = getSharedPreferences("myPrefs",MODE_PRIVATE);
        userEmail = sharedPreferences.getString("userEmail","");

        save = (Button) findViewById(R.id.saveSharedMoment);
        save.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        selectedMoment = (Moment) intent.getSerializableExtra("selectedMoment");
        Bundle extras = intent.getExtras();

        activityName = extras.getString("activityName");
        Log.d("VMomentDetails",selectedMoment.getMomentName()+selectedMoment.getMomentID());
        Log.d("VMomentDetails",activityName);

        //momentHeading = (TextView) findViewById(R.id.momentHeading);
        momentName = (TextView)findViewById(R.id.momentName);


        /*if(activityName.equalsIgnoreCase("MenuCurtain"))
        {
            momentHeading.setText("Today's Suprise Moment !!!!!!!!!");
        }
        else
        {
            momentHeading.setText("My Moment !!");
        }*/

        if(!activityName.equalsIgnoreCase("FirebaseMoment"))
        {
            momentName.setText(selectedMoment.getMomentName());
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 8;  // Experiment with different sizes
            Bitmap b = BitmapFactory.decodeFile(selectedMoment.getMomentPath());
            momentImage = (ImageView) findViewById(R.id.MomentImage);
            momentImage.setImageBitmap(b);

        }
        else
        {
            momentName.setText(selectedMoment.getMomentName());
            momentImage = (ImageView) findViewById(R.id.MomentImage);
            //Toast.makeText(ViewUserMoment.this, "what is the path?"+selectedMoment.getMomentPath(), Toast.LENGTH_SHORT).show();
            Log.d("ImagePath View Moment", selectedMoment.getMomentPath());
            File myFile = new File(selectedMoment.getMomentPath());
            Picasso.with(this).load(selectedMoment.getMomentPath())
                    //.noFade().centerCrop().resize(50,50)
                    .into(momentImage);
            //Picasso.with(this).load(myFile).into(momentImage);
            loggedUserId = extras.getString("loggedUserId");
            Firebase.setAndroidContext(ViewUserMoment.this);
            Log.d("Friend", loggedUserId);
            final Firebase mysaveMomentFirebaseRef = new Firebase("https://blazing-inferno-9452.firebaseio.com/users");
            save.setVisibility(View.VISIBLE);
            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AndroidDB momentDBHelper = new AndroidDB(ViewUserMoment.this);
                    momentId = (userEmail + ((Long) (System.currentTimeMillis() / 1000)).toString()).hashCode();
                    try {
                         mCurrentPhotoPath=createImageFile(momentImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d("PhotoPath",mCurrentPhotoPath.getAbsolutePath());
                    momentDBHelper.insertMoment(momentName.getText().toString(), selectedMoment.getMomentType(), momentId, mCurrentPhotoPath.getAbsolutePath(), "Shared", userEmail, selectedMoment.getUserId(), "0.0", "0.0");
                    AlertDialog alertDialog = new AlertDialog.Builder(ViewUserMoment.this).create();
                    alertDialog.setTitle("Added Moment");
                    alertDialog.setMessage("Saved successfully!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    mysaveMomentFirebaseRef.child(loggedUserId).child("momentsList").child(Integer.toString(selectedMoment.getMomentID())).setValue("false");
                                    dialog.dismiss();

                                }
                            });
                    alertDialog.show();
                }
            });
        }



    }

    private File createImageFile(ImageView momentImage) throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        if(image.exists())
        {
            image.delete();
        }

        FileOutputStream ostream = new FileOutputStream(image);
        momentImage.buildDrawingCache();
        Bitmap b = momentImage.getDrawingCache();
        b.compress(Bitmap.CompressFormat.JPEG,90,ostream);
        ostream.flush();
        ostream.close();

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image;
        //Toast.makeText(this,mCurrentPhotoPath.getAbsolutePath(),Toast.LENGTH_LONG).show();
        Log.d("ImagePath", mCurrentPhotoPath.getAbsolutePath());
        return image;
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
