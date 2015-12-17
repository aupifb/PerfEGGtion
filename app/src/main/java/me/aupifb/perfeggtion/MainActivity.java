package me.aupifb.perfeggtion;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private static final String DIALOG_NEW_RECIPE = "DialogNewRecipe";

    public static boolean activityVisible; // used to determine if app is in foreground
    boolean waspaused;
    TabLayout tabLayout;
    long totaltime1, totaltime2 = 0;
    long notifnumber, timeremaining;
    Ringtone ring;
    ViewPager pager;
    FragmentStatePagerAdapter a;
    int mId, mId2, mId3, mProgressStatus = 50; // id to properly update notification
    String notiftext;
    CountDownTimer timer1; // countdown timer
    int timerstate; // 0 = not running, 1 = running, 2 = paused
    NotificationCompat.Builder mBuilder =
            new NotificationCompat.Builder(this); // required for notification
    NotificationCompat.Builder mBuilder2 =
            new NotificationCompat.Builder(this); // required for notification
    NotificationCompat.Builder mBuilder3 =
            new NotificationCompat.Builder(this); // required for notification

    BroadcastReceiver BroadcastReceiver_STOP_ALARMSERVICE = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent stopIntent = new Intent(context, AlarmService.class);
            context.stopService(stopIntent);
            cancelnotifications();
            notificationdone();
        }
    };

    BroadcastReceiver BroadcastReceiver_STOP_TIMER = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            stoptimer();
        }
    };
    private DrawerLayout drawerLayout;
    private boolean action1added = false, action2added = false;
    private Handler mHandler = new Handler(); // required for progress bar
    BroadcastReceiver BroadcastReceiver_PLAYPAUSE_TIMER = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            pausetimer();
        }
    };

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityPaused() {
        activityVisible = false;
    }

    private void cancelnotifications() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(mId);
        notificationManager.cancel(mId2);
        notificationManager.cancel(mId3);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        if (sharedPref.getBoolean("firstrun", true)) {
            Recipe mRecipe = new Recipe("exampletitle", 10, UUID.randomUUID().toString());
            RecipeKitchen.get(this).addRecipe(mRecipe);
            sharedPref.edit().putBoolean("firstrun", false).apply();
        }

        registerReceiver(BroadcastReceiver_STOP_ALARMSERVICE, new IntentFilter("me.aupifb.perfeggtion.ACTION_STOP_ALARMSERVICE"));
        registerReceiver(BroadcastReceiver_STOP_TIMER, new IntentFilter("me.aupifb.perfeggtion.ACTION_STOP_TIMER"));
        registerReceiver(BroadcastReceiver_PLAYPAUSE_TIMER, new IntentFilter("me.aupifb.perfeggtion.ACTION_PLAYPAUSE_TIMER"));

        // Initializing Toolbar and setting it as the actionbar
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fabList = (FloatingActionButton) findViewById(R.id.fabList);
        fabList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                NewRecipeDialog dialog = NewRecipeDialog
                        .newInstance();
                dialog.show(manager, DIALOG_NEW_RECIPE);
            }
        });

        final FloatingActionButton fabMain = (FloatingActionButton) findViewById(R.id.fabMain);
        fabMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeselectalertdialog();
            }
        });

        fabMain.show();
        fabList.hide();

        pager = (ViewPager) findViewById(R.id.pager);
        a = (FragmentStatePagerAdapter) pager.getAdapter();
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                Log.d("pager", "pager " + position);
                AppBarLayout appbar = (AppBarLayout) findViewById(R.id.appbar);
                if (position == 0) {
                    fabMain.show();
                    fabList.hide();
                    appbar.setExpanded(true);
                } else {
                    fabList.show();
                    fabMain.hide();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        //Initializing NavigationView
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) menuItem.setChecked(false);
                else menuItem.setChecked(true);
                //Closing drawer on item click
                drawerLayout.closeDrawers();
                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    case R.id.drawer_settings:
                        Intent mIntent1 = new Intent(MainActivity.this, PreferenceActivity.class);
                        MainActivity.this.startActivity(mIntent1);
                        return true;
                    case R.id.drawer_about:
                        Intent mIntent2 = new Intent(MainActivity.this, AboutActivity.class);
                        MainActivity.this.startActivity(mIntent2);
                        return true;
                    default:
                        Toast.makeText(getApplicationContext(), "Somethings Wrong", Toast.LENGTH_SHORT).show();
                        return true;

                }
            }
        });
        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.openDrawer, R.string.closeDrawer) {
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

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Timer"));
        tabLayout.addTab(tabLayout.newTab().setText("Presets"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
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
        switch (item.getItemId()) {
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void stoptimer() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        MainActivityFragment mainActivityFragment = (MainActivityFragment) getSupportFragmentManager().findFragmentByTag("mainfragmenttag");
        waspaused = false;
        totaltime2 = 0;
        timeremaining = notifnumber;

        switch (timerstate) {
            case 0:
                break;
            case 1:
                timer1.cancel();
                timerstate = 0;
                notificationManager.cancel(mId);

                makesnackstoptimer();
                MainActivityFragment mainActivityFragment3 = (MainActivityFragment) getSupportFragmentManager().findFragmentByTag(
                        "android:switcher:" + R.id.pager + ":0");
                if (mainActivityFragment3 != null)  // could be null if not instantiated yet
                {
                    if (mainActivityFragment3.getView() != null) {
                        // no need to call if fragment's onDestroyView()
                        //has since been called.
                        mainActivityFragment3.circleprogress(0);
                        mainActivityFragment3.setmTextField("Timer stopped");
                    }
                }
                break;
            case 2:
                timer1.cancel();
                timerstate = 0;
                notificationManager.cancel(mId);
                makesnackstoptimer();
                MainActivityFragment mainActivityFragment4 = (MainActivityFragment) getSupportFragmentManager().findFragmentByTag(
                        "android:switcher:" + R.id.pager + ":0");
                if (mainActivityFragment4 != null)  // could be null if not instantiated yet
                {
                    if (mainActivityFragment4.getView() != null) {
                        // no need to call if fragment's onDestroyView()
                        //has since been called.
                        mainActivityFragment4.circleprogress(0);
                        mainActivityFragment4.setmTextField("Timer stopped");
                    }
                }
                break;
            default:
                break;
        }
    }

    private void makesnackstoptimer() {
        Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.snackbar_timer_stopped, Snackbar.LENGTH_LONG)
                .show();
    }

    public void pausetimer() {
        Log.d("lol", "inactivty");
        MainActivityFragment mainActivityFragment2 = (MainActivityFragment) getSupportFragmentManager().findFragmentByTag(
                "android:switcher:" + R.id.pager + ":0");
        if (mainActivityFragment2 != null)  // could be null if not instantiated yet
        {
            if (mainActivityFragment2.getView() != null) {
                // no need to call if fragment's onDestroyView()
                //has since been called.
                mainActivityFragment2.testmethod(); // do what updates are required
            }
        }

        if (timerstate == 1) {
            timeremaining = notifnumber;
            timer1.cancel();
            timerstate = 2;
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            mBuilder.setContentText(getString(R.string.notif_timer_paused));
            notificationManager.notify(mId, mBuilder.build());
            MainActivityFragment mainActivityFragment5 = (MainActivityFragment) getSupportFragmentManager().findFragmentByTag(
                    "android:switcher:" + R.id.pager + ":0");

            makesnackpausetimer();
        } else if (timerstate == 2) {
            if (totaltime2 == 0) {
                totaltime2 = totaltime1;
            }
            countdownstart(timeremaining / 1000);
            timerstate = 1;
        }
    }

    private void makesnackpausetimer() {
        Snackbar.make(findViewById(R.id.coordinatorLayout), R.string.snackbar_timer_paused, Snackbar.LENGTH_LONG)
                .setAction(R.string.snackbar_undo_timer_paused, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pausetimer();
                    }
                })
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        activityResumed(); // call method to set boolean activityVisible to 'true'
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean onlybackgroundpreference = sharedPref.getBoolean("onlybackgroundpreference", true);
        if (onlybackgroundpreference) {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancel(mId); // cancel notification when app is in foreground (resumed)
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityPaused(); // call method to set boolean activityVisible to 'false'
    }

    public void notifmethod(int mProgressStatus, boolean booleanongoing) {
        // String notiftext = String displayed in notification
        // int mProgressStatus = progress (as %) of progressbar in notification
        // boolean booleanongoing = if true notification is set ongoing

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean displaynotification = sharedPref.getBoolean("displaynotification", true);
        boolean onlybackgroundpreference = sharedPref.getBoolean("onlybackgroundpreference", true);

        String textsecs = ".", textmins = "";

        int mins = (int) (notifnumber / 60000);
        Log.d("lol", Integer.toString(mins));
        int secs = (int) (notifnumber / 1000 - mins * 60);

        /*switch (mins) {
            default:
                textmins = mins + " minutes";
                break;
            case 0:
                textmins = "";
                break;
            case 1:
                textmins = mins + " minute";
                break;
        }
        switch (secs) {
            default:
                if (mins != 0) {
                    textsecs = " and " + secs + " seconds.";
                } else {
                    textsecs = secs + " seconds.";
                }
                break;
            case 0:
                if (mins != 0) {
                    textsecs = ".";
                } else {
                    textsecs = "almost ready."; // not really needed anymore --> countdowninterval = 1000 now
                }
                break;
            case 1:
                if (mins != 0) {
                    textsecs = " and " + secs + " second.";
                } else {
                    textsecs = secs + " second.";
                }
                break;
        }*/

        if (mins != 0) {
            textmins = getResources().getQuantityString(R.plurals.minutes_remaining, mins, mins);
        }
        if (secs != 0) {
            textsecs = getResources().getQuantityString(R.plurals.seconds_remaining, secs, secs);
        }

        if (timerstate == 1) {
            String spaceString = " ";
            if (secs == 0) {
                spaceString = "";
            }
            notiftext = "Time remaining: " + textmins + textsecs;
            String textviewtext = "Time remaining:\n" + textmins + spaceString + textsecs;

            if (pager.getCurrentItem() == 0) {
                MainActivityFragment mainActivityFragment2 = (MainActivityFragment) pager.getAdapter().instantiateItem(pager, pager.getCurrentItem());
                mainActivityFragment2.settextviewtext(textviewtext);
            }
        }

        if (isActivityVisible() && onlybackgroundpreference || !displaynotification) {
        } else {
            // Creates an explicit intent for an Activity in your app
            Intent resultIntent = getIntent();
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplication(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            mBuilder
                    .setOngoing(booleanongoing)
                    .setProgress(100, mProgressStatus, false) //max (100 so progress can be set as %), progress (%), determinate?
                    .setAutoCancel(true) // notification automatically dismissed when the user touches it
                    .setSmallIcon(R.drawable.ic_add_alarm_white_24dp)
                    .setContentTitle(getString(R.string.notif_title))
                    .setPriority(1)
                    .setShowWhen(false)
                    .setContentText(notiftext);

            mBuilder
                    .setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
            mNotificationManager.notify(mId, mBuilder.build());
        }
    }

    public void countdownstart(final long countdowntime) {

        totaltime1 = countdowntime;

        if (timerstate != 1) {
            Intent stopIntent = new Intent(getApplicationContext(), AlarmService.class);
            getApplicationContext().stopService(stopIntent);
            //NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            //notificationManager.cancel(mId);

            if (timerstate == 2) {
                waspaused = true;
            }
            if (!action1added) {
                Intent notificationIntent = new Intent();
                notificationIntent.setAction("me.aupifb.perfeggtion.ACTION_STOP_TIMER");
                PendingIntent resultPendingIntent = PendingIntent.getBroadcast(getApplication(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Action testaction = new NotificationCompat.Action.Builder(R.drawable.ic_stop_black_24dp, "STOP", resultPendingIntent).build();
                mBuilder.addAction(testaction);

                Intent notificationIntent2 = new Intent();
                notificationIntent2.setAction("me.aupifb.perfeggtion.ACTION_PLAYPAUSE_TIMER");
                PendingIntent resultPendingIntent2 = PendingIntent.getBroadcast(getApplication(), 0, notificationIntent2, PendingIntent.FLAG_UPDATE_CURRENT);
                NotificationCompat.Action testaction2 = new NotificationCompat.Action.Builder(R.drawable.ic_pause_black_36dp, "PLAY/PAUSE", resultPendingIntent2).build();
                mBuilder.addAction(testaction2);

                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
                mNotificationManager.notify(mId, mBuilder.build());
                action1added = true;
            }

            timerstate = 1;
            notifmethod(mProgressStatus, true);
            timer1 = new CountDownTimer(countdowntime * 1000, 500) {
                @Override
                public void onTick(final long millisUntilFinished) {
                    notifnumber = millisUntilFinished;

                    new Thread(new Runnable() {
                        public void run() {
                            // Update the progress bar
                            mHandler.post(new Runnable() {
                                public void run() {
                                    if (!waspaused) {
                                        mProgressStatus = (int) (100 - millisUntilFinished / (countdowntime * 10));
                                    } else {
                                        mProgressStatus = (int) (100 - millisUntilFinished / (totaltime2 * 10));
                                    }

                                    notifmethod(mProgressStatus, true);


                                    if (pager.getCurrentItem() == 0) {
                                        MainActivityFragment mainActivityFragment2 = (MainActivityFragment) pager.getAdapter().instantiateItem(pager, pager.getCurrentItem());
                                        mainActivityFragment2.circleprogress(mProgressStatus);
                                    }
                                }

                            });
                        }
                    }).start();

                }

                @Override
                public void onFinish() {
                    //.setContentText(getString(R.string.notifDone))  // wat zat??? int?? getString?????????

                    waspaused = false;
                    totaltime2 = 0;
                    timerstate = 0;
                    mProgressStatus = 100;
                    notifmethod(mProgressStatus, true);
                    if (pager.getCurrentItem() == 0) {
                        MainActivityFragment mainActivityFragment2 = (MainActivityFragment) pager.getAdapter().instantiateItem(pager, pager.getCurrentItem());
                        makesnack("notifdone", "doneaction");
                        mainActivityFragment2.circleprogress(mProgressStatus);
                        mainActivityFragment2.showbuttonalarm();
                        mainActivityFragment2.setmTextField("Eggs are ready!");
                    }

                    SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    boolean playalarm = sharedPref.getBoolean("alarmpreference", true);
                    boolean vibrate = sharedPref.getBoolean("vibratorpreference", false);
                    String alarm = sharedPref.getString("ringtonepreference", "null");
                    Uri alarmuri = Uri.parse(alarm);
                    ring = RingtoneManager.getRingtone(getApplicationContext(), alarmuri);
                    if (playalarm || vibrate) {
                        Intent intent = new Intent(getApplicationContext(), AlarmService.class);
                        intent.putExtra("alarm-uri", alarm);
                        intent.putExtra("vibrate-boolean", vibrate);
                        intent.putExtra("alarm-boolean", playalarm);
                        getApplicationContext().startService(intent);

                        Intent resultIntent = getIntent();
                        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(getApplication(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        int lightscolor = sharedPref.getInt("preference_color", 0xFFA4C639);
                        boolean led = sharedPref.getBoolean("ledpreference", true);
                        mBuilder2
                                .setOngoing(false)
                                .setAutoCancel(true) // notification automatically dismissed when the user touches it
                                .setSmallIcon(R.drawable.ic_alarm_off_24dp)
                                .setContentTitle("PerfEGGtion")
                                .setFullScreenIntent(fullScreenPendingIntent, true)
                                .setCategory("CATEGORY_ALARM")
                                .setContentText("DONEDONEDONE");

                        if (led) {
                            mBuilder2.setLights(lightscolor, 500, 500);
                        }
                        Intent notificationIntent = new Intent();
                        notificationIntent.setAction("me.aupifb.perfeggtion.ACTION_STOP_ALARMSERVICE");
                        PendingIntent notificationtPendingIntent = PendingIntent.getBroadcast(getApplication(), 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                        NotificationCompat.Action testaction = new NotificationCompat.Action.Builder(R.drawable.ic_help_black_24dp, "Stop alarm", notificationtPendingIntent).build();
                        if (!action2added) {
                            mBuilder2.addAction(testaction);
                        }
                        NotificationManager mNotificationManager =
                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        // mId allows you to update the notification later on.
                        mNotificationManager.cancel(mId);
                        mNotificationManager.notify(mId2, mBuilder2.build());
                        action2added = true;
                    } else {
                        notificationdone();
                    }

                }
            }.start();
        }
    }

    private void makesnack(String notifdone, String doneaction) {
        Snackbar.make(findViewById(R.id.coordinatorLayout), notifdone, Snackbar.LENGTH_LONG)
                .setAction(doneaction, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("lol", "snackmethodinfragment");
                    }
                })
                .setActionTextColor(16717848)
                .show();
    }


    private void notificationdone() {
        Intent resultIntent = this.getIntent();
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(getApplication(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        int lightscolor = sharedPref.getInt("preference_color", 0xFFA4C639);
        boolean led = sharedPref.getBoolean("ledpreference", true);

        mBuilder3
                .setOngoing(false)
                .setAutoCancel(true) // notification automatically dismissed when the user touches it
                .setSmallIcon(R.drawable.ic_add_alarm_white_24dp)
                .setContentTitle(getString(R.string.notif_title))
                .setShowWhen(false)
                .setContentText("DONEDONEDONE");

        if (led) {
            mBuilder2.setLights(lightscolor, 500, 500);
        }

        mBuilder3
                .setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(mId3, mBuilder3.build());
    }

    public void timeselectalertdialog() {
        if (timerstate != 0) {
            android.app.DialogFragment newFragment3 = TimerConflictDialogFragment.newInstance(
                    R.string.alert_dialog_conflict_title);
            newFragment3.show(getFragmentManager(), "TimerConflictDialogFragment");
        } else timeselectalertdialog2();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(null);
        Log.d("lol", "onConfigurationChanged ");
    }

    public void timeselectalertdialog2() {
        DialogFragment newFragment = TimeSelectDialogFragment.newInstance(
                R.string.time_select_alert_dialog_title);
        newFragment.show(getSupportFragmentManager(), "timeselectdialogtag");
    }

    public void stopalarm() {
        Intent stopIntent = new Intent(getApplicationContext(), AlarmService.class);
        getApplicationContext().stopService(stopIntent);
    }

    public void setCurrentMainViewPagerItem(int position) {
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setCurrentItem(position);
    }
}
