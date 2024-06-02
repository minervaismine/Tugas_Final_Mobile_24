package com.example.tugas_final_mobile.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tugas_final_mobile.adapter.FavoriteAdapter;
import com.example.tugas_final_mobile.database.DatabaseHelper;
import com.example.tugas_final_mobile.R;
import com.example.tugas_final_mobile.response.MenuResponse;

import java.util.List;

public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavoriteAdapter favoriteAdapter;
    private DatabaseHelper databaseHelper;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseHelper = new DatabaseHelper(requireContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        recyclerView = view.findViewById(R.id.rv_favorite);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        loadFavoriteData();
        return view;
    }

    private void loadFavoriteData() {
        List<MenuResponse> favoriteItems = databaseHelper.getAllFavoriteItems();
        favoriteAdapter = new FavoriteAdapter(requireContext(), favoriteItems, databaseHelper);
        recyclerView.setAdapter(favoriteAdapter);
    }
}
