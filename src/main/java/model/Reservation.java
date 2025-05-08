package model;

import java.sql.Timestamp;

public class Reservation {
    private int id;
    private int userId;
    private int eventId;
    private int quantity;
    private String status; // PENDING, CONFIRMED, CANCELLED
    private Timestamp reservationTime;

    // Optional: event name (for display)
    private String eventName;

    // Constructors
    public Reservation() {}

    public Reservation(int id, int userId, int eventId, int quantity, String status, Timestamp reservationTime) {
        this.id = id;
        this.userId = userId;
        this.eventId = eventId;
        this.quantity = quantity;
        this.status = status;
        this.reservationTime = reservationTime;
    }

    public Reservation(int id, int userId, int eventId, int quantity, String status, Timestamp reservationTime, String eventName) {
        this(id, userId, eventId, quantity, status, reservationTime);
        this.eventName = eventName;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getEventId() {
        return eventId;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(Timestamp reservationTime) {
        this.reservationTime = reservationTime;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }
}
