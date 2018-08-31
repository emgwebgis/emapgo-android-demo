package com.emapgo.android.demo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.emapgo.android.demo.R;
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.maps.MapView;
import com.emapgo.mapsdk.maps.OnMapReadyCallback;
import com.emapgo.mapsdk.style.layers.CircleLayer;
import com.emapgo.mapsdk.style.layers.HeatmapLayer;
import com.emapgo.mapsdk.style.layers.PropertyValue;
import com.emapgo.mapsdk.style.sources.GeoJsonSource;

import java.net.MalformedURLException;
import java.net.URL;

import timber.log.Timber;

import static com.emapgo.mapsdk.style.expressions.Expression.get;
import static com.emapgo.mapsdk.style.expressions.Expression.heatmapDensity;
import static com.emapgo.mapsdk.style.expressions.Expression.interpolate;
import static com.emapgo.mapsdk.style.expressions.Expression.linear;
import static com.emapgo.mapsdk.style.expressions.Expression.literal;
import static com.emapgo.mapsdk.style.expressions.Expression.rgb;
import static com.emapgo.mapsdk.style.expressions.Expression.rgba;
import static com.emapgo.mapsdk.style.expressions.Expression.stop;
import static com.emapgo.mapsdk.style.expressions.Expression.zoom;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.circleColor;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.circleOpacity;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.circleRadius;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.circleStrokeColor;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.circleStrokeWidth;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.heatmapIntensity;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.heatmapOpacity;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.heatmapRadius;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.heatmapWeight;

public class RuntimeHeatmapLayerActivity extends AppCompatActivity {

    private static final String EARTHQUAKE_SOURCE_URL = "http://m.emapgo.cn/geojson/earthquakes.geojson";
    private static final String EARTHQUAKE_SOURCE_ID = "earthquakes";
    private static final String HEATMAP_LAYER_ID = "earthquakes-heat";
    private static final String HEATMAP_LAYER_SOURCE = "earthquakes";
    private static final String CIRCLE_LAYER_ID = "earthquakes-circle";

    private MapView mapView;
    private EmgMap emgMap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runtime_heatmap);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mapView = findViewById(R.id.emgMapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(EmgMap emgMap) {
                RuntimeHeatmapLayerActivity.this.emgMap = emgMap;
                addEarthquakeSource();
                addHeatmapLayer();
                addCircleLayer();
            }
        });
    }

    private void addEarthquakeSource() {
        try {
            emgMap.addSource(new GeoJsonSource(EARTHQUAKE_SOURCE_ID, new URL(EARTHQUAKE_SOURCE_URL)));
        } catch (MalformedURLException malformedUrlException) {
            Timber.e(malformedUrlException, "That's not an url... ");
        }
    }

    private void addHeatmapLayer() {
        HeatmapLayer layer = new HeatmapLayer(HEATMAP_LAYER_ID, EARTHQUAKE_SOURCE_ID);
        layer.setMaxZoom(9);
        layer.setSourceLayer(HEATMAP_LAYER_SOURCE);
        layer.setProperties(
                new PropertyValue<>("heatmap-color", interpolate(
                        linear(), heatmapDensity(),
                        literal(0), rgba(33, 102, 172, 0),
                        literal(0.2), rgb(103, 169, 207),
                        literal(0.4), rgb(209, 229, 240),
                        literal(0.6), rgb(253, 219, 199),
                        literal(0.8), rgb(239, 138, 98),
                        literal(1), rgb(178, 24, 43)
                )),

                heatmapWeight(
                        interpolate(
                                linear(), get("mag"),
                                stop(0, 0),
                                stop(6, 1)
                        )
                ),

                heatmapIntensity(
                        interpolate(
                                linear(), zoom(),
                                stop(0, 1),
                                stop(9, 3)
                        )
                ),

                heatmapRadius(
                        interpolate(
                                linear(), zoom(),
                                stop(0, 2),
                                stop(9, 20)
                        )
                ),

                heatmapOpacity(
                        interpolate(
                                linear(), zoom(),
                                stop(7, 1),
                                stop(9, 0)
                        )
                )
        );

        emgMap.addLayerAbove(layer, "waterway-label");
    }

    private void addCircleLayer() {
        CircleLayer circleLayer = new CircleLayer(CIRCLE_LAYER_ID, EARTHQUAKE_SOURCE_ID);
        circleLayer.setProperties(

                circleRadius(
                        interpolate(
                                linear(), zoom(),
                                literal(7), interpolate(
                                        linear(), get("mag"),
                                        stop(1, 1),
                                        stop(6, 4)
                                ),
                                literal(16), interpolate(
                                        linear(), get("mag"),
                                        stop(1, 5),
                                        stop(6, 50)
                                )
                        )
                ),

// Color circle by earthquake magnitude
                circleColor(
                        interpolate(
                                linear(), get("mag"),
                                literal(1), rgba(33, 102, 172, 0),
                                literal(2), rgb(103, 169, 207),
                                literal(3), rgb(209, 229, 240),
                                literal(4), rgb(253, 219, 199),
                                literal(5), rgb(239, 138, 98),
                                literal(6), rgb(178, 24, 43)
                        )
                ),

                circleOpacity(
                        interpolate(
                                linear(), zoom(),
                                stop(7, 0),
                                stop(8, 1)
                        )
                ),
                circleStrokeColor("white"),
                circleStrokeWidth(1.0f)
        );

        emgMap.addLayerBelow(circleLayer, HEATMAP_LAYER_ID);
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
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}