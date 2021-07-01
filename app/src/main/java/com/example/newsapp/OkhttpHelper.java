package com.example.newsapp;



import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class OkhttpHelper {
    public static void getRequest(String url, Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(url).get().build();
        client.newCall(request).enqueue(callback);
    }
    public static void postRequest(String url, FormBody.Builder builder,Callback callback){
        OkHttpClient client = new OkHttpClient();
        FormBody build = builder.build();
        Request request = new Request.Builder().url(url).post(build).build();
        client.newCall(request).enqueue(callback);
    }
}
