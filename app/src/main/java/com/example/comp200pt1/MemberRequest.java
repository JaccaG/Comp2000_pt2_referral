package com.example.comp200pt1;

import java.io.Serializable;

// Model for book request seen by members in their "My Request" screen
public class MemberRequest implements Serializable {
    private final String title;
    private final String requestedDate;
    private final String status;        // "pending", "approved", "denied"
    private String estText;       // pending
    private String pickupHint;    // pending extra line
    private String approvedDate;  // approved
    private String pickupBy;      // approved
    private String deniedDate;    // denied
    private String denyReason;    // denied

    public MemberRequest(String title, String requestedDate, String status) {
        this.title = title;
        this.requestedDate = requestedDate;
        this.status = status;
    }

    // Getters
    public String getTitle() { return title; }
    public String getRequestedDate() { return requestedDate; }
    public String getStatus() { return status; }
    public String getEstText() { return estText; }
    public String getPickupHint() { return pickupHint; }
    public String getApprovedDate() { return approvedDate; }
    public String getPickupBy() { return pickupBy; }
    public String getDeniedDate() { return deniedDate; }
    public String getDenyReason() { return denyReason; }

    // Setters - fills in status details
    public MemberRequest setPendingDetails(String estText, String pickupHint) {
        this.estText = estText; this.pickupHint = pickupHint; return this;
    }
    public MemberRequest setApprovedDetails(String approvedDate, String pickupBy) {
        this.approvedDate = approvedDate; this.pickupBy = pickupBy; return this;
    }
    public MemberRequest setDeniedDetails(String deniedDate, String denyReason) {
        this.deniedDate = deniedDate; this.denyReason = denyReason; return this;
    }
}
