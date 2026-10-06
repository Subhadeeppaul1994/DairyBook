package com.example.dairybook;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {

    private List<BannerItem> bannerList;

    public BannerAdapter(List<BannerItem> bannerList) {
        this.bannerList = bannerList;
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_banner_card, parent, false);
        return new BannerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        BannerItem banner = bannerList.get(position);

        holder.tvTitle.setText(banner.getTitle());
        holder.tvDiscount.setText(banner.getDiscount());

        // Load Cloudinary image via Glide with neutral placeholder
        Glide.with(holder.itemView.getContext())
                .load(banner.getImageUrl())
                .placeholder(new ColorDrawable(Color.parseColor("#E0E0E0")))
                .error(new ColorDrawable(Color.parseColor("#E0E0E0")))
                .into(holder.ivImage);
    }

    @Override
    public int getItemCount() {
        return bannerList.size();
    }

    static class BannerViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDiscount;
        ImageView ivImage;

        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvBannerTitle);
            tvDiscount = itemView.findViewById(R.id.tvBannerDiscount);
            ivImage = itemView.findViewById(R.id.ivBannerImage);
        }
    }
}