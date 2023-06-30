package com.drago.craze.viewmodels;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.drago.craze.models.InstalledApp;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AppListViewModel extends ViewModel {
    private final MutableLiveData<List<InstalledApp>> installedApps = new MutableLiveData<>();
    private final MutableLiveData<List<String>> selectedApps = new MutableLiveData<>();

    public LiveData<List<InstalledApp>> getInstalledApps() {
        return installedApps;
    }
    public LiveData<List<String>> getSelectedApps() {
        return selectedApps;
    }

    public void updateSelectedApps(String appId){
        List<String> selectedAppsList = new ArrayList<>();
        if (selectedApps.getValue()!=null){
            selectedAppsList.addAll(selectedApps.getValue());
        }
        if(Objects.requireNonNull(selectedAppsList).contains(appId)){
            Objects.requireNonNull(selectedAppsList).remove(appId);
        }else {
            Objects.requireNonNull(selectedAppsList).add(appId);
        }
        selectedApps.setValue(selectedAppsList);

    }
    public void clearSelectedApps(){
        List<String> selectedAppsList = new ArrayList<>();
        selectedApps.setValue(selectedAppsList);

    }
    public void fetchInstalledApps(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<ApplicationInfo> apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        List<InstalledApp> installedAppList = new ArrayList<>();
        for (ApplicationInfo app : apps) {
            if ((app.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                Drawable icon = app.loadIcon(packageManager);
                String appName = app.loadLabel(packageManager).toString();
                String packageName = app.packageName;
                InstalledApp installedApp = new InstalledApp(appName, packageName, icon);

                installedAppList.add(installedApp);
            }
        }
        installedApps.setValue(installedAppList);
    }
}
