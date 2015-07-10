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
    Boolean vibrate, playalarm;
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
        vibrate = intent.getBooleanExtra("vibrate-boolean", false);
        playalarm = intent.getBooleanExtra("alarm-boolean", false);

        if (playalarm) {
            ringtone = RingtoneManager.getRingtone(this, alarmuri);
            String alarm = intent.getExtras().getString("alarm-uri");
            if (ringtone == null || alarm.equals("null")) {
                ringtone = RingtoneManager.getRingtone(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
            }

            ringtone.play();
        }

        if (vibrate) {
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
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy()
    {
        if (playalarm) ringtone.stop();
        if (vibrate) {
            vibratordone.cancel();
            vibrationActive = false;
        }
    }

}
