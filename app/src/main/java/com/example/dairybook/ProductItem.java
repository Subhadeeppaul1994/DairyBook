package com.example.dairybook;

public class ProductItem {
    private String name;
    private String brand;
    private String price;
    private String originalPrice;
    private String discountText;
    private String unit;
    private String imageUrl;
    private boolean isFavorite;
    private int quantity;

    public ProductItem(String name, String brand, String price, String originalPrice, String discountText, String unit, String imageUrl) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.originalPrice = originalPrice;
        this.discountText = discountText;
        this.unit = unit;
        this.imageUrl = imageUrl;
        this.isFavorite = false;
        this.quantity = 0;
    }

    // Convenience constructor for products without discount/brand
    public ProductItem(String name, String price, String imageUrl) {
        this(name, "", price, "", "", "1 Pack", imageUrl);
    }

    public String getName() { return name; }
    public String getBrand() { return brand; }
    public String getPrice() { return price; }
    public String getOriginalPrice() { return originalPrice; }
    public String getDiscountText() { return discountText; }
    public String getUnit() { return unit; }
    public String getImageUrl() { return imageUrl; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}