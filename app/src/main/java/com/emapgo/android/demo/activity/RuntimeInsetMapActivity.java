package com.emapgo.android.demo.activity;

import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.emapgo.android.demo.R;
import com.emapgo.android.demo.util.MapStyleUtil;
import com.emapgo.mapsdk.camera.CameraPosition;
import com.emapgo.mapsdk.camera.CameraUpdateFactory;
import com.emapgo.mapsdk.geometry.LatLng;
import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.maps.EmgMapOptions;
import com.emapgo.mapsdk.maps.MapView;
import com.emapgo.mapsdk.maps.OnMapReadyCallback;
import com.emapgo.mapsdk.maps.UiSettings;
import com.emapgo.mapsdk.utils.MapFragmentUtils;

public class RuntimeInsetMapActivity extends AppCompatActivity implements OnMapReadyCallback, EmgMap.OnCameraMoveListener {

    private MapView mainMapMapView;
    private EmgMap emgMap;
    private OnMapMovedFragmentInterface onMapMovedFragmentInterfaceListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runtime_inset_map);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mainMapMapView = findViewById(R.id.main_mapView);
        mainMapMapView.onCreate(savedInstanceState);
        mainMapMapView.getMapAsync(this);
        CustomSupportMapFragment customSupportMapFragment;
        if (savedInstanceState == null) {

            final FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            customSupportMapFragment = CustomSupportMapFragment.newInstance();

            transaction.add(R.id.mini_map_fragment_container, customSupportMapFragment, "com.emapgo.fragmentMap");
            transaction.commit();

        } else {
            customSupportMapFragment = (CustomSupportMapFragment)
                    getSupportFragmentManager().findFragmentByTag("com.emapgo.fragmentMap");
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
    public void onMapReady(EmgMap emg) {
        this.emgMap = emg;
        emgMap.addOnCameraMoveListener(this);
    }

    @Override
    public void onCameraMove() {
        onMapMovedFragmentInterfaceListener.onMapMoved(emgMap.getCameraPosition());
    }

    private void setOnDataListener(OnMapMovedFragmentInterface onMapMovedFragmentInterface) {
        onMapMovedFragmentInterfaceListener = onMapMovedFragmentInterface;
    }

    public interface OnMapMovedFragmentInterface {
        void onMapMoved(CameraPosition mainMapCameraPosition);
    }

    @Override
    public void onResume() {
        super.onResume();
        mainMapMapView.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mainMapMapView.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainMapMapView.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        mainMapMapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mainMapMapView.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (emgMap != null) {
            emgMap.removeOnCameraMoveListener(this);
        }
        mainMapMapView.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mainMapMapView.onSaveInstanceState(outState);
    }


    public static class CustomSupportMapFragment extends Fragment implements
            OnMapMovedFragmentInterface {
        private EmgMap mSubEmgMap;
        private MapView fragmentMap;
        private OnMapReadyCallback onMapReadyCallback;
        private CameraPosition cameraPositionForFragmentMap;
        private static final int ZOOM_DISTANCE_BETWEEN_MAIN_AND_FRAGMENT_MAPS = 3;


        public static CustomSupportMapFragment newInstance() {
            CustomSupportMapFragment mapFragment = new CustomSupportMapFragment();
            return mapFragment;
        }


        @Override
        public void onMapMoved(final CameraPosition mainMapCameraPosition) {
            if (mSubEmgMap == null) {
                return;
            }
            cameraPositionForFragmentMap = new CameraPosition.Builder()
                    .target(mainMapCameraPosition.target)
                    .zoom(mainMapCameraPosition.zoom - ZOOM_DISTANCE_BETWEEN_MAIN_AND_FRAGMENT_MAPS)
                    .bearing(mainMapCameraPosition.bearing)
                    .tilt(mainMapCameraPosition.tilt)
                    .build();

            mSubEmgMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPositionForFragmentMap));

        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            ((RuntimeInsetMapActivity) getActivity()).setOnDataListener(this);
        }


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            super.onCreateView(inflater, container, savedInstanceState);
            Context context = container.getContext();
            EmgMapOptions options = new EmgMapOptions();
            options.camera(new CameraPosition.Builder()
                    .target(new LatLng(31.005862904624205, 121.82052612304688))
                    .zoom(2)
                    .build());
            return fragmentMap = new MapView(context,options);
        }


        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            fragmentMap.onCreate(savedInstanceState);
            fragmentMap.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(EmgMap emgMap) {
                    mSubEmgMap = emgMap;

                    UiSettings uiSettings = mSubEmgMap.getUiSettings();
                    uiSettings.setLogoEnabled(false);
                    uiSettings.setAttributionEnabled(false);
                    uiSettings.setCompassEnabled(false);
                    uiSettings.setScrollGesturesEnabled(false);
                    uiSettings.setRotateGesturesEnabled(false);
                    uiSettings.setTiltGesturesEnabled(false);
                    MapStyleUtil.darkStyle(mSubEmgMap);
                }
            });
        }

        @Override
        public void onStart() {
            super.onStart();
            fragmentMap.onStart();
            fragmentMap.getMapAsync(onMapReadyCallback);
        }

        @Override
        public void onResume() {
            super.onResume();
            fragmentMap.onResume();
        }


        @Override
        public void onPause() {
            super.onPause();
            fragmentMap.onPause();
        }

        @Override
        public void onSaveInstanceState(@NonNull Bundle outState) {
            super.onSaveInstanceState(outState);
            fragmentMap.onSaveInstanceState(outState);
        }


        @Override
        public void onStop() {
            super.onStop();
            fragmentMap.onStop();
        }


        @Override
        public void onLowMemory() {
            super.onLowMemory();
            fragmentMap.onLowMemory();
        }


        @Override
        public void onDestroyView() {
            super.onDestroyView();
            fragmentMap.onDestroy();
        }


        public void getMapAsync(@NonNull final OnMapReadyCallback onMapReadyCallback) {
            this.onMapReadyCallback = onMapReadyCallback;
        }
    }
}


