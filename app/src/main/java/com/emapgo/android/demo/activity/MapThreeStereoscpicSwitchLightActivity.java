package com.emapgo.android.demo.activity;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.SeekBar;

import com.emapgo.android.demo.MyApplication;
import com.emapgo.android.demo.R;
import com.emapgo.android.demo.util.PreferenceManager;
import com.emapgo.mapsdk.geometry.LatLng;
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.maps.MapView;
import com.emapgo.mapsdk.maps.OnMapReadyCallback;
import com.emapgo.mapsdk.style.light.Light;
import com.emapgo.mapsdk.style.light.Position;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MapThreeStereoscpicSwitchLightActivity extends AppCompatActivity implements OnMapReadyCallback, SeekBar.OnSeekBarChangeListener {
    private EmgMap emgMap;
    private MapView mapView;
    @BindView(R.id.id_switch_light_radial_coordinate)
    SeekBar id_switch_light_radial_coordinate;
    @BindView(R.id.id_switch_light_azimuthal_angle)
    SeekBar id_switch_light_azimuthal_angle;
    @BindView(R.id.id_switch_light_polar_angle)
    SeekBar id_switch_light_polar_angle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_three_stereoscpic_switch_light);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        id_switch_light_radial_coordinate.setOnSeekBarChangeListener(this);
        id_switch_light_azimuthal_angle.setOnSeekBarChangeListener(this);
        id_switch_light_polar_angle.setOnSeekBarChangeListener(this);

        mapView = (MapView) findViewById(R.id.emgMapView);
        mapView.onCreate(savedInstanceState);
        mapView.setStyleUrl("http://192.168.11.148:10003/styles/outdoor_3Dbuilding/style.json");
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(EmgMap emgMap) {
        this.emgMap = emgMap;
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
    float radialCoordinate = 0.015f;
    int azimuthalAngle = 0;
    int polarAngle = 0;
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (emgMap == null) {
            return;
        }


        switch (seekBar.getId()) {
            case R.id.id_switch_light_radial_coordinate:
                radialCoordinate *= progress;
                break;
            case R.id.id_switch_light_azimuthal_angle:
                azimuthalAngle = progress;
                break;
            case R.id.id_switch_light_polar_angle:
                polarAngle = progress;
                break;
        }
        emgMap.getLight().setPosition(new Position(radialCoordinate, azimuthalAngle, polarAngle));

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
