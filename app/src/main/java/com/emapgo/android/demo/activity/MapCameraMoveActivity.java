package com.emapgo.android.demo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.emapgo.android.demo.MyApplication;
import com.emapgo.android.demo.R;
import com.emapgo.android.demo.util.PreferenceManager;
import com.emapgo.geojson.Point;
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

public class MapCameraMoveActivity extends AppCompatActivity implements OnMapReadyCallback {
    private List<Point> points = new ArrayList<>();
    private int mCurrPointIndex = -1;
    private MapView mapView;
    private EmgMap emgMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_camera_move);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        init();
        mapView = (MapView) findViewById(R.id.emgMapView);
        mapView.onCreate(savedInstanceState);
        mapView.setStyleUrl(PreferenceManager.getMapStyle(MyApplication.getAppContext()));
        mapView.getMapAsync(this);
    }

    private void init() {
        points.add(Point.fromLngLat(116.29560470581055,
                39.82706103187656));
        points.add(Point.fromLngLat(116.31654739379883,
                39.842286020743394));
        points.add(Point.fromLngLat( 116.28341674804689,
                39.85203878519601));


    }

    @Override
    public void onMapReady(EmgMap emgMap) {
        this.emgMap = emgMap;
        addMarker(getNextPoint());
    }

    @OnClick(R.id.id_move_camera)
    public void onMoveCameraClick(View view) {
        if (emgMap != null) {
            LatLng nextPoint = getNextPoint();
            addMarker(nextPoint);

            CameraPosition position = new CameraPosition.Builder()
                    .target(nextPoint)
                    .zoom(15)
                    .build();

            emgMap.moveCamera(CameraUpdateFactory
                    .newCameraPosition(position));
        }
    }

    @OnClick(R.id.id_easy_camera)
    public void onEasyCameraClick(View view) {
        if (emgMap != null) {
            LatLng nextPoint = getNextPoint();
            addMarker(nextPoint);

            CameraPosition position = new CameraPosition.Builder()
                    .target(nextPoint)
                    .zoom(15)
                    .build();

            emgMap.easeCamera(CameraUpdateFactory
                    .newCameraPosition(position),3000);
        }
    }

    @OnClick(R.id.id_animate_camera)
    public void onAnimateCameraClick(View view) {
        if (emgMap != null) {
            LatLng nextPoint = getNextPoint();
            addMarker(nextPoint);

            CameraPosition position = new CameraPosition.Builder()
                    .target(nextPoint)
                    .bearing(180)
                    .tilt(50)
                    .zoom(15)
                    .build();

            emgMap.animateCamera(CameraUpdateFactory
                    .newCameraPosition(position),3000);
        }
    }

    private void addMarker(LatLng position) {
        if (emgMap != null) {
            emgMap.clear();
            emgMap.addMarker(new MarkerOptions()
                    .position(position));
        }
    }
    private LatLng getNextPoint() {
        if (mCurrPointIndex == points.size() - 1) {
            mCurrPointIndex = 0;
        }else{
            mCurrPointIndex++;
        }
        return new LatLng(points.get(mCurrPointIndex).latitude(),points.get(mCurrPointIndex).longitude());
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
