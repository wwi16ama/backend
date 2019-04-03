package com.WWI16AMA.backend_api.PlaneLog;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;

@Entity
public class PlaneLogEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDateTime refuelDateTime;
    @NotNull
    private Integer memberId;
    @NotNull
    @Pattern(regexp = ".{0,35}")
    private String location;
    @NotNull
    private float startCount;
    @NotNull
    private float endCount;
    @NotNull
    @PositiveOrZero
    private float fuelPrice;

    public PlaneLogEntry(LocalDateTime refuelDateTime, @NotNull Integer memberId, @NotNull String location, @NotNull float startCount, @NotNull float endCount, @NotNull float fuelPrice) {
        this.refuelDateTime = refuelDateTime;
        this.location = location;
        this.startCount = startCount;
        this.endCount = endCount;
        this.fuelPrice = fuelPrice;
        this.memberId = memberId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getRefuelDateTime() {
        return refuelDateTime;
    }

    public Integer getMemberId() {
        return memberId;
    }

    public void setMemberId(Integer memberId) {
        this.memberId = memberId;
    }

    public void setRefuelDateTime(LocalDateTime refuelDateTime) {
        this.refuelDateTime = refuelDateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public float getStartCount() {
        return startCount;
    }

    public void setStartCount(float startCount) {
        this.startCount = startCount;
    }

    public float getEndCount() {
        return endCount;
    }

    public void setEndCount(float endCount) {
        this.endCount = endCount;
    }

    public float getFuelPrice() {
        return fuelPrice;
    }

    public void setFuelPrice(float fuelPrice) {
        this.fuelPrice = fuelPrice;
    }

    public PlaneLogEntry() {
    }

}
