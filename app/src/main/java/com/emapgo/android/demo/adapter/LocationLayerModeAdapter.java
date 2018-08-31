package com.emapgo.android.demo.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.emapgo.android.demo.R;

import java.util.Arrays;

/**
 * Created by ben on 2018/7/16.
 */

public class LocationLayerModeAdapter extends BaseQuickAdapter<Boolean,BaseViewHolder>{
    public LocationLayerModeAdapter() {
        super(R.layout.item_layer_mode_layer, Arrays.asList(true, false, false));
    }

    @Override
    protected void convert(BaseViewHolder helper, Boolean item) {
        switch (helper.getAdapterPosition()) {
            case 0:
                break;
            case 1:
                break;
            case 2:
                break;
        }
    }

}
