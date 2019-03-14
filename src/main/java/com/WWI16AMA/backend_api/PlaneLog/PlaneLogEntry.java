package com.WWI16AMA.backend_api.PlaneLog;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
public class PlaneLogEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDateTime refuelDateTime;
    @NotNull
    private String location;
    @NotNull
    private float startCount;
    @NotNull
    private float endCount;
    @NotNull
    private float totalPrice;

    public PlaneLogEntry(LocalDateTime refuelDateTime, @NotNull String location, @NotNull float startCount, @NotNull float endCount, @NotNull float totalPrice) {
        this.refuelDateTime = refuelDateTime;
        this.location = location;
        this.startCount = startCount;
        this.endCount = endCount;
        this.totalPrice = totalPrice;
    }

    public PlaneLogEntry() {
    }

}
