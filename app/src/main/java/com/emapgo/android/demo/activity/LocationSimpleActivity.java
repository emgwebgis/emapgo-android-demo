package com.emapgo.android.demo.activity;

import android.annotation.SuppressLint;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.emapgo.android.core.location.LocationEngineListener;
import com.emapgo.android.core.location.LocationEnginePriority;
import com.emapgo.android.core.location.LocationEngineProvider;
import com.emapgo.android.demo.MyApplication;
import com.emapgo.android.demo.R;
import com.emapgo.android.demo.util.PreferenceManager;
import com.emapgo.mapsdk.camera.CameraUpdateFactory;
import com.emapgo.mapsdk.geometry.LatLng;
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.maps.MapView;
import com.emapgo.mapsdk.maps.OnMapReadyCallback;
import com.emapgo.mapsdk.plugins.locationlayer.LocationLayerMode;
import com.emapgo.mapsdk.plugins.locationlayer.LocationLayerPlugin;

/**
 * 如要在应用中使用定位功能，请在工程中添加依赖关系 implementation 'com.emapgo.mapsdk:emapgo-android-plugin-locationlayer:0.5.0'
 */
public class LocationSimpleActivity extends AppCompatActivity implements OnMapReadyCallback, LocationEngineListener {

    private MapView mapView;
    private EmgMap mEmgMap;
    private com.emapgo.android.core.location.LocationEngine locationEngine;
    private LocationLayerPlugin locationPlugin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_simple);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mapView = (MapView) findViewById(R.id.emgMapView);
        mapView.onCreate(savedInstanceState);
        mapView.setStyleUrl(PreferenceManager.getMapStyle(MyApplication.getAppContext()));
        mapView.getMapAsync(this);
    }
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(EmgMap emgMap) {
        mEmgMap = emgMap;

        locationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.BALANCED_POWER_ACCURACY);
        locationEngine.activate();
       Location lastLocation = locationEngine.getLastLocation();
        if (lastLocation != null) {
            //移动Camera到定位的坐标点
            setCameraPosition(lastLocation);
        } else {
            //没有获取到定位数据，需要设置监听器监听定位结果
            locationEngine.addLocationEngineListener(this);
        }
        locationPlugin = new LocationLayerPlugin(mapView, mEmgMap, locationEngine);
        locationPlugin.setLocationLayerEnabled(LocationLayerMode.COMPASS);
    }
    private void setCameraPosition(Location location) {
        //将Camera移动到定位的坐标点
        mEmgMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 14));
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected() {
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            setCameraPosition(location);
            //移除定位监听
            locationEngine.removeLocationEngineListener(this);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
    @Override
    @SuppressWarnings( {"MissingPermission"})
    protected void onStart() {
        super.onStart();
        if (locationPlugin != null) {
            locationPlugin.onStart();
        }
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates();
        }
        if (locationPlugin != null) {
            locationPlugin.onStop();
        }
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (locationEngine != null) {
            locationEngine.deactivate();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}
