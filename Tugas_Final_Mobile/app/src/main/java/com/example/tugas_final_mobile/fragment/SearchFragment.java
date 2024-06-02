package com.example.tugas_final_mobile.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tugas_final_mobile.R;
import com.example.tugas_final_mobile.adapter.SearchAdapter;
import com.example.tugas_final_mobile.response.MenuResponse;

import java.util.ArrayList;
import java.util.List;

public class SearchFragment extends Fragment {

    private SearchView searchView;
    private RecyclerView recyclerView;
    private TextView tv_noData;
    private SearchAdapter searchAdapter;
    private List<MenuResponse> fullMenuList = new ArrayList<>(); // Daftar lengkap dari semua data

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.rv_searchView);
        tv_noData = view.findViewById(R.id.no_data_text);

        // Inisialisasi adapter dengan daftar kosong
        searchAdapter = new SearchAdapter(fullMenuList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(searchAdapter);

        // Mendengarkan perubahan pada search view
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchAdapter.getFilter().filter(newText); // Memfilter daftar berdasarkan teks yang dimasukkan pengguna
                return true;
            }
        });

        return view;
    }

    // Metode ini digunakan untuk mengatur data menu yang akan ditampilkan dalam SearchFragment
    public void setMenuList(List<MenuResponse> menuList) {
        this.fullMenuList.clear();
        this.fullMenuList.addAll(menuList);
        // Ketika data berubah, kita juga harus memperbarui daftar yang ditampilkan
        searchAdapter.notifyDataSetChanged();
    }
}
