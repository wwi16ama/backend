package com.WWI16AMA.backend_api.Member;

import javax.persistence.*;


@Entity
public class Office {

    @Enumerated(EnumType.STRING)
    private Title title;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    Office() {

    }

    public Office(Title title) {
        this.title = title;
    }

    public Title getTitle() {
        return title;
    }

    public enum Title {
        FLUGWART,
        IMBETRIEBSKONTROLLTURMARBEITEND,
        KASSIERER,
        SYSTEMADMINISTRATOR,
        VORSTANDSVORSITZENDER
    }

}

