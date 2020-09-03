package si.lukag.featureflagsdemo.services;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import si.lukag.featureflagsdemo.config.FeatureFlagsModule;
import si.lukag.featureflagsdemo.models.RuleDto;
import si.lukag.featureflagsdemo.retrofit.APICalls;
import si.lukag.featureflagsdemo.retrofit.RetrofitFactory;

import static si.lukag.featureflagsdemo.config.Config.BASE_URL;
import static si.lukag.featureflagsdemo.config.Config.sp_clientId;
import static si.lukag.featureflagsdemo.config.Config.sp_name;

public class HeartbeatService extends JobIntentService {
    public static final String TAG = HeartbeatService.class.getSimpleName();
    private static final int JOB_ID = 573;

    private Retrofit retrofit = RetrofitFactory.getInstance(BASE_URL);
    private APICalls apiCalls = retrofit.create(APICalls.class);

    /**
     * Convenience method for enqueuing work in to this service.
     */
    public static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, HeartbeatService.class, JOB_ID, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        Log.d(TAG, "Request rules");
        SharedPreferences sp = getSharedPreferences(sp_name, Context.MODE_PRIVATE);
        String clientID = sp.getString(sp_clientId, null);

        Call<List<RuleDto>> call = apiCalls.getRulesByClientID(clientID);
        call.enqueue(new retrofit2.Callback<List<RuleDto>>() {
            @Override
            public void onResponse(@NotNull Call<List<RuleDto>> call, @NotNull Response<List<RuleDto>> response) {
                if (response.body() != null) {
                    if (response.isSuccessful()) {
                        Log.d(TAG, response.body().toString());
                        saveFlags(response.body());
                    }
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<RuleDto>> call, @NotNull Throwable t) {
                Log.e(TAG, "Error", t);
            }
        });
    }

    private void saveFlags(List<RuleDto> list) {
        list.forEach(rule -> {
            FeatureFlagsModule.setFeatureFlagValue(rule.getName(), rule);
        });
        FeatureFlagsModule.commitChanges(this);
    }
}
