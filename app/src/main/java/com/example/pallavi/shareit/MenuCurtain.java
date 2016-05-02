package com.example.pallavi.shareit;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Layout;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.squareup.picasso.Picasso;

import java.util.Calendar;

import lt.lemonlabs.android.expandablebuttonmenu.ExpandableButtonMenu;
import lt.lemonlabs.android.expandablebuttonmenu.ExpandableMenuOverlay;

public class MenuCurtain extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private PendingIntent pendingIntent;
    Button createMoment,shuffleMoment;
    RelativeLayout imgEmptyBasket;
    private User loggedInUser;
    TextView username,userEmail;
    private ExpandableMenuOverlay menuOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_curtain);




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        menuOverlay = (ExpandableMenuOverlay) findViewById(R.id.button_menu);
        menuOverlay.setOnMenuButtonClickListener(new ExpandableButtonMenu.OnMenuButtonClick() {
            @Override
            public void onClick(ExpandableButtonMenu.MenuButton action) {
                switch (action) {
                    case MID:
                        // do stuff and dismiss
                        //Toast.makeText(MenuCurtain.this, "Mid pressed and dismissing...", Toast.LENGTH_SHORT).show();
                        menuOverlay.getButtonMenu().toggle();
                        break;
                    case LEFT:
                        //Toast.makeText(MenuCurtain.this, "Left pressed", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(MenuCurtain.this, MomentCreation.class);
                        intent.putExtra("loggedInUser", loggedInUser);
                        startActivity(intent);
                        break;
                    case RIGHT:
                        //Toast.makeText(MenuCurtain.this, "Right pressed", Toast.LENGTH_SHORT).show();

                        shuffleMoment();
                        break;
                }
            }
        });

        Intent intent = getIntent();
        loggedInUser = (User) intent.getSerializableExtra("loggedInUser");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        username= (TextView) hView.findViewById(R.id.username);
        userEmail= (TextView) hView.findViewById(R.id.email);

        if(loggedInUser.getDp()!=null)
        {
            Picasso.with(hView.getContext()).load(loggedInUser.getDp())
                    //.noFade().centerCrop().resize(50,50)
                    .into((ImageView) hView.findViewById(R.id.profile_image));
        }





        SharedPreferences userSettings = getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = userSettings.edit();
        editor.putString("userId",loggedInUser.getUserID());
        editor.putString("userEmail", loggedInUser.getUserEmail());
        editor.commit();


        Intent startIntent = new Intent(MenuCurtain.this,FirebaseBackgroundService.class);
        startIntent.putExtra("userId", loggedInUser.getUserID());
        MenuCurtain.this.startService(startIntent);

        username.setText(loggedInUser.getFirstName() + " " + loggedInUser.getLastName());
        userEmail.setText(loggedInUser.getUserEmail());
        //startService(new Intent(FirebaseBackgroundService.class.getName()).putExtra("userId",loggedInUser.getUserID()));

        //Toast.makeText(MenuCurtain.this, "UserID Menu Curtain"+loggedInUser.getUserID(), Toast.LENGTH_SHORT).show();

        /*createMoment = (Button) findViewById(R.id.createMomentbutton);
        createMoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MenuCurtain.this,MomentCreation.class);
                intent.putExtra("loggedInUser", loggedInUser);
                startActivity(intent);

            }
        });

        shuffleMoment = (Button) findViewById(R.id.shufflebutton);
        shuffleMoment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);

                imgEmptyBasket = (LinearLayout) findViewById(R.id.menuLayout);
                imgEmptyBasket.startAnimation(shake);


                shake.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        AndroidDB momentDBHelper = new AndroidDB(MenuCurtain.this);
                        Moment randomMoment = momentDBHelper.getRandomMoment();

                        if(randomMoment==null)
                        {
                            AlertDialog alertDialog = new AlertDialog.Builder(MenuCurtain.this).create();
                            alertDialog.setTitle("Status");
                            alertDialog.setMessage("No Moments Created!!");
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                            return;
                        }

                        Intent intent = new Intent(MenuCurtain.this, ViewUserMoment.class);
                        intent.putExtra("selectedMoment", randomMoment);
                        intent.putExtra("activityName", "MenuCurtain");
                        startActivity(intent);
                        overridePendingTransition( R.anim.test, R.anim.slide_down );

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });




            }
        });*/

        /*MomentDBHelper momentDBHelperObj = new MomentDBHelper(this);
        momentDBHelperObj.insertMoment("Moment1","image","NULL","Private","dhpallaviv@gmail.com","0.0","0.0");
        momentDBHelperObj.insertMoment("Moment2","image","NULL","Private","dhpallaviv@gmail.com","0.0","0.0");
        momentDBHelperObj.insertMoment("Moment3","image","NULL","Private","dhpallaviv@gmail.com","0.0","0.0");*/

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_curtain, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        /*if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_audio) {

        } else*/ if (id == R.id.nav_inbox) {

            Intent intent = new Intent(MenuCurtain.this,Inbox.class);
            intent.putExtra("loggedInUser", loggedInUser);
            startActivity(intent);

        } else if (id == R.id.nav_add_friends) {

            Intent intent = new Intent(MenuCurtain.this,AddFriend.class);
            intent.putExtra("loggedInUser", loggedInUser);
            startActivity(intent);

        }
        else if (id == R.id.nav_edit_profile) {
            Intent intent = new Intent(MenuCurtain.this,EditProfile.class);
            intent.putExtra("loggedInUser", loggedInUser);
            startActivity(intent);
        }
        else if (id == R.id.nav_view_moments) {

            Intent intent = new Intent(MenuCurtain.this,ListUserMoments.class);
            intent.putExtra("loggedInUser", loggedInUser);
            startActivity(intent);

        }
        /*else if (id == R.id.nav_text) {

        }*/
        else if (id == R.id.nav_uninstall) {

            Uri AppToDelete = Uri.parse("package:com.example.pallavi.shareit");
            Intent RemoveApp = new Intent(Intent.ACTION_DELETE, AppToDelete);
            startActivity(RemoveApp);
            return true;

        }
        /*else if (id == R.id.nav_reminder) {
            //setReminder();

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setReminder()
    {
        Intent myIntent = new Intent(this , NotifyService.class);
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
        pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 30);
        calendar.set(Calendar.HOUR_OF_DAY, 20);


        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * 24, pendingIntent);



        Toast.makeText(MenuCurtain.this, "Start Alarm", Toast.LENGTH_LONG).show();
    }

    public void shuffleMoment()
    {
        Animation shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);

        imgEmptyBasket = (RelativeLayout) findViewById(R.id.menuLayout);
        imgEmptyBasket.startAnimation(shake);


        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                AndroidDB momentDBHelper = new AndroidDB(MenuCurtain.this);
                Moment randomMoment = momentDBHelper.getRandomMoment();

                if(randomMoment==null)
                {
                    AlertDialog alertDialog = new AlertDialog.Builder(MenuCurtain.this).create();
                    alertDialog.setTitle("Status");
                    alertDialog.setMessage("No Moments Created!!");
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                    return;
                }

                Intent intent = new Intent(MenuCurtain.this, ViewUserMoment.class);
                intent.putExtra("selectedMoment", randomMoment);
                intent.putExtra("activityName", "MenuCurtain");
                startActivity(intent);
                overridePendingTransition( R.anim.test, R.anim.slide_down );

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

}
