package com.WWI16AMA.backend_api.Member;

import javax.persistence.Entity;

@Entity

public enum Office {
    VORSTANDSVORSITZENDER("Vorstandsvorsitzender"),
    SYSTEMADMINISTRATOR("Systemadministrator"),
    KASSIERER("Kassierer"),
    FLUGWART("Flugwart"),
    IMBETRIEBSKONTROLLTURMARBEITEND("ImBetriebskontrollturmArbeitend");

    private String title;

    Office(String title) {
        this.title = title;
    }

    public String title() {
        return title;
    }
}

