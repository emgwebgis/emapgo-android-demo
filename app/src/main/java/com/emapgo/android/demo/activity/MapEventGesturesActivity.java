package com.emapgo.android.demo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.emapgo.android.demo.MyApplication;
import com.emapgo.android.demo.R;
import com.emapgo.android.demo.util.PreferenceManager;
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.maps.MapView;
import com.emapgo.mapsdk.maps.OnMapReadyCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class MapEventGesturesActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private EmgMap emgMap;

    @BindView(R.id.id_gesture_scale_enable)
    Switch mGestureScaleSwitch;
    @BindView(R.id.id_gesture_scroll_enable)
    Switch mGestureScrollSwitch;
    @BindView(R.id.id_gesture_rotate_enable)
    Switch mGestureRotateSwitch;
    @BindView(R.id.id_gesture_Tilt_enable)
    Switch mGestureTiltSwitch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_event_gestures);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        mapView = (MapView) findViewById(R.id.emgMapView);
        mapView.onCreate(savedInstanceState);
        mapView.setStyleUrl(PreferenceManager.getMapStyle(MyApplication.getAppContext()));
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(EmgMap emgMap) {
        this.emgMap = emgMap;
        //当地图加载完成以后设置
        mGestureScaleSwitch.setChecked(true);
        mGestureScrollSwitch.setChecked(true);
        mGestureRotateSwitch.setChecked(true);
        mGestureTiltSwitch.setChecked(true);
    }
    @OnCheckedChanged(R.id.id_gesture_scale_enable)
    public void onGestureScaleEnableCheckedChanged(CompoundButton button, boolean checked) {
        if (emgMap != null) {
            emgMap.getUiSettings().setZoomGesturesEnabled(checked);
        }
    }

    @OnCheckedChanged(R.id.id_gesture_scroll_enable)
    public void onGestureScrollEnableCheckedChanged(CompoundButton button, boolean checked) {
        if (emgMap != null) {
            emgMap.getUiSettings().setScrollGesturesEnabled(checked);
        }
    }

    @OnCheckedChanged(R.id.id_gesture_rotate_enable)
    public void onGestureRotateEnableCheckedChanged(CompoundButton button, boolean checked) {
        if (emgMap != null) {
            emgMap.getUiSettings().setRotateGesturesEnabled(checked);
        }
    }

    @OnCheckedChanged(R.id.id_gesture_Tilt_enable)
    public void onGestureTiltEnableCheckedChanged(CompoundButton button, boolean checked) {
        if (emgMap != null) {
            emgMap.getUiSettings().setTiltGesturesEnabled(checked);
        }
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
