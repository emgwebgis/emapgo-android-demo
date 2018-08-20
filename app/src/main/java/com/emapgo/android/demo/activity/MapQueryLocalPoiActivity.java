package com.emapgo.android.demo.activity;

import android.graphics.RectF;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.emapgo.android.demo.MyApplication;
import com.emapgo.android.demo.R;
import com.emapgo.android.demo.util.PreferenceManager;
import com.emapgo.geojson.Feature;
import com.emapgo.geojson.Point;
import com.emapgo.mapsdk.annotations.MarkerOptions;
import com.emapgo.mapsdk.constants.Style;
import com.emapgo.mapsdk.geometry.LatLng;
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.maps.MapView;
import com.emapgo.mapsdk.maps.OnMapReadyCallback;
import com.google.gson.JsonElement;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MapQueryLocalPoiActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private EmgMap emgMap;
    @BindView(R.id.id_query_local_poi_et)
    EditText mPoiEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_query_local_poi);
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
        emgMap.setMinZoomPreference(14);
    }

    @OnClick(R.id.id_query_local_poi_btn)
    public void onQueryClick(View view) {
        if (mPoiEditText.getEditableText().toString().isEmpty()) {
            Toast.makeText(this, "输入关键字开始搜索", Toast.LENGTH_SHORT).show();
            return;
        }
        if (emgMap == null) {
            return;
        }

        emgMap.clear();
        //设置检索字段
        String key = mPoiEditText.getEditableText().toString();
        //设置搜索范围
        int left = mapView.getLeft();
        int top = mapView.getTop();
        int right = mapView.getRight();
        int bootom = mapView.getBottom();
        //设置搜索范围
        RectF box = new RectF(left, top, right, bootom);
        //从地图中读取，注意 若地图缩放比例过小则需要更多的时间进行检索，建议缩放等级在14以上
        List<Feature> features = emgMap.queryRenderedFeatures(box);
        for (Feature feature : features) {
            try {
                if (feature.properties() != null) {
                    for (Map.Entry<String, JsonElement> entry : feature.properties().entrySet()) {
                        //匹配地图POI点是否和搜索项相匹配
                        if (entry.getValue().toString().indexOf(key) != -1) {
                            //添加标注
                            Point point = (Point) feature.geometry();
                            emgMap.addMarker(new MarkerOptions()
                                    .position(new LatLng(point.latitude(), point.longitude())));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
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
