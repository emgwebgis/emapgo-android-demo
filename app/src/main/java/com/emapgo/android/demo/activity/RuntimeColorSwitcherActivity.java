package com.emapgo.android.demo.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.emapgo.android.demo.MyApplication;
import com.emapgo.android.demo.R;
import com.emapgo.android.demo.util.PreferenceManager;
import com.emapgo.mapsdk.SDKInitializer;
import com.emapgo.mapsdk.constants.Style;
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.maps.MapView;
import com.emapgo.mapsdk.maps.OnMapReadyCallback;
import com.emapgo.mapsdk.style.layers.FillExtrusionLayer;
import com.emapgo.mapsdk.style.layers.FillLayer;
import com.emapgo.mapsdk.style.layers.Layer;
import com.emapgo.mapsdk.style.layers.PropertyFactory;

import java.util.List;

import static com.emapgo.mapsdk.style.layers.PropertyFactory.fillColor;

public class RuntimeColorSwitcherActivity extends AppCompatActivity {
    private MapView mapView;
    private EmgMap map;
    FillLayer water;
    FillExtrusionLayer building;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runtime_color_switcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final SeekBar redSeekBar = (SeekBar) findViewById(R.id.red_seek_bar);
        final SeekBar greenSeekBar = (SeekBar) findViewById(R.id.green_seek_bar);
        final SeekBar blueSeekBar = (SeekBar) findViewById(R.id.blue_seek_bar);

        final Spinner layerPicker = (Spinner) findViewById(R.id.spinner_layer_picker);

        layerPicker.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if (layerPicker.getSelectedItem().toString().equals("Building")) {

                   /* if (building != null) {
                        redSeekBar.setProgress(Color.red(building.getFillColorAsInt()));
                        greenSeekBar.setProgress(Color.green(building.getFillColorAsInt()));
                        blueSeekBar.setProgress(Color.blue(building.getFillColorAsInt()));

                    }*/

                } else if (layerPicker.getSelectedItem().toString().equals("Water")) {

                    if (water != null) {
                        redSeekBar.setProgress(Color.red(water.getFillColorAsInt()));
                        greenSeekBar.setProgress(Color.green(water.getFillColorAsInt()));
                        blueSeekBar.setProgress(Color.blue(water.getFillColorAsInt()));
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        redSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (water != null && layerPicker.getSelectedItem().equals("Water") && fromUser) {
                    water.setProperties(
                            fillColor(Color.rgb(progress, greenSeekBar.getProgress(), blueSeekBar.getProgress()))
                    );
                } else if (building != null && layerPicker.getSelectedItem().equals("Building") && fromUser) {
                    building.setProperties(
                            PropertyFactory.fillExtrusionColor(Color.rgb(progress, greenSeekBar.getProgress(), blueSeekBar.getProgress()))
                    );
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        greenSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (water != null && layerPicker.getSelectedItem().equals("Water") && fromUser) {
                    water.setProperties(
                            fillColor(Color.rgb(redSeekBar.getProgress(), progress, blueSeekBar.getProgress()))
                    );
                } else if (building != null && layerPicker.getSelectedItem().equals("Building") && fromUser) {
                    building.setProperties(
                            PropertyFactory.fillExtrusionColor(Color.rgb(progress, greenSeekBar.getProgress(), greenSeekBar.getProgress()))
                    );
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        blueSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (water != null && layerPicker.getSelectedItem().equals("Water") && fromUser) {
                    water.setProperties(
                            fillColor(Color.rgb(redSeekBar.getProgress(), greenSeekBar.getProgress(), progress))
                    );
                } else if (building != null && layerPicker.getSelectedItem().equals("Building") && fromUser) {
                    building.setProperties(
                            PropertyFactory.fillExtrusionColor(Color.rgb(progress, greenSeekBar.getProgress(), blueSeekBar.getProgress()))
                    );
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        mapView = (MapView) findViewById(R.id.emgMapView);
        mapView.onCreate(savedInstanceState);
        mapView.setStyleUrl(PreferenceManager.getMapStyle(MyApplication.getAppContext()));
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(EmgMap emgMap) {
                map = emgMap;
                water = (FillLayer) map.getLayer("china-river");
                building = (FillExtrusionLayer) map.getLayer("china-building");

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
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
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
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
