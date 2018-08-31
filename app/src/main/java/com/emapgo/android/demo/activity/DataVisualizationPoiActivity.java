package com.emapgo.android.demo.activity;

import android.graphics.Color;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.emapgo.android.demo.MyApplication;
import com.emapgo.android.demo.R;
import com.emapgo.android.demo.util.MapStyleUtil;
import com.emapgo.android.demo.util.PreferenceManager;
import com.emapgo.geojson.Feature;
import com.emapgo.geojson.FeatureCollection;
import com.emapgo.geojson.Point;
import com.emapgo.geojson.Polygon;
import com.emapgo.mapsdk.constants.Style;
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.maps.MapView;
import com.emapgo.mapsdk.maps.OnMapReadyCallback;
import com.emapgo.mapsdk.search.poi.PoiInfo;
import com.emapgo.mapsdk.search.poi.PoiResult;
import com.emapgo.mapsdk.search.poi.PoiSearch;
import com.emapgo.mapsdk.search.poi.PoiSearchOption;
import com.emapgo.mapsdk.style.expressions.Expression;
import com.emapgo.mapsdk.style.functions.Function;
import com.emapgo.mapsdk.style.functions.stops.IdentityStops;
import com.emapgo.mapsdk.style.functions.stops.Stops;
import com.emapgo.mapsdk.style.layers.FillExtrusionLayer;
import com.emapgo.mapsdk.style.light.Light;
import com.emapgo.mapsdk.style.light.Position;
import com.emapgo.mapsdk.style.sources.GeoJsonSource;
import com.emapgo.turf.TurfConstants;
import com.emapgo.turf.TurfTransformation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.emapgo.mapsdk.style.layers.PropertyFactory.fillExtrusionColor;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.fillExtrusionHeight;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.fillExtrusionOpacity;

public class DataVisualizationPoiActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ProgressBar progressBar;
    private MapView mapView;
    private EmgMap emgMap;
    private List<PoiInfo> points = new ArrayList<>();
    private int maxPage = 80;
    private int currentPage = 1;
    private int size = 20;
    private int baseHeight = 50;
    private String colors[] = {
            "#efc330"
            , "#cc6328"
            , "#501e48"
            , "#811a2f"
            , "#b02d3c"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_visualization_poi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        progressBar = findViewById(R.id.id_progressbar);
        mapView = (MapView) findViewById(R.id.emgMapView);
        mapView.onCreate(savedInstanceState);
        mapView.setStyleUrl(PreferenceManager.getMapStyle(MyApplication.getAppContext()));
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(EmgMap emgMap) {
        MapStyleUtil.darkStyle(emgMap);
        this.emgMap = emgMap;
        emgMap.setTilt(60);
        getData();
    }

    private void getData() {
        if (isFinishing() || isDestroyed()) {
            return;
        }
        PoiSearchOption option = PoiSearchOption.builder()
                .baseUrl(PreferenceManager.getMapService(MyApplication.getAppContext()))
                .key(getString(R.string.key))
                .secret(getString(R.string.secret))
                .query("酒店")
                .area("110000")
                .type(1)
                .size(size)
                .page(currentPage)
                .build();
        PoiSearch.newInstance().search(option, new Callback<PoiResult>() {
            @Override
            public void onResponse(Call<PoiResult> call, Response<PoiResult> response) {
                points.addAll(response.body().getList());
                loopGetData();
            }


            @Override
            public void onFailure(Call<PoiResult> call, Throwable t) {
                loopGetData();
            }

            private void loopGetData() {
                if (currentPage < maxPage) {
                    currentPage++;
                    progressBar.setProgress(currentPage);
                    getData();
                } else {
                    draw(emgMap);
                }
            }
        });
    }

    private void draw(EmgMap emgMap) {
        Light light = emgMap.getLight();
        light.setPosition(new Position(1.15f, 210, 30));
        List<Feature> features = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < points.size(); i++) {
            PoiInfo poiInfo = points.get(i);
            Polygon circle = TurfTransformation.circle(Point.fromLngLat(Double.parseDouble(poiInfo.getLon()), Double.parseDouble(poiInfo.getLat())), 0.4, 4, TurfConstants.UNIT_KILOMETERS);
            Feature feature = Feature.fromGeometry(circle.outer());
            feature.addNumberProperty("e", (random.nextInt(maxPage) + 1));
            feature.addStringProperty("color_key", colors[random.nextInt(colors.length - 1)]);
            feature.addBooleanProperty("is_loop", false);
            if (i % 5 == 0) {
                feature.addNumberProperty("e", baseHeight / 2 * (random.nextInt(maxPage) + 1));
                feature.addBooleanProperty("is_loop", true);
            }

            features.add(feature);


        }

        for (int i = 0; i < features.size(); i++) {
            Feature feature = features.get(i);
            Boolean isLoop = feature.getBooleanProperty("is_loop");
            feature.addBooleanProperty("is_loop", false);
            if (isLoop != null && isLoop) {
                Number e = feature.getNumberProperty("e");
                int value = e.intValue();
                if (i % 4 == 0) {
                    feature.addNumberProperty("e", baseHeight * (random.nextInt(maxPage) + 1));
                    feature.addBooleanProperty("is_loop", true);
                }
            }

        }
        for (int i = 0; i < features.size(); i++) {
            Feature feature = features.get(i);
            Boolean isLoop = feature.getBooleanProperty("is_loop");
            if (isLoop != null && isLoop) {
                Number e = feature.getNumberProperty("e");
                int value = e.intValue();
                if (i % 4 == 0) {
                    feature.addNumberProperty("e", baseHeight * 2 * (random.nextInt(maxPage) + 1));
                }
            }

        }

        GeoJsonSource courseRouteGeoJson = new GeoJsonSource("coursedata", FeatureCollection.fromFeatures(features));
        emgMap.addSource(courseRouteGeoJson);

        FillExtrusionLayer courseExtrusionLayer = new FillExtrusionLayer("course", "coursedata");
        courseExtrusionLayer.setProperties(
                fillExtrusionColor(Function.property("color_key", new IdentityStops<String>())),
                fillExtrusionOpacity(1f),
                fillExtrusionHeight(Function.property("e", Stops.<Float>identity()))
        );
        emgMap.addLayer(courseExtrusionLayer);
        progressBar.setVisibility(View.GONE);
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
