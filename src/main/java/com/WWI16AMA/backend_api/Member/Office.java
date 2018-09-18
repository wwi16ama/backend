package com.WWI16AMA.backend_api.Member;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Office{

public enum officeName {
    FLUGWART("Flugwart"),
    IMBETRIEBSKONTROLLTURMARBEITEND("ImBetriebskontrollturmArbeitend"),
    KASSIERER("Kassierer"),
    SYSTEMADMINISTRATOR("Systemadministrator"),
    VORSTANDSVORSITZENDER("Vorstandsvorsitzender");

    private String title;
    @ManyToMany(mappedBy = "offices")
    private List<Member> members = new ArrayList<>();

    officeName(String title) {
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

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @Enumerated(EnumType.STRING)
    officeName officeName;

    public Office.officeName getOfficeName() {
        return officeName;
    }

    public void setOfficeName(Office.officeName officeName) {
        this.officeName = officeName;
    }


}

