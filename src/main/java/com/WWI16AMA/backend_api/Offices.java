package com.WWI16AMA.backend_api;

import javax.persistence.Entity;

@Entity
public class Offices {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {

        if (name.equals("Vorstandsvorsitzender")||name.equals("Systemadministrator")|| name.equals("Kassierer")||name.equals("Flugwart")||name.equals("ImBetriebskontrollturmArbeitend")) {

            this.name = name;

        } else {
            //TODO: Fehler in den Daten => Error
        }
    }

    public Offices () {

    }
}
