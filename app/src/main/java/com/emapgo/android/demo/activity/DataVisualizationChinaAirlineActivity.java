package com.emapgo.android.demo.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.LinearInterpolator;

import com.emapgo.android.demo.MyApplication;
import com.emapgo.android.demo.R;
import com.emapgo.android.demo.module.DataVisualizationChinaAirlineAnimation;
import com.emapgo.android.demo.util.MapStyleUtil;
import com.emapgo.android.demo.util.PreferenceManager;
import com.emapgo.geojson.Feature;
import com.emapgo.geojson.FeatureCollection;
import com.emapgo.geojson.LineString;
import com.emapgo.geojson.MultiPoint;
import com.emapgo.geojson.Point;
import com.emapgo.mapsdk.camera.CameraPosition;
import com.emapgo.mapsdk.camera.CameraUpdateFactory;
import com.emapgo.mapsdk.geometry.LatLng;
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.maps.MapView;
import com.emapgo.mapsdk.maps.OnMapReadyCallback;
import com.emapgo.mapsdk.style.functions.Function;
import com.emapgo.mapsdk.style.functions.stops.IdentityStops;
import com.emapgo.mapsdk.style.layers.CircleLayer;
import com.emapgo.mapsdk.style.layers.LineLayer;
import com.emapgo.mapsdk.style.layers.PropertyFactory;
import com.emapgo.mapsdk.style.sources.GeoJsonSource;
import com.emapgo.turf.TurfConstants;
import com.emapgo.turf.TurfMeasurement;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static com.emapgo.mapsdk.style.layers.PropertyFactory.circleColor;

public class DataVisualizationChinaAirlineActivity extends AppCompatActivity implements OnMapReadyCallback {
    private EmgMap emgMap;
    private MapView mapView;
    private AsyncTask<Void, Object, Boolean> task;
    private List<List<Point>> points = new ArrayList<>();
    private ValueAnimator targetCircleValueAnimator;
    private ValueAnimator targetCircleValueAnimatorTwo;
    private ValueAnimator targetMaxCircleValueAnimator;
    private ValueAnimator targetMaxCircleValueAnimatorTwo;
    private ValueAnimator.AnimatorUpdateListener targetCircleAnimatorUpdateListener;
    private AnimatorListenerAdapter targetCircleAnimatorListenerAdapter;
    private ValueAnimator.AnimatorUpdateListener maxCircleAnimatorUpdateListener;
    private AnimatorListenerAdapter maxCircleAnimatorListenerAdapter;
    private CircleLayer targetCircleLayer;
    private CircleLayer targetMaxCircleLayer;

    private String colors[] = {
            "#91f23f"
            , "#8effea"
            , "#d4ff3b"
            , "#b02d3c"};
    private List<String> pointColors = new ArrayList<>();
    private List<List<Point>> linePoints = new ArrayList<>();
    private List<DataVisualizationChinaAirlineAnimation> dataVisualizationChinaAirlineAnimationArrayList = new ArrayList<>();

    private static final int steps = 10;
    private static final int duration = 650;
    private static final float baseLineWidth = 2f;
    private static final float minRadius = 0.5f;
    private static final float maxRadius = 7.5f;
    private static final String POINT_COOR_KEY = "POINT_COOR_KEY";
    private static final String LINE_COOR_KEY = "LINE_COOR_KEY";
    private static final String HOT_LINE_COLOR = "#d02903";
    private static final Point[] HOT_LINES = {
            Point.fromLngLat(104.06661987304688, 30.675715404167743),
            Point.fromLngLat(121.46621704101562, 31.18049705355662)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_visualization_china_airline);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mapView = (MapView) findViewById(R.id.emgMapView);
        mapView.onCreate(savedInstanceState);
        mapView.setStyleUrl(PreferenceManager.getMapStyle(MyApplication.getAppContext()));
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(EmgMap emgMap) {
        this.emgMap = emgMap;
        MapStyleUtil.darkTwoStyle(emgMap);
        initData();
        drawTargetPoint();
        task = new LoadDataAsyncTask().execute();
    }

    /**
     * 画目标点
     */
    private void drawTargetPoint() {
        List<Feature> features = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < points.size(); i++) {
            Point targetPoint = points.get(i).get(1);
            Feature feature = Feature.fromGeometry(targetPoint);
            pointColors.add(isHotLine(targetPoint) ? HOT_LINE_COLOR : colors[random.nextInt(colors.length - 1)]);
            feature.addStringProperty(POINT_COOR_KEY, pointColors.get(i));
            features.add(feature);
        }
        GeoJsonSource targetGeoJsonSource = new GeoJsonSource("target-marker-source", FeatureCollection.fromFeatures(features));
        emgMap.addSource(targetGeoJsonSource);

        targetCircleLayer = new CircleLayer("target-marker-layer", "target-marker-source").withProperties(
                PropertyFactory.circleOpacity(0f),
                PropertyFactory.circleRadius(minRadius),
                PropertyFactory.circleStrokeWidth(1.5f),
                PropertyFactory.circleStrokeColor(Function.property(POINT_COOR_KEY, new IdentityStops<String>()))
        );
        emgMap.addLayer(targetCircleLayer);

        targetMaxCircleLayer = new CircleLayer("target-max-marker-layer", "target-marker-source").withProperties(
                PropertyFactory.circleOpacity(0f),
                PropertyFactory.circleRadius(minRadius),
                PropertyFactory.circleStrokeWidth(1.5f),
                PropertyFactory.circleStrokeColor(Function.property(POINT_COOR_KEY, new IdentityStops<String>()))
        );
        emgMap.addLayerAbove(targetMaxCircleLayer, "target-marker-layer");

        startTargetCircleAnimation();

    }

    private boolean isHotLine(Point line) {
        for (int i = 0; i < HOT_LINES.length; i++) {
            if (HOT_LINES[i].longitude() == line.longitude() &&
                    HOT_LINES[i].latitude() == line.latitude()) {
                return true;
            }
        }
        return false;
    }


    private void initData() {
        points.add(Arrays.asList(Point.fromLngLat(116.46, 39.92), Point.fromLngLat(104.06661987304688,
                30.675715404167743)));
        points.add(Arrays.asList(Point.fromLngLat(116.46, 39.92), Point.fromLngLat(106.55502319335938,
                29.55733160587419)));
        points.add(Arrays.asList(Point.fromLngLat(116.46, 39.92), Point.fromLngLat(102.68646240234375,
                25.04330389487308)));
        points.add(Arrays.asList(Point.fromLngLat(116.46, 39.92), Point.fromLngLat(113.64257812499999,
                34.755153088189324)));
        points.add(Arrays.asList(Point.fromLngLat(116.46, 39.92), Point.fromLngLat(121.46621704101562,
                31.18049705355662)));
        points.add(Arrays.asList(Point.fromLngLat(116.46, 39.92), Point.fromLngLat(113.30474853515625,
                23.104996849988808)));
        points.add(Arrays.asList(Point.fromLngLat(116.46, 39.92), Point.fromLngLat(108.93630981445312,
                34.264026473152875)));
        points.add(Arrays.asList(Point.fromLngLat(116.46, 39.92), Point.fromLngLat(112.5494384765625,
                37.85750715625203)));
        points.add(Arrays.asList(Point.fromLngLat(116.46, 39.92), Point.fromLngLat(87.60223388671875,
                43.8008364060122)));
        points.add(Arrays.asList(Point.fromLngLat(116.46, 39.92), Point.fromLngLat(114.3017578125,
                30.593001325080845)));
        points.add(Arrays.asList(Point.fromLngLat(116.46, 39.92), Point.fromLngLat(91.131591796875,
                29.668962525992505)));
        points.add(Arrays.asList(Point.fromLngLat(116.46, 39.92), Point.fromLngLat(119.28955078124999,
                26.09625490696853)));
        points.add(Arrays.asList(Point.fromLngLat(116.46, 39.92), Point.fromLngLat(126.58447265624999,
                45.767522962149876)));
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
        if (task != null) {
            task.cancel(true);
        }
        targetCircleValueAnimator.cancel();
        targetCircleValueAnimatorTwo.cancel();
        targetMaxCircleValueAnimator.cancel();
        targetMaxCircleValueAnimatorTwo.cancel();
        for (DataVisualizationChinaAirlineAnimation dataVisualizationChinaAirlineAnimation : dataVisualizationChinaAirlineAnimationArrayList) {
            dataVisualizationChinaAirlineAnimation.cancel();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    private void startTargetCircleAnimation() {
        targetCircleAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                if (isDestroyed()) {
                    return;
                }
                float animatedValue = (float) animator.getAnimatedValue();
                float alpha = (maxRadius - animatedValue) / maxRadius + 0.7f;
                targetCircleLayer.setProperties(
                        PropertyFactory.circleStrokeOpacity(animatedValue >= maxRadius / 2 ? alpha : Math.abs(maxRadius / 2 / animatedValue / 2 / 2)),
                        PropertyFactory.circleRadius(animatedValue)
                );

            }
        };
        targetCircleAnimatorListenerAdapter = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (isDestroyed()) {
                    return;
                }
                targetCircleValueAnimatorTwo.start();
            }
        };

        targetCircleValueAnimator = ValueAnimator.ofFloat(minRadius, maxRadius);
        targetCircleValueAnimator.setDuration(duration);
        targetCircleValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        targetCircleValueAnimator.addListener(targetCircleAnimatorListenerAdapter);
        targetCircleValueAnimator.addUpdateListener(targetCircleAnimatorUpdateListener);


        targetCircleValueAnimatorTwo = ValueAnimator.ofFloat(0.5f, 0f);
        targetCircleValueAnimatorTwo.setDuration(duration);
        targetCircleValueAnimatorTwo.setRepeatMode(ValueAnimator.RESTART);
        targetCircleValueAnimatorTwo.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                if (isDestroyed()) {
                    return;
                }
                float animatedValue = (float) animator.getAnimatedValue();
                targetCircleLayer.setProperties(
                        PropertyFactory.circleStrokeOpacity(animatedValue - 0.5f / 2)
                );

                if (animatedValue <= 0.5f / 2) {
                    targetCircleValueAnimatorTwo.cancel();
                    emgMap.removeLayer("target-marker-layer");
                    targetCircleLayer = new CircleLayer("target-marker-layer", "target-marker-source").withProperties(
                            PropertyFactory.circleOpacity(0f),
                            PropertyFactory.circleRadius(minRadius),
                            PropertyFactory.circleStrokeWidth(1.5f),
                            PropertyFactory.circleStrokeColor(Function.property(POINT_COOR_KEY, new IdentityStops<String>()))
                    );
                    emgMap.addLayer(targetCircleLayer);


                }


            }
        });
        targetCircleValueAnimatorTwo.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (isDestroyed()) {
                    return;
                }
                //max circlelayer 开始动动画
                startMaxCircleAnimation();
            }
        });

        targetCircleValueAnimator.start();
    }

    private void startMaxCircleAnimation() {
        maxCircleAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                if (isDestroyed()) {
                    return;
                }
                float animatedValue = (float) animator.getAnimatedValue();
                float alpha = (maxRadius - animatedValue) / maxRadius + 0.7f;
                targetMaxCircleLayer.setProperties(
                        PropertyFactory.circleStrokeOpacity(animatedValue >= maxRadius / 2 ? alpha : Math.abs(maxRadius / 2 / animatedValue / 2 / 2)),
                        PropertyFactory.circleRadius(animatedValue)
                );

            }
        };
        maxCircleAnimatorListenerAdapter = new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if (isDestroyed()) {
                    return;
                }
                targetMaxCircleValueAnimatorTwo.start();
            }
        };

        targetMaxCircleValueAnimator = ValueAnimator.ofFloat(minRadius, maxRadius);
        targetMaxCircleValueAnimator.setDuration(duration);
        targetMaxCircleValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        targetMaxCircleValueAnimator.addListener(maxCircleAnimatorListenerAdapter);
        targetMaxCircleValueAnimator.addUpdateListener(maxCircleAnimatorUpdateListener);


        targetMaxCircleValueAnimatorTwo = ValueAnimator.ofFloat(0.5f, 0f);
        targetMaxCircleValueAnimatorTwo.setDuration(duration);
        targetMaxCircleValueAnimatorTwo.setRepeatMode(ValueAnimator.RESTART);
        targetMaxCircleValueAnimatorTwo.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                if (isDestroyed()) {
                    return;
                }
                float animatedValue = (float) animator.getAnimatedValue();
                targetMaxCircleLayer.setProperties(
                        PropertyFactory.circleStrokeOpacity(animatedValue - 0.5f / 2)
                );
                if (animatedValue <= 0.5f / 2) {
                    targetMaxCircleValueAnimatorTwo.cancel();
                    emgMap.removeLayer("target-max-marker-layer");
                    targetMaxCircleLayer = new CircleLayer("target-max-marker-layer", "target-marker-source").withProperties(
                            PropertyFactory.circleOpacity(0f),
                            PropertyFactory.circleRadius(minRadius),
                            PropertyFactory.circleStrokeWidth(1.5f),
                            PropertyFactory.circleStrokeColor(Function.property(POINT_COOR_KEY, new IdentityStops<String>()))
                    );
                    //在指定的图层之上
                    emgMap.addLayerAbove(targetMaxCircleLayer, "target-marker-layer");
                }

            }
        });
        targetMaxCircleValueAnimatorTwo.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                if (isDestroyed()) {
                    return;
                }
                //targetcirclelayer 开始动动画
                startTargetCircleAnimation();
            }
        });
        targetMaxCircleValueAnimator.start();
    }

    public class LoadDataAsyncTask extends AsyncTask<Void, Object, Boolean> {


        @Override
        protected Boolean doInBackground(Void... voids) {

            for (int k = 0; k < points.size() && !isCancelled(); k++) {
                List<Point> point = points.get(k);
                LineString lineString = LineString.fromLngLats(point);
                double lineDistance = TurfMeasurement.lineDistance(lineString, TurfConstants.UNIT_KILOMETERS);

                List<Point> _point = new ArrayList<>();
                for (int i = 0; i <= lineDistance + lineDistance / steps && !isCancelled(); i += lineDistance / steps) {
                    _point.add(TurfMeasurement.along(lineString, i, TurfConstants.UNIT_KILOMETERS));
                }
                linePoints.add(_point);
                Feature feature = Feature.fromGeometry(LineString.fromLngLats(_point));
                feature.addStringProperty(LINE_COOR_KEY, pointColors.get(k));
                onProgressUpdate(FeatureCollection.fromFeature(feature), k);

            }
            return true;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            super.onProgressUpdate(values);
            GeoJsonSource airGeoJsonSource = new GeoJsonSource("airlinesource-" + values[1], (FeatureCollection) values[0]);
            emgMap.addSource(airGeoJsonSource);

            LineLayer airlineLayer = new LineLayer("airlinelayer-" + values[1], "airlinesource-" + values[1]).withProperties(
                    PropertyFactory.lineColor(Function.property(LINE_COOR_KEY, new IdentityStops<String>())),
                    PropertyFactory.lineCap("round"),
                    PropertyFactory.lineOpacity(0.8f),
                    PropertyFactory.lineWidth(baseLineWidth)
            );
            emgMap.addLayer(airlineLayer);


        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            //drawLightBlurLine();
            drawLightLocationMarker();
            launcherMoveAnimation();
        }

        private void drawLightBlurLine() {
            for (int i = 0; i < linePoints.size(); i++) {

                ///////
                List<Point> pointList = new ArrayList<>();
                for (int l = linePoints.get(i).size() / 4; l < linePoints.get(i).size() / 2; l++) {
                    pointList.add(linePoints.get(i).get(l));
                }
                Feature feature = Feature.fromGeometry(LineString.fromLngLats(pointList));
                /////////
                GeoJsonSource lightBlurLineSource = new GeoJsonSource("light_blur_line_source_" + i, FeatureCollection.fromFeature(feature));
                emgMap.addSource(lightBlurLineSource);

                LineLayer lightBlurLineLayer = new LineLayer("light_blur_line_layer_" + i, "light_blur_line_source_" + i).withProperties(
                        PropertyFactory.lineColor(Color.parseColor(pointColors.get(i))),
                        PropertyFactory.lineWidth(maxRadius + 1f),
                        PropertyFactory.lineCap("round"),
                        PropertyFactory.lineOpacity(0.2f),
                        PropertyFactory.linePattern("")
                );
                emgMap.addLayerAbove(lightBlurLineLayer, "airlinelayer-" + i);

            }
        }

        private void drawLightLocationMarker() {
            for (int i = 0; i < linePoints.size(); i++) {
                GeoJsonSource lightLocationSource = new GeoJsonSource("light_location_source_" + i, FeatureCollection.fromFeatures(new Feature[]{}));
                emgMap.addSource(lightLocationSource);

                CircleLayer lightLocationLayer = new CircleLayer("light_location_layer_" + i, "light_location_source_" + i).withProperties(
                        PropertyFactory.circleRadius(baseLineWidth * 1.4f),
                        PropertyFactory.circleColor(Color.parseColor("#FFFFFF")),
                        PropertyFactory.circleOpacity(1.3f),
                        PropertyFactory.circleBlur(0.1f)
                );
                emgMap.addLayerAbove(lightLocationLayer, "airlinelayer-" + (linePoints.size()-1));


                GeoJsonSource lightLocationBlurSource = new GeoJsonSource("light_location_blur_source_" + i, FeatureCollection.fromFeatures(new Feature[]{}));
                emgMap.addSource(lightLocationBlurSource);

                CircleLayer lightLocationBlurLayer = new CircleLayer("light_location_blur_layer_" + i, "light_location_blur_source_" + i).withProperties(
                        PropertyFactory.circleRadius(baseLineWidth * 7f),
                        PropertyFactory.circleColor(Color.parseColor(pointColors.get(i))),
                        PropertyFactory.circleBlur(1.5f)
                );
                emgMap.addLayerAbove(lightLocationBlurLayer, "airlinelayer-" + (linePoints.size()-1));

            }

        }

        private void launcherMoveAnimation() {
            for (int i = 0; i < linePoints.size(); i++) {
                DataVisualizationChinaAirlineAnimation dataVisualizationChinaAirlineAnimation = new DataVisualizationChinaAirlineAnimation(emgMap, linePoints.get(i), i);
                dataVisualizationChinaAirlineAnimation.start();
                dataVisualizationChinaAirlineAnimationArrayList.add(dataVisualizationChinaAirlineAnimation);
            }

        }
    }

}
