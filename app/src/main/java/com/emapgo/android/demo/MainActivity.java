package com.emapgo.android.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.emapgo.android.core.permissions.PermissionsListener;
import com.emapgo.android.core.permissions.PermissionsManager;
import com.emapgo.android.demo.activity.DataVisualizationListActivity;
import com.emapgo.android.demo.activity.DrawListActivity;
import com.emapgo.android.demo.activity.HelloEmgMapActivity;
import com.emapgo.android.demo.activity.LocationListActivity;
import com.emapgo.android.demo.activity.MapCameraListActivity;
import com.emapgo.android.demo.activity.MapComponentListActivity;
import com.emapgo.android.demo.activity.MapEventListActivity;
import com.emapgo.android.demo.activity.MapQueryListActivity;
import com.emapgo.android.demo.activity.MapRouteListActivity;
import com.emapgo.android.demo.activity.MapThreeStereoscopicListActivity;
import com.emapgo.android.demo.activity.RuntimeListActivity;
import com.emapgo.android.demo.activity.SettingsActivity;
import com.emapgo.android.demo.fragment.MapDemoFragment;
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.maps.OnMapReadyCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PermissionsListener {


    private PermissionsManager permissionsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initToolbarAndNavView();
        initMapView(savedInstanceState);
        initCheckPermission();
    }
    private void initCheckPermission() {

        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            //定位权限已授权
        } else {
            //请求定位权限
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }
    private void initMapView(Bundle savedInstanceState) {
        MapDemoFragment demoFragment = null;
        if (savedInstanceState == null) {
            demoFragment = MapDemoFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.id_container, demoFragment, "fragment:tag:emg")
                    .commit();
        }else{
            demoFragment = (MapDemoFragment) getSupportFragmentManager().findFragmentByTag("fragment:tag:emg");
        }

    }

    /**
     * initToolbarAndNavView
     */
    private void initToolbarAndNavView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setCheckedItem(R.id.nav_demo);
    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

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
        switch (id) {
            case R.id.nav_hello_emg:{
                startActivity(new Intent(this, HelloEmgMapActivity.class));
                return true;
            }
            case R.id.nav_map_draw:{
                startActivity(new Intent(this, DrawListActivity.class));
                break;
            }
            case R.id.nav_map_component:{
                startActivity(new Intent(this, MapComponentListActivity.class));
                break;
            }
            case R.id.nav_map_event:{
                startActivity(new Intent(this, MapEventListActivity.class));
                break;
            }
            case R.id.nav_map_camera:{
                startActivity(new Intent(this, MapCameraListActivity.class));
                break;
            }
            case R.id.nav_map_runtime:{
                startActivity(new Intent(this, RuntimeListActivity.class));
                break;
            }
            case R.id.nav_data_visualization:{
                startActivity(new Intent(this, DataVisualizationListActivity.class));
                break;
            }
            case R.id.nav_map_3d:{
                startActivity(new Intent(this, MapThreeStereoscopicListActivity.class));
                break;
            }
            case R.id.nav_map_location:{
                startActivity(new Intent(this, LocationListActivity.class));
                break;
            }
            case R.id.nav_map_poi:{
                startActivity(new Intent(this, MapQueryListActivity.class));
                break;
            }
            case R.id.nav_map_route:{
                //startActivity(new Intent(this, MapRouteListActivity.class));
                break;
            }
            case R.id.nav_setting:{
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (!granted) {
            Toast.makeText(this, "定位权限被拒绝", Toast.LENGTH_LONG).show();
            this.finish();
        }
    }
}
