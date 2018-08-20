package com.emapgo.android.demo.activity;

import android.graphics.Color;
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
import com.emapgo.mapsdk.style.functions.stops.Stops;
import com.emapgo.mapsdk.style.layers.FillExtrusionLayer;
import com.emapgo.mapsdk.style.sources.GeoJsonSource;

import java.io.IOException;
import java.io.InputStream;

import static com.emapgo.mapsdk.style.layers.PropertyFactory.fillExtrusionColor;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.fillExtrusionHeight;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.fillExtrusionOpacity;

public class MapThreeStereoscopicMarathonActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private EmgMap emgMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_three_stereoscopic_marathon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mapView = (MapView) findViewById(R.id.emgMapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(EmgMap emgMap) {
        this.emgMap = emgMap;
        GeoJsonSource courseRouteGeoJson = new GeoJsonSource("coursedata", loadJsonFromAsset("marathon_route.geojson"));
        emgMap.addSource(courseRouteGeoJson);
        addExtrusionsLayerToMap();
    }

    private void addExtrusionsLayerToMap() {
        FillExtrusionLayer courseExtrusionLayer = new FillExtrusionLayer("course", "coursedata");
        courseExtrusionLayer.setProperties(
                fillExtrusionColor(Color.YELLOW),
                fillExtrusionOpacity(0.7f),
                fillExtrusionHeight(Function.property("e", Stops.<Float>identity()))
        );
        emgMap.addLayer(courseExtrusionLayer);
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
