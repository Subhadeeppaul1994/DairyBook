package com.example.dairybook;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import eightbitlab.com.blurview.BlurView;
import eightbitlab.com.blurview.RenderScriptBlur;

public class MainActivity extends AppCompatActivity {

    private ShapeableImageView ivUserProfile;
    private ImageView ivHeroBanner;
    private ViewPager2 bannerViewPager;
    private TextView tvBannerCounter;
    private LinearLayout layoutDotsIndicator;
    private RecyclerView rvCategories, rvFreshProducts, rvDealOfTheDay, rvNewLaunch;
    private BlurView blurHeaderPill;

    // Bottom Navigation Views
    private LinearLayout navHomeLayout, navFavLayout, navOfferLayout, navCartLayout;
    private ImageView navHomeIcon, navFavIcon, navOfferIcon, navCartIcon;
    private TextView navHomeText, navFavText, navOfferText, navCartText;

    private Handler autoScrollHandler = new Handler(Looper.getMainLooper());
    private Runnable autoScrollRunnable;
    private int bannerCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Bind Views
        ivUserProfile = findViewById(R.id.ivUserProfile);
        ivHeroBanner = findViewById(R.id.ivHeroBanner);
        blurHeaderPill = findViewById(R.id.blurHeaderPill);
        bannerViewPager = findViewById(R.id.bannerViewPager);
        tvBannerCounter = findViewById(R.id.tvBannerCounter);
        layoutDotsIndicator = findViewById(R.id.layoutDotsIndicator);
        rvCategories = findViewById(R.id.rvCategories);
        rvFreshProducts = findViewById(R.id.rvFreshProducts);
        rvDealOfTheDay = findViewById(R.id.rvDealOfTheDay);
        rvNewLaunch = findViewById(R.id.rvNewLaunch);

        // Bind Bottom Navigation
        navHomeLayout = findViewById(R.id.navHomeLayout);
        navFavLayout = findViewById(R.id.navFavLayout);
        navOfferLayout = findViewById(R.id.navOfferLayout);
        navCartLayout = findViewById(R.id.navCartLayout);

        navHomeIcon = findViewById(R.id.navHomeIcon);
        navFavIcon = findViewById(R.id.navFavIcon);
        navOfferIcon = findViewById(R.id.navOfferIcon);
        navCartIcon = findViewById(R.id.navCartIcon);

        navHomeText = findViewById(R.id.navHomeText);
        navFavText = findViewById(R.id.navFavText);
        navOfferText = findViewById(R.id.navOfferText);
        navCartText = findViewById(R.id.navCartText);

        // Setup Glass Blur on Header Pill Container
        setupHeaderBlur();

        // Load Profile Image from Cloudinary
        String profileImageUrl = "https://res.cloudinary.com/bxfg4024/image/upload/v1791023620/profile_image.jpg";
        Glide.with(this)
                .load(profileImageUrl)
                .placeholder(new ColorDrawable(Color.parseColor("#E0E0E0")))
                .error(new ColorDrawable(Color.parseColor("#E0E0E0")))
                .into(ivUserProfile);

        // Load Hero Banner Image from Cloudinary
        String heroBannerUrl = "https://res.cloudinary.com/bxfg4024/image/upload/v1791190072/IMG_20261005_141611.png";
        Glide.with(this)
                .load(heroBannerUrl)
                .placeholder(new ColorDrawable(Color.parseColor("#E0E0E0")))
                .error(new ColorDrawable(Color.parseColor("#E0E0E0")))
                .into(ivHeroBanner);

        // Setup Bottom Navigation
        setupBottomNavigation();

        // Setup Sections
        setupBanners();
        setupCategories();
        setupFreshProducts();
        setupDealOfTheDay();
        setupNewLaunch();
    }

    private void setupHeaderBlur() {
        View decorView = getWindow().getDecorView();
        ViewGroup rootView = decorView.findViewById(android.R.id.content);
        Drawable windowBackground = decorView.getBackground();

        blurHeaderPill.setupWith(rootView, new RenderScriptBlur(this))
                .setFrameClearDrawable(windowBackground)
                .setBlurRadius(16f);
    }

    private void setupBottomNavigation() {
        selectTab(0);

        navHomeLayout.setOnClickListener(v -> selectTab(0));
        navFavLayout.setOnClickListener(v -> selectTab(1));
        navOfferLayout.setOnClickListener(v -> selectTab(2));
        navCartLayout.setOnClickListener(v -> selectTab(3));
    }

    private void selectTab(int index) {
        resetTab(navHomeLayout, navHomeIcon, navHomeText);
        resetTab(navFavLayout, navFavIcon, navFavText);
        resetTab(navOfferLayout, navOfferIcon, navOfferText);
        resetTab(navCartLayout, navCartIcon, navCartText);

        switch (index) {
            case 0:
                activateTab(navHomeLayout, navHomeIcon, navHomeText);
                break;
            case 1:
                activateTab(navFavLayout, navFavIcon, navFavText);
                break;
            case 2:
                activateTab(navOfferLayout, navOfferIcon, navOfferText);
                break;
            case 3:
                activateTab(navCartLayout, navCartIcon, navCartText);
                break;
        }
    }

    private void resetTab(LinearLayout layout, ImageView icon, TextView text) {
        layout.setBackground(null);
        layout.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));
        icon.setColorFilter(Color.parseColor("#B0B0B0"));
        text.setVisibility(View.GONE);
    }

    private void activateTab(LinearLayout layout, ImageView icon, TextView text) {
        layout.setBackgroundResource(R.drawable.bg_active_nav_tab);
        layout.setLayoutParams(new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1.1f));
        icon.setColorFilter(Color.parseColor("#FFFFFF"));
        text.setVisibility(View.VISIBLE);
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

        setupDotsIndicator(bannerCount);
        updateBannerCounterAndDots(0);

        bannerViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                updateBannerCounterAndDots(position);
                autoScrollHandler.removeCallbacks(autoScrollRunnable);
                autoScrollHandler.postDelayed(autoScrollRunnable, 3000);
            }
        });

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
        autoScrollHandler.postDelayed(autoScrollRunnable, 2500);
    }

    @Override
    protected void onPause() {
        super.onPause();
        autoScrollHandler.removeCallbacks(autoScrollRunnable);
    }

    private void setupFreshProducts() {
        List<ProductItem> list = new ArrayList<>();
        list.add(new ProductItem("Toastea Premium Bake Rusk", "BRITANNIA", "₹177.50", "₹250", "29% OFF", "1 kg", "https://res.cloudinary.com/bxfg4024/image/upload/v1791016426/milk_png.png"));
        list.add(new ProductItem("Fresh Paneer Packaging", "AMUL", "₹95", "₹110", "13% OFF", "200 g", "https://res.cloudinary.com/bxfg4024/image/upload/v1791016426/Fresh_Paneer_Packaging_with_Herbs.png"));
        list.add(new ProductItem("Pure Cow Ghee Jar", "MOTHER DAIRY", "₹380", "₹450", "15% OFF", "500 ml", "https://res.cloudinary.com/bxfg4024/image/upload/v1791016426/ghee.png"));
        list.add(new ProductItem("Pure Cheese", "Amul", "₹130", "", "", "200 g", "https://res.cloudinary.com/bxfg4024/image/upload/v1791016426/Premium_Cheese_Product_Mockup.png"));
        rvFreshProducts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvFreshProducts.setAdapter(new ProductAdapter(list));
    }

    private void setupDealOfTheDay() {
        List<ProductItem> list = new ArrayList<>();
        list.add(new ProductItem("Unsalted Butter Pack", "AMUL", "₹52", "₹60", "13% OFF", "100 g", "https://res.cloudinary.com/bxfg4024/image/upload/v1791029827/butter_pack.png"));
        list.add(new ProductItem("Mango Lassi Bottle", "MOTHER DAIRY", "₹30", "₹35", "14% OFF", "200 ml", "https://res.cloudinary.com/bxfg4024/image/upload/v1791029831/lassi_mango.png"));
        list.add(new ProductItem("Processed Cheese Slices", "BRITANNIA", "₹125", "₹150", "16% OFF", "200 g", "https://res.cloudinary.com/bxfg4024/image/upload/v1791029827/slice_cheese.png"));

        rvDealOfTheDay.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvDealOfTheDay.setAdapter(new ProductAdapter(list));
    }

    private void setupNewLaunch() {
        List<ProductItem> list = new ArrayList<>();
        list.add(new ProductItem("Almond Milk Unsweetened", "SOFIT", "₹180", "₹210", "14% OFF", "1 L", "https://res.cloudinary.com/bxfg4024/image/upload/v1791029827/almond_milk.png"));
        list.add(new ProductItem("Flavored Berry Yogurt", "EPIGAMIA", "₹45", "₹50", "10% OFF", "200 g", "https://res.cloudinary.com/bxfg4024/image/upload/v1791191177/Gemini_Generated_Image_pl2j56pl2j56pl2j.png"));
        list.add(new ProductItem("Sweetened Condensed Milk", "NESTLE", "₹140", "₹160", "12% OFF", "400 g", "https://res.cloudinary.com/bxfg4024/image/upload/v1791191177/Gemini_Generated_Image_vthhy7vthhy7vthh.png"));

        rvNewLaunch.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvNewLaunch.setAdapter(new ProductAdapter(list));
    }
}