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

public class MapStyleListActivity extends AppCompatActivity implements BaseQuickAdapter.OnItemClickListener {


    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private List<ExampleItemModel> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_style_list);
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
                R.string.activity_map_style_nomral_title,
                R.string.activity_map_style_nomral_description,
                new Intent(this,MapStyleNomralActivity.class), R.mipmap.emg_example_style_nomral
        ));
        datas.add(new ExampleItemModel(
                R.string.activity_map_style_dark_title,
                R.string.activity_map_style_dark_description,
                new Intent(this,MapStyleDarkActivity.class), R.mipmap.emg_example_style_dark
        ));
        datas.add(new ExampleItemModel(
                R.string.activity_map_style_concise_title,
                R.string.activity_map_style_concise_description,
                new Intent(this,MapStyleConciseActivity.class), R.mipmap.emg_example_style_concise
        ));/*
        datas.add(new ExampleItemModel(
                R.string.activity_map_style_summer_title,
                R.string.activity_map_style_summer_description,
                new Intent(this,MapStyleSummerActivity.class), R.mipmap.emg_example_query_feature
        ));
        datas.add(new ExampleItemModel(
                R.string.activity_map_style_rain_title,
                R.string.activity_map_style_rain_description,
                new Intent(this,MapStyleRainActivity.class), R.mipmap.emg_example_query_feature
        ));
        datas.add(new ExampleItemModel(
                R.string.activity_map_style_winter_title,
                R.string.activity_map_style_winter_description,
                new Intent(this,MapStyleWinterActivity.class), R.mipmap.emg_example_query_feature
        ));
*/
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

