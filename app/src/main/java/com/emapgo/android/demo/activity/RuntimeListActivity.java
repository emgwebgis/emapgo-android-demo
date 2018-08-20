package com.emapgo.android.demo.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.emapgo.android.demo.R;
import com.emapgo.android.demo.adapter.ExampleAdapter;
import com.emapgo.android.demo.module.ExampleItemModel;

import java.util.ArrayList;
import java.util.List;

public class RuntimeListActivity extends AppCompatActivity implements BaseQuickAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private List<ExampleItemModel> datas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runtime_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initRecyclerView();
        initRecyclerViewData();
    }


    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ExampleAdapter();
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initRecyclerViewData() {
        datas = new ArrayList<>();
        datas.add(new ExampleItemModel(
                R.string.activity_runtime_black_map_title,
                R.string.activity_runtime_black_map_description,
                new Intent(this,RuntimeBlackMapActivity.class), R.mipmap.emg_example_runtime_black_map
        ));
        datas.add(new ExampleItemModel(
                R.string.activity_runtime_hillshade_layer_title,
                R.string.activity_runtime_hillshade_layer_description,
                new Intent(this,RuntimeRasterHillshadeLayerActivity.class),R.mipmap.emg_example_runtime_hillshade
        ));

        datas.add(new ExampleItemModel(
                R.string.activity_runtime_wms_title,
                R.string.activity_runtime_wms_description,
                new Intent(this,RuntimeWMSActivity.class),R.mipmap.emg_example_runtime_wms
        ));
        datas.add(new ExampleItemModel(
                R.string.activity_runtime_image_title,
                R.string.activity_runtime_image_description,
                new Intent(this,RuntimeImageActivity.class),R.mipmap.emg_examaple_runtime_image_resource
        ));
        datas.add(new ExampleItemModel(
                R.string.activity_runtime_image_time_title,
                R.string.activity_runtime_image_time_description,
                new Intent(this,RuntimeImageSourceTimeLapseActivity.class),R.mipmap.emg_example_runtime_image_time
        ));
        datas.add(new ExampleItemModel(
                R.string.activity_runtime_heatmap_layer_title,
                R.string.activity_runtime_heatmap_layer_description,
                new Intent(this,RuntimeHeatmapLayerActivity.class), R.mipmap.emg_example_runtime_heatmap
        ));
        datas.add(new ExampleItemModel(
                R.string.activity_runtime_clusters_title,
                R.string.activity_runtime_clusters_description,
                new Intent(this,RuntimeClustersActivity.class), R.mipmap.emg_example_runtime_clusters
        ));
        datas.add(new ExampleItemModel(
                R.string.activity_runtime_color_switcher_title,
                R.string.activity_runtime_color_switcher_description,
                new Intent(this,RuntimeColorSwitcherActivity.class), R.mipmap.emg_example_runtime_color_switcher
        ));
        datas.add(new ExampleItemModel(
                R.string.activity_runtime_zoom_color_title,
                R.string.activity_runtime_zoom_color_description,
                new Intent(this, RuntimeZoomDependentFillColorActivity.class), R.mipmap.emg_example_runtime_zoom_color
        ));
        datas.add(new ExampleItemModel(
                R.string.activity_runtime_inset_map_title,
                R.string.activity_runtime_inset_map_description,
                new Intent(this, RuntimeInsetMapActivity.class), R.mipmap.emg_example_runtime_inset_map
        ));
        datas.add(new ExampleItemModel(
                R.string.activity_runtime_fill_layer_title,
                R.string.activity_runtime_fill_layer_description,
                new Intent(this,RuntimeFillLayerActivity.class), R.mipmap.emg_example_draw_polygon
        ));
        datas.add(new ExampleItemModel(
                R.string.activity_runtime_background_layer_title,
                R.string.activity_runtime_background_layer_description,
                new Intent(this,RuntimeBackgroundLayerActivity.class), R.mipmap.emg_example_runtime_background_layer
        ));
        datas.add(new ExampleItemModel(
                R.string.activity_runtime_line_layer_title,
                R.string.activity_runtime_line_layer_description,
                new Intent(this,RuntimeLineLayerActivity.class), R.mipmap.emg_example_runtime_line_layer
        ));
        datas.add(new ExampleItemModel(
                R.string.activity_runtime_symbol_layer_title,
                R.string.activity_runtime_symbol_layer_description,
                new Intent(this,RuntimeSymbolLayerActivity.class), R.mipmap.emg_example_runtime_symbol_layer
        ));


        mAdapter.setNewData(datas);
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        startActivity(datas.get(position).getActivity());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }
}
