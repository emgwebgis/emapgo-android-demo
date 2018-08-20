package com.emapgo.android.demo.activity;

import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.animation.LinearInterpolator;
import android.widget.CompoundButton;

import com.emapgo.android.demo.MyApplication;
import com.emapgo.android.demo.R;
import com.emapgo.android.demo.util.PreferenceManager;
import com.emapgo.api.directions.v5.models.DirectionsResponse;
import com.emapgo.api.directions.v5.models.DirectionsRoute;
import com.emapgo.api.directions.v5.models.LegStep;
import com.emapgo.api.directions.v5.models.StepIntersection;
import com.emapgo.geojson.Feature;
import com.emapgo.geojson.FeatureCollection;
import com.emapgo.geojson.LineString;
import com.emapgo.geojson.Point;
import com.emapgo.mapsdk.annotations.Icon;
import com.emapgo.mapsdk.annotations.IconFactory;
import com.emapgo.mapsdk.annotations.Marker;
import com.emapgo.mapsdk.annotations.MarkerViewOptions;
import com.emapgo.mapsdk.camera.CameraPosition;
import com.emapgo.mapsdk.camera.CameraUpdateFactory;
import com.emapgo.mapsdk.geometry.LatLng;
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.maps.MapView;
import com.emapgo.mapsdk.maps.OnMapReadyCallback;
import com.emapgo.mapsdk.plugins.building.BuildingPlugin;
import com.emapgo.mapsdk.search.route.RouteSearch;
import com.emapgo.mapsdk.search.route.RouteSearchOption;
import com.emapgo.mapsdk.style.layers.FillExtrusionLayer;
import com.emapgo.mapsdk.style.layers.Layer;
import com.emapgo.mapsdk.style.layers.LineLayer;
import com.emapgo.mapsdk.style.layers.PropertyFactory;
import com.emapgo.mapsdk.style.layers.SymbolLayer;
import com.emapgo.mapsdk.style.light.Light;
import com.emapgo.mapsdk.style.light.Position;
import com.emapgo.mapsdk.style.sources.GeoJsonSource;
import com.emapgo.mapsdk.style.sources.Source;
import com.emapgo.services.Constants;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.emapgo.mapsdk.style.expressions.Expression.concat;
import static com.emapgo.mapsdk.style.expressions.Expression.exponential;
import static com.emapgo.mapsdk.style.expressions.Expression.interpolate;
import static com.emapgo.mapsdk.style.expressions.Expression.stop;
import static com.emapgo.mapsdk.style.expressions.Expression.zoom;
import static com.emapgo.mapsdk.style.layers.Property.ICON_ROTATION_ALIGNMENT_MAP;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.iconAllowOverlap;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.iconIgnorePlacement;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.iconImage;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.iconRotate;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.iconRotationAlignment;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.iconSize;

public class MapThreeStereoscopicNavigationActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private EmgMap emgMap;
    private Handler handler;
    private Runnable runnable;
    private int count = 0;
    private List<Point> points = new ArrayList<>();
    private List<StepIntersection> stepIntersection = new ArrayList<>();
    private long distance;
    private boolean is3dView;
    private boolean camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_three_stereoscopic_navigation);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        ButterKnife.bind(this);
        mapView = (MapView) findViewById(R.id.emgMapView);
        mapView.onCreate(savedInstanceState);
        //mapView.setStyleUrl("http://192.168.11.148:10003/styles/outdoor_3Dbuilding/style.json");
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(EmgMap emgMap) {
        this.emgMap = emgMap;
        getRoute();
    }

    private void getRoute() {
        RouteSearchOption option = RouteSearchOption.builder()
                .origin(Point.fromLngLat(121.50419371121467, 31.238404645918607))
                .destination(Point.fromLngLat(121.51082499400633, 31.22532437267887))
                .profile(RouteSearchOption.DRIVING)
                .alternatives(true)
                .overview("full")
                .steps(true)
                .baseUrl(PreferenceManager.getMapRouteService(MyApplication.getAppContext()))
                .build();
        RouteSearch.newInstance().enqueueCall(option, new Callback<DirectionsResponse>() {

            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                DirectionsResponse body = response.body();
                for (int l = 0; l < body.routes().get(0).legs().get(0).steps().size(); l++) {
                    LegStep legStep = body.routes().get(0).legs().get(0).steps().get(l);
                    for (int i1 = 0; i1 < legStep.intersections().size(); i1++) {
                        points.add(legStep.intersections().get(i1).location());
                        stepIntersection.add(legStep.intersections().get(i1));
                    }

                }

                points.clear();
                points.addAll(LineString.fromPolyline(body.routes().get(0).geometry(), Constants.PRECISION_5).coordinates());


                drawRouteLine(response.body().routes().get(0));
                drawMarker();
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {

            }
        });
    }


    private void drawMarker() {
        emgMap.addImage("location_icon", getBitmapFromDrawable(ContextCompat.getDrawable(this, R.drawable.emg_user_puck_icon)));

        GeoJsonSource location_source = new GeoJsonSource("location_source", FeatureCollection.fromFeature(Feature.fromGeometry(
                Point.fromLngLat(points.get(count).longitude(), points.get(count).latitude()))
        ));
        emgMap.addSource(location_source);


        SymbolLayer locataionLayer = new SymbolLayer("location-layer", "location_source").withProperties(
                iconAllowOverlap(true),
                iconIgnorePlacement(true),
                iconImage("location_icon"),
                iconRotate(180f),
                iconSize(interpolate(
                        exponential(1f), zoom(),
                        stop(22f, 1f),
                        stop(12f, 1f),
                        stop(10f, 0.6f),
                        stop(0f, 0.6f)
                        )
                ),
                iconRotationAlignment(ICON_ROTATION_ALIGNMENT_MAP));
        emgMap.addLayer(locataionLayer);

        handler = new Handler();
        runnable = new Runnable() {
            public double distanceTo(LatLng orign, LatLng other) {
                if (orign.getLatitude() == other.getLatitude() && orign.getLongitude() == other.getLongitude()) {
                    return 0.0D;
                } else {
                    double a1 = Math.toRadians(orign.getLatitude());
                    double a2 = Math.toRadians(orign.getLongitude());
                    double b1 = Math.toRadians(other.getLatitude());
                    double b2 = Math.toRadians(other.getLongitude());
                    double cosa1 = Math.cos(a1);
                    double cosb1 = Math.cos(b1);
                    double t1 = cosa1 * Math.cos(a2) * cosb1 * Math.cos(b2);
                    double t2 = cosa1 * Math.sin(a2) * cosb1 * Math.sin(b2);
                    double t3 = Math.sin(a1) * Math.sin(b1);
                    double tt = Math.acos(t1 + t2 + t3);
                    return 6378137.0D * tt;
                }
            }

            @Override
            public void run() {
                if (emgMap == null) {
                    return;
                }
                if ((points.size()) > count) {

                    LatLng orgin = new LatLng(points.get(count-1<0?0:count-1).latitude(),points.get(count-1<0?0:count-1).longitude());
                    LatLng target = new LatLng(points.get(count).latitude(),points.get(count).longitude());


                    distance = (long) (distanceTo(orgin, target)*50);

                    ValueAnimator markerAnimator = ObjectAnimator.ofObject(
                            new MapThreeStereoscopicNavigationActivity.LatLngEvaluator(), orgin, target);
                    markerAnimator.setDuration(distance);
                    markerAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            LatLng animatedValue = (LatLng) animation.getAnimatedValue();
                            GeoJsonSource locationSource = emgMap.getSourceAs("location_source");
                            if (locationSource == null) {
                                return;
                            }
                            locationSource.setGeoJson(FeatureCollection.fromFeature(Feature.fromGeometry(
                                    Point.fromLngLat(animatedValue.getLongitude(),animatedValue.getLatitude()))
                            ));
                            if (camera) {

                                CameraPosition position = new CameraPosition.Builder()
                                        .target(animatedValue)
                                        .zoom(17.5)
                                        .bearing(180)
                                        .tilt(is3dView ? 50 : 0)
                                        .build();
                                emgMap.easeCamera(CameraUpdateFactory.newCameraPosition(position));
                            }

                        }
                    });
                    markerAnimator.setInterpolator(new LinearInterpolator());
                    markerAnimator.start();

                    count++;
                    handler.postDelayed(this, distance);
                }
            }
        };
        handler.post(runnable);
    }

    private void drawRouteLine(DirectionsRoute directionsRoute) {
        FeatureCollection featureCollection = FeatureCollection.fromFeatures(
                new Feature[]{Feature.fromGeometry(LineString.fromPolyline(directionsRoute.geometry(), Constants.PRECISION_5))});
        GeoJsonSource geoJsonSource = new GeoJsonSource("line-source", featureCollection);
        emgMap.addSource(geoJsonSource);

        LineLayer lineLayer = new LineLayer("line-layer", "line-source");
        lineLayer.setProperties(
                PropertyFactory.lineColor(Color.argb(255, 66, 159, 255)),
                PropertyFactory.lineWidth(12f)

        );
        emgMap.addLayer(lineLayer);
        emgMap.addImage("image-icon", getBitmapFromDrawable(ContextCompat.getDrawable(this, R.drawable.emg_icon_line)));


        lineLayer = new LineLayer("line-icon-layer", "line-source");
        lineLayer.setProperties(
                PropertyFactory.linePattern("image-icon"),
                PropertyFactory.lineWidth(12f)

        );
        emgMap.addLayerAbove(lineLayer,"line-layer");
    }

    @OnCheckedChanged(R.id.id_viewport)
    public void onCheckedChanged(CompoundButton button, boolean check) {
        is3dView = check;
    }
    @OnCheckedChanged(R.id.id_camera)
    public void onCameraCheckedChanged(CompoundButton button, boolean check) {
        camera = check;
    }

    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        } else {
            Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                    drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
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
    public void onResume() {
        super.onResume();
        mapView.onResume();
        if (handler != null && runnable != null) {
            handler.post(runnable);
        }
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
    protected void onStop() {
        super.onStop();
        mapView.onStop();
        handler.removeCallbacks(runnable);
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