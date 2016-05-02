package com.example.pallavi.shareit;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class SolventViewHolders extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView countryName;
    public ImageView countryPhoto;
    List<Moment> itemList;
    public ImageView imageViewGrid,shareMoment;
    private User loggedInUser;
    Moment selectedMoment;

    int pos;

    public SolventViewHolders(View itemView) {
        super(itemView);

        itemView.setOnClickListener(this);
        countryName = (TextView) itemView.findViewById(R.id.country_name);
        countryPhoto = (ImageView) itemView.findViewById(R.id.country_photo);
    }

    public SolventViewHolders(View itemView, List<Moment> itemList, User loggedInUser) {
        super(itemView);
        this.itemList = itemList;
        this.loggedInUser=loggedInUser;
        itemView.setOnClickListener(this);
        countryName = (TextView) itemView.findViewById(R.id.country_name);
        countryPhoto = (ImageView) itemView.findViewById(R.id.country_photo);
    }

    @Override
    public void onClick(View view) {


        //Toast.makeText(view.getContext(), "Clicked Position = " + getPosition(), Toast.LENGTH_SHORT).show();
        pos = getPosition();


        Dialog settingsDialog = new Dialog(view.getContext());
        settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        settingsDialog.setContentView(R.layout.image_layout);
        imageViewGrid = (ImageView) settingsDialog.findViewById(R.id.imageViewGrid);
        shareMoment = (ImageView) settingsDialog.findViewById(R.id.shareMoment);


       selectedMoment = (Moment) itemList.get(pos);


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = 8;  // Experiment with different sizes
        Bitmap b = BitmapFactory.decodeFile(selectedMoment.getMomentPath());

        imageViewGrid.setImageBitmap(b);
        settingsDialog.show();

        shareMoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ShowFriendsList.class);
                intent.putExtra("loggedInUser", loggedInUser);
                intent.putExtra("momentDetails", selectedMoment);
                v.getContext().startActivity(intent);
            }
        });


    }

}

