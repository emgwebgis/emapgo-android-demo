package com.emapgo.android.demo.activity;

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
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.maps.MapView;
import com.emapgo.mapsdk.maps.OnMapReadyCallback;

import java.lang.reflect.Field;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnItemSelected;

public class MapComponentLogoActivity extends AppCompatActivity implements OnMapReadyCallback, SeekBar.OnSeekBarChangeListener {
    private MapView mapView;
    private EmgMap emgMap;

    @BindView(R.id.id_logo_enable)
    Switch mLogoEnableSwitch;
    @BindView(R.id.id_logo_gravity)
    Spinner mLogoGravitySpinner;
    @BindView(R.id.id_logo_margin_left)
    SeekBar mLogoMarginLeftSeekBar;
    @BindView(R.id.id_logo_margin_top)
    SeekBar mLogoMarginToptSeekBar;
    @BindView(R.id.id_logo_margin_right)
    SeekBar mLogoMarginRightSeekBar;
    @BindView(R.id.id_logo_margin_bottom)
    SeekBar mLogoMarginBottomSeekBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_logo_component);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);

        mapView = (MapView) findViewById(R.id.emgMapView);
        mapView.onCreate(savedInstanceState);
        mapView.setStyleUrl(PreferenceManager.getMapStyle(MyApplication.getAppContext()));
        mapView.getMapAsync(this);

        mLogoMarginLeftSeekBar.setOnSeekBarChangeListener(this);
        mLogoMarginToptSeekBar.setOnSeekBarChangeListener(this);
        mLogoMarginRightSeekBar.setOnSeekBarChangeListener(this);
        mLogoMarginBottomSeekBar.setOnSeekBarChangeListener(this);



    }

    @Override
    public void onMapReady(EmgMap emgMap) {
        this.emgMap = emgMap;
        mLogoEnableSwitch.setChecked(true);
        mLogoGravitySpinner.setSelection(8);
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

    @OnCheckedChanged(R.id.id_logo_enable)
    public void onLogoEnableCheckedChanged(CompoundButton button, boolean check) {
        if (emgMap != null) {
            emgMap.getUiSettings().setLogoEnabled(check);
        }
    }

    @OnItemSelected(R.id.id_logo_gravity)
    public void onLogoGravityItemSelected(int i) {
        if (emgMap == null) {
            return;
        }
        String item = (String) mLogoGravitySpinner.getAdapter().getItem(i);
        try {
            Field declaredField = Gravity.class.getDeclaredField(item.toUpperCase());
            emgMap.getUiSettings().setLogoGravity((Integer) declaredField.get(null));
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
        int left = emgMap.getUiSettings().getLogoMarginLeft(),
                top = emgMap.getUiSettings().getLogoMarginTop(),
                right = emgMap.getUiSettings().getLogoMarginRight(),
                bottom = emgMap.getUiSettings().getAttributionMarginBottom();

        switch (seekBar.getId()) {
            case R.id.id_logo_margin_left:
                left = (int) (mapView.getWidth() * (progress / 100f));
                break;
            case R.id.id_logo_margin_top:
                top = (int) (mapView.getHeight() * (progress / 100f));
                break;
            case R.id.id_logo_margin_right:
                right = (int) (mapView.getWidth() * (progress / 100f));
                break;
            case R.id.id_logo_margin_bottom:
                bottom = (int) (mapView.getHeight() * (progress / 100f));
                break;
        }
        emgMap.getUiSettings().setLogoMargins(left, top, right, bottom);

    }

    private void reset() {
        mLogoMarginLeftSeekBar.setProgress(0);
        mLogoMarginToptSeekBar.setProgress(0);
        mLogoMarginRightSeekBar.setProgress(0);
        mLogoMarginBottomSeekBar.setProgress(0);
        emgMap.getUiSettings().setLogoMargins(0,0,0,0);

    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
