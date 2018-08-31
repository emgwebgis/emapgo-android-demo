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

public class MapThreeStereoscopicListActivity extends AppCompatActivity implements BaseQuickAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private List<ExampleItemModel> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_three_stereoscopic_list);
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
                R.string.activity_map_3d_marathon_title,
                R.string.activity_map_3d_marathon_description,
                new Intent(this, MapThreeStereoscopicMarathonActivity.class), R.mipmap.emg_example_3d_marathon
        ));
        datas.add(new ExampleItemModel(
                R.string.activity_map_3d_navigation_title,
                R.string.activity_map_3d_navigation_description,
                new Intent(this, MapThreeStereoscopicNavigationActivity.class), R.mipmap.emg_example_three_stereoscopic_navigation
        ));/*
        datas.add(new ExampleItemModel(
                R.string.activity_map_switch_light_title,
                R.string.activity_map_switch_light_description,
                new Intent(this, MapThreeStereoscpicSwitchLightActivity.class), R.mipmap.emg_example_3d_marathon
        ));
        datas.add(new ExampleItemModel(
                R.string.activity_map_3d_population_density_title,
                R.string.activity_map_3d_population_density_description,
                new Intent(this, MapThreeStereoscopicPopulationDensityActivity.class), R.mipmap.emg_example_3d_marathon
        ));*/
        datas.add(new ExampleItemModel(
                R.string.activity_map_3d_indoor_title,
                R.string.activity_map_3d_indoor_description,
                new Intent(this, MapThreeStereoscopicIndoor3DActivity.class), R.mipmap.emg_example_3d_indoor
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

