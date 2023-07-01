package com.drago.craze;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;

import com.drago.craze.viewmodels.AppListViewModel;

import java.util.Objects;

public class FilterDialogFragment extends DialogFragment {
    private RadioGroup sortRadioGroup;
    private RadioGroup orderRadioGroup;
    private CheckBox userAppCheckBox;
    private CheckBox systemAppCheckBox;
    private sort sorting = sort.NAME;
    private order ordering = order.ASC;
    private boolean userSelected = true;
    private boolean systemSelected = false;

    private AppListViewModel appListViewModel;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_filter, null);
        appListViewModel = new ViewModelProvider(this).get(AppListViewModel.class);

        sortRadioGroup = dialogView.findViewById(R.id.sortRadioGroup);
        orderRadioGroup = dialogView.findViewById(R.id.orderRadioGroup);
        userAppCheckBox = dialogView.findViewById(R.id.userAppCheckBox);
        systemAppCheckBox = dialogView.findViewById(R.id.systemAppCheckBox);

        setInitialSelections();
        setListeners();

        builder.setView(dialogView)
                .setTitle("Filter")
                .setPositiveButton("Apply", (dialog, which) -> applyFilter())
                .setNegativeButton("Cancel", (dialog, which) -> dismiss());
        return builder.create();
    }
    private void setInitialSelections() {
        //appListViewModel.setFilters(sorting, ordering, userSelected, systemSelected);
        sortRadioGroup.check(R.id.appSizeRB);
        orderRadioGroup.check(R.id.appAscRB);
        userAppCheckBox.setChecked(true);
        systemAppCheckBox.setChecked(false);
    }
    private void setListeners() {
        sortRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId==R.id.appSizeRB){
                sorting = sort.SIZE;
            }else {
                sorting = sort.NAME;
            }
        });

        orderRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId==R.id.appAscRB){
                ordering = order.ASC;
            }else {
                ordering = order.DESC;
            }
        });

        userAppCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            userSelected = isChecked;
        });

        systemAppCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            systemSelected = isChecked;
        });
    }

    private void applyFilter() {
        Log.d("CRAZE_APPLY", "applyFilter: "+sorting+ordering+userSelected+systemSelected);
        appListViewModel.setFilters(sorting, ordering, userSelected, systemSelected);
        appListViewModel.fetchInstalledApps(requireActivity());
        Toast.makeText(getContext(), "Applied", Toast.LENGTH_LONG).show();
    }

    public enum sort{
        SIZE,NAME
    }
    public enum order{
        ASC, DESC
    }
}
