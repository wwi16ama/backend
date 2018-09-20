package com.WWI16AMA.backend_api.Member;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

@Entity
public class Office{

public enum officeName {
    FLUGWART("Flugwart"),
    IMBETRIEBSKONTROLLTURMARBEITEND("ImBetriebskontrollturmArbeitend"),
    KASSIERER("Kassierer"),
    SYSTEMADMINISTRATOR("Systemadministrator"),
    VORSTANDSVORSITZENDER("Vorstandsvorsitzender");

    @Enumerated(EnumType.STRING)
    private String title;

    officeName(String title) {
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
    @Column(name="office_name")
    officeName officeName;

    public Office.officeName getOfficeName() {
        return officeName;
    }

    public void setOfficeName(Office.officeName officeName) {
        this.officeName = officeName;
    }

    public Office(Office.officeName officeName) {
        this.officeName = officeName;
    }

    Office(){

    }
}

