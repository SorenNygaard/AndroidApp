package com.example.projekt;



public class Bog {
    public String titel, forfatter, uddannelse, semester, stand, imageUrl, userId;
    public double pris;

    public Bog() {
        // Default constructor required for Firebase
    }

    public Bog(String titel, String forfatter, double pris, String uddannelse, String semester, String stand, String imageUrl) {
        this.titel = titel;
        this.forfatter = forfatter;
        this.pris = pris;
        this.uddannelse = uddannelse;
        this.semester = semester;
        this.stand = stand;
        this.imageUrl = imageUrl;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
