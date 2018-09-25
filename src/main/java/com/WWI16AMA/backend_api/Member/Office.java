package com.WWI16AMA.backend_api.Member;

import javax.persistence.*;


@Entity
public class Office {

    public enum OfficeName {
        FLUGWART("Flugwart"),
        IMBETRIEBSKONTROLLTURMARBEITEND("ImBetriebskontrollturmArbeitend"),
        KASSIERER("Kassierer"),
        SYSTEMADMINISTRATOR("Systemadministrator"),
        VORSTANDSVORSITZENDER("Vorstandsvorsitzender");

        private String title;

        OfficeName(String title) {
            this.title = title.toUpperCase();
        }

        public String getTitle() {
            return title;
        }


    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    private OfficeName title;

    public Office(OfficeName title) {
        this.title = title;
    }

    Office() {

    }

    public OfficeName getTitle() {
        return title;
    }

}

