package com.emapgo.android.demo.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.emapgo.android.demo.MyApplication;
import com.emapgo.android.demo.R;
import com.emapgo.android.demo.util.MapStyleUtil;
import com.emapgo.android.demo.util.PreferenceManager;
import com.emapgo.mapsdk.camera.CameraPosition;
import com.emapgo.mapsdk.camera.CameraUpdate;
import com.emapgo.mapsdk.camera.CameraUpdateFactory;
import com.emapgo.mapsdk.geometry.LatLng;
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.maps.MapView;
import com.emapgo.mapsdk.maps.OnMapReadyCallback;
import com.emapgo.mapsdk.style.functions.Function;
import com.emapgo.mapsdk.style.functions.stops.Stop;
import com.emapgo.mapsdk.style.layers.FillLayer;
import com.emapgo.mapsdk.style.layers.Layer;
import com.emapgo.mapsdk.style.layers.PropertyFactory;
import com.emapgo.mapsdk.style.layers.SymbolLayer;

import static com.emapgo.mapsdk.style.functions.stops.Stops.exponential;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.fillColor;

public class RuntimeZoomDependentFillColorActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private EmgMap mEmgMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runtime_zoom_dependent_fill_color);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mapView = (MapView) findViewById(R.id.emgMapView);
        mapView.onCreate(savedInstanceState);
        mapView.setStyleUrl(PreferenceManager.getMapStyle(MyApplication.getAppContext()));
        mapView.getMapAsync(this);

        mapView.addOnMapChangedListener(new MapView.OnMapChangedListener() {
            @Override
            public void onMapChanged(int status) {
                if (status == MapView.DID_FINISH_LOADING_MAP) {
                    if (mEmgMap != null) {
                        FillLayer layer = mEmgMap.getLayerAs("china-river");
                        if (layer == null) {
                            return;
                        }

                        layer.setProperties(fillColor(Function.zoom(exponential(
                                Stop.stop(3f, fillColor(Color.GREEN)),
                                Stop.stop(5f, fillColor(Color.BLUE)),
                                Stop.stop(7, fillColor(Color.RED)),
                                Stop.stop(9.5f, fillColor(Color.YELLOW))
                        ))));
                        mEmgMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(30.901046352477493, 121.98944091796874), 9.5), 9000);


                    }
                }
            }
        });
    }

    @Override
    public void onMapReady(EmgMap emgMap) {
        this.mEmgMap = emgMap;
        MapStyleUtil.darkStyle(emgMap);
        mEmgMap.setMinZoomPreference(3);
        mEmgMap.setMaxZoomPreference(10);
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
