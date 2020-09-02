package si.lukag.featureflagsdemo.ui.plus;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import si.lukag.featureflagsdemo.R;

public class PlusFragment extends Fragment {
    public static final String TAG = PlusFragment.class.getSimpleName();

    private PlusViewModel plusViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        plusViewModel = new ViewModelProvider(this).get(PlusViewModel.class);
        View root = inflater.inflate(R.layout.fragment_plus, container, false);
        final TextView textView = root.findViewById(R.id.text_plus);
        final TextView joke = root.findViewById(R.id.joke);
        plusViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        plusViewModel.getJoke().observe(getViewLifecycleOwner(), s -> joke.setText(Html.fromHtml(s, Html.FROM_HTML_MODE_LEGACY)));
        return root;
    }
}