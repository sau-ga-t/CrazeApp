package com.drago.craze.models;

import android.graphics.drawable.Drawable;

public class InstalledApp {
    private String appName;
    private String packageName;
    private Drawable appIcon;
    public String getAppName() {
        return appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public InstalledApp(String appName, String packageName, Drawable appIcon) {
        this.appName = appName;
        this.packageName = packageName;
        this.appIcon = appIcon;
    }

}
