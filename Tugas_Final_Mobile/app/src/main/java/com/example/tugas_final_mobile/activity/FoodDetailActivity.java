package com.example.tugas_final_mobile.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tugas_final_mobile.API.ApiConfig;
import com.example.tugas_final_mobile.API.ApiService;
import com.example.tugas_final_mobile.R;
import com.example.tugas_final_mobile.database.DatabaseHelper;
import com.example.tugas_final_mobile.response.MenuResponse;
import com.example.tugas_final_mobile.response.NutritionResponse;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodDetailActivity extends AppCompatActivity {

    private ApiService apiService;
    private static final String API_KEY = "7e591a26a80d434d9c9ff016ae066295";
    private static final String TAG = "FoodDetailActivity";
    private TextView menu_title, tv_menu_calories, tv_menu_protein, tv_menu_fat, tv_menu_carbs;
    private ImageView iv_menu_picture;
    private Button btn_add_to_favorite, btn_delete_from_favorite;
    private DatabaseHelper databaseHelper;
    private String recipeId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        menu_title = findViewById(R.id.tv_menu_title);
        tv_menu_calories = findViewById(R.id.tv_menu_calories);
        tv_menu_protein = findViewById(R.id.tv_menu_protein);
        tv_menu_fat = findViewById(R.id.tv_menu_fat);
        tv_menu_carbs = findViewById(R.id.tv_menu_carbs);
        iv_menu_picture = findViewById(R.id.iv_menu_picture);
        btn_add_to_favorite = findViewById(R.id.btn_add_to_favorite);
        btn_delete_from_favorite = findViewById(R.id.btn_delete_from_favorite);

        apiService = ApiConfig.getApiService();
        databaseHelper = new DatabaseHelper(this);

        recipeId = getIntent().getStringExtra("id");

        if (recipeId != null && !recipeId.isEmpty()) {
            fetchFoodDetails(recipeId);
            checkIfInFavorites(recipeId);
        } else {
            Log.e(TAG, "No recipe id found in intent");
        }

        btn_add_to_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = menu_title.getText().toString();
                String imageUrl = ""; // Isi dengan URL gambar jika ada
                String calories = tv_menu_calories.getText().toString();
                String protein = tv_menu_protein.getText().toString();
                String fat = tv_menu_fat.getText().toString();
                String carbs = tv_menu_carbs.getText().toString();

                addToFavorites(recipeId, title, imageUrl, calories, protein, fat, carbs);
            }
        });

        btn_delete_from_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFromFavorites(recipeId);
            }
        });
    }

    private void fetchFoodDetails(String id) {
        Call<MenuResponse> menuCall = apiService.getMenu(id, API_KEY);
        menuCall.enqueue(new Callback<MenuResponse>() {
            @Override
            public void onResponse(Call<MenuResponse> call, Response<MenuResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    MenuResponse menuResponse = response.body();

                    Call<NutritionResponse> nutritionCall = apiService.getNutrition(id, API_KEY);
                    nutritionCall.enqueue(new Callback<NutritionResponse>() {
                        @Override
                        public void onResponse(Call<NutritionResponse> call, Response<NutritionResponse> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                NutritionResponse nutritionResponse = response.body();
                                updateUI(menuResponse, nutritionResponse);
                            } else {
                                Log.e(TAG, "Failed to get nutrition information: " + response.message());
                            }
                        }

                        @Override
                        public void onFailure(Call<NutritionResponse> call, Throwable t) {
                            Log.e(TAG, "Failed to get nutrition information: " + t.getMessage());
                        }
                    });
                } else {
                    Log.e(TAG, "Failed to get menu information: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MenuResponse> call, Throwable t) {
                Log.e(TAG, "Failed to get menu information: " + t.getMessage());
            }
        });
    }

    private void updateUI(MenuResponse menuResponse, NutritionResponse nutritionResponse) {
        menu_title.setText(menuResponse.getTitle());
        Picasso.get().load(menuResponse.getImageUrl()).into(iv_menu_picture);

        tv_menu_calories.setText(String.valueOf(nutritionResponse.getCalories()));
        tv_menu_protein.setText(String.valueOf(nutritionResponse.getProtein()));
        tv_menu_fat.setText(String.valueOf(nutritionResponse.getFat()));
        tv_menu_carbs.setText(String.valueOf(nutritionResponse.getCarbs()));
    }

    private void checkIfInFavorites(String id_menu) {
        Cursor cursor = databaseHelper.getReadableDatabase()
                .rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_NAME + " WHERE " + DatabaseHelper.COLUMN_ID_MENU + " = ?", new String[]{id_menu});
        if (cursor.getCount() > 0)
        {
            // Jika id_menu ada di tabel favorit, tampilkan tombol delete
            btn_add_to_favorite.setVisibility(View.GONE);
            btn_delete_from_favorite.setVisibility(View.VISIBLE);
        } else {
            // Jika id_menu tidak ada di tabel favorit, tampilkan tombol add
            btn_add_to_favorite.setVisibility(View.VISIBLE);
            btn_delete_from_favorite.setVisibility(View.GONE);
        }
        cursor.close();
    }

    private void addToFavorites(String id_menu, String title, String imageUrl, String calories, String protein, String fat, String carbs) {
        long newRowId = databaseHelper.addFavorite(title, imageUrl, calories, protein, fat, carbs);
        if (newRowId != -1) {
            // Insert berhasil
            btn_add_to_favorite.setVisibility(View.GONE);
            btn_delete_from_favorite.setVisibility(View.VISIBLE);
            Log.d(TAG, "Added to favorites: " + id_menu);
        } else {
            // Insert gagal
            Log.e(TAG, "Failed to add to favorites: " + id_menu);
        }
    }

    private void deleteFromFavorites(String id_menu) {
        int rowsDeleted = databaseHelper.getWritableDatabase()
                .delete(DatabaseHelper.TABLE_NAME, DatabaseHelper.COLUMN_ID_MENU + " = ?", new String[]{id_menu});
        if (rowsDeleted > 0) {
            // Delete berhasil
            btn_add_to_favorite.setVisibility(View.VISIBLE);
            btn_delete_from_favorite.setVisibility(View.GONE);
            Log.d(TAG, "Deleted from favorites: " + id_menu);
        } else {
            // Delete gagal
            Log.e(TAG, "Failed to delete from favorites: " + id_menu);
        }
    }
}

