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

        public void setTitle(String title) {
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

