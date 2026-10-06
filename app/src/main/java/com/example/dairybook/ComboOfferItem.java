package com.example.dairybook;
public class ComboOfferItem {
    private String title;
    private String startingPrice;
    private String imageUrl;

    public ComboOfferItem(String title, String startingPrice, String imageUrl) {
        this.title = title;
        this.startingPrice = startingPrice;
        this.imageUrl = imageUrl;
    }

    public String getTitle() { return title; }
    public String getStartingPrice() { return startingPrice; }
    public String getImageUrl() { return imageUrl; }
}
