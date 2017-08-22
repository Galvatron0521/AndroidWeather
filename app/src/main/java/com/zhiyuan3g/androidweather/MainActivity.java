package com.zhiyuan3g.androidweather;

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

import com.zhiyuan3g.androidweather.fragment.fragment_province;

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
        initView();
        initToolBar();
        drawerLayout.addDrawerListener(this);
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
        return true;
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
}
