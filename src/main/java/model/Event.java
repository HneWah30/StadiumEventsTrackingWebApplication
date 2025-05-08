package model;

import java.io.Serializable;
import java.sql.Timestamp;

public class Event implements Serializable {
    private int id;
    private String name;
    private Timestamp dateTime;
    private String type;
    private String description;
    private int ticketsAvailable;

    public Event(int id, String name, Timestamp dateTime, String type, String description, int ticketsAvailable) {
        this.id = id;
        this.name = name;
        this.dateTime = dateTime;
        this.type = type;
        this.description = description;
        this.ticketsAvailable = ticketsAvailable;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public Timestamp getDateTime() { return dateTime; }
    public String getType() { return type; }
    public String getDescription() { return description; }
    public int getTicketsAvailable() { return ticketsAvailable; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setDateTime(Timestamp dateTime) { this.dateTime = dateTime; }
    public void setType(String type) { this.type = type; }
    public void setDescription(String description) { this.description = description; }
    public void setTicketsAvailable(int ticketsAvailable) { this.ticketsAvailable = ticketsAvailable; }
}
