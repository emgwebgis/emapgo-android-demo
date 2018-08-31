package com.emapgo.android.demo.activity;

import android.annotation.SuppressLint;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RadioGroup;

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

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationModelActivity extends AppCompatActivity  implements OnMapReadyCallback, LocationEngineListener, RadioGroup.OnCheckedChangeListener {

    private MapView mapView;
    private EmgMap mEmgMap;
    private com.emapgo.android.core.location.LocationEngine locationEngine;
    private LocationLayerPlugin locationPlugin;

    @BindView(R.id.id_location_mode)
    RadioGroup mRadioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_model);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        mRadioGroup.setOnCheckedChangeListener(this);

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
        locationEngine.setSmallestDisplacement(0);
        locationEngine.setInterval(1000);
/*        Location lastLocation = locationEngine.getLastLocation();
        if (lastLocation != null) {
            //移动Camera到定位的坐标点
            setCameraPosition(lastLocation);
        } else {
            //没有获取到定位数据，需要设置监听器监听定位结果
        }*/
        locationEngine.addLocationEngineListener(this);
        locationPlugin = new LocationLayerPlugin(mapView, mEmgMap, locationEngine);
        locationPlugin.setLocationLayerEnabled(LocationLayerMode.TRACKING);

        locationEngine.activate();
    }

    private void setCameraPosition(Location location) {
        //将Camera移动到定位的坐标点
        mEmgMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 14));
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected() {
        Log.e("LocationModelActivity", "onConnected");
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("LocationModelActivity", "onLocationChanged");
        if (location != null) {
            setCameraPosition(location);
            //移除定位监听
            //locationEngine.removeLocationEngineListener(this);
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
    @SuppressWarnings({"MissingPermission"})
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

    @SuppressLint("MissingPermission")
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (locationPlugin == null && mEmgMap != null)
            return;
        switch (checkedId) {
            case R.id.id_location_mode_tracking:
                locationPlugin.setLocationLayerEnabled(LocationLayerMode.TRACKING);
                break;
            case R.id.id_location_mode_compass:
                locationPlugin.setLocationLayerEnabled(LocationLayerMode.COMPASS);
                break;
            case R.id.id_location_mode_navigation:
                locationPlugin.setLocationLayerEnabled(LocationLayerMode.NAVIGATION);
                break;
            case R.id.id_location_mode_none:
                locationPlugin.setLocationLayerEnabled(LocationLayerMode.NONE);
                break;
        }
    }
}
