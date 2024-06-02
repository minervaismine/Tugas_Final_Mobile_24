package com.example.tugas_final_mobile.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tugas_final_mobile.API.ApiConfig;
import com.example.tugas_final_mobile.API.ApiService;
import com.example.tugas_final_mobile.adapter.FoodAdapter;
import com.example.tugas_final_mobile.response.MenuResponse;
import com.example.tugas_final_mobile.response.NutritionResponse;
import com.example.tugas_final_mobile.R;
import com.example.tugas_final_mobile.activity.MainActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private ApiService apiService;
    private FoodAdapter foodAdapter;
    private ArrayList<MenuResponse> menuList;
    private static final String API_KEY = "185170fd26744eb5b5049bb6fb72296d";
    private static final List<String> recipeIds = new ArrayList<>();
    private Map<String, MenuResponse> menuMap = new HashMap<>();

    static {
        for (int i = 1; i <= 5; i++) {
            recipeIds.add(String.valueOf(i));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView rv_home = view.findViewById(R.id.rv_home);
        rv_home.setHasFixedSize(true);

        apiService = ApiConfig.getApiService();
        menuList = new ArrayList<>();

        foodAdapter = new FoodAdapter(menuList, recipeIds);
        rv_home.setLayoutManager(new LinearLayoutManager(getContext()));
        rv_home.setAdapter(foodAdapter);

        for (String id : recipeIds) {
            getMenuInformation(id);
        }
    }

    private void getMenuInformation(final String recipeId) {
        Call<MenuResponse> callMenu = apiService.getMenu(recipeId, API_KEY);
        callMenu.enqueue(new Callback<MenuResponse>() {
            @Override
            public void onResponse(Call<MenuResponse> call, Response<MenuResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    final MenuResponse menuResponse = response.body();
                    Call<NutritionResponse> callNutrition = apiService.getNutrition(recipeId, API_KEY);
                    callNutrition.enqueue(new Callback<NutritionResponse>() {
                        @Override
                        public void onResponse(Call<NutritionResponse> call, Response<NutritionResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                menuResponse.setNutrition(response.body());
                                menuMap.put(recipeId, menuResponse);
                                checkAndUpdateMenuList();
                            } else {
                                Log.e("API_ERROR", "Failed to get nutrition information: " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<NutritionResponse> call, Throwable t) {
                            Log.e("API_ERROR", "Failed to get nutrition information: " + t.getMessage());
                        }
                    });
                } else {
                    Log.e("API_ERROR", "Failed to get menu information: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MenuResponse> call, Throwable t) {
                Log.e("API_ERROR", "Failed to get menu information: " + t.getMessage());
            }
        });
    }

    private void checkAndUpdateMenuList() {
        if (menuMap.size() == recipeIds.size()) {
            menuList.clear();
            for (String id : recipeIds) {
                menuList.add(menuMap.get(id));
            }
            foodAdapter.notifyDataSetChanged();
            ((MainActivity) getActivity()).setMenuList(menuList);
        }
    }
}
