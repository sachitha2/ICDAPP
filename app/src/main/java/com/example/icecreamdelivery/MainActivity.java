package com.example.icecreamdelivery;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static String loggedAccount;

    public static SharedPreferences sharedPreferences03;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(lookForLoggedAccount(MainActivity.this)){//there is no logged account

            Intent intentLogin = new Intent();
            intentLogin.setClass(MainActivity.this, Login.class);
            intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            finish();

            startActivity(intentLogin);

        }else {

            sharedPreferences03 = getSharedPreferences("loginInfo", MainActivity.MODE_PRIVATE);

            SharedPreferences sharedPreferences01 = getSharedPreferences("loginInfo", MainActivity.MODE_PRIVATE);
            loggedAccount = sharedPreferences01.getString("uName", "");

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            navigationView.setNavigationItemSelectedListener(this);

        }

    }

    public boolean lookForLoggedAccount(Context context){
        SharedPreferences sharedPreferences = getSharedPreferences("loginInfo", context.MODE_PRIVATE);
        String Username = sharedPreferences.getString("uName","");
        int loginStatus = sharedPreferences.getInt("loginStatus",-1);
        int vehicleId = sharedPreferences.getInt("vehicleId",-1);
        if(loginStatus != 1){
            return true;
        }else {
            return false;
        }
    }

    long back_pressed;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (back_pressed + 1000 > System.currentTimeMillis()){
//                super.onBackPressed();

                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                return;
            }
            else{
                Toast.makeText(getBaseContext(),
                        "Press again to exit!", Toast.LENGTH_SHORT).show();
            }
            back_pressed = System.currentTimeMillis();

        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_items) {

            Intent intentItems = new Intent(MainActivity.this, Items.class);
            startActivity(intentItems);

        } else if (id == R.id.nav_shops) {

            Intent intentShops = new Intent(MainActivity.this, Shops.class);
            startActivity(intentShops);

        } else if (id == R.id.nav_return_items) {

            Intent intentReturnItems = new Intent(MainActivity.this, ReturnItems.class);
            startActivity(intentReturnItems);

        } else if (id == R.id.nav_all_invoices) {

            Intent intentAllInvoices = new Intent(MainActivity.this, AllInvoices.class);
            startActivity(intentAllInvoices);

        } else if (id == R.id.nav_download_data) {

            Toast.makeText(MainActivity.this, "Downloading...", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_upload_data) {

            Toast.makeText(MainActivity.this, "Uploading...", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_settings) {

            Intent intentSettings = new Intent(MainActivity.this, Settings.class);
            startActivity(intentSettings);

        } else if (id == R.id.nav_logout) {

            SharedPreferences.Editor editor = sharedPreferences03.edit();

            editor.putInt("loginStatus", 0);
            editor.apply();

            Intent intentLogin = new Intent();
            intentLogin.setClass(MainActivity.this, Login.class);
            intentLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            MainActivity.this.startActivity(intentLogin);

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
