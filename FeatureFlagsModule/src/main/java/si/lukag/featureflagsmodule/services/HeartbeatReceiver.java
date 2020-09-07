package si.lukag.featureflagsmodule.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class HeartbeatReceiver extends BroadcastReceiver {
    private static final String TAG = BroadcastReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Received broadcast");
        HeartbeatService.enqueueWork(context, intent);
    }
}
