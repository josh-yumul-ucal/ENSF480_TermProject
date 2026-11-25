/**
 * ENSF480 Group 1
 * Final Project: Flight Management System
 * Due Date: November 30, 2026 at 2PM MT
 * Project Members:
 *      Tyler Birkbeck
 *      Josh Yumul
 *      Li Tan
 *      Mustafa Abeidi
 */

package model;

import javafx.beans.property.*;
import java.util.Date;

/**
 * Model class representing a promotional news item.
 * Contains the date and text information for promotional announcements.
 */
public class Promotion {
    private final IntegerProperty promotionId;
    private final ObjectProperty<Date> promotionDate;
    private final StringProperty promotionText;

    /**
     * Default constructor for creating a promotion object.
     */
    public Promotion() {
        this.promotionId = new SimpleIntegerProperty();
        this.promotionDate = new SimpleObjectProperty<>();
        this.promotionText = new SimpleStringProperty();
    }

    /**
     * Constructor for creating a promotion object with predefined values.
     * @param promotionId The unique ID of the promotion
     * @param promotionDate The date when the promotion is to be effective
     * @param promotionText The text of the promotion
     */
    public Promotion(int promotionId, Date promotionDate, String promotionText) {
        this.promotionId = new SimpleIntegerProperty(promotionId);
        this.promotionDate = new SimpleObjectProperty<>(promotionDate);
        this.promotionText = new SimpleStringProperty(promotionText);
    }

    /**
     * Gets the unique identifier of the promotion.
     * @return The promotion ID
     */
    public int getPromotionId() { return promotionId.get(); }

    /**
     * Sets the unique identifier of the promotion.
     * @param promotionId The new promotion ID
     */
    public void setPromotionId(int promotionId) { this.promotionId.set(promotionId); }

    /**
     * Gets the JavaFX property for promotion ID.
     * @return The promotion ID property
     */
    public IntegerProperty promotionIdProperty() { return promotionId; }

    /**
     * Gets the date the promotion is effective.
     * @return The promotion date
     */
    public Date getPromotionDate() { return promotionDate.get(); }

    /**
     * Sets the date when the promotion is effective.
     * @param promotionDate The new promotion date
     */
    public void setPromotionDate(Date promotionDate) { this.promotionDate.set(promotionDate); }

    /**
     * Gets the JavaFX property for promotion date.
     * @return The promotion date property
     */
    public ObjectProperty<Date> promotionDateProperty() { return promotionDate; }

    /**
     * Gets the content of the promotion.
     * @return The promotion text
     */
    public String getPromotionText() { return promotionText.get(); }

    /**
     * Sets the content of the promotion.
     * @param promotionText The promotion text
     */
    public void setPromotionText(String promotionText) { this.promotionText.set(promotionText); }

    /**
     * Gets the JavaFX property for the promotion text.
     * @return The promotion text property
     */
    public StringProperty promotionTextProperty() { return promotionText; }
}

