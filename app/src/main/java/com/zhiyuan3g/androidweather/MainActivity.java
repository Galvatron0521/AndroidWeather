package com.zhiyuan3g.androidweather;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zhiyuan3g.androidweather.base.ContractUtils;
import com.zhiyuan3g.androidweather.db.ProvinceDB;
import com.zhiyuan3g.androidweather.entity.ProvinceEntity;
import com.zhiyuan3g.androidweather.fragment.fragment_province;
import com.zhiyuan3g.androidweather.utils.OkHttpCallBack;
import com.zhiyuan3g.androidweather.utils.OkHttpUtils;

import org.litepal.tablemanager.Connector;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements DrawerLayout.DrawerListener {

    @BindView(R.id.main_title)
    TextView mainTitle;
    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;
    @BindView(R.id.drawerLayout)
    DrawerLayout drawerLayout;

    private FragmentManager fragmentManager;
    private boolean isOk = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        Connector.getDatabase();
        initView();
        initToolBar();
        drawerLayout.addDrawerListener(this);
        initSp();
    }

    private void initSp() {
        SharedPreferences sharedPreferences = getSharedPreferences("Cool", MODE_PRIVATE);
        boolean spIsOk = sharedPreferences.getBoolean("spIsOK", false);
        if (!spIsOk) {
            initHttp();
        }
    }

    private void initHttp() {
        OkHttpUtils.sendRequestGETMethod(this, ContractUtils.URL_PROVINCE, new OkHttpCallBack() {
            @Override
            public void Success(String result) {
                System.out.println(result);
                Gson gson = new Gson();
                List<ProvinceEntity> ProvinceList = gson.fromJson(result, new TypeToken<List<ProvinceEntity>>() {
                }.getType());
                for (ProvinceEntity entity : ProvinceList) {
                    ProvinceDB provinceDB = new ProvinceDB();
                    provinceDB.setId(entity.getId());
                    provinceDB.setName(entity.getName());
                    provinceDB.save();
                }
                SharedPreferences sharedPreferences = getSharedPreferences("Cool", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("spIsOk", true);
                editor.apply();
            }

            @Override
            public void Failure(String failure) {

            }
        });
    }


    private void initView() {
        fragmentManager = getSupportFragmentManager();
    }

    private void initToolBar() {
        setSupportActionBar(mainToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.home);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {
        isOk = true;
    }

    @Override
    public void onDrawerStateChanged(int newState) {
        if (newState > 0 && isOk == true) {
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.replace(R.id.frame_change, new fragment_province());
            transaction.commit();
            isOk = false;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
