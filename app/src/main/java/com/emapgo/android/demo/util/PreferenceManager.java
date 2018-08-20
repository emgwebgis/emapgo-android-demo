package com.emapgo.android.demo.util;

import android.content.Context;

import com.emapgo.mapsdk.BuildConfig;
import com.emapgo.mapsdk.constants.EmgConstants;
import com.emapgo.mapsdk.constants.Style;

/**
 * Created by ben on 2018/7/2.
 */

public class PreferenceManager {
    public static String getMapStyle(Context context) {
        return android.preference.PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString("source_url_key", BuildConfig.EMAPGO_SDK_VERSION_FIX.equals(EmgConstants.EMAPGO_VECTOR_FIX) ? Style.EMG_STREETS_VECTOR : Style.EMG_STREETS);
    }

    public static String getMapService(Context context) {
        return android.preference.PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString("emapgo_service_key", BuildConfig.EMAPGO_LCS_SERVICES_URL);
    }

    public static String getMapRouteService(Context context) {
        return android.preference.PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString("route_service_key", BuildConfig.EMAPGO_ROUTE_SERVICES_URL);

    }

}
