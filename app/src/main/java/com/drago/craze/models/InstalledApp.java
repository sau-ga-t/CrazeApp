package com.drago.craze.models;

import android.graphics.drawable.Drawable;

public class InstalledApp {
    private String appName;
    private String packageName;
    private Drawable appIcon;
    private long appSize;

    public long getAppSize() {
        return appSize;
    }

    public String getAppName() {
        return appName;
    }

    public String getPackageName() {
        return packageName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public InstalledApp(String appName, String packageName, Drawable appIcon, long appSize) {
        this.appName = appName;
        this.packageName = packageName;
        this.appIcon = appIcon;
        this.appSize = appSize;
    }

}
