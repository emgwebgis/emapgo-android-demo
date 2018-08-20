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
import com.emapgo.mapsdk.style.functions.Function;
import com.emapgo.mapsdk.style.functions.stops.IdentityStops;
import com.emapgo.mapsdk.style.layers.FillExtrusionLayer;
import com.emapgo.mapsdk.style.sources.GeoJsonSource;

import java.io.IOException;
import java.io.InputStream;

import static com.emapgo.mapsdk.style.layers.PropertyFactory.fillExtrusionBase;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.fillExtrusionColor;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.fillExtrusionHeight;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.fillExtrusionOpacity;

public class MapThreeStereoscopicIndoor3DActivity extends AppCompatActivity {
    private MapView mapView;
    private EmgMap map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_three_stereoscopic_indoor3d);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mapView = (MapView) findViewById(R.id.emgMapView);
        mapView.onCreate(savedInstanceState);
        mapView.setStyleUrl(PreferenceManager.getMapStyle(MyApplication.getAppContext()));
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EmgMap emgMap) {
                map = emgMap;

                GeoJsonSource roomDataSource = new GeoJsonSource("room-data", loadJsonFromAsset("indoor-3d-map.geojson"));
                map.addSource(roomDataSource);

                FillExtrusionLayer roomExtrusionLayer = new FillExtrusionLayer("room-extrusion", "room-data");
                roomExtrusionLayer.setProperties(
                        fillExtrusionColor(Function.property("color", new IdentityStops<String>())),
                        fillExtrusionHeight(Function.property("height", new IdentityStops<Float>())),
                        fillExtrusionBase(Function.property("base_height", new IdentityStops<Float>())),
                        fillExtrusionOpacity(0.5f)
                );

                map.addLayer(roomExtrusionLayer);
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private String loadJsonFromAsset(String filename) {
        try {
            InputStream is = getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
