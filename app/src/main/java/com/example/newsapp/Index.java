package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.newsapp.gson.NewsBean;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.makeramen.roundedimageview.RoundedImageView;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class Index extends AppCompatActivity {
    private String result;
    private List<NewsBean.ResultBean> newsList = new ArrayList<>();
    private RecyclerView recyclerView;


    private DrawerLayout mDrawerLayout;
    public TextView tvName;
    //接收传递过来的用户名
    private String main_name;

    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        //接收
        main_name = getIntent().getStringExtra("username");
        //同步用户名
        tvName = (TextView) findViewById(R.id.nav_header_name);
        tvName.setText(new String(String.format(getResources().getString(R.string.nav_header_name),main_name)));

        //主布局初始化
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.daohang);
        }

        //侧滑栏内事件点击
        final NavigationView navView =(NavigationView) findViewById(R.id.nav_view);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_myinfo:
                        mDrawerLayout.closeDrawers();
                        Intent info = new Intent(Index.this,Information.class);
                        info.putExtra("info_user",main_name);
                        startActivity(info);
                        break;
                    case R.id.nav_mycollection:
                        mDrawerLayout.closeDrawers();
//                        Intent coll = new Intent(Index.this,Collection.class);
//                        startActivity(coll);
                        break;
                    case R.id.nav_news:
                        new AlertDialog.Builder(Index.this).setTitle("通知").setMessage("当前已是最新版本!")
                                .setPositiveButton("确定",null).show();
                        break;
                    case R.id.nav_back:
                        new AlertDialog.Builder(Index.this).setTitle("提示").setMessage("确定退出应用吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                }).setNeutralButton("取消",null).show();
                        break;
                    default:
                        mDrawerLayout.closeDrawers();
                }
                return true;
            }
        });

        fragmentList.add(new HomeFragment());
        fragmentList.add(new PictureFragment());
        ViewPager viewPager = findViewById(R.id.content_view_pager);
        viewPager.setOffscreenPageLimit(2);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        FragmentPagerAdapter adapter = new MainAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_home:viewPager.setCurrentItem(0);break;
                    case R.id.nav_picture:viewPager.setCurrentItem(1);break;
                    default:break;
                }
                return false;
            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                bottomNavigationView.getMenu().getItem(position).setChecked(true);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    class MainAdapter extends FragmentPagerAdapter {

        public MainAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }

    }


    //加载菜单
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return  true;
    }
    //顶部双开关
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.setting:
                Toast.makeText(this, "You clicked Setting", Toast.LENGTH_SHORT).show();
                break;
            default:
        }
        return true;
    }
}


