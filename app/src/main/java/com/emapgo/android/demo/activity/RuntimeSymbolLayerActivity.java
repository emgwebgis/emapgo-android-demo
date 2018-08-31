package com.emapgo.android.demo.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.emapgo.android.demo.MyApplication;
import com.emapgo.android.demo.R;
import com.emapgo.android.demo.util.PreferenceManager;
import com.emapgo.geojson.Feature;
import com.emapgo.geojson.FeatureCollection;
import com.emapgo.geojson.Point;
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.maps.MapView;
import com.emapgo.mapsdk.maps.OnMapReadyCallback;
import com.emapgo.mapsdk.style.expressions.Expression;
import com.emapgo.mapsdk.style.functions.Function;
import com.emapgo.mapsdk.style.functions.stops.Stop;
import com.emapgo.mapsdk.style.functions.stops.Stops;
import com.emapgo.mapsdk.style.layers.CircleLayer;
import com.emapgo.mapsdk.style.layers.Property;
import com.emapgo.mapsdk.style.layers.PropertyFactory;
import com.emapgo.mapsdk.style.layers.SymbolLayer;
import com.emapgo.mapsdk.style.sources.GeoJsonSource;
import com.emapgo.mapsdk.style.sources.Source;
import com.emapgo.mapsdk.style.sources.VectorSource;

import java.util.ArrayList;
import java.util.List;

import static com.emapgo.mapsdk.style.expressions.Expression.e;
import static com.emapgo.mapsdk.style.expressions.Expression.exponential;
import static com.emapgo.mapsdk.style.expressions.Expression.get;
import static com.emapgo.mapsdk.style.expressions.Expression.gte;
import static com.emapgo.mapsdk.style.expressions.Expression.literal;
import static com.emapgo.mapsdk.style.expressions.Expression.min;
import static com.emapgo.mapsdk.style.expressions.Expression.toNumber;
import static com.emapgo.mapsdk.style.expressions.Expression.zoom;
import static com.emapgo.mapsdk.style.layers.Property.VISIBLE;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.circleColor;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.circlePitchScale;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.circleRadius;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.circleStrokeColor;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.circleStrokeWidth;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.textColor;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.textField;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.textFont;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.textSize;
import static com.emapgo.mapsdk.style.layers.PropertyFactory.visibility;

public class RuntimeSymbolLayerActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private static final String NUMBER_KEY = "NUMBER_KEY";
    private List<Feature> markerCoordinates;
    private List<Point> mPoints = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runtime_symbol_layer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mapView = (MapView) findViewById(R.id.emgMapView);
        mapView.onCreate(savedInstanceState);
        mapView.setStyleUrl(PreferenceManager.getMapStyle(MyApplication.getAppContext()));
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(EmgMap emgMap) {
        initFeature();

        FeatureCollection featureCollection = FeatureCollection.fromFeatures(markerCoordinates);
        Source geoJsonSource = new GeoJsonSource("marker-source", featureCollection);
        emgMap.addSource(geoJsonSource);

        /*Bitmap icon = BitmapFactory.decodeResource(
                getResources(), R.drawable.emg_marker_icon_default);
        //将图标添加至地图，需要在Layer中渲染才能展示在地图
        emgMap.addImage("my-marker-image", icon);

        //添加SymbolLayer并且设置Icon资源
        SymbolLayer markers = new SymbolLayer("marker-layer", "marker-source")
                .withProperties(PropertyFactory.iconImage("my-marker-image"));
        emgMap.addLayer(markers);*/

        CircleLayer circlelayer = new CircleLayer("circlelayer", "marker-source")
                .withProperties(
                        circleRadius(Function.zoom(Stops.exponential(
                                Stop.stop(1f,circleRadius(0f)),
                                Stop.stop(13f,circleRadius(18f)),
                                Stop.stop(15f,circleRadius(28f)),
                                Stop.stop(18f,circleRadius(38f)),
                                Stop.stop(20f,circleRadius(48f))
                                ))), //圆的半径
                        circleColor(ContextCompat.getColor(this, android.R.color.holo_purple))//填充颜色
                );
        emgMap.addLayer(circlelayer);

        SymbolLayer markers = new SymbolLayer("marker-layer", "marker-source")
                .withProperties(textFont(new String[]{"NotoSansCJKSCBoldArialUnicodeMSRegular"}), //如SymbolLayer需要显示字符，则该属性必须添加
                        textField("{" + NUMBER_KEY + "}"), //将Feature中的NUMBER_KEY属性绑定在图层中
                        textSize(16f),
                        textColor(Color.WHITE));
        emgMap.addLayer(markers);

    }

    private void initFeature() {
        mPoints.add(Point.fromLngLat(116.38722896575926, 39.91888696020796));
        mPoints.add(Point.fromLngLat(116.38746500015257, 39.91949586798925));
        mPoints.add(Point.fromLngLat(116.39469623565674, 39.91850844723661));
        mPoints.add(Point.fromLngLat(116.38843059539794, 39.91771850038315));
        mPoints.add(Point.fromLngLat(116.39291524887086, 39.91571067778447));
        mPoints.add(Point.fromLngLat(116.38997554779054, 39.91628669848582));
        mPoints.add(Point.fromLngLat(116.38877391815186, 39.91454216376516));
        mPoints.add(Point.fromLngLat(116.39068365097046, 39.91452570567887));
        mPoints.add(Point.fromLngLat(116.39044761657713, 39.91951232488118));
        mPoints.add(Point.fromLngLat(116.38877391815186, 39.91454216376516));
        mPoints.add(Point.fromLngLat(116.39411687850951, 39.913982586611944));
        mPoints.add(Point.fromLngLat(116.39349460601807, 39.92008831360661));
        mPoints.add(Point.fromLngLat(116.38720750808716, 39.91263299937423));

        //构建GeoJsonSource
        markerCoordinates = new ArrayList<>();
        for (int i = 0; i < mPoints.size(); i++) {
            Feature feature = Feature.fromGeometry(
                    Point.fromLngLat(mPoints.get(i).longitude(), mPoints.get(i).latitude())
            );
            feature.addNumberProperty(NUMBER_KEY, i + 1);
            markerCoordinates.add(feature);
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
