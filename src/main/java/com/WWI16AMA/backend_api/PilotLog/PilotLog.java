package com.WWI16AMA.backend_api.PilotLog;


import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PilotLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "pilot_log_id")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<PilotLogEntry> pilotLogEntries = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<PilotLogEntry> getPilotLogEntries() {
        return pilotLogEntries;
    }

    public void setPilotLogEntry(List<PilotLogEntry> pilotLogEntries) {
        this.pilotLogEntries = pilotLogEntries;
    }

    public void addPilotLogEntry(PilotLogEntry pilotLogEntry){
        this.pilotLogEntries.add(pilotLogEntry);
    }
}
