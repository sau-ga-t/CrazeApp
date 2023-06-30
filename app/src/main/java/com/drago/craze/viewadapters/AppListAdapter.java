package com.drago.craze.viewadapters;

import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drago.craze.R;
import com.drago.craze.models.InstalledApp;
import com.drago.craze.viewmodels.AppListViewModel;

import java.util.ArrayList;
import java.util.List;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> {
    private List<InstalledApp> apps;
    private List<String> selectedApps = new ArrayList<>();
    private AppListViewModel appListViewModel;
    private boolean selectionEnabled = false;

    public AppListAdapter(List<InstalledApp> apps, AppListViewModel viewModel) {
        this.apps = apps;
        this.appListViewModel = viewModel;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        InstalledApp app = apps.get(position);
        holder.bind(app);
    }

    public void setApps(List<InstalledApp> apps) {
        this.apps = apps;
    }
    public void setSelectedApps(List<String> selectedApps){
        this.selectedApps = selectedApps;
    }
    @Override
    public int getItemCount() {
        return apps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView appNameTextView;
        private TextView packageNameTextView;
        private ImageView appIconImageView;
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(view -> {
                if (selectionEnabled) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        InstalledApp app = apps.get(position);
                        appListViewModel.updateSelectedApps(app.getPackageName());
                        notifyItemChanged(position);
                    }
                }
            });
            itemView.setOnLongClickListener(view -> {
                selectionEnabled = true;
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    InstalledApp app = apps.get(position);
                    appListViewModel.updateSelectedApps(app.getPackageName());
                    notifyItemChanged(position);
                }
                return false;
            });
            appNameTextView = itemView.findViewById(R.id.appNameTextView);
            appNameTextView.setTypeface(null, Typeface.BOLD);
            packageNameTextView = itemView.findViewById(R.id.packageNameTextView);
            appIconImageView = itemView.findViewById(R.id.appIconImageView);
        }

        public void bind(InstalledApp app) {
            appNameTextView.setText(app.getAppName());
            packageNameTextView.setText(app.getPackageName());
            appIconImageView.setImageDrawable(app.getAppIcon());
            // Set the background color based on the selection state
            boolean isSelected = selectedApps.contains(app.getPackageName());

            int backgroundColor =  isSelected ? Color.parseColor("#deebff") : Color.WHITE;
            itemView.setBackgroundColor(backgroundColor);
        }

    }
}
