package com.example.dairybook;

public class ProductItem {
    private String name;
    private String price;
    private String imageUrl; // Cloudinary URL String
    private boolean isFavorite;
    private boolean isAddedToCart;

    public ProductItem(String name, String price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isFavorite = false;
        this.isAddedToCart = false;
    }

    public String getName() { return name; }
    public String getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }

    public boolean isAddedToCart() { return isAddedToCart; }
    public void setAddedToCart(boolean addedToCart) { isAddedToCart = addedToCart; }
}