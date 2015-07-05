package me.aupifb.perfeggtion;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    //Defining Variables
    private Toolbar toolbar;
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;

    public static boolean isStatering() {
        return statering;
    }

    public static void setStatering(boolean statering) {
        MainActivity.statering = statering;
    }

    public static boolean statering = false;

    long notifnumber, timeremaining;
    Ringtone ring;


    int mId, mProgressStatus = 50; // id to properly update notification
    String notiftext;
    CountDownTimer timer1; // countdown timer
    int timerstate; // 0 = not running, 1 = running, 2 = paused
    public static boolean activityVisible; // used to determine if app is in foreground
    private Handler mHandler = new Handler(); // required for progress bar
    NotificationCompat.Builder mBuilder =
            new NotificationCompat.Builder(this); // required for notification

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initializing Toolbar and setting it as the actionbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //Initializing NavigationView
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Checking if the item is in checked state or not, if not make it in checked state
                if(menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);
                //Closing drawer on item click
                drawerLayout.closeDrawers();
                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()){
                    case R.id.drawer_settings:
                        Intent mIntent1 = new Intent(MainActivity.this, PreferenceActivity.class);
                        MainActivity.this.startActivity(mIntent1);
                        return true;
                    case R.id.drawer_about:
                        Intent mIntent2 = new Intent(MainActivity.this, AboutActivity.class);
                        MainActivity.this.startActivity(mIntent2);
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(),"Somethings Wrong",Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });
        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.openDrawer, R.string.closeDrawer){
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };
        //Setting the actionbarToggle to drawer layout
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        //calling sync state is necessay or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new MainActivityFragment(), "mainfragmenttag")
                        .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch(item.getItemId()) {
            case R.id.action_stop_alarm:
                if (statering) {
                    Intent stopIntent = new Intent(getApplicationContext(), AlarmService.class);
                    getApplicationContext().stopService(stopIntent);;
                }
            case R.id.action_stop_timer:
                stoptimer();
                break;
            case R.id.action_pause_timer:
                pausetimer();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void stoptimer() {
        timer1.cancel();
        timerstate = 0;
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(mId);
    }

    private void pausetimer() {
        if (timerstate == 1) {
            timeremaining = notifnumber;
            timer1.cancel();
            timerstate = 2;
            NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(mId);
        } else if (timerstate == 2){
            countdownstart(timeremaining/1000);
            timerstate = 1;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        activityResumed(); // call method to set boolean activityVisible to 'true'
        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(mId); // cancel notification when app is in foreground (resumed)
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityPaused(); // call method to set boolean activityVisible to 'false'
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    public void notifmethod(String notiftext, int mProgressStatus, boolean booleanongoing) {
        // String notiftext = String displayed in notification
        // int mProgressStatus = progress (as %) of progressbar in notification
        // boolean booleanongoing = if true notification is set ongoing

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean displaynotification = sharedPref.getBoolean("displaynotification", true);
        boolean onlybackgroundpreference = sharedPref.getBoolean("onlybackgroundpreference", true);

            if (isActivityVisible() && onlybackgroundpreference || !displaynotification) {
            } else {
                mBuilder
                        .setOngoing(booleanongoing)
                        .setProgress(100, mProgressStatus, false) //max (100 so progress can be set as %), progress (%), determinate?
                        .setAutoCancel(true) // notification automatically dismissed when the user touches it
                        .setSmallIcon(R.drawable.ic_timer_black_24dp)
                        .setContentTitle("My notification")
                        .setContentText(notiftext);
// Creates an explicit intent for an Activity in your app
                Intent resultIntent = this.getIntent();
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

                PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplication(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

                mBuilder
                        .setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
                mNotificationManager.notify(mId, mBuilder.build());
            }
    }

    public void countdownstart(final long countdowntime) {
        if (timerstate != 1) {

            timerstate = 1;
            timer1 = new CountDownTimer(countdowntime * 1000, 100) {
                @Override
                public void onTick(final long millisUntilFinished) {
                    notifnumber = millisUntilFinished;
                    notiftext = Long.toString(notifnumber/1000);

                    new Thread(new Runnable() {
                        public void run() {
                            // Update the progress bar
                            mHandler.post(new Runnable() {
                                public void run() {
                                    mProgressStatus = (int) (100 - millisUntilFinished / (countdowntime * 10));
                                    notifmethod(notiftext, mProgressStatus, true);

                                }
                            });
                        }
                    }).start();


                }

                @Override
                public void onFinish() {
                    //.setContentText(getString(R.string.notifDone))  // wat zat??? int?? getString?????????
                    timerstate = 0;
                    notifmethod(getString(R.string.notification_done), 100, false);
                    MainActivityFragment mainActivityFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentByTag("mainfragmenttag");
                    mainActivityFragment.snackinfragment("notifdone", "doneaction");

                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    boolean playalarm = sharedPref.getBoolean("alarmpreference", true);
                    String alarm = sharedPref.getString("ringtonepreference", "null");
                    Uri alarmuri = Uri.parse(alarm);
                    ring = RingtoneManager.getRingtone(getApplicationContext(), alarmuri);
                    if (playalarm) {
                        if (ring == null || alarm.equals("null")) {
                            Intent intent = new Intent(getApplicationContext(), AlarmService.class);
                            intent.putExtra("alarm-uri", alarm);
                            getApplicationContext().startService(intent);
                        }
                        else {
                            Intent intent2 = new Intent(getApplicationContext(), AlarmService.class);
                            intent2.putExtra("alarm-uri", alarm);
                            getApplicationContext().startService(intent2);
                        }
                    }

                }
            }.start();
        } else Log.d("lol", "timerstate == 0 ");
    }

    public void timeselectalertdialog() {
        Log.d("lolo", "timeselectalertdialog ");
        DialogFragment newFragment = TimeSelectDialogFragment.newInstance(
                R.string.time_select_alert_dialog_title);
        newFragment.show(getSupportFragmentManager(), "timeselectdialogtag");
    }

}