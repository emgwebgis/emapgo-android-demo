package com.emapgo.android.demo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.emapgo.android.demo.MyApplication;
import com.emapgo.android.demo.R;
import com.emapgo.android.demo.util.PreferenceManager;
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.maps.MapView;
import com.emapgo.mapsdk.maps.OnMapReadyCallback;
import com.emapgo.mapsdk.style.layers.RasterLayer;
import com.emapgo.mapsdk.style.sources.RasterSource;
import com.emapgo.mapsdk.style.sources.TileSet;

public class RuntimeWMSActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runtime_wms);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mapView = (MapView) findViewById(R.id.emgMapView);
        mapView.onCreate(savedInstanceState);
        mapView.setStyleUrl(PreferenceManager.getMapStyle(MyApplication.getAppContext()));
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(EmgMap emgMap) {
        emgMap.setMaxZoomPreference(18);
        emgMap.setMinZoomPreference(14);
        
        RasterSource wzHandXiMapSource = new RasterSource(
                "wz_hand_xi",
                new TileSet("tileset", "http://server.emapgo.com.cn/geoserver/wms?bbox={bbox-epsg-3857}&format=image/png&service=WMS&version=1.1.0&request=GetMap&srs=EPSG:3857&width=256&height=256&layers=wuzhen%3Awuzhenxi&TRANSPARENT=true"),
                256);
        emgMap.addSource(wzHandXiMapSource);

        RasterLayer wzHandXiMapLayer = new RasterLayer("xi-map-layer", "wz_hand_xi");
        emgMap.addLayerBelow(wzHandXiMapLayer, "housenum-label");
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