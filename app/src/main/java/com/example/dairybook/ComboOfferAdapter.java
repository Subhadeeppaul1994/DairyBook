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

public class ComboOfferAdapter extends RecyclerView.Adapter<ComboOfferAdapter.ComboViewHolder> {

    private final List<ComboOfferItem> comboList;

    public ComboOfferAdapter(List<ComboOfferItem> comboList) {
        this.comboList = comboList;
    }

    @NonNull
    @Override
    public ComboViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_combo_offer, parent, false);
        return new ComboViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComboViewHolder holder, int position) {
        ComboOfferItem item = comboList.get(position);
        holder.tvComboTitle.setText(item.getTitle());
        holder.tvComboPriceTag.setText("STARTING @ " + item.getStartingPrice());

        Glide.with(holder.itemView.getContext())
                .load(item.getImageUrl())
                .into(holder.ivComboImage);
    }

    @Override
    public int getItemCount() {
        return comboList != null ? comboList.size() : 0;
    }

    static class ComboViewHolder extends RecyclerView.ViewHolder {
        TextView tvComboTitle, tvComboPriceTag;
        ImageView ivComboImage;

        public ComboViewHolder(@NonNull View itemView) {
            super(itemView);
            tvComboTitle = itemView.findViewById(R.id.tvComboTitle);
            tvComboPriceTag = itemView.findViewById(R.id.tvComboPriceTag);
            ivComboImage = itemView.findViewById(R.id.ivComboImage);
        }
    }
}