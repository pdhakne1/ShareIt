package com.example.pallavi.shareit;

import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageDialog extends AppCompatActivity {
    private ImageView mDialog;
    User loggedInUser;
    public Button shareButton, deleteButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_dialog);


        mDialog = (ImageView)findViewById(R.id.your_image);

        mDialog.setClickable(true);


        //finish the activity (dismiss the image dialog) if the user clicks
        //anywhere on the image
        mDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();

            }
        });




    }

    @Nullable
    @Override
    protected Dialog onCreateDialog(int id, Bundle args) {

        String s = args.getString("myname");
        Toast.makeText(ImageDialog.this, "bundle"+ s, Toast.LENGTH_SHORT).show();
        return super.onCreateDialog(id, args);

    }
}
