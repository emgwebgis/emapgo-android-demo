package com.emapgo.android.demo.activity;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.animation.LinearInterpolator;

import com.emapgo.android.demo.R;
import com.emapgo.mapsdk.annotations.Icon;
import com.emapgo.mapsdk.annotations.IconFactory;
import com.emapgo.mapsdk.annotations.Marker;
import com.emapgo.mapsdk.annotations.MarkerViewOptions;
import com.emapgo.mapsdk.annotations.PolylineOptions;
import com.emapgo.mapsdk.camera.CameraPosition;
import com.emapgo.mapsdk.camera.CameraUpdateFactory;
import com.emapgo.mapsdk.geometry.LatLng;
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.maps.MapView;
import com.emapgo.mapsdk.maps.OnMapReadyCallback;
import com.emapgo.mapsdk.style.layers.SymbolLayer;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class DrawMarkerFollowingRouteActivity extends AppCompatActivity {
    private static final String TAG = "MarkerFollowingRoute";

    private MapView mapView;
    private EmgMap map;
    private Handler handler;
    private Runnable runnable;

    private static int count = 0;
    private long distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_marker_following_route);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mapView = (MapView) findViewById(R.id.emgMapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(EmgMap emgMap) {
                map = emgMap;
                new DrawGeoJson().execute();

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        // When the activity is resumed we restart the marker animating.
        if (handler != null && runnable != null) {
            handler.post(runnable);
        }
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
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
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


    private class DrawGeoJson extends AsyncTask<Void, Void, List<LatLng>> {
        @Override
        protected List<LatLng> doInBackground(Void... voids) {

            List<LatLng> points = new ArrayList<>();

            try {
                InputStream inputStream = getAssets().open("matched_route.geojson");
                BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
                StringBuilder sb = new StringBuilder();
                int cp;
                while ((cp = rd.read()) != -1) {
                    sb.append((char) cp);
                }

                inputStream.close();

                // Parse JSON
                JSONObject json = new JSONObject(sb.toString());
                JSONArray features = json.getJSONArray("features");
                JSONObject feature = features.getJSONObject(0);
                JSONObject geometry = feature.getJSONObject("geometry");
                if (geometry != null) {
                    String type = geometry.getString("type");

                    if (!TextUtils.isEmpty(type) && type.equalsIgnoreCase("LineString")) {

                        JSONArray coords = geometry.getJSONArray("coordinates");
                        for (int lc = 0; lc < coords.length(); lc++) {
                            JSONArray coord = coords.getJSONArray(lc);
                            LatLng latLng = new LatLng(coord.getDouble(1), coord.getDouble(0));
                            points.add(latLng);
                        }
                    }
                }
            } catch (Exception exception) {
                Log.e(TAG, "Exception Loading GeoJSON: " + exception.toString());
            }

            return points;
        }

        @Override
        protected void onPostExecute(final List<LatLng> points) {
            super.onPostExecute(points);
            if (points.size() > 0) {
                LatLng[] pointsArray = points.toArray(new LatLng[points.size()]);

                map.addPolyline(new PolylineOptions()
                        .add(pointsArray)
                        .color(Color.parseColor("#F13C6E"))
                        .width(7));

                Icon icon = IconFactory.getInstance(DrawMarkerFollowingRouteActivity.this).fromResource(R.mipmap.pink_dot);

                final Marker marker = map.addMarker(new MarkerViewOptions()
                        .position(points.get(count))
                        .icon(icon)
                        .anchor(0.5f, 0.5f)
                        .flat(true));

                handler = new Handler();
                runnable = new Runnable() {
                    @Override
                    public void run() {

                        if ((points.size() - 1) > count) {

                            distance = (long) marker.getPosition().distanceTo(points.get(count)) * 10;

                            ValueAnimator markerAnimator = ObjectAnimator.ofObject(marker, "position",
                                    new LatLngEvaluator(), marker.getPosition(), points.get(count));
                            markerAnimator.setDuration(distance);
                            markerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                                @Override
                                public void onAnimationUpdate(ValueAnimator animation) {
                                    LatLng animatedValue = (LatLng) animation.getAnimatedValue();
                                    CameraPosition position = new CameraPosition.Builder()
                                            .target(animatedValue)
                                            .zoom(15)
                                            .tilt(60)
                                            .bearing(90)
                                            .build();
                                    map.easeCamera(CameraUpdateFactory.newCameraPosition(position));
                                }
                            });
                            markerAnimator.setInterpolator(new LinearInterpolator());
                            markerAnimator.start();


                            map.getMarkerViewManager().update();

                            count++;


                            handler.postDelayed(this, distance);
                        }
                    }
                };
                handler.post(runnable);
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
