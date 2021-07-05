package com.example.newsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
    private NewsRecyclerViewAdapter mAdapter;

    private DrawerLayout mDrawerLayout;
    public TextView tvName;
    //接收传递过来的用户名
    private String main_name;

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

        recyclerView = findViewById(R.id.news_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new NewsRecyclerViewAdapter(newsList);
        recyclerView.setAdapter(mAdapter);
        initRequest();
    }

    private void initRequest() {
        String url = "https://api.apiopen.top/getWangYiNews";
        FormBody.Builder builder = new FormBody.Builder();
//        builder.add("page","1");
//        builder.add("count","10");
        OkhttpHelper.postRequest(url,builder,new Callback(){

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                new Thread(){
                    @Override
                    public void run() {
                        try {
                            result = response.body().string();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        System.out.println(result);
                        Type type = new TypeToken<NewsBean>(){}.getType();
                        NewsBean newsBean = new Gson().fromJson(result, type);
                        newsList = newsBean.getResult();
                        System.out.println(newsList);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter = new NewsRecyclerViewAdapter(newsList);
                                recyclerView.setAdapter(mAdapter);
//                                mAdapter.notifyDataSetChanged();
                            }
                        });

                    }
                }.start();
            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }
        });
    }

    class NewsRecyclerViewAdapter extends RecyclerView.Adapter<NewsRecyclerViewAdapter.ViewHolder>{

        private List<NewsBean.ResultBean> dataList;

        NewsRecyclerViewAdapter(List<NewsBean.ResultBean> dataList){
            this.dataList = dataList;
        }

        @Override
        public NewsRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news,parent,false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(NewsRecyclerViewAdapter.ViewHolder holder, int position) {
            NewsBean.ResultBean news = dataList.get(position);
            holder.title.setText(news.getTitle());
            holder.passtime.setText(news.getPasstime());
            Glide.with(Index.this).load(news.getImage()).into(holder.image);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Index.this,DetailActivity.class);
                    intent.putExtra("url",dataList.get(position).getPath());
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return dataList == null ? 0 : dataList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            private TextView title;
            private TextView passtime;
            private RoundedImageView image;
            public ViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.news_title);
                passtime = itemView.findViewById(R.id.news_passtime);
                image = itemView.findViewById(R.id.news_image);
            }
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


