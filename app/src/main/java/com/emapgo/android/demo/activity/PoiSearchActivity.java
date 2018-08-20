package com.emapgo.android.demo.activity;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.emapgo.android.demo.MyApplication;
import com.emapgo.android.demo.R;
import com.emapgo.android.demo.adapter.PoiSearchAdapter;
import com.emapgo.android.demo.util.PreferenceManager;
import com.emapgo.api.geocoding.v5.EmgGeocoding;
import com.emapgo.mapsdk.search.poi.PoiResult;
import com.emapgo.mapsdk.search.poi.PoiSearch;
import com.emapgo.mapsdk.search.poi.PoiSearchOption;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PoiSearchActivity extends AppCompatActivity {
    public static final String SELECT_RESULT_KEY = "SELECT_RESULT_KEY";
    @BindView(R.id.id_poi_rv)
    RecyclerView mPoiRecyclerView;
    private PoiSearchAdapter poiSearchAdapter;
    private Location mLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_poi_search);
        mLocation = getIntent().getParcelableExtra("location");

        ButterKnife.bind(this);
        initAdapter();
    }

    private void initAdapter() {
        poiSearchAdapter = new PoiSearchAdapter();
        mPoiRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPoiRecyclerView.setAdapter(poiSearchAdapter);
        poiSearchAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent data = new Intent();
                data.putExtra(SELECT_RESULT_KEY, (Parcelable) poiSearchAdapter.getData().get(position));
                setResult(RESULT_OK, data);
                finish();
            }
        });
    }

    @OnTextChanged(value = R.id.edittext_search, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void onEditTextChange(Editable editable) {
        PoiSearchOption option = PoiSearchOption.builder()
                .baseUrl(PreferenceManager.getMapService(MyApplication.getAppContext()))
                .key(getString(R.string.key))
                .secret(getString(R.string.secret))
                .lon(String.valueOf(mLocation.getLongitude()))
                .lat(String.valueOf(mLocation.getLatitude()))
                .query(editable.toString())
                .range(5000)
                .type(1)
                .build();

        PoiSearch.newInstance().search(option, new Callback<PoiResult>() {
            @Override
            public void onResponse(Call<PoiResult> call, Response<PoiResult> response) {
                if (response.body() == null) {
                    Toast.makeText(PoiSearchActivity.this, "未检索到相关POI", Toast.LENGTH_SHORT).show();
                    return;
                }
                poiSearchAdapter.setNewData(response.body().getList());
            }

            @Override
            public void onFailure(Call<PoiResult> call, Throwable t) {

            }
        });
    }

    @OnClick(R.id.button_search_back)
    public void onBackClick(View view) {
        this.finish();
    }
}
