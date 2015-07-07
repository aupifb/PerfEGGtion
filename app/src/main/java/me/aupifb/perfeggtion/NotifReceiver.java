package me.aupifb.perfeggtion;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by aupifb on 07/07/2015.
 */
public class NotifReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent stopIntent = new Intent(context, AlarmService.class);
        context.stopService(stopIntent);
    }
}
