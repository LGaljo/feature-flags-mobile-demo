package si.lukag.featureflagsdemo.ui.info;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import butterknife.OnClick;
import si.lukag.featureflagsdemo.R;
import si.lukag.featureflagsdemo.services.HeartbeatService;

public class InfoFragment extends Fragment {
    public static final String TAG = InfoFragment.class.getSimpleName();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @OnClick(R.id.refreshFlagsBtn)
    public void refreshFF() {
        Log.d(TAG, "refreshFF, send work");
        HeartbeatService.enqueueWork(getContext(), new Intent());
    }
}