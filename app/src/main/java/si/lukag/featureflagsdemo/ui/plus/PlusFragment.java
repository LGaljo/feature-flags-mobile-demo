package si.lukag.featureflagsdemo.ui.plus;

import android.os.Bundle;
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

import si.lukag.featureflagsdemo.R;
import si.lukag.featureflagsdemo.ui.info.InfoFragment;

public class PlusFragment extends Fragment {
    public static final String TAG = PlusFragment.class.getSimpleName();

    private PlusViewModel plusViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        plusViewModel = new ViewModelProvider(this).get(PlusViewModel.class);
        View root = inflater.inflate(R.layout.fragment_plus, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        plusViewModel.getText().observe(getViewLifecycleOwner(), s -> textView.setText(s));
        return root;
    }
}