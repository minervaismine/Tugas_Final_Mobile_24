package com.example.tugas_final_mobile.response;

import com.google.gson.annotations.SerializedName;

public class MenuResponse {

    @SerializedName("id") // Assuming 'id' is the key in your JSON response
    private String id;

    @SerializedName("title")
    private String title;

    @SerializedName("image") // Update this according to your JSON response
    private String imageUrl;

    private NutritionResponse nutrition;

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public NutritionResponse getNutrition() {
        return nutrition;
    }

    public void setNutrition(NutritionResponse nutrition) {
        this.nutrition = nutrition;
    }
}
