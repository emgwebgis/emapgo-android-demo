package com.emapgo.android.demo.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.emapgo.android.demo.MyApplication;
import com.emapgo.android.demo.R;
import com.emapgo.android.demo.util.PreferenceManager;
import com.emapgo.mapsdk.annotations.PolygonOptions;
import com.emapgo.mapsdk.geometry.LatLng;
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.maps.MapView;
import com.emapgo.mapsdk.maps.OnMapReadyCallback;

import java.util.ArrayList;
import java.util.List;

public class DrawPolygonActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_polygon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mapView = (MapView) findViewById(R.id.emgMapView);
        mapView.onCreate(savedInstanceState);
        mapView.setStyleUrl(PreferenceManager.getMapStyle(MyApplication.getAppContext()));
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(EmgMap emgMap) {
        //构建多边形坐标点
        List<LatLng> polygon = new ArrayList<>();
        polygon.add(new LatLng(39.94054100025837, 116.38057708740236));
        polygon.add(new LatLng(39.91816284660943, 116.36512756347656));
        polygon.add(new LatLng(39.899728286187894, 116.38710021972658));
        polygon.add(new LatLng(39.909209536859834, 116.41937255859376));
        polygon.add(new LatLng(39.93606595478707, 116.4166259765625));
        polygon.add(new LatLng(39.94054100025837, 116.38057708740236));
        //添加到地图中
        emgMap.addPolygon(new PolygonOptions()
                .addAll(polygon)
                .fillColor(Color.parseColor("#001aff")) //填充颜色
                .alpha(0.2f) //透明度
        );
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
