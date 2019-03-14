package com.WWI16AMA.backend_api.PilotLog;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class PilotLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

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
}
