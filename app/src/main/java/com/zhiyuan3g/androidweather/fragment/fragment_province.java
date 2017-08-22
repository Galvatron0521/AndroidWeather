package com.zhiyuan3g.androidweather.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zhiyuan3g.androidweather.R;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_province, container, false);
        ButterKnife.bind(this, view);
        initToolBar();
        return view;
    }

    private void initToolBar() {
        ((AppCompatActivity) getActivity()).setSupportActionBar(provinceToolbar);
        ActionBar actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }
}
