package com.example.dairybook;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ShapeableImageView ivUserProfile;
    private ViewPager2 bannerViewPager;
    private TextView tvBannerCounter;
    private LinearLayout layoutDotsIndicator;
    private RecyclerView rvCategories, rvFreshProducts, rvDealOfTheDay, rvNewLaunch;

    private Handler autoScrollHandler = new Handler(Looper.getMainLooper());
    private Runnable autoScrollRunnable;
    private int bannerCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind Views
        ivUserProfile = findViewById(R.id.ivUserProfile);
        bannerViewPager = findViewById(R.id.bannerViewPager);
        tvBannerCounter = findViewById(R.id.tvBannerCounter);
        layoutDotsIndicator = findViewById(R.id.layoutDotsIndicator);
        rvCategories = findViewById(R.id.rvCategories);
        rvFreshProducts = findViewById(R.id.rvFreshProducts);
        rvDealOfTheDay = findViewById(R.id.rvDealOfTheDay);
        rvNewLaunch = findViewById(R.id.rvNewLaunch);

        // Load Profile Image from Cloudinary
        String profileImageUrl = "https://res.cloudinary.com/bxfg4024/image/upload/v1791023620/profile_image.jpg";
        Glide.with(this)
                .load(profileImageUrl)
                .placeholder(android.R.drawable.ic_menu_gallery)
                .error(android.R.drawable.ic_menu_gallery)
                .into(ivUserProfile);

        // Setup Sections
        setupBanners();
        setupCategories();
        setupFreshProducts();
        setupDealOfTheDay();
        setupNewLaunch();
    }

    private void setupCategories() {
        List<CategoryItem> categories = new ArrayList<>();
        categories.add(new CategoryItem("Milk", "https://res.cloudinary.com/bxfg4024/image/upload/v1791024629/milk_icon.png"));
        categories.add(new CategoryItem("Butter", "https://res.cloudinary.com/bxfg4024/image/upload/v1791024629/butter_icon.png"));
        categories.add(new CategoryItem("Cheese", "https://res.cloudinary.com/bxfg4024/image/upload/v1791024628/cheese_icon.png"));
        categories.add(new CategoryItem("Paneer", "https://res.cloudinary.com/bxfg4024/image/upload/v1791024629/paneer_icon.png"));
        categories.add(new CategoryItem("Ghee", "https://res.cloudinary.com/bxfg4024/image/upload/v1791024628/ghee_icon.png"));
        categories.add(new CategoryItem("Yogurt", "https://res.cloudinary.com/bxfg4024/image/upload/v1791024896/Yogurt_icon.png"));
        categories.add(new CategoryItem("Sweet", "https://res.cloudinary.com/bxfg4024/image/upload/v1791024628/sweets_icon.png"));
        rvCategories.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvCategories.setAdapter(new CategoryAdapter(categories));
    }

    private void setupBanners() {
        List<BannerItem> banners = new ArrayList<>();
        banners.add(new BannerItem("Fresh Cow Milk", "15% off", "https://res.cloudinary.com/bxfg4024/image/upload/v1791018259/banner_milk.png"));
        banners.add(new BannerItem("Artisanal Butter", "20% off", "https://res.cloudinary.com/bxfg4024/image/upload/v1791018546/butter_banner.png"));
        banners.add(new BannerItem("Cottage Cheese", "10% off", "https://res.cloudinary.com/bxfg4024/image/upload/v1791018465/cheese_banner.png"));
        banners.add(new BannerItem("Greek Yogurt Pack", "25% off", "https://res.cloudinary.com/bxfg4024/image/upload/v1791018102/banner_curd.png"));
        banners.add(new BannerItem("Pure Desi Ghee", "30% off", "https://res.cloudinary.com/bxfg4024/image/upload/v1791018102/banner_ghee.png"));

        bannerCount = banners.size();
        BannerAdapter bannerAdapter = new BannerAdapter(banners);
        bannerViewPager.setAdapter(bannerAdapter);

        // Initialize Indicators
        setupDotsIndicator(bannerCount);
        updateBannerCounterAndDots(0);

        // Register ViewPager2 Callbacks
        bannerViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateBannerCounterAndDots(position);

                // Reset timer when swiped manually
                autoScrollHandler.removeCallbacks(autoScrollRunnable);
                autoScrollHandler.postDelayed(autoScrollRunnable, 3000);
            }
        });

        // Runnable for 3-second Auto Slide
        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                if (bannerCount > 0) {
                    int nextItem = (bannerViewPager.getCurrentItem() + 1) % bannerCount;
                    bannerViewPager.setCurrentItem(nextItem, true);
                }
            }
        };
    }

    private void setupDotsIndicator(int count) {
        layoutDotsIndicator.removeAllViews();
        for (int i = 0; i < count; i++) {
            View dot = new View(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(16, 16);
            params.setMargins(6, 0, 6, 0);
            dot.setLayoutParams(params);

            GradientDrawable shape = new GradientDrawable();
            shape.setShape(GradientDrawable.OVAL);
            shape.setColor(Color.parseColor("#CCCCCC"));
            dot.setBackground(shape);

            layoutDotsIndicator.addView(dot);
        }
    }

    private void updateBannerCounterAndDots(int position) {
        if (tvBannerCounter != null) {
            tvBannerCounter.setText((position + 1) + "/" + bannerCount);
        }

        for (int i = 0; i < layoutDotsIndicator.getChildCount(); i++) {
            View dot = layoutDotsIndicator.getChildAt(i);
            GradientDrawable shape = new GradientDrawable();

            if (i == position) {
                shape.setShape(GradientDrawable.RECTANGLE);
                shape.setCornerRadius(10f);
                shape.setColor(Color.parseColor("#FF5722"));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(40, 16);
                params.setMargins(6, 0, 6, 0);
                dot.setLayoutParams(params);
            } else {
                shape.setShape(GradientDrawable.OVAL);
                shape.setColor(Color.parseColor("#CCCCCC"));
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(16, 16);
                params.setMargins(6, 0, 6, 0);
                dot.setLayoutParams(params);
            }
            dot.setBackground(shape);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        autoScrollHandler.postDelayed(autoScrollRunnable, 3000);
    }

    @Override
    protected void onPause() {
        super.onPause();
        autoScrollHandler.removeCallbacks(autoScrollRunnable);
    }

    private void setupFreshProducts() {
        List<ProductItem> list = new ArrayList<>();
        list.add(new ProductItem("Full Cream Milk 1L", "₹66", "https://res.cloudinary.com/bxfg4024/image/upload/v1791016426/milk_png.png"));
        list.add(new ProductItem("Fresh Paneer 200g", "₹95", "https://res.cloudinary.com/bxfg4024/image/upload/v1791016426/Fresh_Paneer_Packaging_with_Herbs.png"));
        list.add(new ProductItem("Cow Ghee 500ml", "₹380", "https://res.cloudinary.com/bxfg4024/image/upload/v1791016426/ghee.png"));
        list.add(new ProductItem("Cheese 200g", "₹128", "https://res.cloudinary.com/bxfg4024/image/upload/v1791016426/Premium_Cheese_Product_Mockup.png"));

        rvFreshProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvFreshProducts.setAdapter(new ProductAdapter(list));
    }

    private void setupDealOfTheDay() {
        List<ProductItem> list = new ArrayList<>();
        list.add(new ProductItem("Unsalted Butter 100g", "₹52", "https://res.cloudinary.com/bxfg4024/image/upload/v1791029827/butter_pack.png"));
        list.add(new ProductItem("Mango Lassi 200ml", "₹30", "https://res.cloudinary.com/bxfg4024/image/upload/v1791029831/lassi_mango.png"));
        list.add(new ProductItem("Cheese Slices 200g", "₹125", "https://res.cloudinary.com/bxfg4024/image/upload/v1791029827/slice_cheese.png"));

        rvDealOfTheDay.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvDealOfTheDay.setAdapter(new ProductAdapter(list));
    }

    private void setupNewLaunch() {
        List<ProductItem> list = new ArrayList<>();
        list.add(new ProductItem("Almond Milk 1L", "₹180", "https://res.cloudinary.com/bxfg4024/image/upload/v1791029827/almond_milk.png"));
        list.add(new ProductItem("Flavored Yogurt (Berry)", "₹45", "https://res.cloudinary.com/demo/image/upload/v1312461204/sample.jpg"));
        list.add(new ProductItem("Condensed Milk 400g", "₹140", "https://res.cloudinary.com/demo/image/upload/v1312461204/sample.jpg"));

        rvNewLaunch.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvNewLaunch.setAdapter(new ProductAdapter(list));
    }
}