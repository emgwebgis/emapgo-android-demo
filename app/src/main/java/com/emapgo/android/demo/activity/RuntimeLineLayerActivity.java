package com.emapgo.android.demo.activity;

import android.arch.lifecycle.ProcessLifecycleOwner;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.emapgo.android.demo.MyApplication;
import com.emapgo.android.demo.R;
import com.emapgo.android.demo.util.PreferenceManager;
import com.emapgo.geojson.Feature;
import com.emapgo.geojson.FeatureCollection;
import com.emapgo.geojson.LineString;
import com.emapgo.geojson.Point;
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.maps.MapView;
import com.emapgo.mapsdk.maps.OnMapReadyCallback;
import com.emapgo.mapsdk.style.functions.stops.Stop;
import com.emapgo.mapsdk.style.functions.stops.Stops;
import com.emapgo.mapsdk.style.layers.FillLayer;
import com.emapgo.mapsdk.style.layers.Layer;
import com.emapgo.mapsdk.style.layers.LineLayer;
import com.emapgo.mapsdk.style.layers.Property;
import com.emapgo.mapsdk.style.layers.PropertyFactory;
import com.emapgo.mapsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class RuntimeLineLayerActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runtime_line_layer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mapView = (MapView) findViewById(R.id.emgMapView);
        mapView.onCreate(savedInstanceState);
        mapView.setStyleUrl(PreferenceManager.getMapStyle(MyApplication.getAppContext()));
        mapView.getMapAsync(this);
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
    public void onMapReady(EmgMap emgMap) {
        drawLine(emgMap);
        drawLine2(emgMap);
        drawLine3(emgMap);
        drawLine4(emgMap);
    }

    private void drawLine(EmgMap emgMap) {
        //构建坐标点
        List routeCoordinates = new ArrayList<Point>();
        routeCoordinates.add(Point.fromLngLat(116.36856079101562, 39.920532644455875));
        routeCoordinates.add(Point.fromLngLat(116.40478134155273, 39.92132255884663));

        LineString lineString = LineString.fromLngLats(routeCoordinates);
        FeatureCollection featureCollection = FeatureCollection.fromFeatures(
                new Feature[]{Feature.fromGeometry(lineString)});

        //将坐标点转化为GeoJSON
        GeoJsonSource geoJsonSource = new GeoJsonSource("line-source", featureCollection);
        emgMap.addSource(geoJsonSource);

        //构建图层并与数据源绑定
        LineLayer lineLayer = new LineLayer("line-layer", "line-source");

        //设置图层属性样式
        lineLayer.setProperties(
                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                PropertyFactory.lineWidth(5f), //线的宽度
                PropertyFactory.lineColor(Color.BLUE) //线的颜色
        );
        emgMap.addLayer(lineLayer);
    }

    private void drawLine2(EmgMap emgMap) {
        //构建坐标点
        List routeCoordinates = new ArrayList<Point>();
        routeCoordinates.add(Point.fromLngLat(116.36890411376955, 39.91592462889624));
        routeCoordinates.add(Point.fromLngLat(116.40460968017578, 39.916714596441984));

        LineString lineString = LineString.fromLngLats(routeCoordinates);
        FeatureCollection featureCollection = FeatureCollection.fromFeatures(
                new Feature[]{Feature.fromGeometry(lineString)});

        //将坐标点转化为GeoJSON
        GeoJsonSource geoJsonSource = new GeoJsonSource("line2-source", featureCollection);
        emgMap.addSource(geoJsonSource);

        //构建图层并与数据源绑定
        LineLayer lineLayer = new LineLayer("line2-layer", "line2-source");

        //设置图层属性样式
        lineLayer.setProperties(
                PropertyFactory.lineDasharray(new Float[]{0.5f, 1.5f}), //设置虚线
                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                PropertyFactory.lineJoin(Property.LINE_JOIN_ROUND),
                PropertyFactory.lineWidth(5f),//线的宽度
                PropertyFactory.lineColor(Color.RED) //线的颜色
        );
        emgMap.addLayer(lineLayer);
    }

    private void drawLine3(EmgMap emgMap) {
        emgMap.addImage("image-line", getBitmapFromDrawable(ContextCompat.getDrawable(this, R.drawable.emg_icon_line2)));
        //构建坐标点
        List routeCoordinates = new ArrayList<Point>();
        routeCoordinates.add(Point.fromLngLat(116.3697624206543, 39.9105262734005));
        routeCoordinates.add(Point.fromLngLat(116.40512466430665, 39.91118463226871));

        LineString lineString = LineString.fromLngLats(routeCoordinates);
        FeatureCollection featureCollection = FeatureCollection.fromFeatures(
                new Feature[]{Feature.fromGeometry(lineString)});

        //将坐标点转化为GeoJSON
        GeoJsonSource geoJsonSource = new GeoJsonSource("line3-source", featureCollection);
        emgMap.addSource(geoJsonSource);

        //构建图层并与数据源绑定
        LineLayer lineLayer = new LineLayer("line3-layer", "line3-source");

        //设置图层属性样式
        lineLayer.setProperties(
                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                PropertyFactory.lineWidth(8f), //线的宽度
                PropertyFactory.linePattern("image-line")
        );

        emgMap.addLayer(lineLayer);

        LineLayer lineBackgroundLayer = new LineLayer("line-background-layer", "line3-source").withProperties(
                PropertyFactory.lineWidth(8f),
                PropertyFactory.lineColor(Color.argb(255, 66, 159, 255))
        );
        emgMap.addLayerBelow(lineBackgroundLayer, "line3-layer");
    }

    private void drawLine4(EmgMap emgMap) {
        emgMap.addImage("image2-line", getBitmapFromDrawable(ContextCompat.getDrawable(this, R.drawable.emg_icon_line)));
        //构建坐标点
        List routeCoordinates = new ArrayList<Point>();
        routeCoordinates.add(Point.fromLngLat(116.37027740478516, 39.90525917519759));
        routeCoordinates.add(Point.fromLngLat(116.40521049499512, 39.90598342522164));

        LineString lineString = LineString.fromLngLats(routeCoordinates);
        FeatureCollection featureCollection = FeatureCollection.fromFeatures(
                new Feature[]{Feature.fromGeometry(lineString)});

        //将坐标点转化为GeoJSON
        GeoJsonSource geoJsonSource = new GeoJsonSource("line4-source", featureCollection);
        emgMap.addSource(geoJsonSource);

        //构建图层并与数据源绑定
        LineLayer lineLayer = new LineLayer("line4-layer", "line4-source");

        //设置图层属性样式
        lineLayer.setProperties(
                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                PropertyFactory.lineWidth(8f), //线的宽度
                PropertyFactory.linePattern("image2-line")
        );

        emgMap.addLayer(lineLayer);

        LineLayer lineBackgroundLayer = new LineLayer("line-background2-layer", "line4-source").withProperties(
                PropertyFactory.lineWidth(8f),
                PropertyFactory.lineColor(Color.argb(255, 66, 159, 255))
        );
        emgMap.addLayerBelow(lineBackgroundLayer, "line4-layer");
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
