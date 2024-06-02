package com.example.tugas_final_mobile.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.tugas_final_mobile.R;
import com.example.tugas_final_mobile.fragment.FavoriteFragment;
import com.example.tugas_final_mobile.fragment.HomeFragment;
import com.example.tugas_final_mobile.fragment.SearchFragment;
import com.example.tugas_final_mobile.response.MenuResponse;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<MenuResponse> menuList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageView iv_home = findViewById(R.id.home);
        ImageView iv_favorite = findViewById(R.id.favourite);
        ImageView iv_search = findViewById(R.id.search);

        FragmentManager fragmentManager = getSupportFragmentManager();

        HomeFragment homeFragment = new HomeFragment();
        fragmentManager.beginTransaction().add(R.id.frame_container, homeFragment).commit();

        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomeFragment homeFragment = new HomeFragment();
                fragmentManager.beginTransaction().replace(R.id.frame_container, homeFragment).addToBackStack(null).commit();
            }
        });

        iv_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FavoriteFragment favoriteFragment = new FavoriteFragment();
                fragmentManager.beginTransaction().replace(R.id.frame_container, favoriteFragment).addToBackStack(null).commit();
            }
        });

        iv_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (menuList != null) {
                    SearchFragment searchFragment = new SearchFragment(menuList);
                    fragmentManager.beginTransaction().replace(R.id.frame_container, searchFragment).addToBackStack(null).commit();
                }
            }
        });
    }

    public void setMenuList(ArrayList<MenuResponse> menuList) {
        this.menuList = menuList;
    }
}
