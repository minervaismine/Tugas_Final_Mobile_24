package com.example.tugas_final_mobile.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tugas_final_mobile.R;
import com.example.tugas_final_mobile.database.DatabaseHelper;
import com.example.tugas_final_mobile.response.MenuResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {

    private Context context;
    private List<MenuResponse> menuResponseList;
    private DatabaseHelper databaseHelper;

    public FavoriteAdapter(Context context, List<MenuResponse> menuResponseList, DatabaseHelper databaseHelper) {
        this.context = context;
        this.menuResponseList = menuResponseList;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuResponse menuResponse = menuResponseList.get(position);

        holder.tvTitle.setText(menuResponse.getTitle());
        holder.tvCalories.setText(menuResponse.getNutrition().getCalories());
        holder.tvProtein.setText(menuResponse.getNutrition().getProtein());
        holder.tvFat.setText(menuResponse.getNutrition().getFat());
        holder.tvCarbs.setText(menuResponse.getNutrition().getCarbs());

        String imageUrl = menuResponse.getImageUrl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.get().load(imageUrl).into(holder.ivMenuPicture);
        } else {
            // Handle empty or null imageUrl, for example set a placeholder image
            holder.ivMenuPicture.setImageResource(R.drawable.contoh);
        }

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove item from list and database
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    MenuResponse removedItem = menuResponseList.get(position);
                    databaseHelper.deleteItem(removedItem.getId()); // Menghapus item dari database menggunakan ID
                    menuResponseList.remove(position);
                    notifyItemRemoved(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuResponseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivMenuPicture;
        TextView tvTitle, tvCalories, tvProtein, tvFat, tvCarbs;
        ImageView btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivMenuPicture = itemView.findViewById(R.id.iv_menu_picture);
            tvTitle = itemView.findViewById(R.id.tv_menu_title);
            tvCalories = itemView.findViewById(R.id.calories_number);
            tvProtein = itemView.findViewById(R.id.tv_menu_protein);
            tvFat = itemView.findViewById(R.id.tv_menu_fat);
            tvCarbs = itemView.findViewById(R.id.tv_menu_carbs);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
