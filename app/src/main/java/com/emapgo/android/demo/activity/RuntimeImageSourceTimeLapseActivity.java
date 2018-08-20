package com.emapgo.android.demo.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.emapgo.android.demo.R;
import com.emapgo.mapsdk.geometry.LatLng;
import com.emapgo.mapsdk.geometry.LatLngQuad;
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.maps.MapView;
import com.emapgo.mapsdk.maps.OnMapReadyCallback;
import com.emapgo.mapsdk.style.layers.RasterLayer;
import com.emapgo.mapsdk.style.sources.ImageSource;

public class RuntimeImageSourceTimeLapseActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private Handler handler;
    private Runnable runnable;
    private static final String ID_IMAGE_SOURCE = "animated_image_source";
    private static final String ID_IMAGE_LAYER = "animated_image_layer";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runtime_image_source_time_lapse);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mapView = (MapView) findViewById(R.id.emgMapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(EmgMap emgMap) {
        LatLngQuad quad = new LatLngQuad(
                new LatLng(41.27780646738183, 98.39355468749999),
                new LatLng(41.27780646738183, 123.3984375),
                new LatLng(21.49396356306447, 123.3984375),
                new LatLng(21.49396356306447,  98.39355468749999));
        emgMap.addSource(new ImageSource(ID_IMAGE_SOURCE, quad, R.mipmap.southeast_radar_0));

        RasterLayer layer = new RasterLayer(ID_IMAGE_LAYER, ID_IMAGE_SOURCE);
        emgMap.addLayer(layer);

        handler = new Handler();
        runnable = new RefreshImageRunnable(emgMap, handler);
        handler.postDelayed(runnable, 100);
    }

    private static class RefreshImageRunnable implements Runnable {
        private final EmgMap mapboxMap;
        private final Handler handler;
        private int[] drawables;
        private int drawableIndex;

        RefreshImageRunnable(EmgMap mapboxMap, Handler handler) {
            this.mapboxMap = mapboxMap;
            this.handler = handler;
            drawables = new int[4];
            drawables[0] = R.mipmap.southeast_radar_0;
            drawables[1] = R.mipmap.southeast_radar_1;
            drawables[2] = R.mipmap.southeast_radar_2;
            drawables[3] = R.mipmap.southeast_radar_3;
            drawableIndex = 1;
        }

        @Override
        public void run() {
            ((ImageSource) mapboxMap.getSource(ID_IMAGE_SOURCE)).setImage(drawables[drawableIndex++]);
            if (drawableIndex > 3) {
                drawableIndex = 0;
            }
            handler.postDelayed(this, 1000);
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
    public void onResume() {
        super.onResume();
        mapView.onResume();
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
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
        mapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }
}