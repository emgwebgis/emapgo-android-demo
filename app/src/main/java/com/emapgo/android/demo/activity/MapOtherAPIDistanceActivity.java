package com.emapgo.android.demo.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.emapgo.android.demo.BuildConfig;
import com.emapgo.android.demo.MyApplication;
import com.emapgo.android.demo.R;
import com.emapgo.android.demo.util.PreferenceManager;
import com.emapgo.geojson.FeatureCollection;
import com.emapgo.geojson.LineString;
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.maps.MapView;
import com.emapgo.mapsdk.maps.OnMapReadyCallback;
import com.emapgo.mapsdk.style.layers.LineLayer;
import com.emapgo.mapsdk.style.layers.Property;
import com.emapgo.mapsdk.style.layers.PropertyFactory;
import com.emapgo.mapsdk.style.sources.GeoJsonSource;
import com.emapgo.turf.TurfConstants;
import com.emapgo.turf.TurfMeasurement;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapOtherAPIDistanceActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    @BindView(R.id.map_distance_info)
    TextView map_distance_info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_other_apidistance);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);


        mapView = (MapView) findViewById(R.id.emgMapView);
        mapView.onCreate(savedInstanceState);
        mapView.setStyleUrl(PreferenceManager.getMapStyle(MyApplication.getAppContext()));
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(EmgMap emgMap) {
        FeatureCollection featureCollection = loadGeoJSON();
        emgMap.addSource(new GeoJsonSource("line-source", featureCollection));

        LineLayer lineLayer = new LineLayer("line-layer", "line-source").withProperties(
                PropertyFactory.lineWidth(6f),
                PropertyFactory.lineColor(Color.argb(255, 66, 159, 255)),
                PropertyFactory.lineCap(Property.LINE_CAP_ROUND)
        );
        emgMap.addLayer(lineLayer);

        //测算多个点之间的距离,单位公里
        double lineDistance = TurfMeasurement.lineDistance(LineString.fromJson(featureCollection.features().get(0).geometry().toJson()), TurfConstants.UNIT_KILOMETERS);
        //测算两个点之间的距离
        //TurfMeasurement.distance(point1,point2,TurfConstants.UNIT_KILOMETERS);
        map_distance_info.setText(String.format("测算结果为%.2f",lineDistance)+"KM");
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

    private FeatureCollection loadGeoJSON() {
        return FeatureCollection.fromJson("{\n" +
                "  \"type\": \"FeatureCollection\",\n" +
                "  \"features\": [\n" +
                "    {\n" +
                "      \"type\": \"Feature\",\n" +
                "      \"properties\": {},\n" +
                "      \"geometry\": {\n" +
                "        \"type\": \"LineString\",\n" +
                "        \"coordinates\": [\n" +
                "          [\n" +
                "            116.290283203125,\n" +
                "            39.95449073417355\n" +
                "          ],\n" +
                "          [\n" +
                "            116.36959075927736,\n" +
                "            39.95633293919831\n" +
                "          ],\n" +
                "          [\n" +
                "            116.36993408203125,\n" +
                "            39.89604077881996\n" +
                "          ],\n" +
                "          [\n" +
                "            116.44546508789062,\n" +
                "            39.89709437260048\n" +
                "          ]\n" +
                "        ]\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}");

    }
}
