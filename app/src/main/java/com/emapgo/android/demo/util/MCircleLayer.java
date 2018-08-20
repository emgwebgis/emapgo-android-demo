package com.emapgo.android.demo.util;

import com.emapgo.mapsdk.style.expressions.Expression;
import com.emapgo.mapsdk.style.layers.CircleLayer;
import com.emapgo.mapsdk.style.layers.Filter;

/**
 * Created by ben on 2018/7/17.
 */

public class MCircleLayer extends CircleLayer {
    public MCircleLayer(long nativePtr) {
        super(nativePtr);
    }

    public MCircleLayer(String layerId, String sourceId) {
        super(layerId, sourceId);
    }

    public void setFilter(Expression filter) {
        this.nativeSetFilter(filter.toArray());
    }
}
