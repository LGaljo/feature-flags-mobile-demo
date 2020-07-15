package si.lukag.featureflagsdemo.config;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
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
import static si.lukag.featureflagsdemo.config.Config.sp_featureFlagsMap;
import static si.lukag.featureflagsdemo.config.Config.sp_name;

public class FeatureFlagsModule {
    private static final String TAG = FeatureFlagsModule.class.getSimpleName();

    private static FeatureFlagsModule ffm;

    private static UUID clientID;

    private Retrofit retrofit = RetrofitFactory.getInstance(BASE_URL);
    private APICalls apiCalls = retrofit.create(APICalls.class);

    private static HashMap<String, Integer> features;

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

        loadConfig(context);
    }

    private static String getJsonString(Context context) {
        String jsonString;
        try {
            InputStream is = context.getAssets().open("feature-flags.json");

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException e) {
            Log.e(TAG, "Unable to load feature flags file", e);
            return null;
        }

        return jsonString;
    }

    private static void readIntoMap(String rawJson) {
        // Read JSON
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String, Integer>>() {
        }.getType();
        Log.d(TAG, rawJson);
        HashMap<String, Integer> map = gson.fromJson(rawJson, type);

        if (features == null) {
            features = new HashMap<>();
        }
        features.putAll(map);
    }

    private static void loadDefaults(Context context) {
        String rawFlags = getJsonString(context);
        readIntoMap(rawFlags);
    }

    private static void loadFromSharedPrefs(Context context) {
        SharedPreferences sp = context.getSharedPreferences(sp_name, Context.MODE_PRIVATE);
        String rawFlags = sp.getString(sp_featureFlagsMap, null);

        if (rawFlags == null) {
            return;
        }

        readIntoMap(rawFlags);
    }

    private static void saveToSharedPrefs(Context context) {
        Gson gson = new Gson();
        context
                .getSharedPreferences(sp_name, Context.MODE_PRIVATE)
                .edit()
                .putString(sp_featureFlagsMap, gson.toJson(features))
                .apply();
    }

    private static void loadConfig(Context context) {
        // Load default flags if missing from shared prefs
        loadDefaults(context);
        loadFromSharedPrefs(context);
        saveToSharedPrefs(context);
    }

    public static Integer getFeatureFlagValue(String name) {
        if (!features.containsKey(name)) {
            // TODO replace throwing exception with predetermined value of flag (think about it)
            throw new RuntimeException("No flag with this name exit");
        }
        return features.get(name);
    }

    public static void setFeatureFlagValue(String name, Integer value) {
        if (features == null) {
            throw new RuntimeException("Feature flag map is null");
        }
        Log.d(TAG, String.format("Add flag %s: %s", name, value));
        features.put(name, value);
    }

    public static void commitChanges(Context context) {
        Log.d(TAG, "Save changes to shared prefs");
        saveToSharedPrefs(context);
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
