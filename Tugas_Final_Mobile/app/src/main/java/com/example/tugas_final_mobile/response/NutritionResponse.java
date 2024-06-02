package com.example.tugas_final_mobile.response;

import com.google.gson.annotations.SerializedName;

public class NutritionResponse {
    @SerializedName("calories")
    private String calories;

    @SerializedName("carbs")
    private String carbs;

    @SerializedName("fat")
    private String fat;

    @SerializedName("protein")
    private String protein;

    // Constructor
    public NutritionResponse(String calories, String carbs, String fat, String protein) {
        this.calories = calories;
        this.carbs = carbs;
        this.fat = fat;
        this.protein = protein;
    }

    // Getter dan Setter
    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getCarbs() {
        return carbs;
    }

    public void setCarbs(String carbs) {
        this.carbs = carbs;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }
}
