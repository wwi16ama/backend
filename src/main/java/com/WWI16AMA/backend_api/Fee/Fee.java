package com.WWI16AMA.backend_api.Fee;

import javax.persistence.*;

@Entity
public class Fee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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

    public int getId() {
        return id;
    }

    public Status getCategory() {
        return category;
    }

    public enum Status {
        ACTIVE,
        U20ACTIVE,
        PASSIVE,
        SPECIAL
    }




}
