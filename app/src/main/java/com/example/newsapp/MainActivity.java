package com.example.newsapp;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.makeramen.roundedimageview.RoundedImageView;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private String result;
    private List<NewsBean.ResultBean> newsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NewsRecyclerViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.news_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
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
                                mAdapter.notifyDataSetChanged();
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
            Glide.with(MainActivity.this).load(news.getImage()).into(holder.image);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(MainActivity.this, "新闻"+position, Toast.LENGTH_SHORT).show();
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
}


