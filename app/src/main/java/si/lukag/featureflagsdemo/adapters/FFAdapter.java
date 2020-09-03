package si.lukag.featureflagsdemo.adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import si.lukag.featureflagsdemo.MainActivity;
import si.lukag.featureflagsdemo.R;
import si.lukag.featureflagsdemo.config.FeatureFlagsModule;
import si.lukag.featureflagsdemo.models.RuleDto;

public class FFAdapter extends RecyclerView.Adapter<FFAdapter.ffViewHolder> {
    private final FeatureFlagsModule ffm;
    private ArrayList<RuleDto> rules;
    private Context context;

    public class ffViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ff_name) TextView ff_name;
        @BindView(R.id.ff_value) TextView ff_value;
        @BindView(R.id.ff_description) TextView ff_description;

        public ffViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        void bind(RuleDto ruleDto) {
            itemView.setOnClickListener(v -> openDialog(ruleDto));
        }

        private void openDialog(RuleDto ruleDto) {
            AlertDialog dialog = new AlertDialog.Builder(context).create();
            dialog.setTitle(context.getString(R.string.ff_dialog_title) + " " + ruleDto.getName());
            dialog.setMessage(context.getString(R.string.ff_dialog_message));

            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.ff_dialog, null);
            dialog.setView(view);
            EditText editText = view.findViewById(R.id.ff_dialog_value);
            editText.setText(String.valueOf(ruleDto.getValue()));

            dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Save", (dialog1, which) -> {
                ffm.changeRuleValue(ruleDto.getName(), Integer.decode(editText.getText().toString()), context);
                rules = toArrayList(ffm.getFFs());
                notifyDataSetChanged();

                Intent i = new Intent(context, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            });
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", (dialog1, which) -> {
                Toast.makeText(context, "No changes applied", Toast.LENGTH_SHORT).show();
            });
            dialog.show();
        }
    }

    public FFAdapter(Context context) {
        this.ffm = FeatureFlagsModule.getInstance(context);
        this.rules = toArrayList(ffm.getFFs());
        this.context = context;
    }

    @NonNull
    @Override
    public ffViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ff_list_item, parent, false);

        return new ffViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ffViewHolder holder, int position) {
        RuleDto r = rules.get(position);

        holder.ff_name.setText(r.getName());
        holder.ff_description.setText(r.getDescription());
        holder.ff_value.setText(String.valueOf(r.getValue()));

        holder.bind(r);
    }

    @Override
    public int getItemCount() {
        return rules.size();
    }

    private ArrayList<RuleDto> toArrayList(HashMap<String, RuleDto> fFs) {
        ArrayList<RuleDto> list = new ArrayList<>();
        for (Map.Entry<String, RuleDto> r : fFs.entrySet()) {
            list.add(r.getValue());
        }
        return list;
    }
}
