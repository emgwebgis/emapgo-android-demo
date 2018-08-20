package com.emapgo.android.demo.util;

import android.graphics.Color;
import android.widget.ProgressBar;

import com.emapgo.mapsdk.maps.EmgMap;
import com.emapgo.mapsdk.style.layers.Layer;
import com.emapgo.mapsdk.style.layers.PropertyFactory;
import com.emapgo.mapsdk.style.layers.SymbolLayer;

/**
 * Created by ben on 2018/7/26.
 */

public class MapStyleUtil {
    public static void darkStyle(EmgMap emgMap) {
        for (int i = 0; i < emgMap.getLayers().size(); i++) {
            Layer layer = emgMap.getLayers().get(i);
            if (layer.getId().equals("background")) {
                //背景颜色
                layer.setProperties(PropertyFactory.backgroundColor(Color.parseColor("#000000")));
            } else if (layer.getId().contains("river") || layer.getId().contains("water")) {
                //水
                layer.setProperties(
                        PropertyFactory.fillColor(Color.parseColor("#191a1a")),
                        PropertyFactory.fillOutlineColor(Color.parseColor("#191a1a")),
                        PropertyFactory.lineColor(Color.parseColor("#191a1a")));
            } else if (layer.getId().contains("road")) {
                //路
                if (layer.getId().contains("road")) {
                    layer.setProperties(PropertyFactory.lineColor(Color.parseColor("#2b2b2b")));
                }
            } else {
                layer.setProperties(
                        PropertyFactory.fillColor(Color.parseColor("#323432")),
                        PropertyFactory.lineColor(Color.parseColor("#323432")),
                        PropertyFactory.backgroundColor(Color.parseColor("#323432")),
                        PropertyFactory.circleColor(Color.parseColor("#323432"))
                );

                if (layer instanceof SymbolLayer) {
                    ((SymbolLayer) layer).withProperties(PropertyFactory.iconOpacity(0f),
                            PropertyFactory.textColor(Color.parseColor("#e6e6e6")),
                            PropertyFactory.textHaloColor(Color.parseColor("#1a1a1a")),
                            PropertyFactory.textHaloWidth(1.5f));
                }

            }
        }
    }

    public static void darkTwoStyle(EmgMap emgMap) {
        for (int i = 0; i < emgMap.getLayers().size(); i++) {
            Layer layer = emgMap.getLayers().get(i);
            if (layer.getId().equals("background")) {
                //背景颜色
                layer.setProperties(PropertyFactory.backgroundColor(Color.parseColor("#373e50")));
            } else if (layer.getId().contains("river") || layer.getId().contains("water")) {
                //水
                layer.setProperties(
                        PropertyFactory.fillColor(Color.parseColor("#49505e")),
                        PropertyFactory.fillOutlineColor(Color.parseColor("#49505e")),
                        PropertyFactory.lineColor(Color.parseColor("#49505e")));
            } else if (layer.getId().contains("road")) {
                //路
                if (layer.getId().contains("road")) {
                    layer.setProperties(PropertyFactory.lineColor(Color.parseColor("#4c586d")));
                }
            } else {
                layer.setProperties(
                        PropertyFactory.fillColor(Color.parseColor("#4c586d")),
                        PropertyFactory.lineColor(Color.parseColor("#4c586d")),
                        PropertyFactory.backgroundColor(Color.parseColor("#4c586d")),
                        PropertyFactory.circleColor(Color.parseColor("#4c586d"))
                );

                if (layer instanceof SymbolLayer) {
                    ((SymbolLayer) layer).withProperties(PropertyFactory.iconOpacity(0f),
                            PropertyFactory.textColor(Color.parseColor("#e6e6e6")),
                            PropertyFactory.textHaloColor(Color.parseColor("#1a1a1a")),
                            PropertyFactory.textHaloWidth(1.5f),
                            PropertyFactory.visibility("none"));
                }

            }
        }
    }
}
