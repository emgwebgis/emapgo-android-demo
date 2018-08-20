package com.emapgo.android.demo.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.emapgo.android.core.location.LocationEngine;
import com.emapgo.android.core.location.LocationEngineListener;
import com.emapgo.android.core.location.LocationEnginePriority;
import com.emapgo.android.core.location.LocationEngineProvider;
import com.emapgo.android.demo.MyApplication;
import com.emapgo.android.demo.R;
import com.emapgo.android.demo.activity.PoiSearchActivity;
import com.emapgo.android.demo.activity.RouteActivity;
import com.emapgo.android.demo.util.MapStyleUtil;
import com.emapgo.android.demo.util.PreferenceManager;
import com.emapgo.mapsdk.annotations.MarkerOptions;
import com.emapgo.mapsdk.camera.CameraPosition;
import com.emapgo.mapsdk.camera.CameraUpdate;
import com.emapgo.mapsdk.camera.CameraUpdateFactory;
import com.emapgo.mapsdk.geometry.LatLng;
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.maps.EmgMapOptions;
import com.emapgo.mapsdk.maps.MapView;
import com.emapgo.mapsdk.maps.OnMapReadyCallback;
import com.emapgo.mapsdk.maps.SupportMapFragment;
import com.emapgo.mapsdk.plugins.locationlayer.LocationLayerMode;
import com.emapgo.mapsdk.plugins.locationlayer.LocationLayerPlugin;
import com.emapgo.mapsdk.search.poi.PoiInfo;
import com.emapgo.mapsdk.search.poi.PoiResult;
import com.emapgo.mapsdk.search.poi.PoiSearch;
import com.emapgo.mapsdk.search.poi.PoiSearchOption;
import com.emapgo.mapsdk.utils.MapFragmentUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.support.design.widget.BottomSheetBehavior.STATE_HIDDEN;

/**
 * Created by ben on 2018/7/2.
 */

public class MapDemoFragment extends Fragment implements OnMapReadyCallback, LocationEngineListener, EmgMap.OnMapLongClickListener {
    @BindView(R.id.id_poi_name)
    TextView mPoiName;
    @BindView(R.id.id_poi_distance)
    TextView mPoiDistance;
    @BindView(R.id.id_poi_nearby)
    TextView mPoiNearby;
    private Location mLocation;
    private LocationEngine locationEngine;
    private LocationLayerPlugin locationPlugin;
    private View mRootView;

    private MapView mMapView;
    private EmgMap mEmgMap;

    private BottomSheetBehavior mBottomSheetBehavior;

    private PoiInfo mSelectPoi;
    private PoiSearch poiSearch;

    private boolean isAnimCamera;
    public static MapDemoFragment newInstance() {
        return new MapDemoFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_demo_layout, container, false);
        mMapView = mRootView.findViewById(R.id.emgMapView);
        mMapView.setStyleUrl(PreferenceManager.getMapStyle(MyApplication.getAppContext()));
        ButterKnife.bind(this, mRootView);


        mBottomSheetBehavior = BottomSheetBehavior.from(mRootView.findViewById(R.id.id_bottom_sheet_layout));
        mBottomSheetBehavior.setHideable(true);
        mBottomSheetBehavior.setState(STATE_HIDDEN);
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mMapView.onCreate(savedInstanceState);
        this.mMapView.getMapAsync(this);

        Snackbar.make(mRootView, "在地图中长按查询POI热点", Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onMapReady(EmgMap emgMap) {
        this.mEmgMap = emgMap;
        initEventListener();
        enableLocationPlugin();
    }

    private void initEventListener() {
        this.mEmgMap.addOnMapLongClickListener(this);
    }

    @SuppressLint("MissingPermission")
    private void enableLocationPlugin() {
        locationEngine = new LocationEngineProvider(getContext()).obtainBestLocationEngineAvailable();
        locationEngine.setPriority(LocationEnginePriority.LOW_POWER);
        locationEngine.activate();
        Location lastLocation = locationEngine.getLastLocation();
        if (lastLocation != null) {
            //移动Camera到定位的坐标点
            setCameraPosition(lastLocation);
        }else{
            locationEngine.addLocationEngineListener(this);
        }
        locationPlugin = new LocationLayerPlugin(mMapView, mEmgMap, locationEngine);
        locationPlugin.setLocationLayerEnabled(LocationLayerMode.COMPASS);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected() {
        Log.e("MapDemoFragment", "onConnected" );
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("MapDemoFragment", "onLocationChanged" );
        if (location != null) {
            locationEngine.removeLocationEngineListener(this);
            setCameraPosition(location);
        }
    }

    private void setCameraPosition(Location location) {
        mLocation = location;
        //将Camera移动到定位的坐标点
        mEmgMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 14));
    }

    @OnClick(R.id.id_search)
    public void onStartPoiSearch(View view) {
        Intent intent = new Intent(getActivity(), PoiSearchActivity.class);
        intent.putExtra("location", mLocation);
        startActivityForResult(intent, 0x1010);
    }

    @OnClick(R.id.id_to_target)
    public void onStartRoute(View view) {
        Intent intent = new Intent(getActivity(), RouteActivity.class);
        intent.putExtra(RouteActivity.ROUTE_TARGET_KEY, (Parcelable) mSelectPoi);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0x1010 && resultCode == RESULT_OK) {
            mSelectPoi = data.getParcelableExtra(PoiSearchActivity.SELECT_RESULT_KEY);
            addMarker(Double.parseDouble(mSelectPoi.getLon()), Double.parseDouble(mSelectPoi.getLat()));

            isAnimCamera = true;

            setPoiInfoToView();
            ;
        }
    }

    public void onStart() {
        super.onStart();
       // this.mMapView.onStart();
    }

    public void onResume() {
        super.onResume();
       // this.mMapView.onResume();
        if (isAnimCamera) {
            CameraPosition position = new CameraPosition.Builder()
                    .target(new LatLng(Double.parseDouble(mSelectPoi.getLat()), Double.parseDouble(mSelectPoi.getLon()))) // 设置相机位置
                    .zoom(16) // 设置相机缩放级别
                    .build(); // 构建一个CameraPosition

            CameraUpdate cameraUpdate = CameraUpdateFactory
                    .newCameraPosition(position);

            mEmgMap.animateCamera(cameraUpdate, 5000);

            isAnimCamera = false;
        }
    }

  /*  public void onPause() {
        super.onPause();
        this.mMapView.onPause();
    }

    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        this.mMapView.onSaveInstanceState(outState);
    }

    public void onStop() {
        super.onStop();
        this.mMapView.onStop();
    }

    public void onLowMemory() {
        super.onLowMemory();
        this.mMapView.onLowMemory();
    }

    public void onDestroyView() {
        super.onDestroyView();
        this.mMapView.onDestroy();
    }*/

    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
        longClickSearch(latLng);

        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private void longClickSearch(LatLng point) {
        addMarker(point.getLongitude(), point.getLatitude());
        preSearchPoi();
        if (poiSearch != null) {
            poiSearch.cancel();
        }
        PoiSearchOption option = PoiSearchOption.builder()
                .baseUrl(PreferenceManager.getMapService(MyApplication.getAppContext()))
                .key(getString(R.string.key))
                .secret(getString(R.string.secret))
                .lat(String.valueOf(point.getLatitude()))
                .lon(String.valueOf(point.getLongitude()))
                .range(5000)
                .sort(1)
                .size(1)
                .type(1)
                .build();
        poiSearch = PoiSearch.newInstance().search(option, new Callback<PoiResult>() {
            @Override
            public void onResponse(Call<PoiResult> call, Response<PoiResult> response) {
                if (response.body().getList() != null) {
                    mSelectPoi = response.body().getList().get(0);
                    setPoiInfoToView();
                }
            }

            @Override
            public void onFailure(Call<PoiResult> call, Throwable t) {

            }
        });
    }

    private void setPoiInfoToView() {
        mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        if (mSelectPoi != null) {
            mPoiName.setText(mSelectPoi.getName());
            mPoiDistance.setText(mSelectPoi.getDistance());
            mPoiNearby.setText(mSelectPoi.getArea_name()+" "+mSelectPoi.getAddress());
        }
    }

    private void preSearchPoi() {
        mPoiName.setText("地点信息获取中...");
        mPoiDistance.setText("");
        mPoiNearby.setText("...");
    }

    private void addMarker(double longitude, double latitude) {
        mEmgMap.clear();

        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(latitude, longitude));
        mEmgMap.addMarker(markerOptions);
    }
}
