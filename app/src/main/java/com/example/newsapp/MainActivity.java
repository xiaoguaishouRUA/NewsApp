package com.example.newsapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Fragment;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private TextView textView_news;
    private TextView textView_my;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView_news = findViewById(R.id.news_text_view);
        textView_my = findViewById(R.id.my_text_view);
        textView_news.setOnClickListener(l);
        textView_my.setOnClickListener(l);
    }

    View.OnClickListener l = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            FragmentManager fm = getFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment f = null;
            switch (v.getId()){
                case R.id.news_text_view:
                    f = new NewsFragment();
                    Toast.makeText(MainActivity.this, "NewsFragment", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.my_text_view:
                    f = new MyFragment();
                    Toast.makeText(MainActivity.this, "MyFragment", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;

            }
            ft.replace(R.id.fragment,f);
            ft.commit();
        }
    };
}