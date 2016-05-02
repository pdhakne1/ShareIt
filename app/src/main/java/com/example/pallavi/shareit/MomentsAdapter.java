package com.example.pallavi.shareit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import io.filepicker.models.FPFile;

/**
 * Created by pallavi on 2/20/16.
 */
public class MomentsAdapter extends ArrayAdapter<Moment> {

    private final List<Moment> momentArrayList;
    private String activityName;
    static class ViewHolder {
        TextView textView;
        ImageView imageView;

    }

    public MomentsAdapter(Context context,int resource,List<Moment> momentList,String activityName) {
        super(context,resource,momentList);
        this.momentArrayList= momentList;
        this.activityName=activityName;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Moment momentList = momentArrayList.get(position);

        View myView;
        ViewHolder holder;

        if(convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            myView = inflater.inflate(R.layout.list,null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) myView.findViewById(R.id.rowImage);
            holder.textView = (TextView) myView.findViewById(R.id.rowText);
            myView.setTag(holder);

        }
        else {
            myView = convertView;
            holder = (ViewHolder) myView.getTag();
        }

        holder.textView.setText(momentList.getMomentName());
        try{
            /*if(this.activityName.equalsIgnoreCase("Inbox"))
            {
                Picasso.with(getContext()).load(momentList.getMomentPath()).into(holder.imageView);
            }*/

            Log.d("Where stored?",momentList.getMomentPath());

                File imageFile = new File(momentList.getMomentPath());
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 8;  // Experiment with different sizes
                Bitmap b = BitmapFactory.decodeFile(imageFile.getAbsolutePath(),options);
                holder.imageView.setImageBitmap(b);


        } catch (Exception e) {
            e.printStackTrace();
        }

        return myView;
    }
}
