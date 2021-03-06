package com.WWI16AMA.backend_api.Member;

import javax.persistence.*;
import java.util.List;


@Entity
public class Office {

    @Enumerated(EnumType.STRING)
    private Title title;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToMany(mappedBy = "offices")
    private List<Member> members;

    Office() {

    }

    public Office(Title title) {
        this.title = title;
    }

    public Title getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return getTitle().toString();
    }

    public enum Title {
        FLUGWART,
        IMBETRIEBSKONTROLLTURMARBEITEND,
        KASSIERER,
        SYSTEMADMINISTRATOR,
        VORSTANDSVORSITZENDER
    }

}

