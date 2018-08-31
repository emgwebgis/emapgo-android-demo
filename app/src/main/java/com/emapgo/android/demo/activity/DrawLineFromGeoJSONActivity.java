package com.emapgo.android.demo.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.emapgo.mapsdk.style.functions.Function;
import com.emapgo.mapsdk.style.functions.stops.Stop;
import com.emapgo.mapsdk.style.functions.stops.Stops;
import com.emapgo.mapsdk.style.layers.LineLayer;
import com.emapgo.mapsdk.style.layers.Property;
import com.emapgo.mapsdk.style.layers.PropertyFactory;
import com.emapgo.mapsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;

public class DrawLineFromGeoJSONActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_line);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mapView = (MapView) findViewById(R.id.emgMapView);
        mapView.onCreate(savedInstanceState);
        mapView.setStyleUrl(PreferenceManager.getMapStyle(MyApplication.getAppContext()));
        mapView.getMapAsync(this);
    }

    @Override
    public void onMapReady(EmgMap emgMap) {
        //构建坐标点
      /*  List routeCoordinates = new ArrayList<Point>();
        routeCoordinates.add(Point.fromLngLat(116.39036178588867, 39.921717512624866));
        routeCoordinates.add(Point.fromLngLat(116.39122009277342, 39.90687226705979));

        LineString lineString = LineString.fromLngLats(routeCoordinates);
        FeatureCollection featureCollection = FeatureCollection.fromFeatures(
                new Feature[]{Feature.fromGeometry(lineString)});*/

        FeatureCollection featureCollection = FeatureCollection.fromJson("{\n" +
                "  \"type\": \"FeatureCollection\",\n" +
                "  \"features\": [\n" +
                "    {\n" +
                "      \"type\": \"Feature\",\n" +
                "      \"properties\": {\n" +
                "        \"stroke\": \"#228e13\",\n" +
                "        \"stroke-width\": 10,\n" +
                "        \"stroke-opacity\": 1,\n" +
                "        \"congestion\": \"low\"\n" +
                "      },\n" +
                "      \"geometry\": {\n" +
                "        \"type\": \"LineString\",\n" +
                "        \"coordinates\": [\n" +
                "          [\n" +
                "            116.3847827911377,\n" +
                "            39.92076303709448\n" +
                "          ],\n" +
                "          [\n" +
                "            116.38504028320312,\n" +
                "            39.91668168130951\n" +
                "          ],\n" +
                "          [\n" +
                "            116.38521194458006,\n" +
                "            39.913390088172434\n" +
                "          ],\n" +
                "          [\n" +
                "            116.38546943664551,\n" +
                "            39.909769153007566\n" +
                "          ],\n" +
                "          [\n" +
                "            116.38559818267822,\n" +
                "            39.906148026432085\n" +
                "          ],\n" +
                "          [\n" +
                "            116.39070510864258,\n" +
                "            39.90627970711568\n" +
                "          ],\n" +
                "          [\n" +
                "            116.39508247375487,\n" +
                "            39.906510147702974\n" +
                "          ],\n" +
                "          [\n" +
                "            116.3971424102783,\n" +
                "            39.90660890771739\n" +
                "          ],\n" +
                "          [\n" +
                "            116.39664888381957,\n" +
                "            39.913745999308105\n" +
                "          ],\n" +
                "          [\n" +
                "            116.4002752304077,\n" +
                "            39.913883837227964\n" +
                "          ],\n" +
                "          [\n" +
                "            116.40012502670287,\n" +
                "            39.91867301835079\n" +
                "          ],\n" +
                "          [\n" +
                "            116.39997482299805,\n" +
                "            39.92293527243327\n" +
                "          ],\n" +
                "          [\n" +
                "            116.39894485473631,\n" +
                "            39.922869448132474\n" +
                "          ],\n" +
                "          [\n" +
                "            116.39774322509764,\n" +
                "            39.92296818455995\n" +
                "          ],\n" +
                "          [\n" +
                "            116.3971424102783,\n" +
                "            39.92273779934104\n" +
                "          ],\n" +
                "          [\n" +
                "            116.39684200286865,\n" +
                "            39.922408676255024\n" +
                "          ],\n" +
                "          [\n" +
                "            116.39615535736084,\n" +
                "            39.92224411411874\n" +
                "          ],\n" +
                "          [\n" +
                "            116.39396667480469,\n" +
                "            39.92211246412495\n" +
                "          ],\n" +
                "          [\n" +
                "            116.38894557952881,\n" +
                "            39.92181625071345\n" +
                "          ],\n" +
                "          [\n" +
                "            116.38564109802246,\n" +
                "            39.92175042533686\n" +
                "          ],\n" +
                "          [\n" +
                "            116.38508319854736,\n" +
                "            39.92152003602051\n" +
                "          ],\n" +
                "          [\n" +
                "            116.3847827911377,\n" +
                "            39.92115799410003\n" +
                "          ]\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"Feature\",\n" +
                "      \"properties\": {\n" +
                "        \"stroke\": \"#d10014\",\n" +
                "        \"stroke-width\": 10,\n" +
                "        \"stroke-opacity\": 1,\n" +
                "        \"congestion\": \"severe\"\n" +
                "      },\n" +
                "      \"geometry\": {\n" +
                "        \"type\": \"LineString\",\n" +
                "        \"coordinates\": [\n" +
                "          [\n" +
                "            116.39664888381957,\n" +
                "            39.91374394202361\n" +
                "          ],\n" +
                "          [\n" +
                "            116.4002725481987,\n" +
                "            39.91388589450823\n" +
                "          ]\n" +
                "        ]\n" +
                "      }\n" +
                "    },\n" +
                "    {\n" +
                "      \"type\": \"Feature\",\n" +
                "      \"properties\": {\n" +
                "        \"stroke\": \"#e78421\",\n" +
                "        \"stroke-width\": 10,\n" +
                "        \"stroke-opacity\": 1,\n" +
                "        \"congestion\": \"heavy\"\n" +
                "      },\n" +
                "      \"geometry\": {\n" +
                "        \"type\": \"LineString\",\n" +
                "        \"coordinates\": [\n" +
                "          [\n" +
                "            116.4002752304077,\n" +
                "            39.91390029546853\n" +
                "          ],\n" +
                "          [\n" +
                "            116.39997750520706,\n" +
                "            39.922931158416326\n" +
                "          ]\n" +
                "        ]\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}");

        //将坐标点转化为GeoJSON
        GeoJsonSource geoJsonSource = new GeoJsonSource("line-source", featureCollection);
        emgMap.addSource(geoJsonSource);


        //构建图层并与数据源绑定
        LineLayer lineLayer = new LineLayer("line-layer", "line-source");

        //设置图层属性样式
        lineLayer.setProperties(
                PropertyFactory.lineCap(Property.LINE_CAP_ROUND),
                PropertyFactory.lineWidth(10f),
                //使用表达式解析JSON中的属性
                PropertyFactory.lineColor(
                        Function.property("congestion", Stops.categorical(
                                Stop.stop("severe",PropertyFactory.lineColor(Color.RED)),
                                Stop.stop("heavy",PropertyFactory.lineColor(Color.YELLOW)),
                                Stop.stop("low",PropertyFactory.lineColor(Color.BLUE)))
                        )
                )
        );

        emgMap.addLayer(lineLayer);
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
