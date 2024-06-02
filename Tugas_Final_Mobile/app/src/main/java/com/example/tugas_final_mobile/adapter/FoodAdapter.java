package com.example.tugas_final_mobile.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tugas_final_mobile.activity.FoodDetailActivity;
import com.example.tugas_final_mobile.response.MenuResponse;
import com.example.tugas_final_mobile.response.NutritionResponse;
import com.example.tugas_final_mobile.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {

    private ArrayList<MenuResponse> menuList;
    private List<String> recipeIds;

    public FoodAdapter(ArrayList<MenuResponse> menuList, List<String> recipeIds) {
        this.menuList = menuList;
        this.recipeIds = recipeIds;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Menu Response
        MenuResponse menuResponse = menuList.get(position);
        holder.tv_menu_title.setText(menuResponse.getTitle());
        Picasso.get().load(menuResponse.getImageUrl()).into(holder.iv_menu_picture);

        final String id = recipeIds.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.itemView.getContext(), FoodDetailActivity.class);
                intent.putExtra("id", id);
                holder.itemView.getContext().startActivity(intent);
            }
        });

        // Nutrition Response
        NutritionResponse nutrition = menuResponse.getNutrition();
        if (nutrition != null) {
            holder.calories_number.setText(String.valueOf(nutrition.getCalories()));
            holder.tv_menu_protein.setText(String.valueOf(nutrition.getProtein()));
            holder.tv_menu_fat.setText(String.valueOf(nutrition.getFat()));
            holder.tv_menu_carbs.setText(String.valueOf(nutrition.getCarbs()));
        } else {
            // Handle case when nutrition information is not available
            holder.calories_number.setText("-");
            holder.tv_menu_protein.setText("-");
            holder.tv_menu_fat.setText("-");
            holder.tv_menu_carbs.setText("-");
        }
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_menu_picture;
        TextView tv_menu_title, calories_number, tv_menu_protein, tv_menu_fat, tv_menu_carbs;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_menu_picture = itemView.findViewById(R.id.iv_menu_picture);
            tv_menu_title = itemView.findViewById(R.id.tv_menu_title);
            calories_number = itemView.findViewById(R.id.calories_number);
            tv_menu_protein = itemView.findViewById(R.id.tv_menu_protein);
            tv_menu_fat = itemView.findViewById(R.id.tv_menu_fat);
            tv_menu_carbs = itemView.findViewById(R.id.tv_menu_carbs);
        }
    }
}
