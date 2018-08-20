package com.emapgo.android.demo.module;

import android.animation.ValueAnimator;

import com.emapgo.mapsdk.style.layers.CircleLayer;

/**
 * Created by ben on 2018/8/6.
 */

public class DataVisualizationPopulationCircleLayer {
    private CircleLayer circleLayer;
    private CircleLayer blurCircleLayer;
    private ValueAnimator valueAnimator;

    public CircleLayer getCircleLayer() {
        return circleLayer;
    }

    public void setCircleLayer(CircleLayer circleLayer) {
        this.circleLayer = circleLayer;
    }

    public CircleLayer getBlurCircleLayer() {
        return blurCircleLayer;
    }

    public void setBlurCircleLayer(CircleLayer blurCircleLayer) {
        this.blurCircleLayer = blurCircleLayer;
    }

    public ValueAnimator getValueAnimator() {
        return valueAnimator;
    }

    public void setValueAnimator(ValueAnimator valueAnimator) {
        this.valueAnimator = valueAnimator;
    }
}
