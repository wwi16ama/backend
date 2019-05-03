package com.WWI16AMA.backend_api.Fee;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Fee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Fee.Status category;

    @NotNull
    private double fee;

    public Fee(Status category, int fee) {
        this.category = category;
        this.fee = fee;
    }

    public Fee(Status category) {
        this.category = category;
    }

    Fee() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getCategory() {
        return category;
    }

    public void setCategory(Status category) {
        this.category = category;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public enum Status {
        ACTIVE,
        U20ACTIVE,
        PASSIVE,
        HONORARYMEMBER
    }


}
