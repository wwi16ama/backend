package com.WWI16AMA.backend_api.PilotLog;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Entity
public class PilotLogEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long flightId;
    @NotBlank
    private String planeNumber;
    @NotBlank
    @Pattern(regexp = ".{0,35}")
    private String departureLocation;
    @NotNull
    private LocalDateTime departureTime;
    @NotBlank
    @Pattern(regexp = ".{0,35}")
    private String arrivalLocation;
    @NotNull
    private LocalDateTime arrivalTime;
    @NotNull
    private boolean flightWithGuests;
    @PositiveOrZero
    private int usageTime;
    @NotNull
    private double airfair;


    public PilotLogEntry(String planeNumber, String departureLocation, LocalDateTime departureTime,
                         String arrivalLocation, LocalDateTime arrivalTime, boolean flightWithGuests, int usageTime)
    {
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

    public double getAirfair() {
        return airfair;
    }

    public int getUsageTime() {
        return usageTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public boolean isFlightWithGuests() {
        return flightWithGuests;
    }

    public void setFlightWithGuests(boolean flightWithGuests) {
        this.flightWithGuests = flightWithGuests;
    }

    public void setUsageTime(int usageTime) {
        this.usageTime = usageTime;
    }

    public void setAirfair(double airfair) {
        this.airfair = airfair;
    }
}