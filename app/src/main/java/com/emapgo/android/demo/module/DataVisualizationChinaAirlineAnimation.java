package com.emapgo.android.demo.module;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.os.Handler;
import android.view.animation.LinearInterpolator;

import com.emapgo.geojson.Feature;
import com.emapgo.geojson.FeatureCollection;
import com.emapgo.geojson.Point;
import com.emapgo.mapsdk.camera.CameraPosition;
import com.emapgo.mapsdk.camera.CameraUpdateFactory;
import com.emapgo.mapsdk.geometry.LatLng;
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.style.sources.GeoJsonSource;
import com.emapgo.turf.TurfMeasurement;

import java.util.List;

/**
 * Created by ben on 2018/8/10.
 */

public class DataVisualizationChinaAirlineAnimation implements Runnable {
    private EmgMap emgMap;
    private List<Point> points;
    private int layerIndex;
    private Runnable runnable;
    private Handler handler;
    private int count = 0;

    public DataVisualizationChinaAirlineAnimation(EmgMap emgMap, List<Point> points, int layerIndex) {
        this.emgMap = emgMap;
        this.points = points;
        this.layerIndex = layerIndex;
        init();
    }

    private void init() {
        handler = new Handler();
        runnable = this;
    }


    @Override
    public void run() {
        if (count < points.size() && emgMap != null) {

            LatLng orgin = new LatLng(points.get(count - 1 < 0 ? 0 : count - 1).latitude(), points.get(count - 1 < 0 ? 0 : count - 1).longitude());
            LatLng target = new LatLng(points.get(count).latitude(), points.get(count).longitude());


            long distance = (long) TurfMeasurement.distance(Point.fromLngLat(orgin.getLongitude(), orgin.getLatitude()),
                    Point.fromLngLat(target.getLongitude(), target.getLatitude())) * 2;

            ValueAnimator markerAnimator = ObjectAnimator.ofObject(
                    new LatLngEvaluator(), orgin, target);
            markerAnimator.setDuration(distance);
            markerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if (emgMap == null) {
                        return;
                    }
                    LatLng animatedValue = (LatLng) animation.getAnimatedValue();
                    GeoJsonSource locationSource = emgMap.getSourceAs("light_location_source_" + layerIndex);
                    if (locationSource == null) {
                        return;
                    }
                    locationSource.setGeoJson(FeatureCollection.fromFeature(Feature.fromGeometry(
                            Point.fromLngLat(animatedValue.getLongitude(), animatedValue.getLatitude()))
                    ));
                    GeoJsonSource locationBlurSource = emgMap.getSourceAs("light_location_blur_source_" + layerIndex);
                    if (locationSource == null) {
                        return;
                    }
                    locationBlurSource.setGeoJson(FeatureCollection.fromFeature(Feature.fromGeometry(
                            Point.fromLngLat(animatedValue.getLongitude(), animatedValue.getLatitude()))
                    ));


                }
            });
            markerAnimator.setInterpolator(new LinearInterpolator());
            markerAnimator.start();
            count++;
            count = count < points.size() ? count : 0;
            handler.postDelayed(this, distance);
        }
    }

    public void start() {
        if (runnable != null && handler != null) {
            handler.post(runnable);
        }
    }

    public void cancel() {
        if (runnable != null && handler != null) {
            handler.removeCallbacks(runnable);
        }
        handler = null;
        runnable = null;
        emgMap = null;
    }

    private static class LatLngEvaluator implements TypeEvaluator<LatLng> {

        private LatLng latLng = new LatLng();

        @Override
        public LatLng evaluate(float fraction, LatLng startValue, LatLng endValue) {
            latLng.setLatitude(startValue.getLatitude()
                    + ((endValue.getLatitude() - startValue.getLatitude()) * fraction));
            latLng.setLongitude(startValue.getLongitude()
                    + ((endValue.getLongitude() - startValue.getLongitude()) * fraction));
            return latLng;
        }
    }
}

