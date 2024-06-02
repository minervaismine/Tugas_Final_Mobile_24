package com.example.tugas_final_mobile.API;

import com.example.tugas_final_mobile.response.MenuResponse;
import com.example.tugas_final_mobile.response.NutritionResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("recipes/{id}/nutritionWidget.json")
    Call<NutritionResponse> getNutrition(@Path("id") String id, @Query("apiKey") String apiKey);

    @GET("recipes/{id}/information")
    Call<MenuResponse> getMenu(@Path("id") String id, @Query("apiKey") String apiKey);
}
