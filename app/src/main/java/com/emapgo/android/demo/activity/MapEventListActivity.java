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

public class MapEventListActivity extends AppCompatActivity implements BaseQuickAdapter.OnItemClickListener {

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
                R.string.activity_map_event_gestures_title,
                R.string.activity_map_event_gestures_description,
                new Intent(this,MapEventGesturesActivity.class), R.mipmap.emg_example_event_gesture
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
