package com.WWI16AMA.backend_api.Member;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public enum Office {
    FLUGWART("Flugwart"),
    IMBETRIEBSKONTROLLTURMARBEITEND("ImBetriebskontrollturmArbeitend"),
    KASSIERER("Kassierer"),
    SYSTEMADMINISTRATOR("Systemadministrator"),
    VORSTANDSVORSITZENDER("Vorstandsvorsitzender");

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String title;
    @ManyToMany(mappedBy = "offices")
    private List<Member> members = new ArrayList<>();

    Office(String title) {
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = title.toUpperCase();
    }

    @JsonValue
    public String getTitle() {
        return title;
    }

}

