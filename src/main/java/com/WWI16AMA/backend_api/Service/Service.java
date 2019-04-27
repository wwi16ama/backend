package com.WWI16AMA.backend_api.Service;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@Entity
public class Service {

    @Id
    @GeneratedValue
    private Integer id;
    @Enumerated(EnumType.STRING)
    @NotNull
    private ServiceName name;
    @NotNull
    private double gutschrift;
    @NotNull
    private LocalDate startDate;
    @NotNull
    private LocalDate endDate;


    public Service(ServiceName name, LocalDate startDate, LocalDate endDate, double gutschrift) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.gutschrift = gutschrift;
    }

    public Service() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ServiceName getName() {
        return name;
    }

    public void setName(ServiceName name) {
        this.name = name;
    }

    public double getGutschrift() {
        return gutschrift;
    }

    public void setGutschrift(double gutschrift) {
        this.gutschrift = gutschrift;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
}
