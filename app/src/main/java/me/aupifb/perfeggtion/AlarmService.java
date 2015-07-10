package me.aupifb.perfeggtion;

import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;

public class AlarmService extends Service {

    Vibrator vibratordone;
    boolean vibrationActive = true;
    private Ringtone ringtone;
    private Handler mHandler = new Handler(); // required for progress bar

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Uri alarmuri = Uri.parse(intent.getExtras().getString("alarm-uri"));

        ringtone = RingtoneManager.getRingtone(this, alarmuri);
        String alarm = intent.getExtras().getString("alarm-uri");
        if (ringtone == null || alarm.equals("null")) {
            ringtone = RingtoneManager.getRingtone(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
        }

        ringtone.play();

        /*vibratordone = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        new Thread(new Runnable() {
        public void run() {
            // Update the progress bar
            mHandler.post(new Runnable() {
                public void run() {
                    while (vibrationActive) {
                        try {
                            vibratordone.vibrate(500);
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            Log.d("lol", "no sleep!!!! ");
                        }
                    }

                }
            });
        }
    }).start();*/

        vibratordone = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (vibrationActive) {
                    try {
                        vibratordone.vibrate(400);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        /*long pattern[] = {500, 500, 500};
        vibratordone.vibrate(pattern, 0);*/

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy()
    {
        ringtone.stop();
        vibratordone.cancel();
        vibrationActive = false;
    }

}
