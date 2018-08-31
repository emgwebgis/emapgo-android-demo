package com.emapgo.android.demo.activity;

import android.graphics.Color;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.emapgo.android.demo.MyApplication;
import com.emapgo.android.demo.R;
import com.emapgo.android.demo.util.MapStyleUtil;
import com.emapgo.android.demo.util.PreferenceManager;
import com.emapgo.api.directions.v5.EmgDirections;
import com.emapgo.api.directions.v5.models.DirectionsResponse;
import com.emapgo.api.directions.v5.models.DirectionsRoute;
import com.emapgo.api.directions.v5.models.LegStep;
import com.emapgo.api.directions.v5.models.RouteLeg;
import com.emapgo.api.directions.v5.models.StepIntersection;
import com.emapgo.geojson.Feature;
import com.emapgo.geojson.FeatureCollection;
import com.emapgo.geojson.LineString;
import com.emapgo.geojson.Point;
import com.emapgo.mapsdk.annotations.MarkerOptions;
import com.emapgo.mapsdk.geometry.LatLng;
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.maps.MapView;
import com.emapgo.mapsdk.maps.OnMapReadyCallback;
import com.emapgo.mapsdk.search.route.RouteSearch;
import com.emapgo.mapsdk.search.route.RouteSearchOption;
import com.emapgo.mapsdk.style.layers.LineLayer;
import com.emapgo.mapsdk.style.sources.GeoJsonSource;
import com.emapgo.services.api.utils.turf.TurfInvariant;
import com.emapgo.turf.TurfAssertions;
import com.emapgo.turf.TurfConversion;
import com.emapgo.turf.TurfJoins;
import com.emapgo.turf.TurfMisc;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.emapgo.mapsdk.style.layers.Property.LINE_CAP_ROUND;
import static com.emapgo.mapsdk.style.layers.Property.LINE_JOIN_ROUND;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.lineCap;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.lineColor;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.lineJoin;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.lineOpacity;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.lineWidth;

public class DrawSnakingDirectionsRouteActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final float NAVIGATION_LINE_WIDTH = 6;
    private static final String DRIVING_ROUTE_POLYLINE_LINE_LAYER_ID = "DRIVING_ROUTE_POLYLINE_LINE_LAYER_ID";
    private static final String DRIVING_ROUTE_POLYLINE_SOURCE_ID = "DRIVING_ROUTE_POLYLINE_SOURCE_ID";
    private static final String TAG = "SnakingRouteActivity";
    private MapView mapView;
    private EmgMap emgMap;
    private EmgDirections client;
    private AsyncTask<Response<DirectionsResponse>, FeatureCollection, Void> task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_snaking_directions_route);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mapView = (MapView) findViewById(R.id.emgMapView);
        mapView.onCreate(savedInstanceState);
        mapView.setStyleUrl(PreferenceManager.getMapStyle(MyApplication.getAppContext()));
        mapView.getMapAsync(this);
        mapView.addOnMapChangedListener(new MapView.OnMapChangedListener() {
            @Override
            public void onMapChanged(int status) {
                if (status == MapView.DID_FINISH_LOADING_MAP) {
                    initDrivingRouteSourceAndLayer();
                    final Point origin = Point.fromLngLat(116.29995507512434, 39.8288916490182);
                    // final Point destination = Point.fromLngLat(104.0185546875, 30.86451022625836);
                    final Point destination = Point.fromLngLat(116.36856079101562, 39.85137985826863);

                    emgMap.addMarker(new MarkerOptions()
                            .position(new LatLng(origin.latitude(), origin.longitude())));
                    emgMap.addMarker(new MarkerOptions()
                            .position(new LatLng(destination.latitude(), destination.longitude())));
                    getDirectionsRoute(origin, destination);
                }
            }
        });
    }

    @Override
    public void onMapReady(EmgMap emgMap) {
        this.emgMap = emgMap;
        MapStyleUtil.darkStyle(emgMap);

    }

    private void getDirectionsRoute(Point origin, Point destination) {
        RouteSearchOption option = new RouteSearchOption.Builder()
                .origin(origin)
                .destination(destination)
                .profile(RouteSearchOption.DRIVING)
                .alternatives(true)
                .steps(true)
                .overview("full")
                .baseUrl(PreferenceManager.getMapRouteService(this))
                .build();

        RouteSearch.newInstance().enqueueCall(option, new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                if (response.body() == null) {
                    Log.d(TAG, "No routes found, make sure you set the right user and access token.");
                    return;
                } else if (response.body().routes().size() < 1) {
                    Log.d(TAG, "No routes found");
                    return;
                }
                task = new ParseStepAsyncTask(emgMap).execute(response);
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                Log.d("SnakingDirectionsActivity", "Error: " + t.getMessage());
            }
        });

    }

    private void initDrivingRouteSourceAndLayer() {
        GeoJsonSource drivingRouteGeoJsonSource = new GeoJsonSource(DRIVING_ROUTE_POLYLINE_SOURCE_ID,
                FeatureCollection.fromFeatures(new Feature[]{}));
        emgMap.addSource(drivingRouteGeoJsonSource);
        LineLayer drivingRouteLineLayer = new LineLayer(DRIVING_ROUTE_POLYLINE_LINE_LAYER_ID,
                DRIVING_ROUTE_POLYLINE_SOURCE_ID)
                .withProperties(
                        lineWidth(NAVIGATION_LINE_WIDTH),
                        lineOpacity(.3f),
                        lineCap(LINE_CAP_ROUND),
                        lineJoin(LINE_JOIN_ROUND),
                        lineColor(Color.parseColor("#d742f4"))
                );
        emgMap.addLayer(drivingRouteLineLayer);
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
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    public static class ParseStepAsyncTask extends AsyncTask<Response<DirectionsResponse>, FeatureCollection, Void> {
        private EmgMap emgMap;

        public ParseStepAsyncTask(EmgMap emgMap) {
            this.emgMap = emgMap;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            emgMap = null;
        }

        @Override
        protected Void doInBackground(Response<DirectionsResponse>... params) {
            DirectionsRoute currentRoute = params[0].body().routes().get(0);
            List<Point> directionsPointsForLineLayer = new ArrayList<>();
            for (int i = 0; i < currentRoute.legs().size() && !isCancelled(); i++) {
                RouteLeg leg = currentRoute.legs().get(i);
                List<LegStep> steps = leg.steps();
                for (int j = 0; j < steps.size() && !isCancelled(); j++) {
                    LegStep step = steps.get(j);
                    List<StepIntersection> intersections = step.intersections();
                    for (int k = 0; k < intersections.size() && !isCancelled(); k++) {
                        Point location = intersections.get(k).location();
                        directionsPointsForLineLayer.add(Point.fromLngLat(location.longitude(), location.latitude()));
                        List<Feature> drivingRoutePolyLineFeatureList = new ArrayList<>();
                        LineString lineString = LineString.fromLngLats(directionsPointsForLineLayer);
                        List<Point> coordinates = lineString.coordinates();
                        for (int x = 0; x < coordinates.size() && !isCancelled(); x++) {
                            drivingRoutePolyLineFeatureList.add(Feature.fromGeometry(LineString.fromLngLats(coordinates)));
                        }
                        publishProgress(FeatureCollection.fromFeatures(drivingRoutePolyLineFeatureList));
                        try {
                            Thread.sleep(300);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(FeatureCollection... values) {
            super.onProgressUpdate(values);
            if (values[0] == null) {
                return;
            }
            GeoJsonSource source = emgMap.getSourceAs(DRIVING_ROUTE_POLYLINE_SOURCE_ID);
            if (source != null) {
                source.setGeoJson(values[0]);
            }

        }
    }

}
