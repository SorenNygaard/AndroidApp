package com.example.projekt;

/**
 * ModelRecyclerView class represents a model for data to be displayed in a RecyclerView.
 * It encapsulates information about a book, including its title, author, education, semester, condition, price, and image URL.
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
     * @param stand      The condition information related to the book.
     * @param pris       The price of the book.
     * @param imageUrl   The URL of the book's image.
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

    /**
     * Retrieves the author of the book.
     *
     * @return The author of the book.
     */
    public String getForfatter() {
        return forfatter;
    }

    /**
     * Sets the author of the book.
     *
     * @param forfatter The author of the book.
     */
    public void setForfatter(String forfatter) {
        this.forfatter = forfatter;
    }

    /**
     * Retrieves the education related to the book.
     *
     * @return The education related to the book.
     */
    public String getUddannelse() {
        return uddannelse;
    }

    /**
     * Sets the education related to the book.
     *
     * @param uddannelse The education related to the book.
     */
    public void setUddannelse(String uddannelse) {
        this.uddannelse = uddannelse;
    }

    /**
     * Retrieves the semester information related to the book.
     *
     * @return The semester information related to the book.
     */
    public String getSemester() {
        return semester;
    }

    /**
     * Sets the semester information related to the book.
     *
     * @param semester The semester information related to the book.
     */
    public void setSemester(String semester) {
        this.semester = semester;
    }

    /**
     * Retrieves the condition information related to the book.
     *
     * @return The condition information related to the book.
     */
    public String getStand() {
        return stand;
    }

    /**
     * Sets the condition information related to the book.
     *
     * @param stand The condition information related to the book.
     */
    public void setStand(String stand) {
        this.stand = stand;
    }

    /**
     * Retrieves the price of the book.
     *
     * @return The price of the book.
     */
    public Double getPris() {
        return pris;
    }

    /**
     * Sets the price of the book.
     *
     * @param pris The price of the book.
     */
    public void setPris(Double pris) {
        this.pris = pris;
    }

    /**
     * Retrieves the URL of the book's image.
     *
     * @return The URL of the book's image.
     */
    public String getImageUrl() {
        return imageUrl;
    }

    /**
     * Sets the URL of the book's image.
     *
     * @param imageUrl The URL of the book's image.
     */
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
