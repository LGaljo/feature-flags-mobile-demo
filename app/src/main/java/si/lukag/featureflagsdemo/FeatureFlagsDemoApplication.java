package si.lukag.featureflagsdemo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import si.lukag.featureflagsmodule.FeatureFlagsModule;

import static si.lukag.featureflagsdemo.StaticConfig.APP_NAME;
import static si.lukag.featureflagsdemo.StaticConfig.BASE_URL;
import static si.lukag.featureflagsmodule.Config.sp_appName;
import static si.lukag.featureflagsmodule.Config.sp_baseUrl;
import static si.lukag.featureflagsmodule.Config.sp_name;

public class FeatureFlagsDemoApplication extends Application {
    private static final String TAG = FeatureFlagsDemoApplication.class.getSimpleName();

    public FeatureFlagsDemoApplication() {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        getSharedPreferences(sp_name, Context.MODE_PRIVATE)
                .edit()
                .putString(sp_appName, APP_NAME)
                .putString(sp_baseUrl, BASE_URL)
                .apply();

        FeatureFlagsModule ffm = FeatureFlagsModule.getInstance(this);
        ffm.heartbeat(this);
    }
}
