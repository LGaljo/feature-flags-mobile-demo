package si.lukag.featureflagsdemo.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import java.util.Random;

import si.lukag.featureflagsdemo.config.FeatureFlagsModule;
import si.lukag.featureflagsdemo.databinding.ActivityMainBBinding;
import si.lukag.featureflagsdemo.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view;

        Integer decision = FeatureFlagsModule.getFeatureFlagValue("Flag1");

        if (decision == 0) {
            ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
            view = binding.getRoot();
        } else {
            ActivityMainBBinding binding2 = ActivityMainBBinding.inflate(getLayoutInflater());
            view = binding2.getRoot();
        }

        setContentView(view);
    }
}