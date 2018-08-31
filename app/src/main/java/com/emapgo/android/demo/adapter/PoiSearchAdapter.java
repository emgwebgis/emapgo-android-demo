package com.emapgo.android.demo.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.emapgo.android.demo.R;
import com.emapgo.mapsdk.search.poi.PoiInfo;

/**
 * Created by ben on 2018/4/4.
 */

public class PoiSearchAdapter extends BaseQuickAdapter<PoiInfo,BaseViewHolder> {
    public PoiSearchAdapter() {
        super(R.layout.emg_item_search_result);
    }

    @Override
    protected void convert(BaseViewHolder helper, PoiInfo item) {
        helper.setText(R.id.tv_place_name, item.getName());
        helper.setText(R.id.tv_address, item.getAddress());
    }
}
