package com.drago.craze.extensions;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.StatFs;
import android.text.format.Formatter;
import android.util.Log;

import java.io.File;

public class AppExtensions {
    final Context mainContext;
    final PackageManager packageManager;

    public AppExtensions(Context mainContext) {
        this.mainContext = mainContext;
        this.packageManager = mainContext.getPackageManager();
    }
    public long getAppSize(String packageName) {
        try {
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            String apkPath = applicationInfo.sourceDir;
            long apkSize = getFileSize(apkPath);

            return apkSize;

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    // Get file size in bytes
    private long getFileSize(String path) {
        File file = new File(path);
        long size = file.length();
        Log.d("FILESIZE", "getFilePath: "+size);
        return size;
    }

    // Get cache size for API level below Nougat (Android 7.0)

}
