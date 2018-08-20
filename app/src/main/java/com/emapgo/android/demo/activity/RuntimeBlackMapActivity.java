package com.emapgo.android.demo.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.emapgo.android.demo.MyApplication;
import com.emapgo.android.demo.R;
import com.emapgo.android.demo.util.MapStyleUtil;
import com.emapgo.android.demo.util.PreferenceManager;
import com.emapgo.geojson.FeatureCollection;
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.maps.MapView;
import com.emapgo.mapsdk.maps.OnMapReadyCallback;
import com.emapgo.mapsdk.style.functions.Function;
import com.emapgo.mapsdk.style.functions.stops.Stops;
import com.emapgo.mapsdk.style.layers.FillExtrusionLayer;
import com.emapgo.mapsdk.style.layers.FillLayer;
import com.emapgo.mapsdk.style.layers.Layer;
import com.emapgo.mapsdk.style.layers.LineLayer;
import com.emapgo.mapsdk.style.layers.PropertyFactory;
import com.emapgo.mapsdk.style.layers.SymbolLayer;
import com.emapgo.mapsdk.style.sources.GeoJsonSource;

import static com.emapgo.mapsdk.style.layers.PropertyFactory.fillExtrusionColor;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.fillExtrusionHeight;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.fillExtrusionOpacity;

public class RuntimeBlackMapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runtime_black_map);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mapView = (MapView) findViewById(R.id.emgMapView);
        mapView.onCreate(savedInstanceState);
        mapView.setStyleUrl(PreferenceManager.getMapStyle(MyApplication.getAppContext()));
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(EmgMap emgMap) {
        MapStyleUtil.darkStyle(emgMap);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
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
        mapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}
