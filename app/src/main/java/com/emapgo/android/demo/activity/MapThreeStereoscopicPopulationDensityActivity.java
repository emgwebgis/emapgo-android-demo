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
import com.emapgo.mapsdk.style.layers.FillExtrusionLayer;
import com.emapgo.mapsdk.style.layers.FillLayer;
import com.emapgo.mapsdk.style.layers.Filter;
import com.emapgo.mapsdk.style.sources.VectorSource;

import static com.emapgo.mapsdk.style.functions.stops.Stop.stop;
import static com.emapgo.mapsdk.style.functions.stops.Stops.exponential;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.fillColor;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.fillExtrusionBase;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.fillExtrusionColor;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.fillExtrusionHeight;

public class MapThreeStereoscopicPopulationDensityActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private EmgMap emgMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_three_stereoscopic_population_density);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mapView = (MapView) findViewById(R.id.emgMapView);
        mapView.onCreate(savedInstanceState);
        mapView.setStyleUrl(PreferenceManager.getMapStyle(MyApplication.getAppContext()));
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(EmgMap emgMap) {
        this.emgMap = emgMap;
        VectorSource vectorSource = new VectorSource("population", "mapbox://peterqliu.d0vin3el");
        emgMap.addSource(vectorSource);

        addFillsLayer();
        addExtrusionsLayer();
    }
    private void addFillsLayer() {
        FillLayer fillsLayer = new FillLayer("fills", "population");
        fillsLayer.setSourceLayer("outgeojson");
        fillsLayer.setFilter(Filter.all(Filter.lt("pkm2", 300000)));
        fillsLayer.withProperties(
                fillColor(Function.property("pkm2", exponential(
                        stop(0, fillColor(Color.parseColor("#160e23"))),
                        stop(14500, fillColor(Color.parseColor("#00617f"))),
                        stop(145000, fillColor(Color.parseColor("#55e9ff"))))
                        .withBase(1f)))
        );
        emgMap.addLayerBelow(fillsLayer, "china-river");
    }

    private void addExtrusionsLayer() {
        FillExtrusionLayer fillExtrusionLayer = new FillExtrusionLayer("extrusions", "population");
        fillExtrusionLayer.setSourceLayer("outgeojson");
        fillExtrusionLayer.setFilter(Filter.all(Filter.gt("p", 1), Filter.lt("pkm2", 300000)));
        fillExtrusionLayer.withProperties(
                fillExtrusionColor(Function.property("pkm2", exponential(
                        stop(0, fillColor(Color.parseColor("#160e23"))),
                        stop(14500, fillColor(Color.parseColor("#00617f"))),
                        stop(145000, fillColor(Color.parseColor("#55e9ff"))))
                        .withBase(1f))),
                fillExtrusionBase(0f),
                fillExtrusionHeight(Function.property("pkm2", exponential(
                        stop(0, fillExtrusionHeight(0f)),
                        stop(1450000, fillExtrusionHeight(20000f)))
                        .withBase(1f))));
        emgMap.addLayerBelow(fillExtrusionLayer, "airport-label");
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
