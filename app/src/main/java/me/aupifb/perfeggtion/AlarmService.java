package me.aupifb.perfeggtion;

import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;

public class AlarmService extends Service {

    private Ringtone ringtone;
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
        MainActivity.statering = true;

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy()
    {
        ringtone.stop();
        MainActivity.statering = false;
    }

}
