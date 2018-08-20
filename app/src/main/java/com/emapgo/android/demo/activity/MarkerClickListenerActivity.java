package com.emapgo.android.demo.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.emapgo.android.demo.MyApplication;
import com.emapgo.android.demo.R;
import com.emapgo.android.demo.util.PreferenceManager;
import com.emapgo.mapsdk.annotations.Icon;
import com.emapgo.mapsdk.annotations.IconFactory;
import com.emapgo.mapsdk.annotations.Marker;
import com.emapgo.mapsdk.annotations.MarkerOptions;
import com.emapgo.mapsdk.geometry.LatLng;
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.maps.MapView;
import com.emapgo.mapsdk.maps.OnMapReadyCallback;

import butterknife.BindView;

public class MarkerClickListenerActivity extends AppCompatActivity implements OnMapReadyCallback {
    @BindView(R.id.emgMapView)
    MapView mapView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_click_listener);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mapView = (MapView) findViewById(R.id.emgMapView);
        mapView.onCreate(savedInstanceState);
        mapView.setStyleUrl(PreferenceManager.getMapStyle(MyApplication.getAppContext()));
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(EmgMap emgMap) {
        //指定Marker坐标点
        LatLng position = new LatLng(39.8288916490182, 116.29995507512434);

        //自定义Marker图标，如果不指定Icon将使用SDK默认Marker图标
        Icon icon = IconFactory.getInstance(this).defaultMarker();

        //MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions()
                .position(position)
                .icon(icon)
                .title("Hello EmapgoMap");

        //添加Marker到地图
        emgMap.addMarker(markerOptions);

        //添加Marker点击监听事件
        emgMap.setOnMarkerClickListener(new EmgMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                Toast.makeText(MarkerClickListenerActivity.this, "点击事件", Toast.LENGTH_SHORT).show();
                return true; //如果返回true 则单机事件被消费，false会有mapview处理事件
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
