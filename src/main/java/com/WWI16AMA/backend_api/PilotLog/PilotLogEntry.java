package com.WWI16AMA.backend_api.PilotLog;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
public class PilotLogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long flightId;
    @NotBlank
    private String planeNumber;
    @NotBlank
    @Pattern(regexp = "[a-zA-Z_äÄöÖüÜß0-9/. \\-\\(\\)\\.]{0,35}")
    private String departureLocation;
    @NotNull
    @Past
    private LocalDateTime departureTime;
    @NotBlank
    @Pattern(regexp = "[a-zA-Z_äÄöÖüÜß0-9/. \\-\\(\\)\\.]{0,35}")
    private String arrivalLocation;
    @NotNull
    @Past
    private LocalDateTime arrivalTime;
    @NotNull
    private boolean flightWithGuests;
    @PositiveOrZero
    @Max(99)
    private int usageTime;
    @NotNull
    private double flightPrice;


    public PilotLogEntry(String planeNumber, String departureLocation, LocalDateTime departureTime,
                         String arrivalLocation, LocalDateTime arrivalTime, boolean flightWithGuests, int usageTime) {
        this.planeNumber = planeNumber;
        this.departureLocation = departureLocation;
        this.departureTime = departureTime;
        this.arrivalLocation = arrivalLocation;
        this.arrivalTime = arrivalTime;
        this.flightWithGuests = flightWithGuests;
        this.usageTime = usageTime;
    }

    public PilotLogEntry() {
    }

    public long getFlightId() {
        return flightId;
    }

    public void setFlightId(long flightId) {
        this.flightId = flightId;
    }

    public String getPlaneNumber() {
        return planeNumber;
    }

    public void setPlaneNumber(String planeNumber) {
        this.planeNumber = planeNumber;
    }

    public String getDepartureLocation() {
        return departureLocation;
    }

    public void setDepartureLocation(String departureLocation) {
        this.departureLocation = departureLocation;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalLocation() {
        return arrivalLocation;
    }

    public void setArrivalLocation(String arrivalLocation) {
        this.arrivalLocation = arrivalLocation;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public double getFlightPrice() {
        return flightPrice;
    }

    public void setFlightPrice(double flightPrice) {
        this.flightPrice = flightPrice;
    }

    public int getUsageTime() {
        return usageTime;
    }

    public void setUsageTime(int usageTime) {
        this.usageTime = usageTime;
    }

    public boolean isFlightWithGuests() {
        return flightWithGuests;
    }

    public void setFlightWithGuests(boolean flightWithGuests) {
        this.flightWithGuests = flightWithGuests;
    }
}