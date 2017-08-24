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

public class fragment_city extends Fragment {
    @BindView(R.id.tv_province)
    TextView tvProvince;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.province_toolbar)
    Toolbar Toolbar;
    private int id;
    private FragmentManager fragmentManager;
    private List<CityDB> cityDBList;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_province, container, false);
        ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        id = bundle.getInt("id");
        String name = bundle.getString("name");
        initToolBar(name);
        initFindDB();
        return view;
    }

    private void initFindDB() {
        final List<CityDB> cityDBs = DataSupport.where("provinceCode = ?", String.valueOf(id)).find(CityDB.class);
        if (cityDBs.isEmpty()) {
            initHttp();
        } else {
            CityAdapter cityAdapter = new CityAdapter(getActivity(), cityDBs);
            LinearLayoutManager manager = new LinearLayoutManager(getActivity());
            recycler.setLayoutManager(manager);
            recycler.setAdapter(cityAdapter);
            cityAdapter.setOnItemClick(new CityAdapter.onItemClick() {
                @Override
                public void onItemClick(int position) {
                    fragment_country fragment_country = new fragment_country();
                    Bundle bundle = new Bundle();
                    bundle.putInt("id", cityDBs.get(position).getCityCode());
                    bundle.putInt("provinceCode", cityDBs.get(position).getProvinceCode());
                    bundle.putString("cityName", cityDBs.get(position).getName());
                    fragment_country.setArguments(bundle);

                    FragmentManager fragmentManager = getFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.frame_change, fragment_country);
                    transaction.commit();
                }
            });

        }
    }

    private void initHttp() {
        progressDialog.setTitle("提示");
        progressDialog.setMessage("请稍等...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        OkHttpUtils.sendRequestGETMethod(getActivity(), ContractUtils.URL_CITY + id, new OkHttpCallBack() {
            @Override
            public void Success(String result) {
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                        CityDB cityDB = new CityDB();
                        cityDB.setCityCode(jsonObject.getInt("id"));
                        cityDB.setName(jsonObject.getString("name"));
                        cityDB.setProvinceCode(id);
                        cityDB.save();
                        cityDBList.add(cityDB);
                    }
                    CityAdapter cityAdapter = new CityAdapter(getActivity(), cityDBList);
                    LinearLayoutManager manager = new LinearLayoutManager(getActivity());
                    recycler.setLayoutManager(manager);
                    recycler.setAdapter(cityAdapter);
                    progressDialog.dismiss();
                    cityAdapter.setOnItemClick(new CityAdapter.onItemClick() {
                        @Override
                        public void onItemClick(int position) {
                            fragment_country fragment_country = new fragment_country();
                            Bundle bundle = new Bundle();
                            bundle.putInt("id", cityDBList.get(position).getCityCode());
                            bundle.putInt("provinceCode", cityDBList.get(position).getProvinceCode());
                            bundle.putString("cityName", cityDBList.get(position).getName());
                            fragment_country.setArguments(bundle);

                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction transaction = fragmentManager.beginTransaction();
                            transaction.replace(R.id.frame_change, fragment_country);
                            transaction.commit();
                        }
                    });

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
        cityDBList = new ArrayList<>();
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
                fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame_change, new fragment_province());
                transaction.commit();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
