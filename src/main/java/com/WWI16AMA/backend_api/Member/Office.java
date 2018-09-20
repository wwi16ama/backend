package com.WWI16AMA.backend_api.Member;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.*;

@Entity
public class Office {

    public enum OfficeName {
        FLUGWART("Flugwart"),
        IMBETRIEBSKONTROLLTURMARBEITEND("ImBetriebskontrollturmArbeitend"),
        KASSIERER("Kassierer"),
        SYSTEMADMINISTRATOR("Systemadministrator"),
        VORSTANDSVORSITZENDER("Vorstandsvorsitzender");

        @Enumerated(EnumType.STRING)
        private String title;

        OfficeName(String title) {
            this.title = title.toUpperCase();
        }

        public void setTitle(String title) {
            this.title = title.toUpperCase();
        }

        @JsonValue
        public String getTitle() {
            return title;
        }


    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "office_name")
    OfficeName officeName;

    public OfficeName getOfficeName() {
        return officeName;
    }

    public void setOfficeName(OfficeName officeName) {
        this.officeName = officeName;
    }

    public Office(OfficeName officeName) {
        this.officeName = officeName;
    }

    Office() {

    }
}

