package com.emapgo.android.demo.activity;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.emapgo.android.demo.R;
import com.emapgo.android.demo.util.MCircleLayer;
import com.emapgo.mapsdk.camera.CameraUpdateFactory;
import com.emapgo.mapsdk.geometry.LatLng;
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.maps.MapView;
import com.emapgo.mapsdk.maps.OnMapReadyCallback;
import com.emapgo.mapsdk.style.expressions.Expression;
import com.emapgo.mapsdk.style.layers.CircleLayer;
import com.emapgo.mapsdk.style.layers.Filter;
import com.emapgo.mapsdk.style.layers.PropertyFactory;
import com.emapgo.mapsdk.style.layers.PropertyValue;
import com.emapgo.mapsdk.style.layers.SymbolLayer;
import com.emapgo.mapsdk.style.sources.GeoJsonOptions;
import com.emapgo.mapsdk.style.sources.GeoJsonSource;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.function.Function;


import static com.emapgo.mapsdk.style.expressions.Expression.all;
import static com.emapgo.mapsdk.style.expressions.Expression.get;
import static com.emapgo.mapsdk.style.expressions.Expression.gte;
import static com.emapgo.mapsdk.style.expressions.Expression.literal;
import static com.emapgo.mapsdk.style.expressions.Expression.lt;
import static com.emapgo.mapsdk.style.expressions.Expression.toNumber;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.circleColor;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.circleRadius;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.iconImage;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.textColor;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.textField;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.textFont;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.textSize;

public class RuntimeClustersActivity extends AppCompatActivity {


    private MapView mapView;
    private EmgMap emgMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runtime_clusters);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mapView = findViewById(R.id.emgMapView);

        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(EmgMap map) {

                emgMap = map;

                emgMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(12.099, -79.045), 3));

                addClusteredGeoJsonSource();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onStop() {
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


    private void addClusteredGeoJsonSource() {

        try {
            emgMap.addSource(
                    new GeoJsonSource("earthquakes",
                            new URL("http://m.emapgo.cn/geojson/clusters.geojson"),
                            new GeoJsonOptions()
                                    .withCluster(true)
                                    .withClusterMaxZoom(14)
                                    .withClusterRadius(50)
                    )
            );
        } catch (MalformedURLException malformedUrlException) {
            Log.e("dataClusterActivity", "Check the URL " + malformedUrlException.getMessage());
        }


        int[][] layers = new int[][]{
                new int[]{150, ContextCompat.getColor(this, android.R.color.holo_red_light)},
                new int[]{20, ContextCompat.getColor(this, android.R.color.holo_green_light)},
                new int[]{0, ContextCompat.getColor(this, android.R.color.holo_blue_light)}
        };

        SymbolLayer unclustered = new SymbolLayer("unclustered-points", "earthquakes");
        unclustered.setProperties(iconImage("marker-15"));
        emgMap.addLayer(unclustered);

        for (int i = 0; i < layers.length; i++) {
            CircleLayer circles = new CircleLayer("cluster-" + i, "earthquakes");
            circles.setProperties(
                    circleColor(layers[i][1]),
                    circleRadius(15f)
            );

            Expression pointCount = toNumber(get("point_count"));

            circles.setFilter(
                    i == 0
                            ? gte(pointCount, literal(layers[i][0])) :
                            all(
                                    gte(pointCount, literal(layers[i][0])),
                                    lt(pointCount, literal(layers[i - 1][0]))
                            )
            );
            emgMap.addLayer(circles);
        }
        SymbolLayer count = new SymbolLayer("count", "earthquakes");
        count.setProperties(
                textFont(new String[]{"NotoSansCJKSCBoldArialUnicodeMSRegular"}),
                textField("{point_count}"),
                textSize(12f),
                textColor(Color.WHITE)
        );
        emgMap.addLayer(count);


    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
