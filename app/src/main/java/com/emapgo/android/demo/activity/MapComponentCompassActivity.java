package com.emapgo.android.demo.activity;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;

import com.emapgo.android.demo.MyApplication;
import com.emapgo.android.demo.R;
import com.emapgo.android.demo.util.PreferenceManager;
import com.emapgo.mapsdk.geometry.LatLng;
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.maps.MapView;
import com.emapgo.mapsdk.maps.OnMapReadyCallback;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnItemSelected;

public class MapComponentCompassActivity extends AppCompatActivity implements OnMapReadyCallback, SeekBar.OnSeekBarChangeListener {

    private MapView mapView;
    private EmgMap emgMap;

    @BindView(R.id.id_compass_enable)
    Switch mCompassEnableSwitch;
    @BindView(R.id.id_compass_gravity)
    Spinner mCompassGravitySpinner;
    @BindView(R.id.id_compass_margin_left)
    SeekBar mCompassMarginLeftSeekBar;
    @BindView(R.id.id_compass_margin_top)
    SeekBar mCompassMarginToptSeekBar;
    @BindView(R.id.id_compass_margin_right)
    SeekBar mCompassMarginRightSeekBar;
    @BindView(R.id.id_compass_margin_bottom)
    SeekBar mCompassMarginBottomSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_compass_component);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        mapView = (MapView) findViewById(R.id.emgMapView);
        mapView.onCreate(savedInstanceState);
        mapView.setStyleUrl(PreferenceManager.getMapStyle(MyApplication.getAppContext()));
        mapView.getMapAsync(this);

        mCompassMarginLeftSeekBar.setOnSeekBarChangeListener(this);
        mCompassMarginToptSeekBar.setOnSeekBarChangeListener(this);
        mCompassMarginRightSeekBar.setOnSeekBarChangeListener(this);
        mCompassMarginBottomSeekBar.setOnSeekBarChangeListener(this);


    }

    @Override
    public void onMapReady(EmgMap emgMap) {
        this.emgMap = emgMap;
        emgMap.addOnMapClickListener(new EmgMap.OnMapClickListener() {
            @Override
            public void onMapClick(@NonNull LatLng latLng) {
                emgMap.getUiSettings().setCompassImage(getResources().getDrawable(R.mipmap.emg_example_compass_logo));
            }
        });
        Snackbar.make(mapView, "点击地图切换指南针图标资" +
                "源", Snackbar.LENGTH_LONG).show();
        mCompassEnableSwitch.setChecked(true);
        mCompassGravitySpinner.setSelection(8);
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

    @OnCheckedChanged(R.id.id_compass_enable)
    public void onCompassEnableCheckedChanged(CompoundButton button, boolean check) {
        if (emgMap != null) {
            emgMap.getUiSettings().setCompassEnabled(check);
        }
    }

    @OnItemSelected(R.id.id_compass_gravity)
    public void onCompassGravityItemSelected(int i) {
        if (emgMap == null) {
            return;
        }
        String item = (String) mCompassGravitySpinner.getAdapter().getItem(i);
        try {
            Field declaredField = Gravity.class.getDeclaredField(item.toUpperCase());
            emgMap.getUiSettings().setCompassGravity((Integer) declaredField.get(null));
            reset();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (emgMap == null) {
            return;
        }
        int left = emgMap.getUiSettings().getCompassMarginLeft(),
                top = emgMap.getUiSettings().getCompassMarginTop(),
                right = emgMap.getUiSettings().getCompassMarginRight(),
                bottom = emgMap.getUiSettings().getAttributionMarginBottom();

        switch (seekBar.getId()) {
            case R.id.id_compass_margin_left:
                left = (int) (mapView.getWidth() * (progress / 100f));
                break;
            case R.id.id_compass_margin_top:
                top = (int) (mapView.getHeight() * (progress / 100f));
                break;
            case R.id.id_compass_margin_right:
                right = (int) (mapView.getWidth() * (progress / 100f));
                break;
            case R.id.id_compass_margin_bottom:
                bottom = (int) (mapView.getHeight() * (progress / 100f));
                break;
        }
        emgMap.getUiSettings().setCompassMargins(left, top, right, bottom);

    }

    private void reset() {
        mCompassMarginLeftSeekBar.setProgress(0);
        mCompassMarginToptSeekBar.setProgress(0);
        mCompassMarginRightSeekBar.setProgress(0);
        mCompassMarginBottomSeekBar.setProgress(0);
        emgMap.getUiSettings().setCompassMargins(0, 0, 0, 0);

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
