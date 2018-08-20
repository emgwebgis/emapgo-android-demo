package com.emapgo.android.demo.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.emapgo.android.demo.MyApplication;
import com.emapgo.android.demo.R;
import com.emapgo.android.demo.util.PreferenceManager;
import com.emapgo.mapsdk.annotations.Icon;
import com.emapgo.mapsdk.annotations.IconFactory;
import com.emapgo.mapsdk.annotations.Marker;
import com.emapgo.mapsdk.annotations.MarkerOptions;
import com.emapgo.mapsdk.camera.CameraPosition;
import com.emapgo.mapsdk.camera.CameraUpdate;
import com.emapgo.mapsdk.camera.CameraUpdateFactory;
import com.emapgo.mapsdk.geometry.LatLng;
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.maps.MapView;
import com.emapgo.mapsdk.maps.OnMapReadyCallback;
import com.emapgo.mapsdk.style.expressions.Expression;
import com.emapgo.mapsdk.style.layers.FillLayer;

import static com.emapgo.mapsdk.style.expressions.Expression.stop;
import static com.emapgo.mapsdk.style.expressions.Expression.zoom;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.fillColor;

public class AddMarkerActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private EmgMap mEmgMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_marker);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mapView = (MapView) findViewById(R.id.emgMapView);
        mapView.onCreate(savedInstanceState);
        mapView.setStyleUrl(PreferenceManager.getMapStyle(MyApplication.getAppContext()));
        mapView.getMapAsync(this);
        mapView.addOnMapChangedListener(new MapView.OnMapChangedListener() {
            @Override
            public void onMapChanged(int status) {
                //地图样式加载完成，详情参考https://emapgo.gitbook.io/document/emapgomapsdk-for-android
                if (status == MapView.DID_FINISH_LOADING_MAP) {
                    CameraPosition position = new CameraPosition.Builder()
                            .target(new LatLng(39.8288916490182, 116.29995507512434)) // 设置相机位置
                            .zoom(15) // 设置相机缩放级别
                            .build(); // 构建一个CameraPosition

                    CameraUpdate cameraUpdate = CameraUpdateFactory
                            .newCameraPosition(position);
                    if (mEmgMap != null) {
                        mEmgMap.animateCamera(cameraUpdate, 3000);
                    }
                }
            }
        });
    }

    @Override
    public void onMapReady(EmgMap emgMap) {
        this.mEmgMap = emgMap;
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
        Marker marker = emgMap.addMarker(markerOptions);
        //如不需要自动显示Marker 请注释该代码
        emgMap.selectMarker(marker);
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
