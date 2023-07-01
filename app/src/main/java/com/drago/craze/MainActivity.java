package com.drago.craze;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drago.craze.extensions.AppInstallReceiver;
import com.drago.craze.models.Filter;
import com.drago.craze.viewadapters.AppListAdapter;
import com.drago.craze.viewmodels.AppListViewModel;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity{
    String CREATED_TAG = "CREATED_TAG";
    int MAIN_RECYCLER_VIEW = R.id.main_app_list;
    int UNINSTALL_FAB = R.id.uninstall_fab;
    private AppListViewModel appListViewModel;
    private final ActivityResultLauncher<String> uninstallLauncher =
            registerForActivityResult(new UninstallAppContract(), result -> {
                if (result) {
                    appListViewModel.fetchInstalledApps(getApplicationContext());
                    appListViewModel.clearSelectedApps();
                } else {
                    Toast.makeText(getApplicationContext(), "Uninstallation failed", Toast.LENGTH_SHORT).show();
                }
            });

    private AppListAdapter appListAdapter;
    private BroadcastReceiver appInstallReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            appListViewModel.fetchInstalledApps(getApplicationContext());
        }
    };
    private Filter filter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(CREATED_TAG, "onCreate: Main activity");
        filter = new Filter(FilterDialogFragment.sort.SIZE, FilterDialogFragment.order.ASC, true, true);
        ExtendedFloatingActionButton uninstall_fab = findViewById(UNINSTALL_FAB);
        uninstall_fab.setOnClickListener(view -> {
            List<String> uninstallList = appListViewModel.getSelectedApps().getValue();
            if (uninstallList != null) {
                for (String app : uninstallList
                ) {
                    uninstallApp(app);
                }
            }
        });

        RecyclerView appList = findViewById(MAIN_RECYCLER_VIEW);
        appList.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        appListViewModel = new ViewModelProvider(this).get(AppListViewModel.class);

        appListViewModel.setFilter(filter);
        appListAdapter = new AppListAdapter(new ArrayList<>(), appListViewModel);
        appList.setAdapter(appListAdapter);

        appListViewModel.getInstalledApps().observe(this, installedApps -> {
            appListAdapter.setApps(installedApps);
            appListAdapter.notifyDataSetChanged();
        });
        appListViewModel.getTotalSize().observe(this, appSize->{
            Log.d("APPSIZE", "onCreate: "+appSize);
            if(appSize>0) {
                uninstall_fab.setText(String.format("Remove %s", getFormattedApkSize(appSize, getApplicationContext())));
            }
            });
        appListViewModel.getSelectedApps().observe(this, selectedApps -> {
            appListAdapter.setSelectedApps(selectedApps);
            appListAdapter.notifyDataSetChanged();
            if (selectedApps.size()>0){
                getSupportActionBar().setTitle(String.format("%d Apps Selected", selectedApps.size()) );

                uninstall_fab.setVisibility(View.VISIBLE);
            }else {
                uninstall_fab.setVisibility(View.GONE);
                appListAdapter.setSelectionEnabled(false);
            }
        });
        appListViewModel.fetchInstalledApps(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        appInstallReceiver = new AppInstallReceiver(appListViewModel, this);
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        intentFilter.addDataScheme("package");
        registerReceiver(appInstallReceiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(appInstallReceiver);
    }

    private void uninstallApp(String packageName) {
        uninstallLauncher.launch(packageName);
    }
    private void showFilterDialog() {
        FilterDialogFragment filterDialog = new FilterDialogFragment();
        filterDialog.show(getSupportFragmentManager(), "filter_dialog");
    }

    public static class UninstallAppContract extends ActivityResultContract<String, Boolean> {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, String packageName) {
            Intent uninstallIntent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE);
            uninstallIntent.setData(Uri.parse("package:" + packageName));
            uninstallIntent.putExtra(Intent.EXTRA_RETURN_RESULT, true);
            return uninstallIntent;
        }

        @Override
        public Boolean parseResult(int resultCode, @Nullable Intent intent) {
            return resultCode == Activity.RESULT_OK;
        }
    }
    String getFormattedApkSize(long apkSize, Context mainContext) {
        return Formatter.formatFileSize(mainContext, apkSize);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_filter) {
            showFilterDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}