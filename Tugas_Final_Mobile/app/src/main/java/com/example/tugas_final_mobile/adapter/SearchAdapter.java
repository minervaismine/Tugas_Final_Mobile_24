package com.example.tugas_final_mobile.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tugas_final_mobile.response.MenuResponse;
import com.example.tugas_final_mobile.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> implements Filterable {

    private List<MenuResponse> fullMenuList; // Daftar lengkap dari semua data
    private List<MenuResponse> filteredMenuList; // Daftar yang difilter berdasarkan kriteria pencarian

    public SearchAdapter(List<MenuResponse> menuList) {
        this.fullMenuList = menuList;
        this.filteredMenuList = new ArrayList<>(menuList); // Menginisialisasi filteredMenuList dengan daftar lengkap
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_food, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuResponse menu = filteredMenuList.get(position);
        holder.tv_menu_title.setText(menu.getTitle());
        // Anda perlu menambahkan kode untuk menampilkan gambar di sini
        // Picasso.get().load(menu.getImageUrl()).into(holder.iv_menu_picture);
    }

    @Override
    public int getItemCount() {
        return filteredMenuList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String filterPattern = constraint.toString().toLowerCase().trim();
                List<MenuResponse> filteredList = new ArrayList<>();
                if (filterPattern.isEmpty()) {
                    filteredList.addAll(fullMenuList); // Jika kriteria pencarian kosong, tampilkan semua data
                } else {
                    for (MenuResponse menu : fullMenuList) {
                        if (menu.getTitle().toLowerCase().contains(filterPattern)) {
                            filteredList.add(menu); // Tambahkan data yang cocok dengan kriteria pencarian
                        }
                    }
                }
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredMenuList.clear();
                filteredMenuList.addAll((List) results.values); // Perbarui daftar yang difilter
                notifyDataSetChanged(); // Perbarui tampilan daftar
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_menu_picture;
        TextView tv_menu_title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_menu_picture = itemView.findViewById(R.id.iv_menu_picture);
            tv_menu_title = itemView.findViewById(R.id.tv_menu_title);
        }
    }
}
