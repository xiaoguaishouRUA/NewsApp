package com.example.newsapp;


import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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


public class NewsFragment extends Fragment {

    private String newsType;
    private RecyclerView recyclerView;
    private String result;
    private List<NewsBean.ResultBean> newsList = new ArrayList<>();
    private NewsRecyclerViewAdapter mAdapter;

    public NewsFragment(String newsType) {
        this.newsType = newsType;
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new NewsRecyclerViewAdapter(newsList);
        recyclerView.setAdapter(mAdapter);
        initRequest();
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news,container,false);
        recyclerView = view.findViewById(R.id.news_recycler_view);
        return view;
    }

    private void initRequest() {
        String url = "http://api.tianapi.com/allnews/index";
        FormBody.Builder builder = new FormBody.Builder();
        builder.add("key","6ea03d6b819288ca2215d6f00744ef23");
        builder.add("num","10");
        builder.add("col",newsType);
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
                        newsList = newsBean.getNewslist();
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter = new NewsRecyclerViewAdapter(newsList);
                                recyclerView.setAdapter(mAdapter);
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
            holder.description.setText(news.getDescription());
            Glide.with(getActivity()).load(news.getPicUrl()).into(holder.image);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),DetailActivity.class);
                    intent.putExtra("url",dataList.get(position).getUrl());
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
            private TextView description;
            private RoundedImageView image;
            public ViewHolder(View itemView) {
                super(itemView);
                title = itemView.findViewById(R.id.news_title);
                description = itemView.findViewById(R.id.news_description);
                image = itemView.findViewById(R.id.news_image);
            }
        }
    }
}
