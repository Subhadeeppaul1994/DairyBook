package com.example.dairybook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
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

        // Load Cloudinary image via Glide
        Glide.with(holder.itemView.getContext())
                .load(product.getImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.ivProduct);

        // Heart Icon Toggle (Favorite)
        holder.ivFavorite.setImageResource(product.isFavorite() ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
        holder.ivFavorite.setOnClickListener(v -> {
            boolean newState = !product.isFavorite();
            product.setFavorite(newState);
            holder.ivFavorite.setImageResource(newState ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
        });

        // Bag Icon Toggle (Add to Cart)
        holder.btnAddToCart.setImageResource(product.isAddedToCart() ? R.drawable.ic_bag_filled : R.drawable.ic_bag_outline);
        holder.btnAddToCart.setOnClickListener(v -> {
            boolean newState = !product.isAddedToCart();
            product.setAddedToCart(newState);
            holder.btnAddToCart.setImageResource(newState ? R.drawable.ic_bag_filled : R.drawable.ic_bag_outline);
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;
        ImageView ivProduct, ivFavorite, btnAddToCart;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvProductName);
            tvPrice = itemView.findViewById(R.id.tvProductPrice);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);
            btnAddToCart = itemView.findViewById(R.id.btnAddToCart);
        }
    }
}