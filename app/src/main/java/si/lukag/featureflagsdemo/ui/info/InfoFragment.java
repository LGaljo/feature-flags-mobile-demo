package si.lukag.featureflagsdemo.ui.info;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import butterknife.OnClick;
import si.lukag.featureflagsdemo.R;
import si.lukag.featureflagsdemo.services.HeartbeatService;

public class InfoFragment extends Fragment {
    public static final String TAG = InfoFragment.class.getSimpleName();

    private InfoViewModel infoViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        infoViewModel = new ViewModelProvider(this).get(InfoViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        infoViewModel.getText().observe(getViewLifecycleOwner(), s -> textView.setText(s));
        return root;
    }

    @OnClick(R.id.refreshFlagsBtn)
    public void refreshFF() {
        Log.d(TAG, "refreshFF, send work");
        HeartbeatService.enqueueWork(getContext(), new Intent());
    }
}