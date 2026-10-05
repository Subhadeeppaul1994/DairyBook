package com.example.dairybook;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<CategoryItem> categories;

    // Light pastel colors: Red, Sky Blue, Green, Yellow, Pink, Violet/Purple, Orange
    private final String[] lightColors = {
            "#ffd6d6", // Light Red
            "#d6efff", // Light Sky Blue
            "#fff2d6", // Light orange
            "#edd6ff", // Light purple
            "#deffd6", // Light green
            "#ffd6f1", // Light pink
            "#d6fffb"  // Light cyan
    };

    public CategoryAdapter(List<CategoryItem> categories) {
        this.categories = categories;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        CategoryItem item = categories.get(position);
        holder.tvCategoryName.setText(item.getName());

        // Assign different light background color based on position
        int colorIndex = position % lightColors.length;
        holder.cardCategory.setCardBackgroundColor(Color.parseColor(lightColors[colorIndex]));

        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .placeholder(android.R.drawable.ic_menu_gallery)
                .into(holder.ivCategoryImage);
    }

    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardCategory;
        ImageView ivCategoryImage;
        TextView tvCategoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            cardCategory = itemView.findViewById(R.id.cardCategory);
            ivCategoryImage = itemView.findViewById(R.id.ivCategoryImage);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }
}