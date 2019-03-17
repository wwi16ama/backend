package com.WWI16AMA.backend_api.Fee;

import javax.persistence.*;

@Entity
public class Fee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Enumerated(EnumType.STRING)
    private Fee.Status category;

    private int fee;

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

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public enum Status {
        ACTIVE,
        U20ACTIVE,
        PASSIVE,
        HONORARYMEMBER
    }


}
