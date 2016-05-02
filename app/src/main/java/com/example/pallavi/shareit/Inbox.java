package com.example.pallavi.shareit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Inbox extends AppCompatActivity {

    User loggedInUser;
    Moment moments;
    List<Moment> momentArrayList = new ArrayList<>();
    MomentsAdapter mAdapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox);

        getSupportActionBar().setHomeButtonEnabled(true);
        //Firebase.setAndroidContext(this);
        Intent intent = getIntent();
        loggedInUser = (User) intent.getSerializableExtra("loggedInUser");
        //listView = (ListView) findViewById(R.id.inbox_moment_list);

        //populateMomentList();
        populateMomentListSQL();
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(ListUserMoments.this,"Position"+position,Toast.LENGTH_LONG).show();
                Moment selectedMoment = (Moment) listView.getAdapter().getItem(position);
                Intent intent = new Intent(Inbox.this, ViewUserMoment.class);
                intent.putExtra("selectedMoment", selectedMoment);
                intent.putExtra("activityName", "Inbox");
                startActivity(intent);

            }
        });*/

    }

    private void populateMomentListSQL() {

        ArrayList<Moment> momentArrayList = new ArrayList<>();
        AndroidDB momentDBHelperObj = new AndroidDB(this);
        momentArrayList = momentDBHelperObj.getSharedMoments();

        final ListView listView = (ListView) findViewById(R.id.inbox_moment_list);
        listView.setAdapter(new MomentsAdapter(this, R.layout.list, momentArrayList,"Inbox"));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(ListUserMoments.this,"Position"+position,Toast.LENGTH_LONG).show();
                Moment selectedMoment = (Moment) listView.getAdapter().getItem(position);
                Intent intent = new Intent(Inbox.this, ViewUserMoment.class);
                intent.putExtra("selectedMoment", selectedMoment);
                intent.putExtra("activityName", "Inbox");
                startActivity(intent);

            }
        });
    }

    public void populateMomentList()
    {

        //AndroidDB momentDBHelperObj = new AndroidDB(this);
        //momentArrayList = momentDBHelperObj.getMoment();
        //Toast.makeText(ListUserMoments.this,"inside populate",Toast.LENGTH_LONG).show();
        final Firebase myFirebaseRef = new Firebase("https://blazing-inferno-9452.firebaseio.com/users/"+loggedInUser.getUserID()+"/momentsList");
        Query queryRef = myFirebaseRef.orderByChild("-"+"momentId");

        queryRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String groupKey = dataSnapshot.getKey();
                final Firebase myFirebaseRef = new Firebase("https://blazing-inferno-9452.firebaseio.com/moments");
                myFirebaseRef.child(groupKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        moments = dataSnapshot.getValue(Moment.class);
                        momentArrayList.add(moments);
                        if (mAdapter == null) {
                            mAdapter = new MomentsAdapter(Inbox.this, R.layout.list, momentArrayList, "Inbox");
                            listView.setAdapter(mAdapter);
                        }
                        //mAdapter.notifyDataSetChanged();

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //mAdapter.notifyDataSetChanged();

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
