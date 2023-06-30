package com.drago.craze.extensions;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.drago.craze.viewmodels.AppListViewModel;

public class AppInstallReceiver extends BroadcastReceiver {
    private final AppListViewModel appListViewModel;
    private final Activity activity;

    public AppInstallReceiver(AppListViewModel viewModel, Activity activity) {
        this.appListViewModel = viewModel;
        this.activity = activity;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_PACKAGE_ADDED.equals(intent.getAction())) {
            String packageName = intent.getData().getSchemeSpecificPart();
            // Handle the newly installed app here
            Toast.makeText(context, "New app installed: " + packageName, Toast.LENGTH_SHORT).show();
            appListViewModel.fetchInstalledApps(activity);
        }
    }
}
