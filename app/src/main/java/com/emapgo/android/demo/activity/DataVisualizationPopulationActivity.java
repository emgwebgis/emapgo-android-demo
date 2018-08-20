package com.emapgo.android.demo.activity;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.emapgo.android.demo.MyApplication;
import com.emapgo.android.demo.R;
import com.emapgo.android.demo.module.DataVisualizationPopulationCircleLayer;
import com.emapgo.android.demo.util.MapStyleUtil;
import com.emapgo.android.demo.util.PreferenceManager;
import com.emapgo.geojson.Feature;
import com.emapgo.geojson.FeatureCollection;
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.maps.MapView;
import com.emapgo.mapsdk.maps.OnMapReadyCallback;
import com.emapgo.mapsdk.style.functions.Function;
import com.emapgo.mapsdk.style.functions.stops.Stops;
import com.emapgo.mapsdk.style.layers.CircleLayer;
import com.emapgo.mapsdk.style.layers.FillLayer;
import com.emapgo.mapsdk.style.layers.Property;
import com.emapgo.mapsdk.style.layers.PropertyFactory;
import com.emapgo.mapsdk.style.sources.GeoJsonSource;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.emapgo.mapsdk.style.layers.PropertyFactory.circleColor;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.fillColor;

/**
 * @author @emapgo
 * @version 1.0
 * @create 2018/8/6
 * 中国人口分布数据可视化
 */
public class DataVisualizationPopulationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private float radius = 5.5f;
    private static final String PROPERTY_RADIUS_KEY = "PROPERTY_RADIUS_KEY";
    private static final String PROPERTY_BLUR_RADIUS_KEY = "PROPERTY_BLUR_RADIUS_KEY";
    private static final int MAX_SOURCES = 10;
    private Random random = new Random();
    private List<DataVisualizationPopulationCircleLayer> circleLayerList = new ArrayList<DataVisualizationPopulationCircleLayer>();
    private List<FeatureCollection> featureCollectionArrayList = new ArrayList<>();
    private FeatureCollection orignFeatureCollection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_visualization_population);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mapView = (MapView) findViewById(R.id.emgMapView);
        mapView.onCreate(savedInstanceState);
        mapView.setStyleUrl(PreferenceManager.getMapStyle(MyApplication.getAppContext()));
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(EmgMap emgMap) {
        MapStyleUtil.darkTwoStyle(emgMap);
        initFeatureCollectionFromFile();

        for (int i = 0; i < MAX_SOURCES; i++) {
            for (int k = 0; k < orignFeatureCollection.features().size() / MAX_SOURCES; k++) {
                getFeatureFromRandomFeatures(orignFeatureCollection, i);
            }
        }

        for (int i = 0; i < featureCollectionArrayList.size(); i++) {
            GeoJsonSource attractionsSource = new GeoJsonSource("attractions-" + i, featureCollectionArrayList.get(i));
            emgMap.addSource(attractionsSource);

            DataVisualizationPopulationCircleLayer dataVisualizationPopulationCircleLayer = new DataVisualizationPopulationCircleLayer();


            CircleLayer attractionsLayer = new CircleLayer("attractions-" + i, "attractions-" + i).withProperties(
                    circleColor(Color.parseColor("#a9fba6")),
                    PropertyFactory.circleRadius(Function.property(PROPERTY_RADIUS_KEY, Stops.<Float>identity())),
                    PropertyFactory.circleStrokeWidth(1f),
                    PropertyFactory.circlePitchScale("viewport"),
                    PropertyFactory.circleStrokeColor("#FFFFFF")
            );
            emgMap.addLayer(attractionsLayer);

            dataVisualizationPopulationCircleLayer.setCircleLayer(attractionsLayer);
            //添加光晕图层
            CircleLayer circleBlurLayer = new CircleLayer("circleBlurLayer-" + i, "attractions-" + i).withProperties(
                    circleColor(Color.parseColor("#b1faaa")),
                    PropertyFactory.circleRadius(Function.property(PROPERTY_BLUR_RADIUS_KEY, Stops.<Float>identity())),
                    PropertyFactory.circleOpacity(0.8f),
                    PropertyFactory.circleStrokeWidth(0f),
                    PropertyFactory.circleStrokeOpacity(0f),
                    PropertyFactory.circlePitchScale("viewport"),
                    PropertyFactory.circleBlur(1.5f)
            );
            emgMap.addLayer(circleBlurLayer);
            dataVisualizationPopulationCircleLayer.setBlurCircleLayer(circleBlurLayer);

            ValueAnimator colorAnimator = ValueAnimator.ofFloat(i % 2 == 0 ? 1f : 0.5f, i % 2 == 0 ? 0.5f : 1f);
            colorAnimator.setDuration(random.nextInt(800) + 500);
            colorAnimator.setRepeatCount(ValueAnimator.INFINITE);
            colorAnimator.setRepeatMode(ValueAnimator.REVERSE);
            colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                @Override
                public void onAnimationUpdate(ValueAnimator animator) {

                    attractionsLayer.setProperties(
                            PropertyFactory.circleOpacity((float) animator.getAnimatedValue()),
                            PropertyFactory.circleStrokeOpacity((float) animator.getAnimatedValue())
                    );
                    circleBlurLayer.setProperties(
                            PropertyFactory.circleOpacity(((float) animator.getAnimatedValue() - 0.5f))
                    );
                }

            });
            dataVisualizationPopulationCircleLayer.setValueAnimator(colorAnimator);

            circleLayerList.add(dataVisualizationPopulationCircleLayer);
        }

        for (int i = 0; i < circleLayerList.size(); i++) {
            circleLayerList.get(i).getValueAnimator().start();
        }

    }


    @NonNull
    private FeatureCollection initFeatureCollectionFromFile() {
        orignFeatureCollection = FeatureCollection.fromJson(loadJsonFromAsset("data_visuallization.geojson"));
        for (int i = 0; i < orignFeatureCollection.features().size(); i++) {
            Feature feature = orignFeatureCollection.features().get(i);
            if (i % 7 == 0) {
                feature.addNumberProperty(PROPERTY_RADIUS_KEY, radius / 1.3);
                feature.addNumberProperty(PROPERTY_BLUR_RADIUS_KEY, radius / 1.3 * 3.5);
            } else if (i % 5 == 0) {
                feature.addNumberProperty(PROPERTY_RADIUS_KEY, radius / 1.5);
                feature.addNumberProperty(PROPERTY_BLUR_RADIUS_KEY, radius / 1.5 * 3.5);
            } else {
                feature.addNumberProperty(PROPERTY_RADIUS_KEY, radius);
                feature.addNumberProperty(PROPERTY_BLUR_RADIUS_KEY, radius * 3.5);
            }
        }

        return orignFeatureCollection;
    }


    private void getFeatureFromRandomFeatures(FeatureCollection orignFeatures, int index) {
        Feature feature = orignFeatures.features().get(random.nextInt(orignFeatures.features().size() - 1) + 1);
        if (featureCollectionArrayList.size() == index) {
            featureCollectionArrayList.add(FeatureCollection.fromFeature(feature));
        } else if (check(feature)) {
            getFeatureFromRandomFeatures(orignFeatures, index);
        } else {
            featureCollectionArrayList.get(index).features().add(feature);
        }

    }

    private boolean check(Feature feature) {
        for (int i = 0; i < featureCollectionArrayList.size(); i++) {
            if (featureCollectionArrayList.get(i).features().contains(feature)) {
                return true;
            }
        }
        return false;
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
        for (int i = 0; i < circleLayerList.size(); i++) {
            circleLayerList.get(i).getValueAnimator().cancel();
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private String loadJsonFromAsset(String filename) {
        try {
            InputStream is = getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
