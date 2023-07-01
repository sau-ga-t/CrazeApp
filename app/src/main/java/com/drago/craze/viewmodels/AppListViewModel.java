package com.drago.craze.viewmodels;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.drago.craze.FilterDialogFragment;
import com.drago.craze.extensions.AppExtensions;
import com.drago.craze.models.Filter;
import com.drago.craze.models.InstalledApp;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

public class AppListViewModel extends ViewModel {
    private final MutableLiveData<List<InstalledApp>> installedApps = new MutableLiveData<>();
    private final MutableLiveData<List<String>> selectedApps = new MutableLiveData<>();
    private final MutableLiveData<Long> totalAppSize = new MutableLiveData<>();
    private final MutableLiveData<Filter> filterMutableLiveData = new MutableLiveData<>();

    public LiveData<List<InstalledApp>> getInstalledApps() {
        return installedApps;
    }
    public LiveData<List<String>> getSelectedApps() {
        return selectedApps;
    }
    public LiveData<Long> getTotalSize(){
        return totalAppSize;
    }

    public LiveData<Filter> getFilter(){
        return filterMutableLiveData;
    }

    public void setFilter(Filter filter){
        filterMutableLiveData.setValue(filter);
    }
    private AppExtensions appExtensions ;
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
        totalAppSize.setValue(0L);
    }

    public void updateTotalAppSize(String appId, long appSize){
        long totalSize = totalAppSize.getValue()!=null?totalAppSize.getValue():0;

        if (Objects.requireNonNull(getSelectedApps().getValue()).contains(appId)){
            totalSize+=appSize;
        }else {
            totalSize-=appSize;
        }
        totalAppSize.setValue(totalSize);
    }
    public void fetchInstalledApps(Context context) {
        PackageManager packageManager = context.getPackageManager();
        appExtensions = new AppExtensions(context);
        List<ApplicationInfo> apps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        List<InstalledApp> installedAppList = new ArrayList<>();
        Filter filter = filterMutableLiveData.getValue();

        for (ApplicationInfo app : apps) {
            if ((app.flags & ApplicationInfo.FLAG_SYSTEM) == 0 && filter.isSelectedUser()) {
                Drawable icon = app.loadIcon(packageManager);
                String appName = app.loadLabel(packageManager).toString();
                String packageName = app.packageName;
                long appSize = appExtensions.getAppSize(packageName);
                InstalledApp installedApp = new InstalledApp(appName, packageName, icon, appSize);
                installedAppList.add(installedApp);
            }
            if ((app.flags & ApplicationInfo.FLAG_SYSTEM) == 1 && filter.isSelectedSystem()) {
                Drawable icon = app.loadIcon(packageManager);
                String appName = app.loadLabel(packageManager).toString();
                String packageName = app.packageName;
                long appSize = appExtensions.getAppSize(packageName);
                InstalledApp installedApp = new InstalledApp(appName, packageName, icon, appSize);
                installedAppList.add(installedApp);
            }
        }
        if (filter.getSorting()== FilterDialogFragment.sort.SIZE && filter.getOrdering()== FilterDialogFragment.order.DESC){
            installedAppList.sort((t, t1) -> Integer.compare((int) t1.getAppSize(),(int)t.getAppSize()));
        }
        if (filter.getSorting()== FilterDialogFragment.sort.SIZE && filter.getOrdering()== FilterDialogFragment.order.ASC){
            installedAppList.sort((t, t1) -> Integer.compare((int)t.getAppSize(),(int) t1.getAppSize()));
        }if (filter.getSorting()== FilterDialogFragment.sort.NAME && filter.getOrdering()== FilterDialogFragment.order.DESC){
            installedAppList.sort((t, t1) -> t1.getAppName().compareToIgnoreCase(t.getAppName()));
        }
        if (filter.getSorting()== FilterDialogFragment.sort.NAME && filter.getOrdering()== FilterDialogFragment.order.ASC){
            installedAppList.sort((t, t1) -> t.getAppName().compareToIgnoreCase(t1.getAppName()));
        }
        installedApps.setValue(installedAppList);
    }

    public void setFilters(FilterDialogFragment.sort sorting, FilterDialogFragment.order ordering, boolean isSystem, boolean isUser){
        Filter filter = new Filter(sorting, ordering, isUser, isSystem);
        filterMutableLiveData.setValue(filter);
    }
}
