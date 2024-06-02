package com.example.tugas_final_mobile.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
    private ProgressBar progressBar;
    private SearchAdapter searchAdapter;
    private List<MenuResponse> fullMenuList;
    private List<MenuResponse> filteredMenuList;

    public SearchFragment(List<MenuResponse> menuList) {
        if (menuList == null) {
            this.fullMenuList = new ArrayList<>();
        } else {
            this.fullMenuList = new ArrayList<>(menuList);
        }
        this.filteredMenuList = new ArrayList<>(this.fullMenuList);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        searchView = view.findViewById(R.id.searchView);
        recyclerView = view.findViewById(R.id.rv_searchView);
        tv_noData = view.findViewById(R.id.no_data_text);
        progressBar = view.findViewById(R.id.pb);

        recyclerView.setVisibility(View.GONE); // Sembunyikan RecyclerView saat pertama kali fragment dibuat
        progressBar.setVisibility(View.GONE); // Sembunyikan ProgressBar saat pertama kali fragment dibuat

        searchAdapter = new SearchAdapter(filteredMenuList);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(searchAdapter);

        setupSearchView();

        return view;
    }


    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });
    }

    private void filter(String query) {
        progressBar.setVisibility(View.VISIBLE); // Tampilkan ProgressBar saat proses pencarian dimulai
        recyclerView.setVisibility(View.VISIBLE); // Tampilkan RecyclerView saat pencarian dimulai
        filteredMenuList.clear();
        for (MenuResponse menu : fullMenuList) {
            if (menu.getTitle().toLowerCase().contains(query.toLowerCase())) {
                filteredMenuList.add(menu);
            }
        }
        searchAdapter.getFilter().filter(query); // Menyaring daftar menggunakan kriteria pencarian
        tv_noData.setVisibility(filteredMenuList.isEmpty() ? View.VISIBLE : View.GONE);

        // Sembunyikan ProgressBar setelah proses pencarian selesai
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressBar.setVisibility(View.GONE);
            }
        }, 500); // Sesuaikan dengan kebutuhan Anda, misalnya 500ms
    }
}
