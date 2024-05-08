package com.example.projekt;

/**
 * ModelRecyclerView class represents a model for data to be displayed in a RecyclerView.
 * It encapsulates information about a book, including its titel, forfatter, uddannelse, semester, stand, pris og billede.
 */
public class ModelRecyclerView {

    String titel, forfatter, uddannelse, semester, stand, imageUrl;
    Double pris;

    /**
     * Default constructor for ModelRecyclerView.
     * Initializes the object with default values.
     */
    public ModelRecyclerView() {
    }

    /**
     * Parameterized constructor for ModelRecyclerView.
     *
     * @param titel      The title of the book.
     * @param forfatter  The author of the book.
     * @param uddannelse The education related to the book.
     * @param semester   The semester information related to the book.
     * @param stand      The stand information related to the book.
     * @param pris       The price of the book.
     */
    public ModelRecyclerView(String titel, String forfatter, String uddannelse, String semester, String stand, Double pris, String imageUrl) {
        this.titel = titel;
        this.forfatter = forfatter;
        this.uddannelse = uddannelse;
        this.semester = semester;
        this.stand = stand;
        this.pris = pris;
        this.imageUrl = imageUrl;
    }


// Getter and setter methods for each attribute

    /**
     * Retrieves the title of the book.
     *
     * @return The title of the book.
     */
    public String getTitel() {
        return titel;
    }

    /**
     * Sets the title of the book.
     *
     * @param titel The title of the book.
     */
    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getForfatter() {
        return forfatter;
    }

    public void setForfatter(String forfatter) {
        this.forfatter = forfatter;
    }

    public String getUddannelse() {
        return uddannelse;
    }

    public void setUddannelse(String uddannelse) {
        this.uddannelse = uddannelse;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getStand() {
        return stand;
    }

    public void setStand(String stand) {
        this.stand = stand;
    }

    public Double getPris() {
        return pris;
    }

    public void setPris(Double pris) {
        this.pris = pris;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}