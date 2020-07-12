package si.lukag.featureflagsdemo.config;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import si.lukag.featureflagsdemo.retrofit.APICalls;
import si.lukag.featureflagsdemo.retrofit.RetrofitFactory;
import si.lukag.featureflagsdemo.services.HeartbeatReceiver;

import static si.lukag.featureflagsdemo.config.Config.APP_ID;
import static si.lukag.featureflagsdemo.config.Config.BASE_URL;
import static si.lukag.featureflagsdemo.config.Config.sp_clientId;
import static si.lukag.featureflagsdemo.config.Config.sp_name;

public class FeatureFlagsModule {
    private static final String TAG = FeatureFlagsModule.class.getSimpleName();

    private static FeatureFlagsModule ffm;

    private static UUID clientID;

    private Retrofit retrofit = RetrofitFactory.getInstance(BASE_URL);
    private APICalls apiCalls = retrofit.create(APICalls.class);

    public static FeatureFlagsModule getInstance(Context context) {
        if (ffm == null) {
            ffm = new FeatureFlagsModule();
        }
        init(context);
        return ffm;
    }

    private static void init(Context context) {
        SharedPreferences sp = context.getSharedPreferences(sp_name, Context.MODE_PRIVATE);
        String tmp = sp.getString(sp_clientId, null);

        if (tmp == null) {
            clientID = UUID.randomUUID();
            sp.edit().putString(sp_clientId, clientID.toString()).apply();
        } else {
            clientID = UUID.fromString(tmp);
        }
    }

    public void heartbeat(Context context) {
        Log.d(TAG, "Send heartbeat");
        Call<Response<String>> call = apiCalls.registerUser(clientID.toString(), APP_ID);
        call.enqueue(new retrofit2.Callback<Response<String>>() {
            @Override
            public void onResponse(@NotNull Call<Response<String>> call, @NotNull Response<Response<String>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, "Successfully sent a heartbeat");
                        // TODO schedule service wake up
                        enqueueWork(context);
                    }
                    Log.d(TAG, "Sent a heartbeat" + response.raw().toString());
                }
            }

            @Override
            public void onFailure(@NotNull Call<Response<String>> call, @NotNull Throwable t) {
                Log.e(TAG, "Error", t);
            }
        });
    }

    public static void enqueueWork(Context context) {
        Log.d(TAG, "Work enqueued");
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        int sync_interval = 1000 * 30; // 30 sec

        // Set alarm for next wakeup
        Intent intent = new Intent(context, HeartbeatReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 133, intent, 0);
        alarmManager.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + sync_interval, pendingIntent);

    }
}
