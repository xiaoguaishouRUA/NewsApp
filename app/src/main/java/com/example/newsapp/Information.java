package com.example.newsapp;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.newsapp.gson.User;

import org.litepal.crud.DataSupport;

import java.util.List;

public class Information extends AppCompatActivity {

    private DrawerLayout infoDrawer;
    final String[] gender = new String[]{"男","女"};
    final String[] city = new String[]{
            "北京","天津","河北","山西","内蒙古","辽宁","吉林","黑龙江",
            "上海","江苏","浙江省","安徽","福建","江西","山东","河南",
            "湖北","湖南","广东","广西","海南","重庆","四川","贵州",
            "云南","西藏","陕西","甘肃","青海","宁夏","新疆","台湾","香港","澳门"
    };
    private String info_user;
    private TextView info_name;
    private TextView info_gender;
    private TextView info_mail;
    private TextView info_location;
    private TextView info_motto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        Toolbar infoToolbar = (Toolbar)findViewById(R.id.info_toolbar);
        setSupportActionBar(infoToolbar);
        infoDrawer = (DrawerLayout) findViewById(R.id.info_drawer_layout);
        ActionBar infoActionBar = getSupportActionBar();
        if (infoActionBar!=null){
            infoActionBar.setDisplayHomeAsUpEnabled(true);
        }

        //初始化页面信息
        info_user = getIntent().getStringExtra("info_user");
        info_name = (TextView) findViewById(R.id.info_name);
        info_gender = (TextView) findViewById(R.id.info_gender);
        info_mail = (TextView) findViewById(R.id.info_mail);
        info_location = (TextView) findViewById(R.id.info_location);
        info_motto = (TextView) findViewById(R.id.info_motto);
        List<User> users = DataSupport.findAll(User.class);
        for (User user:users){
            if (info_user.equals(user.getName())){
                info_name.setText(user.getName());
                info_gender.setText(user.getGender());
                info_mail.setText(user.getMail());
                info_location.setText(user.getLocation());
                info_motto.setText(user.getMotto());
            }
        }

        //设置性别
        info_gender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Information.this).setTitle("请选择你的性别")
                        .setItems(gender, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                info_gender.setText(gender[which].toString());
                                User user_gender = new User();
                                user_gender.setGender(gender[which].toString());
                                user_gender.updateAll("name = ?",info_user);
                            }
                        }).show();
            }
        });

        //设置省份
        info_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Information.this).setTitle("请选择你的省份")
                        .setItems(city, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                info_location.setText(city[which].toString());
                                User user_location = new User();
                                user_location.setLocation(city[which].toString());
                                user_location.updateAll("name = ?",info_user);
                            }
                        }).show();
            }
        });

        //设置个性签名
        info_motto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent motto = new Intent(Information.this,Motto.class);
                motto.putExtra("alter_name",info_name.getText().toString());
                motto.putExtra("motto",info_motto.getText().toString());
                startActivity(motto);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        User re_user = new User();
        List<User> re_motto = DataSupport.select("name","motto").find(User.class);
        for (User re_mottos:re_motto){
            if (info_user.equals(re_mottos.getName())){
                info_motto.setText(re_mottos.getMotto());
            }
        }
    }
    //返回
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==android.R.id.home){
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
