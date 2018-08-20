package com.emapgo.android.demo.activity;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.emapgo.android.demo.R;
import com.emapgo.geojson.Feature;
import com.emapgo.mapsdk.annotations.Marker;
import com.emapgo.mapsdk.annotations.MarkerOptions;
import com.emapgo.mapsdk.geometry.LatLng;
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.maps.MapView;
import com.emapgo.mapsdk.maps.OnMapReadyCallback;
import com.google.gson.JsonElement;

import java.util.List;
import java.util.Map;

public class MapQueryFeatureActivity extends AppCompatActivity implements OnMapReadyCallback, EmgMap.OnMapClickListener {

    private MapView mapView;
    private Marker featureMarker;
    private EmgMap emgMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_query_feature);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mapView = findViewById(R.id.emgMapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(EmgMap emgMap) {
        MapQueryFeatureActivity.this.emgMap = emgMap;
        emgMap.addOnMapClickListener(this);
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {
        if (featureMarker != null) {
            emgMap.removeMarker(featureMarker);
        }

        final PointF pixel = emgMap.getProjection().toScreenLocation(point);
        List<Feature> features = emgMap.queryRenderedFeatures(pixel);

        if (features.size() > 0) {
            Feature feature = features.get(0);

            String property;

            StringBuilder stringBuilder = new StringBuilder();
            if (feature.properties() != null) {
                for (Map.Entry<String, JsonElement> entry : feature.properties().entrySet()) {
                    stringBuilder.append(String.format("%s - %s", entry.getKey(), entry.getValue()));
                    stringBuilder.append(System.getProperty("line.separator"));
                }

                featureMarker = emgMap.addMarker(new MarkerOptions()
                        .position(point)
                        .title("Feature Properties:")
                        .snippet(stringBuilder.toString())
                );

            } else {
                property = "No feature properties found";
                featureMarker = emgMap.addMarker(new MarkerOptions()
                        .position(point)
                        .snippet(property)
                );
            }
        } else {
            featureMarker = emgMap.addMarker(new MarkerOptions()
                    .position(point)
                    .snippet("No feature properties found")
            );
        }
        emgMap.selectMarker(featureMarker);
    }

    @Override
    public void onResume() {
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
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (emgMap != null) {
            emgMap.removeOnMapClickListener(this);
        }
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
        

