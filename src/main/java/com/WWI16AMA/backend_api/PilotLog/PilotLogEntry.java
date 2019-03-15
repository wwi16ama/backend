package com.WWI16AMA.backend_api.PilotLog;


import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
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
    private boolean flightWithGuests;

    public PilotLogEntry(String planeNumber, String departureLocation, LocalDateTime departureTime,
                         String arrivalLocation, LocalDateTime arrivalTime, boolean flightWithGuests)
    {
        this.planeNumber = planeNumber;
        this.departureLocation = departureLocation;
        this.departureTime = departureTime;
        this.arrivalLocation = arrivalLocation;
        this.arrivalTime = arrivalTime;
        this.flightWithGuests = flightWithGuests;
    }

    public PilotLogEntry(){
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

    public String getArrvialLocation() {
        return arrivalLocation;
    }

    public void setArrvialLocation(String arrvialLocation) {
        this.arrivalLocation = arrvialLocation;
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
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
}
