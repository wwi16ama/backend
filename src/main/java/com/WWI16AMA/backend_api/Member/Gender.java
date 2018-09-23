package com.WWI16AMA.backend_api.Member;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public enum Gender {

    MALE("male"),
    FEMALE("female"),
    OTHER("other");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;

    Gender(String title) {
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = title.toUpperCase();
    }

    public String getTitle() {
        return title;
    }
}
