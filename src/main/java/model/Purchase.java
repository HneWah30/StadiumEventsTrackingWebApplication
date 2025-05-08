package model;

public class Purchase {
    private int purchaseId;
    private int userId;
    private int eventId;
    private int quantity;
    

    // Constructor
    public Purchase(int userId, int eventId, int quantity) {
        this.userId = userId;
        this.eventId = eventId;
        this.quantity = quantity;
    }

    // Full Constructor (optional, in case you want to use purchaseId too)
    public Purchase(int purchaseId, int userId, int eventId, int quantity) {
        this.purchaseId = purchaseId;
        this.userId = userId;
        this.eventId = eventId;
        this.quantity = quantity;
    }

    // Getters
    public int getPurchaseId() {
        return purchaseId;
    }

    public int getUserId() {
        return userId;
    }

    public int getEventId() {
        return eventId;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setters
    public void setPurchaseId(int purchaseId) {
        this.purchaseId = purchaseId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
