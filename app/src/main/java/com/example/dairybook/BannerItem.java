package com.example.dairybook;

public class BannerItem {
    private String title;
    private String discount;
    private String imageUrl; // Cloudinary URL String

    public BannerItem(String title, String discount, String imageUrl) {
        this.title = title;
        this.discount = discount;
        this.imageUrl = imageUrl;
    }

    public String getTitle() { return title; }
    public String getDiscount() { return discount; }
    public String getImageUrl() { return imageUrl; }
}