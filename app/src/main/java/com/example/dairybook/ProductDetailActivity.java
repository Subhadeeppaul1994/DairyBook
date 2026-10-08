package com.example.dairybook;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView ivDetailProductImage, ivDetailFavorite;
    private MaterialCardView btnBack, btnShare;
    private TextView tvDetailBrand, tvDetailTitle, tvDetailPrice, tvDetailUnit, tvDetailOriginalPrice;
    private TextView tvDetailQuantity, btnDetailMinus, btnDetailPlus;
    private TextView tvDetailDescription, tvBottomTotalPrice;
    private MaterialButton btnDetailAddToCart;
    private RecyclerView rvRecommended;

    private ProductItem currentProduct;
    private int itemQuantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Bind UI Elements
        btnBack = findViewById(R.id.btnBack);
        btnShare = findViewById(R.id.btnShare);
        ivDetailProductImage = findViewById(R.id.ivDetailProductImage);
        ivDetailFavorite = findViewById(R.id.ivDetailFavorite);

        tvDetailBrand = findViewById(R.id.tvDetailBrand);
        tvDetailTitle = findViewById(R.id.tvDetailTitle);
        tvDetailPrice = findViewById(R.id.tvDetailPrice);
        tvDetailUnit = findViewById(R.id.tvDetailUnit);
        tvDetailOriginalPrice = findViewById(R.id.tvDetailOriginalPrice);

        tvDetailQuantity = findViewById(R.id.tvDetailQuantity);
        btnDetailMinus = findViewById(R.id.btnDetailMinus);
        btnDetailPlus = findViewById(R.id.btnDetailPlus);

        tvDetailDescription = findViewById(R.id.tvDetailDescription);
        tvBottomTotalPrice = findViewById(R.id.tvBottomTotalPrice);
        btnDetailAddToCart = findViewById(R.id.btnDetailAddToCart);
        rvRecommended = findViewById(R.id.rvRecommended);

        // Back Navigation
        btnBack.setOnClickListener(v -> finish());

        // Share Action Placeholder
        btnShare.setOnClickListener(v ->
                Toast.makeText(this, "Sharing product...", Toast.LENGTH_SHORT).show()
        );

        // Unpack Product Item Intent Extra
        if (getIntent().hasExtra("EXTRA_PRODUCT_ITEM")) {
            currentProduct = (ProductItem) getIntent().getSerializableExtra("EXTRA_PRODUCT_ITEM");
        }

        if (currentProduct != null) {
            bindProductData(currentProduct);
        }

        // Setup Interactive Stepper & Listeners
        setupQuantityStepper();
        setupFavoriteToggle();
        setupAddToCartButton();

        // Populate Recommendations Horizontal List
        setupRecommendations();
    }

    private void bindProductData(ProductItem product) {
        tvDetailTitle.setText(product.getName());
        tvDetailPrice.setText(product.getPrice());
        tvDetailUnit.setText("/" + product.getUnit());
        tvBottomTotalPrice.setText(product.getPrice());

        if (product.getBrand() != null && !product.getBrand().isEmpty()) {
            tvDetailBrand.setText(product.getBrand());
        } else {
            tvDetailBrand.setText("DAIRYBOOK FRESH");
        }

        if (product.getOriginalPrice() != null && !product.getOriginalPrice().isEmpty()) {
            tvDetailOriginalPrice.setText(product.getOriginalPrice());
            tvDetailOriginalPrice.setPaintFlags(tvDetailOriginalPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        // Set initial quantity stepper
        itemQuantity = Math.max(product.getQuantity(), 1);
        updateQuantityDisplay();

        // Load Main Product Image with Glide
        Glide.with(this)
                .load(product.getImageUrl())
                .placeholder(new ColorDrawable(Color.parseColor("#F5F5F5")))
                .error(new ColorDrawable(Color.parseColor("#F5F5F5")))
                .into(ivDetailProductImage);

        // Sync Favorite Heart State
        ivDetailFavorite.setImageResource(product.isFavorite() ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
    }

    private void setupFavoriteToggle() {
        ivDetailFavorite.setOnClickListener(v -> {
            if (currentProduct != null) {
                boolean newState = !currentProduct.isFavorite();
                currentProduct.setFavorite(newState);
                ivDetailFavorite.setImageResource(newState ? R.drawable.ic_heart_filled : R.drawable.ic_heart_outline);
            }
        });
    }

    private void setupQuantityStepper() {
        btnDetailPlus.setOnClickListener(v -> {
            itemQuantity++;
            updateQuantityDisplay();
        });

        btnDetailMinus.setOnClickListener(v -> {
            if (itemQuantity > 1) {
                itemQuantity--;
                updateQuantityDisplay();
            }
        });
    }

    private void updateQuantityDisplay() {
        tvDetailQuantity.setText(String.format("%02d", itemQuantity));
        if (currentProduct != null) {
            currentProduct.setQuantity(itemQuantity);
        }
    }

    private void setupAddToCartButton() {
        btnDetailAddToCart.setOnClickListener(v -> {
            if (currentProduct != null) {
                currentProduct.setQuantity(itemQuantity);
                Toast.makeText(this, itemQuantity + " x " + currentProduct.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRecommendations() {
        if (currentProduct == null) return;

        // Get all available products (In real app, fetch from database or Intent extra)
        List<ProductItem> allProducts = getAllAvailableProducts();
        List<ProductItem> filteredRecommendations = new ArrayList<>();

        // Determine target category keywords based on the clicked product's name/brand
        String productNameLower = currentProduct.getName().toLowerCase();

        for (ProductItem item : allProducts) {
            // Skip comparing the current product itself
            if (item.getName().equalsIgnoreCase(currentProduct.getName())) {
                continue;
            }

            String itemNameLower = item.getName().toLowerCase();

            // Rule 1: If current product is Milk-related
            if (productNameLower.contains("milk") || productNameLower.contains("rusk")) {
                if (itemNameLower.contains("milk") || itemNameLower.contains("condensed") ||
                        itemNameLower.contains("curd") || itemNameLower.contains("lassi") ||
                        itemNameLower.contains("yogurt")) {
                    filteredRecommendations.add(item);
                }
            }
            // Rule 2: If current product is Paneer/Cheese-related
            else if (productNameLower.contains("paneer") || productNameLower.contains("cheese")) {
                if (itemNameLower.contains("paneer") || itemNameLower.contains("cheese") ||
                        itemNameLower.contains("ghee") || itemNameLower.contains("butter") ||
                        itemNameLower.contains("curd")) {
                    filteredRecommendations.add(item);
                }
            }
            // Rule 3: Default fallback for other dairy categories
            else {
                if (itemNameLower.contains("ghee") || itemNameLower.contains("butter") ||
                        itemNameLower.contains("lassi")) {
                    filteredRecommendations.add(item);
                }
            }
        }

        // Bind filtered list to RecyclerView
        rvRecommended.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvRecommended.setAdapter(new ProductAdapter(filteredRecommendations));
    }

    // Master pool of products to filter from
    private List<ProductItem> getAllAvailableProducts() {
        List<ProductItem> list = new ArrayList<>();
        list.add(new ProductItem("Fresh Paneer Packaging", "AMUL", "₹95", "₹110", "13% OFF", "200 g", "https://res.cloudinary.com/bxfg4024/image/upload/v1791016426/Fresh_Paneer_Packaging_with_Herbs.png"));
        list.add(new ProductItem("Pure Cow Ghee Jar", "MOTHER DAIRY", "₹380", "₹450", "15% OFF", "500 ml", "https://res.cloudinary.com/bxfg4024/image/upload/v1791016426/ghee.png"));
        list.add(new ProductItem("Sweetened Condensed Milk", "NESTLE", "₹140", "₹160", "12% OFF", "400 g", "https://res.cloudinary.com/bxfg4024/image/upload/v1791283298/pngwing.com_2.png"));
        list.add(new ProductItem("Mango Lassi Bottle", "AMUL", "₹30", "₹35", "14% OFF", "200 ml", "https://res.cloudinary.com/bxfg4024/image/upload/v1791283299/02-fop_amul-mango-lassi-tetrapack-1l-480x480.png"));
        list.add(new ProductItem("Flavored Berry Yogurt", "EPIGAMIA", "₹45", "₹50", "10% OFF", "200 g", "https://res.cloudinary.com/bxfg4024/image/upload/v1791283302/pngwing.com_3.png"));
        list.add(new ProductItem("Unsalted Butter Pack", "AMUL", "₹52", "₹60", "13% OFF", "100 g", "https://res.cloudinary.com/bxfg4024/image/upload/v1791029827/butter_pack.png"));
        return list;
    }
}