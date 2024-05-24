package com.example.projekt;

/**
 * The Bog class represents a book with various attributes such as title, author, price, education, semester, condition, image URL, and user ID.
 * This class is used to store and retrieve book information in the Firebase Realtime Database.
 */
public class Bog {
    // Public member variables to hold book attributes
    public String titel;
    public String forfatter;
    public String uddannelse;
    public String semester;
    public String stand;
    public String imageUrl;
    public String userId;
    public double pris;

    /**
     * Default constructor required for Firebase.
     * This constructor is needed for Firebase to be able to deserialize objects(firebase indeholder JSON filer "deserialize" gør at de kan konverteres tilbage).
     */
    public Bog() {
        // Default constructor required for Firebase
    }

    /**
     * Parameterized constructor to initialize a Bog object with specified attributes.
     *
     * @param titel      The title of the book.
     * @param forfatter  The author of the book.
     * @param pris       The price of the book.
     * @param uddannelse The education level or field related to the book.
     * @param semester   The semester related to the book.
     * @param stand      The condition of the book.
     * @param imageUrl   The URL of the book image.
     */
    public Bog(String titel, String forfatter, double pris, String uddannelse, String semester, String stand, String imageUrl) {
     //initialize the book info
        this.titel = titel;
        this.forfatter = forfatter;
        this.pris = pris;
        this.uddannelse = uddannelse;
        this.semester = semester;
        this.stand = stand;
        this.imageUrl = imageUrl;
    }

    /**
     * Sets the user ID associated with the book.
     * This can be useful for identifying which user added the book.
     *
     * @param userId The ID of the user.
     */
    public void setUserId(String userId) {
        this.userId = userId; // Set the user ID associated with the book
    }
}
