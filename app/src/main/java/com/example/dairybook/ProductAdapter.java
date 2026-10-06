package com.example.dairybook;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<ProductItem> productList;

    public ProductAdapter(List<ProductItem> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_card, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        ProductItem product = productList.get(position);

        holder.tvName.setText(product.getName());
        holder.tvPrice.setText(product.getPrice());
        holder.tvUnit.setText(product.getUnit());

        // Brand Name Handling
        if (product.getBrand() != null && !product.getBrand().isEmpty()) {
            holder.tvBrand.setText(product.getBrand());
            holder.tvBrand.setVisibility(View.VISIBLE);
        } else {
            holder.tvBrand.setVisibility(View.GONE);
        }

        // Original Price with Strikethrough
        if (product.getOriginalPrice() != null && !product.getOriginalPrice().isEmpty()) {
            holder.tvOriginalPrice.setText(product.getOriginalPrice());
            holder.tvOriginalPrice.setPaintFlags(holder.tvOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.tvOriginalPrice.setVisibility(View.VISIBLE);
        } else {
            holder.tvOriginalPrice.setVisibility(View.GONE);
        }

        // Discount Badge Visibility
        if (product.getDiscountText() != null && !product.getDiscountText().isEmpty()) {
            holder.tvDiscountBadge.setText(product.getDiscountText());
            holder.tvDiscountBadge.setVisibility(View.VISIBLE);
        } else {
            holder.tvDiscountBadge.setVisibility(View.GONE);
        }

        // Load Image via Glide with clean light-gray placeholder
        Glide.with(holder.itemView.getContext())
                .load(product.getImageUrl())
                .placeholder(new ColorDrawable(Color.parseColor("#F5F5F5")))
                .error(new ColorDrawable(Color.parseColor("#F5F5F5")))
                .into(holder.ivProduct);

        // Heart Icon Favorite Toggle
        holder.ivFavorite.setImageResource(product.isFavorite() ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
        holder.ivFavorite.setOnClickListener(v -> {
            boolean newState = !product.isFavorite();
            product.setFavorite(newState);
            holder.ivFavorite.setImageResource(newState ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
        });

        // Sync Cart Button / Stepper View according to item quantity
        updateCartStepperUI(holder, product);

        // Click Bag Icon -> Initial Add to Cart
        holder.btnAddToCart.setOnClickListener(v -> {
            product.setQuantity(1);
            updateCartStepperUI(holder, product);
        });

        // Click Plus Button -> Increment Counter
        holder.btnPlus.setOnClickListener(v -> {
            int newQty = product.getQuantity() + 1;
            product.setQuantity(newQty);
            updateCartStepperUI(holder, product);
        });

        // Click Minus Button -> Decrement or Remove
        holder.btnMinus.setOnClickListener(v -> {
            int currentQty = product.getQuantity();
            if (currentQty > 1) {
                product.setQuantity(currentQty - 1);
            } else {
                product.setQuantity(0); // Toggles back to Bag icon
            }
            updateCartStepperUI(holder, product);
        });
    }

    private void updateCartStepperUI(ProductViewHolder holder, ProductItem product) {
        if (product.getQuantity() > 0) {
            holder.btnAddToCart.setVisibility(View.GONE);
            holder.layoutStepper.setVisibility(View.VISIBLE);
            holder.tvQuantityCount.setText(String.valueOf(product.getQuantity()));
        } else {
            holder.layoutStepper.setVisibility(View.GONE);
            holder.btnAddToCart.setVisibility(View.VISIBLE);
            holder.btnAddToCart.setImageResource(R.drawable.ic_bag_outline);
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvBrand, tvPrice, tvOriginalPrice, tvDiscountBadge, tvUnit, tvQuantityCount, btnMinus, btnPlus;
        ImageView ivProduct, ivFavorite, btnAddToCart;
        MaterialCardView layoutStepper;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvBrand = itemView.findViewById(R.id.tvBrandName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            tvOriginalPrice = itemView.findViewById(R.id.tvOriginalPrice);
            tvDiscountBadge = itemView.findViewById(R.id.tvDiscountBadge);
            tvUnit = itemView.findViewById(R.id.tvProductUnit);
            tvQuantityCount = itemView.findViewById(R.id.tvQuantityCount);
            btnMinus = itemView.findViewById(R.id.btnMinus);
            btnPlus = itemView.findViewById(R.id.btnPlus);

            ivProduct = itemView.findViewById(R.id.ivProduct);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
            layoutStepper = itemView.findViewById(R.id.layoutQuantityStepper);
        }
    }
}