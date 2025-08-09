package com.example.comp200pt1;

import java.io.Serializable;

// model for a single book request. serializable so I can pass it between activities
public class BookRequest implements Serializable {
    private final String title;
    private final String requestedBy;
    private final String requestedDate;
    private String estText;
    private String pickupText;
    // Current state of the request
    private boolean approved;

    // Build a request
    public BookRequest(String title, String requestedBy, String requestedDate, String estText, boolean approved) {
        this.title = title;
        this.requestedBy = requestedBy;
        this.requestedDate = requestedDate;
        this.estText = estText;
        this.pickupText = "";
        this.approved = approved;
    }


    // Getters
    public String getTitle() { return title; }
    public String getRequestedBy() { return requestedBy; }
    public String getRequestedDate() { return requestedDate; }
    public String getEstText() { return estText; }
    public String getPickupText() { return pickupText; }
    public boolean isApproved() { return approved; }

    // Mark as approved and update the displayed text
    public void approve(String pickupText) {
        this.approved = true;
        this.pickupText = pickupText;
        this.estText = "";
    }
}
