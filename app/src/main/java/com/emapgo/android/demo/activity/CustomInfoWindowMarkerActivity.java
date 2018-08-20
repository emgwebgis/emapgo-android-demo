package com.emapgo.android.demo.activity;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.emapgo.android.demo.MyApplication;
import com.emapgo.android.demo.R;
import com.emapgo.android.demo.util.PreferenceManager;
import com.emapgo.geojson.Point;
import com.emapgo.mapsdk.annotations.Icon;
import com.emapgo.mapsdk.annotations.IconFactory;
import com.emapgo.mapsdk.annotations.Marker;
import com.emapgo.mapsdk.annotations.MarkerOptions;
import com.emapgo.mapsdk.geometry.LatLng;
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.maps.MapView;
import com.emapgo.mapsdk.maps.OnMapReadyCallback;
import com.emapgo.mapsdk.search.poi.PoiResult;
import com.emapgo.mapsdk.search.poi.PoiSearch;
import com.emapgo.mapsdk.search.poi.PoiSearchOption;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CustomInfoWindowMarkerActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_info_window_marker);
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
                .icon(icon);

        //添加Marker到地图
        emgMap.addMarker(markerOptions);

        //自定义InfoWIndow
        emgMap.setInfoWindowAdapter(new EmgMap.InfoWindowAdapter() {
            @Nullable
            @Override
            public View getInfoWindow(@NonNull Marker marker) {
                //若要为不同的Marker添加不同的View，可以通过marker进行判断
                View view = getLayoutInflater().inflate(R.layout.layout_marker_info_window, null);
                view.findViewById(R.id.id_info_window_btn1).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(CustomInfoWindowMarkerActivity.this, "设为起点", Toast.LENGTH_SHORT).show();
                    }
                });
                view.findViewById(R.id.id_info_window_btn2).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(CustomInfoWindowMarkerActivity.this, "设为终点", Toast.LENGTH_SHORT).show();
                    }
                });
                return view;
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
