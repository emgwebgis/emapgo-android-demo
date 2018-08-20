package com.emapgo.android.demo.activity;

import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.emapgo.android.demo.MyApplication;
import com.emapgo.android.demo.R;
import com.emapgo.android.demo.util.PreferenceManager;
import com.emapgo.geojson.Feature;
import com.emapgo.geojson.LineString;
import com.emapgo.geojson.Point;
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.maps.MapView;
import com.emapgo.mapsdk.maps.OnMapReadyCallback;
import com.emapgo.mapsdk.style.layers.FillLayer;
import com.emapgo.mapsdk.style.layers.PropertyFactory;
import com.emapgo.mapsdk.style.layers.SymbolLayer;
import com.emapgo.mapsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;

public class RuntimeFillLayerActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_fill_layer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mapView = (MapView) findViewById(R.id.emgMapView);
        mapView.onCreate(savedInstanceState);
        mapView.setStyleUrl(PreferenceManager.getMapStyle(MyApplication.getAppContext()));
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(EmgMap emgMap) {
        //构建GeoJsonSource
        ArrayList<Point> points = new ArrayList<>();
        points.add(Point.fromLngLat(116.39122009277342, 39.91710957679777));
        points.add(Point.fromLngLat(116.34122009277342, 39.92710957679777));
        points.add(Point.fromLngLat(116.35963439941405, 39.89847718985659));
        GeoJsonSource geoJsonSource = new GeoJsonSource("fill-source", Feature.fromGeometry(LineString.fromLngLats(points)));
        emgMap.addSource(geoJsonSource);

        //使用FillLayer填充数据源
        FillLayer fillLayer = new FillLayer("fill-layer", "fill-source");
        //更改填充颜色
        fillLayer.withProperties(
                PropertyFactory.fillColor(Color.RED)
        );
        //添加到地图
        emgMap.addLayer(fillLayer);

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
