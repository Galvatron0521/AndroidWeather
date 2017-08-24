package com.zhiyuan3g.androidweather.fragment;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyuan3g.androidweather.R;
import com.zhiyuan3g.androidweather.adapter.ProvinceAdapter;
import com.zhiyuan3g.androidweather.db.ProvinceDB;

import org.litepal.crud.DataSupport;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2017/8/22.
 */

public class fragment_province extends Fragment {
    @BindView(R.id.tv_province)
    TextView tvProvince;
    @BindView(R.id.province_toolbar)
    Toolbar provinceToolbar;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    private ProvinceAdapter provinceAdapter;
    private List<ProvinceDB> all;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_province, container, false);
        ButterKnife.bind(this, view);
        initToolBar();
        initView();
        return view;
    }

    private void initView() {
        all = DataSupport.findAll(ProvinceDB.class);
        provinceAdapter = new ProvinceAdapter(getActivity(), all);
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(manager);
        recycler.setAdapter(provinceAdapter);
        provinceAdapter.setOnItemClick(new ProvinceAdapter.onItemClick() {
            @Override
            public void onItemClick(int position) {
                fragment_city fragment_city = new fragment_city();
                Bundle bundle = new Bundle();
                bundle.putInt("id",all.get(position).getId());
                bundle.putString("name",all.get(position).getName());
                fragment_city.setArguments(bundle);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.frame_change,fragment_city);
                transaction.commit();
            }
        });
    }

    private void initToolBar() {
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).setSupportActionBar(provinceToolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }
}
