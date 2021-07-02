package com.example.newsapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {

    private String[] newsTypeList = {"7","8","10","12","13","31","17","18","33"};
    private String[] titleLsit = {"国内","国际","娱乐","体育","科技","游戏","健康","旅游","动漫"};
    private List<NewsFragment> fragmentList = new ArrayList<NewsFragment>();
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home,container,false);
        tabLayout = view.findViewById(R.id.news_tab_layout);
        viewPager = view.findViewById(R.id.news_view_pager);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        for (String newsType : newsTypeList) {
            fragmentList.add(new NewsFragment(newsType));
        }
        FragmentPagerAdapter adapter = new NewsViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setOffscreenPageLimit(titleLsit.length);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    class NewsViewPagerAdapter extends FragmentPagerAdapter {

        public NewsViewPagerAdapter(FragmentManager fm) {
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

        @Override
        public CharSequence getPageTitle(int position) {
            return titleLsit[position];
        }
    }
}