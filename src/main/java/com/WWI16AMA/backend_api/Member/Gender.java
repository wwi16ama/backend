package com.WWI16AMA.backend_api.Member;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public enum Gender {

    MALE("male"),
    FEMALE("female");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;

    Gender(String title) {
        this.title = title.toUpperCase(); //TODO prüfen, ob hier nun male auch in lowercase akzeptiert wird
    }

    public void setTitle(String title) {
        this.title = title.toUpperCase();
    }

    public String getTitle() {
        return title;
    }
}
