package com.emapgo.android.demo.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.emapgo.android.core.location.LocationEngine;
import com.emapgo.android.core.location.LocationEngineListener;
import com.emapgo.android.core.location.LocationEnginePriority;
import com.emapgo.android.core.location.LocationEngineProvider;
import com.emapgo.android.core.permissions.PermissionsListener;
import com.emapgo.android.core.permissions.PermissionsManager;
import com.emapgo.android.demo.MyApplication;
import com.emapgo.android.demo.R;
import com.emapgo.android.demo.adapter.DirectionsProgramAdapter;
import com.emapgo.android.demo.adapter.DirectionsStepAdapter;
import com.emapgo.android.demo.util.DensityUtil;
import com.emapgo.android.demo.util.PreferenceManager;
import com.emapgo.api.directions.v5.models.DirectionsResponse;
import com.emapgo.api.directions.v5.models.DirectionsRoute;
import com.emapgo.api.directions.v5.models.LegStep;
import com.emapgo.api.directions.v5.models.RouteOptions;
import com.emapgo.core.constants.Constants;
import com.emapgo.geojson.LineString;
import com.emapgo.geojson.Point;
import com.emapgo.mapsdk.SDKInitializer;
import com.emapgo.mapsdk.annotations.Icon;
import com.emapgo.mapsdk.annotations.IconFactory;
import com.emapgo.mapsdk.annotations.Marker;
import com.emapgo.mapsdk.annotations.MarkerOptions;
import com.emapgo.mapsdk.annotations.Polyline;
import com.emapgo.mapsdk.annotations.PolylineOptions;
import com.emapgo.mapsdk.camera.CameraPosition;
import com.emapgo.mapsdk.camera.CameraUpdateFactory;
import com.emapgo.mapsdk.exceptions.InvalidLatLngBoundsException;
import com.emapgo.mapsdk.geometry.LatLng;
import com.emapgo.mapsdk.geometry.LatLngBounds;
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.maps.MapView;
import com.emapgo.mapsdk.maps.OnMapReadyCallback;
import com.emapgo.mapsdk.plugins.locationlayer.LocationLayerMode;
import com.emapgo.mapsdk.plugins.locationlayer.LocationLayerPlugin;
import com.emapgo.mapsdk.search.poi.PoiInfo;
import com.emapgo.mapsdk.search.poi.PoiResult;
import com.emapgo.mapsdk.search.poi.PoiSearch;
import com.emapgo.mapsdk.search.poi.PoiSearchOption;
import com.emapgo.mapsdk.search.route.RouteSearch;
import com.emapgo.mapsdk.search.route.RouteSearchOption;
import com.emapgo.mapsdk.search.route.maneuver.parse.DirectionsManeuverParse;
import com.emapgo.mapsdk.search.route.maneuver.parse.DirectionsManeuverParseInfo;
import com.emapgo.services.android.navigation.ui.v5.NavigationLauncher;
import com.emapgo.services.android.navigation.ui.v5.NavigationLauncherOptions;
import com.emapgo.services.android.navigation.ui.v5.route.NavigationMapRoute;
import com.emapgo.services.android.navigation.ui.v5.route.OnRouteSelectionChangeListener;
import com.emapgo.services.android.navigation.v5.navigation.NavigationRoute;
import com.emapgo.services.android.navigation.v5.navigation.NavigationUnitType;
import com.emapgo.services.android.navigation.v5.utils.LocaleUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RouteActivity extends AppCompatActivity implements OnMapReadyCallback, EmgMap.OnMapClickListener, LocationEngineListener, PermissionsListener, DirectionsProgramAdapter.OnClickListener, BaseQuickAdapter.OnItemClickListener, EmgMap.OnMapLongClickListener, OnRouteSelectionChangeListener {
    public static final String ROUTE_TARGET_KEY = "ROUTE_TARGET_KEY";
    @BindView(R.id.id_route_navigation_btn)
    Button mLaunchNavigation;
    @BindView(R.id.id_mapview)
    MapView mapView;
    EmgMap emgMap;
    @BindView(R.id.id_route_ll)
    LinearLayout routeLinearLayout;
    @BindView(R.id.id_directions_program_rv)
    LinearLayout mDirectionsProgramRv;
    @BindView(R.id.id_directions_step_rv)
    RecyclerView mDirectionsStepRv;
    @BindView(R.id.id_route_type)
    TabLayout mRouteTypeTabLayout;

    @BindView(R.id.id_location_start)
    EditText mStartEditText;
    @BindView(R.id.id_location_end)
    EditText mEndEditText;


    @BindView(R.id.id_route_header_ll)
    LinearLayout id_route_header_ll;

    private BottomSheetBehavior<LinearLayout> bottomSheetBehavior;
    private DirectionsProgramAdapter directionsProgramAdapter;
    private DirectionsStepAdapter directionsStepAdapter;

    private int mCurrentSelectModule;
    private String mCurrentSelectRouteModule = RouteSearchOption.DRIVING;
    private PoiInfo mSelectStartPoiInfo = new PoiInfo();
    private PoiInfo mSelectEndPoiInfo = new PoiInfo();

    private LocationEngine locationEngine;
    private LocationLayerPlugin locationPlugin;
    private PermissionsManager permissionsManager;
    private Location lastLocation;

    List<DirectionsRoute> modifiedRoutes = new ArrayList<>();

    /**
     * 当前路线规划状态
     * true：正在规划，已规划
     * false：未开始规划
     */
    private boolean mCurrRouteStatus;
    /**
     * 当前选择的路线
     */
    private DirectionsRoute mCurrDirectionsRoute;
    private DirectionsResponse directionsResponse;
    private List<DirectionsManeuverParseInfo> directionsManeuverParseInfos;
    private List<Point> points = new ArrayList<>();
    private NavigationMapRoute mapRoute;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route);
        getSupportActionBar().hide();
        ButterKnife.bind(this);

        initView();
        initMapView();
        initSetting();
    }

    private void initSetting() {
        mSelectEndPoiInfo = getIntent().getParcelableExtra(ROUTE_TARGET_KEY);
        if (mSelectEndPoiInfo != null) {
            mEndEditText.setText(mSelectEndPoiInfo.getName());
        }

    }

    private void initView() {
        changeButtonEnable(true);

        mRouteTypeTabLayout.addTab(mRouteTypeTabLayout.newTab().setText("驾车"));
        mRouteTypeTabLayout.addTab(mRouteTypeTabLayout.newTab().setText("步行"));
        mRouteTypeTabLayout.addTab(mRouteTypeTabLayout.newTab().setText("骑行"));
        mRouteTypeTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        mCurrentSelectRouteModule = RouteSearchOption.DRIVING;
                        break;
                    case 1:
                        mCurrentSelectRouteModule = RouteSearchOption.WALKING;
                        break;
                    case 2:
                        mCurrentSelectRouteModule = RouteSearchOption.CYCLING;
                        break;
                }
                startRoute();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        bottomSheetBehavior = BottomSheetBehavior.from(routeLinearLayout);

        mDirectionsStepRv.setLayoutManager(new LinearLayoutManager(this));
        directionsStepAdapter = new DirectionsStepAdapter();
        directionsStepAdapter.setOnItemClickListener(this);
        mDirectionsStepRv.setAdapter(directionsStepAdapter);

        directionsProgramAdapter = new DirectionsProgramAdapter(mDirectionsProgramRv);
        directionsProgramAdapter.setOnClickListener(this);
    }

    private void initMapView() {
        mapView.getMapAsync(this);

    }

    private void initMapRoute() {
        mapRoute = new NavigationMapRoute(mapView, emgMap);
        mapRoute.setOnRouteSelectionChangeListener(this);
    }


    @Override
    public void onMapReady(EmgMap emgMap) {
        this.emgMap = emgMap;
        this.emgMap.addOnMapClickListener(this);
        this.emgMap.addOnMapLongClickListener(this);

        initMapRoute();
        enableLocationPlugin();
    }

    @SuppressWarnings({"MissingPermission"})
    private void enableLocationPlugin() {
        if (PermissionsManager.areLocationPermissionsGranted(this)) {
            locationEngine = new LocationEngineProvider(this).obtainBestLocationEngineAvailable();
            locationEngine.setPriority(LocationEnginePriority.BALANCED_POWER_ACCURACY);
            locationEngine.activate();
            lastLocation = locationEngine.getLastLocation();
            if (lastLocation != null) {
                setLocationPosition(lastLocation);
            } else {
                locationEngine.addLocationEngineListener(this);
            }
            locationPlugin = new LocationLayerPlugin(mapView, emgMap, locationEngine);
            locationPlugin.setLocationLayerEnabled(LocationLayerMode.COMPASS);
        } else {
            permissionsManager = new PermissionsManager(this);
            permissionsManager.requestLocationPermissions(this);
        }
    }

    /**
     * 定位成功
     *
     * @param location
     */
    private void setLocationPosition(Location location) {
        if (location == null) {
            return;
        }

        lastLocation = location;


        emgMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(location.getLatitude(), location.getLongitude()), 14));

        mStartEditText.setText("我的位置");

        mSelectStartPoiInfo.setLat(String.valueOf(location.getLatitude()));
        mSelectStartPoiInfo.setLon(String.valueOf(location.getLongitude()));

        //开始路线规划
        startRoute();
    }


    @OnClick({R.id.id_location_start, R.id.id_location_end})
    public void onLocationEditViewClick(View view) {
        switch (view.getId()) {
            case R.id.id_location_start:
                mCurrentSelectModule = 0;
                break;
            case R.id.id_location_end:
                mCurrentSelectModule = 1;
                break;
        }
        startActivityForResult(new Intent(this, PoiSearchActivity.class), 0x1010);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        PoiInfo poiInfo = data.getParcelableExtra(PoiSearchActivity.SELECT_RESULT_KEY);
        if (poiInfo == null) {
            return;
        }
        if (mCurrentSelectModule == 0) {
            mStartEditText.setText(poiInfo.getName());
            mSelectStartPoiInfo = poiInfo;
        } else {
            mEndEditText.setText(poiInfo.getName());
            mSelectEndPoiInfo = poiInfo;
        }

        startRoute();
    }

    private void startRoute() {
        if (mSelectStartPoiInfo == null || mSelectEndPoiInfo == null) {
            return;
        }

        addRouteProgring();

        RouteSearchOption option = RouteSearchOption.builder()
                .origin(Point.fromLngLat(Double.parseDouble(mSelectStartPoiInfo.getLon()), Double.parseDouble(mSelectStartPoiInfo.getLat())))
                .destination(Point.fromLngLat(Double.parseDouble(mSelectEndPoiInfo.getLon()), Double.parseDouble(mSelectEndPoiInfo.getLat())))
                .additionalPoints(points)
                .profile(mCurrentSelectRouteModule)
                .alternatives(true)
                .overview("full")
                .steps(true)
                .baseUrl(PreferenceManager.getMapRouteService(MyApplication.getAppContext()))
//                .baseUrl("http://192.168.11.132:5000/")
                .build();
        RouteSearch.newInstance().enqueueCall(option, new Callback<DirectionsResponse>() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                modifiedRoutes.clear();
                if (response.body() == null) {
                    addRouteErrorText("未查询到路线规划！");
                    return;
                }

                ArrayList<Point> coordinates = new ArrayList<>();
                coordinates.add(option.getOption().getOrigin());
                coordinates.add(option.getOption().getDestination());

                List<DirectionsRoute> routes = response.body().routes();
                for (DirectionsRoute route : routes) {
                    modifiedRoutes.add(route.toBuilder().routeOptions(
                            RouteOptions.builder()
                                    .profile(RouteSearchOption.DRIVING)
                                    .coordinates(coordinates)
                                    .continueStraight(true)
                                    .annotations(option.getOption().getAnnotations())
                                    .alternatives(true)
                                    .language("zh")
                                    .radiuses(option.getOption().getRadiuses())
                                    .user("emapgo")
                                    .voiceInstructions(true)
                                    .bannerInstructions(true)
                                    .exclude(option.getOption().getExclude())
                                    .accessToken("empty token")
                                    .requestUuid(UUID.randomUUID().toString())
                                    .baseUrl(option.getOption().getBaseUrl())
                                    .build()
                    ).build());
                }

                emgMap.clear();
                mapRoute.removeRoute();
                mapRoute.addRoutes(modifiedRoutes);
                RouteActivity.this.directionsManeuverParseInfos = DirectionsManeuverParse.parseDirectionsManeuver(response.body());

                //添加路线规划图层
                changeRoute(0);
                boundCameraToRoute();
            }

            @Override
            public void onFailure(Call<DirectionsResponse> call, Throwable t) {
                t.printStackTrace();
                addRouteErrorText("路线规划失败，点我重试！");
            }
        });
    }

    private void changeRoute(int position) {
        directionsStepAdapter.setNewData(directionsManeuverParseInfos.get(position).getManeuverItems());
        directionsProgramAdapter.setNewData(directionsManeuverParseInfos, position);
        mCurrDirectionsRoute = modifiedRoutes.get(position);


        for (int i = 0; i < points.size(); i++) {
            Point point = points.get(i);
            View view = getLayoutInflater().inflate(R.layout.item_poi_layout, null, false);
            ((TextView) view.findViewById(R.id.id_id_poi_position)).setText(String.valueOf(i + 1));
            Bitmap bitmap = generate(view);
            IconFactory iconFactory = IconFactory.getInstance(RouteActivity.this);
            Icon icon = iconFactory.fromBitmap(bitmap);
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(new LatLng(point.latitude(), point.longitude()))
                    .title("remove:" + String.valueOf(i + 1))
                    .icon(icon);
            emgMap.addMarker(markerOptions);

            emgMap.setInfoWindowAdapter(new EmgMap.InfoWindowAdapter() {
                @Nullable
                @Override
                public View getInfoWindow(@NonNull Marker marker) {
                    if (marker.getTitle().startsWith("remove:")) {
                        View view = getLayoutInflater().inflate(R.layout.layout_remove_poi, null);
                        TextView titleTv = (TextView) view.findViewById(R.id.id_route_poi_info_tv);
                        titleTv.setText("途经点" + marker.getTitle().split(":")[1]);
                        view.findViewById(R.id.id_route_poi_info_remove).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                points.remove(Integer.parseInt(marker.getTitle().split(":")[1]) - 1);
                                startRoute();
                            }
                        });
                        return view;
                    } else {
                        return null;
                    }
                }
            });

        }
        boundCameraToRoute();
    }


    /**
     * 距离缩放
     */
    public void boundCameraToRoute() {
        if (mCurrDirectionsRoute != null) {
            List<Point> routeCoords = LineString.fromPolyline(mCurrDirectionsRoute.geometry(),
                    Constants.PRECISION_5).coordinates();
            List<LatLng> bboxPoints = new ArrayList<>();
            for (Point point : routeCoords) {
                bboxPoints.add(new LatLng(point.latitude(), point.longitude()));
            }
            if (bboxPoints.size() > 1) {
                try {
                    LatLngBounds bounds = new LatLngBounds.Builder().includes(bboxPoints).build();
                    animateCameraBbox(bounds, 1000, new int[]{100, 750, 100, 600});
                } catch (InvalidLatLngBoundsException exception) {
                    Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }

    private void animateCameraBbox(LatLngBounds bounds, int animationTime, int[] padding) {
        CameraPosition position = emgMap.getCameraForLatLngBounds(bounds, padding);
        emgMap.animateCamera(CameraUpdateFactory.newCameraPosition(position), animationTime);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected() {
        locationEngine.requestLocationUpdates();
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            setLocationPosition(location);
            locationEngine.removeLocationEngineListener(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        permissionsManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onExplanationNeeded(List<String> permissionsToExplain) {

    }

    @Override
    public void onPermissionResult(boolean granted) {
        if (granted) {
            enableLocationPlugin();
        } else {
            Toast.makeText(this, "定位权限被拒绝", Toast.LENGTH_LONG).show();
            finish();
        }
    }


    @Override
    @SuppressWarnings({"MissingPermission"})
    protected void onStart() {
        super.onStart();
        if (locationPlugin != null) {
            locationPlugin.onStart();
        }
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
        if (locationPlugin != null) {
            locationPlugin.onStop();
        }
        if (locationEngine != null) {
            locationEngine.removeLocationUpdates();
        }
        mapView.onStop();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        if (locationEngine != null) {
            locationEngine.deactivate();
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onClickListener(int position) {
        try {
            int orignPosition = 0;
            Field primaryRouteIndex = mapRoute.getClass().getDeclaredField("primaryRouteIndex");
            primaryRouteIndex.setAccessible(true);
            orignPosition = (int) primaryRouteIndex.get(mapRoute);
            primaryRouteIndex.set(mapRoute,position);

            Method checkNewRouteFound = mapRoute.getClass().getDeclaredMethod("checkNewRouteFound", int.class);
            checkNewRouteFound.setAccessible(true);
            checkNewRouteFound.invoke(mapRoute, orignPosition);
        } catch (Exception e) {
            e.printStackTrace();
        }
        changeRoute(position);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        DirectionsManeuverParseInfo.DirectionsManeuverItem directionsManeuverItem = directionsStepAdapter.getData().get(position);
        LegStep orignStep = directionsManeuverItem.getOrignStep();


        emgMap.addMarker(new MarkerOptions()
                .position(new LatLng(orignStep.maneuver().location().latitude(), orignStep.maneuver().location().longitude())));

        emgMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(orignStep.maneuver().location().latitude(), orignStep.maneuver().location().longitude()), 18), 1000);
    }

    private List<Marker> markers = new ArrayList<>();


    @Override
    public void onMapClick(@NonNull LatLng point) {

    }


    @Override
    public void onMapLongClick(@NonNull LatLng point) {
        if (markers.size() > 0) {
            emgMap.removeMarker(markers.get(markers.size() - 1));
        }

        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(point.getLatitude(), point.getLongitude()));
        Marker marker = emgMap.addMarker(markerOptions);
        markers.add(marker);
        emgMap.setInfoWindowAdapter(new EmgMap.InfoWindowAdapter() {

            private PoiInfo poiInfo;

            @Nullable
            @Override
            public View getInfoWindow(@NonNull Marker marker) {
                if (!TextUtils.isEmpty(marker.getTitle())) {
                    return null;
                }
                View view = getLayoutInflater().inflate(R.layout.layout_callout, null);
                TextView titleTv = (TextView) view.findViewById(R.id.id_route_poi_info_tv);
                titleTv.setText("查询中...");
                view.findViewById(R.id.id_route_add_poi).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (poiInfo != null) {
                            points.add(Point.fromLngLat(Double.parseDouble(poiInfo.getLon()), Double.parseDouble(poiInfo.getLat())));
                        }
                        startRoute();
                    }
                });
                view.findViewById(R.id.id_route_end).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (poiInfo != null) {
                            mEndEditText.setText(poiInfo.getName());
                            mSelectEndPoiInfo = poiInfo;
                            startRoute();
                        }
                    }
                });

                PoiSearchOption option = PoiSearchOption.builder()
                        .key(getString(R.string.key))
                        .secret(getString(R.string.secret))
                        .lat(String.valueOf(marker.getPosition().getLatitude()))
                        .lon(String.valueOf(marker.getPosition().getLongitude()))
                        .range(5000)
                        .sort(1)
                        .size(1)
                        .type(1)
                        .build();
                PoiSearch.newInstance().search(option, new Callback<PoiResult>() {
                    @Override
                    public void onResponse(Call<PoiResult> call, Response<PoiResult> response) {
                        if (response.body().getList() != null) {
                            if (response.body().getList().size() > 0) {
                                poiInfo = response.body().getList().get(0);
                                titleTv.setText(poiInfo.getName());
                            } else {
                                titleTv.setText("未查询到相关POI！");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<PoiResult> call, Throwable t) {
                        titleTv.setText("出错了！");
                    }
                });
                return view;
            }
        });
        //marker.showInfoWindow(emgMap, mapView);
        emgMap.selectMarker(marker);

    }

    private Bitmap generate(@NonNull View view) {
        int measureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        view.measure(measureSpec, measureSpec);

        int measuredWidth = view.getMeasuredWidth();
        int measuredHeight = view.getMeasuredHeight();

        view.layout(0, 0, measuredWidth, measuredHeight);
        Bitmap bitmap = Bitmap.createBitmap(measuredWidth, measuredHeight, Bitmap.Config.ARGB_8888);
        bitmap.eraseColor(Color.TRANSPARENT);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    @OnClick(R.id.id_route_navigation_btn)
    public void launchNavigation(View v) {
        Toast.makeText(this, "导航暂不可用", Toast.LENGTH_SHORT).show();
        /*
        if (mCurrDirectionsRoute == null) {
            Snackbar.make(mapView, "路线为空", Snackbar.LENGTH_SHORT).show();
            return;
        }
        Locale locale = getLocale();
        NavigationLauncherOptions.Builder optionsBuilder = NavigationLauncherOptions.builder()
                .shouldSimulateRoute(true)
                .locale(locale)
                .unitType(getUnitType())
                .directionsProfile(getRouteProfile());
        optionsBuilder.directionsRoute(mCurrDirectionsRoute);
        NavigationLauncher.startNavigation(RouteActivity.this, optionsBuilder.build());*/
    }

    private void setFieldsFromSharedPreferences(NavigationRoute.Builder builder) {
        Locale locale = getLocale();
        builder
                .language(locale)
                .voiceUnits(NavigationUnitType.getDirectionsCriteriaUnitType(getUnitType(), locale));
    }

    private String getRouteProfile() {
        SharedPreferences sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getString("route_profile", "");
    }

    @NavigationUnitType.UnitType
    private int getUnitType() {
        SharedPreferences sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(this);
        return Integer.parseInt(sharedPreferences.getString("unit_type",
                Integer.toString(NavigationUnitType.NONE_SPECIFIED)));
    }

    private boolean getShouldSimulateRoute() {
        SharedPreferences sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(this);
        return sharedPreferences.getBoolean("simulate_route", false);
    }

    private Locale getLocale() {
        SharedPreferences sharedPreferences = android.preference.PreferenceManager.getDefaultSharedPreferences(this);
        String defaultString = "device_locale";
        String localeString = sharedPreferences.getString("language", defaultString);
        return localeString.equals(defaultString) ? LocaleUtils.getDeviceLocale(this) : new Locale(localeString);
    }

    private void changeButtonEnable(boolean enable) {
        mLaunchNavigation.setText(enable ? "开始导航" : "路线规划中...");
        mLaunchNavigation.setEnabled(enable);
    }

    private void addRouteErrorText(String text) {
        mDirectionsProgramRv.removeAllViews();
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        textView.setGravity(Gravity.CENTER);
        textView.setText(text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRoute();
            }
        });
        mDirectionsProgramRv.addView(textView);
    }

    private void addRouteProgring() {
        mDirectionsProgramRv.removeAllViews();
        ProgressBar progressBar = new ProgressBar(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DensityUtil.dip2px(this, 50));
        layoutParams.gravity = Gravity.CENTER;
        progressBar.setLayoutParams(layoutParams);
        mDirectionsProgramRv.addView(progressBar);
    }

    @OnClick(R.id.id_back)
    public void shutdownWindow(View view) {
        this.finish();
    }

    @Override
    public void onNewPrimaryRouteSelected(DirectionsRoute directionsRoute) {
        mCurrDirectionsRoute = directionsRoute;
    }
}
