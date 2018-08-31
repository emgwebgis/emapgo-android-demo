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

public class DrawListActivity  extends AppCompatActivity implements BaseQuickAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    private List<ExampleItemModel> datas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_list);
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
                R.string.activity_add_marker_title,
                R.string.activity_add_marker_description,
                new Intent(this,AddMarkerActivity.class), R.mipmap.emg_example_add_marker
        ));
        datas.add(new ExampleItemModel(
                R.string.activity_remove_marker_title,
                R.string.activity_remove_marker_description,
                new Intent(this,RemoveMarkerActivity.class), R.mipmap.emg_example_marker_remove
        ));
        datas.add(new ExampleItemModel(
                R.string.activity_marker_click_title,
                R.string.activity_marker_click_description,
                new Intent(this,MarkerClickListenerActivity.class), R.mipmap.emg_example_marker_click
        ));
        datas.add(new ExampleItemModel(
                R.string.activity_marker_info_window_title,
                R.string.activity_marker_info_window_description,
                new Intent(this,CustomInfoWindowMarkerActivity.class), R.mipmap.emg_example_marker_info_window
        ));
        datas.add(new ExampleItemModel(
                R.string.activity_draw_line_title,
                R.string.activity_draw_line_description,
                new Intent(this,DrawLineActivity.class), R.mipmap.emg_example_draw_line
        ));
        datas.add(new ExampleItemModel(
                R.string.activity_draw_line_geojson_title,
                R.string.activity_draw_line_gson_description,
                new Intent(this,DrawLineFromGeoJSONActivity.class), R.mipmap.emg_example_draw_geojson
        ));
        datas.add(new ExampleItemModel(
                R.string.activity_draw_polygon_title,
                R.string.activity_draw_polygon_description,
                new Intent(this,DrawPolygonActivity.class), R.mipmap.emg_example_draw_polygon
        ));
        datas.add(new ExampleItemModel(
                R.string.activity_draw_marker_following_title,
                R.string.activity_draw_marker_following_description,
                new Intent(this,DrawMarkerFollowingRouteActivity.class), R.mipmap.emg_example_runtime_marker_following
        ));

        datas.add(new ExampleItemModel(
                R.string.activity_draw_snaking_directions_route_title,
                R.string.activity_draw_snaking_directions_route_description,
                new Intent(this,DrawSnakingDirectionsRouteActivity.class), R.mipmap.emg_example_draw_snaking
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
