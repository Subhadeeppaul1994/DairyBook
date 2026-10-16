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
    private int selectedPosition = 0;
    private OnCategoryClickListener listener;
    private float scrollProgress = 0f;

    // Distinct soft background colors for the category icon containers
    private final int[] iconBackgroundColors = {
            Color.parseColor("#ffd6d6"), // Light Red
            Color.parseColor("#d6efff"), // Light Sky Blue
            Color.parseColor("#fff2d6"), // Light orange
            Color.parseColor("#edd6ff"), // Light purple
            Color.parseColor("#deffd6"), // Light green
            Color.parseColor("#ffd6f1"), // Light pink
            Color.parseColor("#d6fffb")  // Light cyan
    };

    public interface OnCategoryClickListener {
        void onCategoryClick(int position, CategoryItem item);
    }

    public CategoryAdapter(List<CategoryItem> categories, OnCategoryClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    public void updateScrollProgress(float progress) {
        float newProgress = Math.max(0f, Math.min(1f, progress));
        if (Math.abs(this.scrollProgress - newProgress) > 0.01f) {
            this.scrollProgress = newProgress;
            notifyDataSetChanged();
        }
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

        boolean isSelected = (position == selectedPosition);

        int targetBackgroundColor = isSelected ? Color.parseColor("#90EE90") : Color.parseColor("#FFFFFF");
        int targetTextColor = isSelected ? Color.parseColor("#FFFFFF") : Color.parseColor("#333333");
        int targetStrokeColor = isSelected ? Color.parseColor("#168D16") : Color.parseColor("#E0E0E0");

        holder.cardCategory.setCardBackgroundColor(targetBackgroundColor);
        holder.cardCategory.setStrokeColor(targetStrokeColor);
        holder.tvCategoryName.setTextColor(targetTextColor);

        // Apply a distinct background color to the icon container based on its position
        int iconBgColor = iconBackgroundColors[position % iconBackgroundColors.length];
        holder.imageContainerCard.setCardBackgroundColor(iconBgColor);

        float density = holder.itemView.getContext().getResources().getDisplayMetrics().density;

        // 1. Dynamically shrink pill card height from 45dp down to 34dp
        int expandedHeight = (int) (45 * density);
        int collapsedHeight = (int) (34 * density);

        // 2. Dynamically shrink pill card width from 115dp down to 75dp as user scrolls up
        int expandedWidth = (int) (115 * density);
        int collapsedWidth = (int) (75 * density);

        ViewGroup.LayoutParams cardParams = holder.cardCategory.getLayoutParams();
        cardParams.height = (int) (expandedHeight - (expandedHeight - collapsedHeight) * scrollProgress);
        cardParams.width = (int) (expandedWidth - (expandedWidth - collapsedWidth) * scrollProgress);
        holder.cardCategory.setLayoutParams(cardParams);

        // Maintain smooth pill corner radius
        holder.cardCategory.setRadius(cardParams.height / 2f);

        // 3. Clean uniform scale down & fade for the image container
        float scaleFactor = Math.max(0f, 1f - scrollProgress);
        holder.imageContainerCard.setScaleX(scaleFactor);
        holder.imageContainerCard.setScaleY(scaleFactor);
        holder.imageContainerCard.setAlpha(scaleFactor);
        holder.imageContainerCard.setVisibility(scrollProgress > 0.95f ? View.GONE : View.VISIBLE);

        // 4. Smooth text alignment transition: adjust start margin and center gravity when collapsed
        ViewGroup.MarginLayoutParams textParams = (ViewGroup.MarginLayoutParams) holder.tvCategoryName.getLayoutParams();
        textParams.leftMargin = (int) (12 * density * scaleFactor);
        holder.tvCategoryName.setLayoutParams(textParams);

        if (scrollProgress > 0.8f) {
            holder.tvCategoryName.setGravity(android.view.Gravity.CENTER);
        } else {
            holder.tvCategoryName.setGravity(android.view.Gravity.CENTER_VERTICAL);
        }

        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .placeholder(android.R.color.transparent)
                .into(holder.ivCategoryImage);

        // Reliable selection click handling
        holder.cardCategory.setOnClickListener(v -> {
            int previousSelected = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            if (selectedPosition != RecyclerView.NO_POSITION) {
                notifyItemChanged(previousSelected);
                notifyItemChanged(selectedPosition);
                if (listener != null) {
                    listener.onCategoryClick(selectedPosition, item);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories != null ? categories.size() : 0;
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        MaterialCardView cardCategory;
        MaterialCardView imageContainerCard;
        ImageView ivCategoryImage;
        TextView tvCategoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            cardCategory = (MaterialCardView) itemView;
            imageContainerCard = itemView.findViewById(R.id.imageContainerCard);
            ivCategoryImage = itemView.findViewById(R.id.ivCategoryImage);
            tvCategoryName = itemView.findViewById(R.id.tvCategoryName);
        }
    }
}