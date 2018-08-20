package com.emapgo.android.demo.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.emapgo.android.demo.R;
import com.emapgo.android.demo.module.ExampleItemModel;

import java.util.List;

/**
 * Created by ben on 2018/7/2.
 */

public class ExampleAdapter extends BaseQuickAdapter<ExampleItemModel,BaseViewHolder> {
    public ExampleAdapter() {
        super(R.layout.item);
        openLoadAnimation(BaseQuickAdapter.SCALEIN);
        isFirstOnly(false);
    }

    @Override
    protected void convert(BaseViewHolder helper, ExampleItemModel item) {
        helper.setText(R.id.example_title, item.getTitle());
        helper.setText(R.id.example_description, item.getDescription());
        helper.setImageResource(R.id.example_image, item.getImageUrl()!=0?item.getImageUrl():R.mipmap.emg_example_map_bg);
    }
}
