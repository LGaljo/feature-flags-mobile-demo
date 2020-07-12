package si.lukag.featureflagsdemo;

import android.app.Application;

import si.lukag.featureflagsdemo.config.FeatureFlagsModule;

public class FeatureFlagsDemoApplication extends Application {
    private static final String TAG = FeatureFlagsDemoApplication.class.getSimpleName();

    public FeatureFlagsDemoApplication() {

    }

    @Override
    public void onCreate() {
        super.onCreate();

        FeatureFlagsModule ffm = FeatureFlagsModule.getInstance(this);
        ffm.heartbeat(this);
    }
}
