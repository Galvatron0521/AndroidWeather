package com.zhiyuan3g.androidweather.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyuan3g.androidweather.R;
import com.zhiyuan3g.androidweather.adapter.CityAdapter;
import com.zhiyuan3g.androidweather.adapter.CountryAdapter;
import com.zhiyuan3g.androidweather.base.ContractUtils;
import com.zhiyuan3g.androidweather.db.CityDB;
import com.zhiyuan3g.androidweather.db.CountryDB;
import com.zhiyuan3g.androidweather.db.ProvinceDB;
import com.zhiyuan3g.androidweather.utils.OkHttpCallBack;
import com.zhiyuan3g.androidweather.utils.OkHttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/23.
 */

public class fragment_country extends Fragment {
    @BindView(R.id.tv_province)
    TextView tvProvince;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.province_toolbar)
    Toolbar Toolbar;
    private int id, provinceId;
    private FragmentManager fragmentManager;
    private List<CountryDB> countryDBs;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_province, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        id = bundle.getInt("id");
        provinceId = bundle.getInt("provinceCode");
        String cityName = bundle.getString("cityName");
        initToolBar(cityName);
        initFindDB();
        return view;
    }

    private void initFindDB() {
        List<CountryDB> countryDBs = DataSupport.where("cityCode = ?", String.valueOf(id)).find(CountryDB.class);
        if (countryDBs.isEmpty()) {
            initHttp();
        } else {
            CountryAdapter countryAdapter = new CountryAdapter(getActivity(), countryDBs);
            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            recycler.setLayoutManager(manager);
            recycler.setAdapter(countryAdapter);
        }
    }

    private void initHttp() {
        progressDialog.setTitle("提示");
        progressDialog.setMessage("请稍等...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        OkHttpUtils.sendRequestGETMethod(getActivity(), ContractUtils.URL_CITY + provinceId + "/" + id, new OkHttpCallBack() {
            @Override
            public void Success(String result) {
                System.out.println(result);
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        CountryDB countryDB = new CountryDB();
                        countryDB.setCityCode(id);
                        countryDB.setProvinceCode(provinceId);
                        countryDB.setCountryCode(jsonObject.getInt("id"));
                        countryDB.setCountryName(jsonObject.getString("name"));
                        countryDB.save();
                        countryDBs.add(countryDB);
                    }
                    CountryAdapter countryAdapter = new CountryAdapter(getActivity(), countryDBs);
                    LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                    recycler.setLayoutManager(manager);
                    recycler.setAdapter(countryAdapter);
                    progressDialog.dismiss();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void Failure(String failure) {
                progressDialog.dismiss();
            }
        });
    }

    private void initToolBar(String name) {
        progressDialog = new ProgressDialog(getActivity());
        countryDBs = new ArrayList<>();
        setHasOptionsMenu(true);
        fragmentManager = getFragmentManager();
        ((AppCompatActivity) getActivity()).setSupportActionBar(Toolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        tvProvince.setText(name);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                fragment_city fragment_city = new fragment_city();
                List<ProvinceDB> provinceDBs = DataSupport.where("id = ?", String.valueOf(provinceId)).find(ProvinceDB.class);
                int cityCode = provinceDBs.get(0).getId();
                String name = provinceDBs.get(0).getName();
                Bundle bundle = new Bundle();
                bundle.putInt("id",cityCode);
                bundle.putString("name",name);
                fragment_city.setArguments(bundle);
                fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame_change, fragment_city);
                transaction.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
