package com.example.pallavi.shareit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ListUserMoments extends AppCompatActivity {

    private StaggeredGridLayoutManager gaggeredGridLayoutManager;
    public static List<Moment> gaggeredList;
    private User loggedInUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user_moments);
        getSupportActionBar().setHomeButtonEnabled(true);
        Intent intent = getIntent();
        loggedInUser = (User) intent.getSerializableExtra("loggedInUser");
        populateMomentList();
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

    /*public void populateMomentList()
    {
        List<Moment> momentArrayList = new ArrayList<>();
        AndroidDB momentDBHelperObj = new AndroidDB(this);
        momentArrayList = momentDBHelperObj.getMoment();
        //Toast.makeText(ListUserMoments.this,"inside populate",Toast.LENGTH_LONG).show();
        final ListView listView = (ListView) findViewById(R.id.custom_list_view);
        listView.setAdapter(new MomentsAdapter(this, R.layout.list, momentArrayList,"ListUserMoments"));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(ListUserMoments.this,"Position"+position,Toast.LENGTH_LONG).show();
                Moment selectedMoment = (Moment) listView.getAdapter().getItem(position);
                Intent intent = new Intent(ListUserMoments.this, ViewUserMoment.class);
                intent.putExtra("selectedMoment", selectedMoment);
                intent.putExtra("activityName","ListUserMoments");
                startActivity(intent);

            }
        });
    }*/

    public void populateMomentList()
    {

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        gaggeredGridLayoutManager = new StaggeredGridLayoutManager(3, 1);
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);

        gaggeredList = new ArrayList<>();
        AndroidDB momentDBHelperObj = new AndroidDB(this);
        gaggeredList = momentDBHelperObj.getMoment();
        //Toast.makeText(ListUserMoments.this,"inside populate",Toast.LENGTH_LONG).show();


        SolventRecyclerViewAdapter rcAdapter = new SolventRecyclerViewAdapter(ListUserMoments.this, gaggeredList,loggedInUser);
        recyclerView.setAdapter(rcAdapter);

    }



    AdapterView.OnItemClickListener myOnItemClickListener
            = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            String prompt = (String)parent.getItemAtPosition(position);
            Toast.makeText(getApplicationContext(),
                    prompt,
                    Toast.LENGTH_LONG).show();

        }};


}
