package com.emapgo.android.demo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.emapgo.android.demo.R;
import com.emapgo.geojson.Point;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RemoveMarkerActivity extends AppCompatActivity implements OnMapReadyCallback {
    private List<Point> mPoints = new ArrayList<>();
    private List<Marker> mMarkers = new ArrayList<>();
    @BindView(R.id.emgMapView)
    MapView mMapView;

    EmgMap mEmgMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_marker);
        ButterKnife.bind(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);
        mMapView.addOnMapChangedListener(new MapView.OnMapChangedListener() {
            @Override
            public void onMapChanged(int status) {
                if (status == MapView.DID_FINISH_LOADING_MAP) {
                    CameraPosition position = new CameraPosition.Builder()
                            .target(new LatLng(39.91454216376516, 116.38877391815186)) // 设置相机位置
                            .zoom(14) // 设置相机缩放级别
                            .build(); // 构建一个CameraPosition

                    CameraUpdate cameraUpdate = CameraUpdateFactory
                            .newCameraPosition(position);
                    if (mEmgMap != null) {
                        mEmgMap.animateCamera(cameraUpdate, 4000);
                    }
                }
            }
        });
        initPoint();

    }

    private void initPoint() {
        mPoints.add(Point.fromLngLat(116.38722896575926, 39.91888696020796));
        mPoints.add(Point.fromLngLat(116.38746500015257, 39.91949586798925));
        mPoints.add(Point.fromLngLat(116.39469623565674, 39.91850844723661));
        mPoints.add(Point.fromLngLat(116.38843059539794, 39.91771850038315));
        mPoints.add(Point.fromLngLat(116.39291524887086, 39.91571067778447));
        mPoints.add(Point.fromLngLat(116.38997554779054, 39.91628669848582));
        mPoints.add(Point.fromLngLat(116.38877391815186, 39.91454216376516));
        mPoints.add(Point.fromLngLat(116.39068365097046, 39.91452570567887));
        mPoints.add(Point.fromLngLat(116.39044761657713, 39.91951232488118));
        mPoints.add(Point.fromLngLat(116.38877391815186, 39.91454216376516));
        mPoints.add(Point.fromLngLat(116.39411687850951, 39.913982586611944));
        mPoints.add(Point.fromLngLat(116.39349460601807, 39.92008831360661));
        mPoints.add(Point.fromLngLat(116.38720750808716, 39.91263299937423));
    }

    @Override
    public void onMapReady(EmgMap emgMap) {
        this.mEmgMap = emgMap;
        for (int i = 0; i < mPoints.size(); i++) {
            //指定Marker坐标点
            LatLng position = new LatLng(mPoints.get(i).latitude(), mPoints.get(i).longitude());

            //自定义Marker图标，如果不指定Icon将使用SDK默认Marker图标
            Icon icon = IconFactory.getInstance(this).defaultMarker();

            //MarkerOptions
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(position)
                    .icon(icon)
                    .title("第" + (i + 1) + "个Marker");

            //添加Marker到地图
            mMarkers.add(emgMap.addMarker(markerOptions));
        }
    }

    @OnClick(R.id.id_marker_remove)
    public void onRemoveClick(View view) {
        mMarkers.get(mMarkers.size()-1).remove();
        mMarkers.remove(mMarkers.size() - 1);
    }
    @OnClick(R.id.id_marker_remove_all)
    public void onRemoveAllClick(View view) {
        //1、可以通过clear的方式清除，这也将删除您添加到地图上的任何折线和多边形。
        mEmgMap.clear();
        //2、通过marker.remove方式
        /*for (Marker mMarker : mMarkers) {
            mMarker.remove();
        }*/
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
        mMapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mMapView.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }


}
