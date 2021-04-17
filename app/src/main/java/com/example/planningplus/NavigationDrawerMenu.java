package com.example.planningplus;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class NavigationDrawerMenu extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer_menu);
        TaskViewModel taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView textView = (TextView) headerView.findViewById(R.id.usernameDrawerHeader);
        textView.setText("\n\n" + Database.username);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.mainFragment, R.id.tasksFragment, R.id.plansFragment, R.id.assignedFragment)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        getSupportActionBar().setTitle("Home");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_drawer_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(NavigationDrawerMenu.this, MainActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void proximityAlert(double latitude, double longitude, String planTitle, String planDescription, String username){
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Intent intent = new Intent("com.example.planningplus.proximityAlert").addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("title", planTitle);
        intent.putExtra("description", planDescription);
        intent.putExtra("username", username);
        PendingIntent proximityIntent = PendingIntent.getBroadcast(getApplicationContext(), 5, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            String[] array = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
            ActivityCompat.requestPermissions(this, array, 6);
            Log.i("test", "remind");
            return;
        }
        locationManager.addProximityAlert(latitude, longitude, 1000F, -1, proximityIntent);

        IntentFilter filter = new IntentFilter("com.example.planningplus.proximityAlert");
        Log.i("test", "remind1");
        this.registerReceiver(new ProximityAlertBroadcastReceiver(), filter);
    }

    public void timedAlert(String planTitle, String planDescription, String username, Long time){
        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(NavigationDrawerMenu.this, TimedBroadcastReceiver.class);
        intent.putExtra("title", planTitle);
        intent.putExtra("description", planDescription);
        intent.putExtra("username", username);
        intent.setAction(Long.toString(System.currentTimeMillis()));
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, pendingIntent);
    }
}