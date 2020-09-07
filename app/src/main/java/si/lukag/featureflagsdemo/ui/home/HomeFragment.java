package si.lukag.featureflagsdemo.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import si.lukag.featureflagsdemo.R;
import si.lukag.featureflagsdemo.adapters.FFAdapter;
import si.lukag.featureflagsmodule.FeatureFlagsModule;

public class HomeFragment extends Fragment {
    public static final String TAG = HomeFragment.class.getSimpleName();

    @BindView(R.id.ff_list)
    RecyclerView ff_list;
    private RecyclerView.LayoutManager layoutManager;
    private FFAdapter ffAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        ButterKnife.bind(this, root);
        ff_list.hasFixedSize();

        Integer decision = FeatureFlagsModule.isEnabled("ui_type");
        if (decision == 1) {
            layoutManager = new GridLayoutManager(getContext(), 2);
            ff_list.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.HORIZONTAL));
        } else {
            layoutManager = new LinearLayoutManager(getContext());
        }
        ff_list.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));

        ff_list.setLayoutManager(layoutManager);
        ffAdapter = new FFAdapter(getContext());
        ff_list.setItemAnimator(new DefaultItemAnimator());
        ff_list.setAdapter(ffAdapter);

        return root;
    }
}