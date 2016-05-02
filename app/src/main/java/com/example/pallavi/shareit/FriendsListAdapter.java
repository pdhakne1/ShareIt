package com.example.pallavi.shareit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pallavi on 2/28/16.
 */
public class FriendsListAdapter extends ArrayAdapter<FriendDetails> {

    private final ArrayList<FriendDetails> friendArrayList;
    static class ViewHolder {
        TextView textView;
        ImageView imageView;
        CheckBox friendListCheckbox;

    }

    public FriendsListAdapter(Context context,int resource,ArrayList<FriendDetails> friendArrayList) {
        super(context,resource,friendArrayList);
        this.friendArrayList= friendArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        FriendDetails friendList = friendArrayList.get(position);

        View myView;
        ViewHolder holder;

        if(convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService
                    (Context.LAYOUT_INFLATER_SERVICE);
            myView = inflater.inflate(R.layout.friendlist,null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) myView.findViewById(R.id.rowImage);
            holder.textView = (TextView) myView.findViewById(R.id.rowText);
            holder.friendListCheckbox = (CheckBox) myView.findViewById(R.id.friendListcheckBox);
            myView.setTag(holder);



        }
        else {
            myView = convertView;
            holder = (ViewHolder) myView.getTag();
        }

        holder.textView.setText(friendList.getFriendFirstName() + " " + friendList.getFriendLastName());
        holder.friendListCheckbox.setChecked(friendList.getIsSelected());

        try{
            if(friendList.getFriendDp()!=null)
            {
                /*byte[] decodedByte = com.firebase.client.utilities.Base64.decode(friendList.getFriendDp());
                Bitmap myImage = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
                holder.imageView.setImageBitmap(myImage);*/
                Picasso.with(myView.getContext()).load(friendList.getFriendDp())
                        //.noFade().centerCrop().resize(50,50)
                        .into(holder.imageView);

            }
            else
            {
                /*Drawable drawable = getContext().getDrawable(R.drawable.empty_profile_picture);
                holder.imageView.setImageDrawable(drawable);*/
                holder.imageView.setImageResource(R.drawable.empty_profile_picture);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return myView;
    }
}
