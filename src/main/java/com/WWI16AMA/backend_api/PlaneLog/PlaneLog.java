package com.WWI16AMA.backend_api.PlaneLog;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class PlaneLog {

    @Id
    @GenericGenerator(name = "5-digit-Id", strategy = "com.WWI16AMA.backend_api.CustomGenerator.PlaneLogIdGenerator")
    @GeneratedValue(generator = "5-digit-Id")
    private Integer id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "plane_log_id")
    private List<PlaneLogEntry> entries = new ArrayList<>();

    public PlaneLogEntry addPlaneLogEntry(PlaneLogEntry entry) {
        this.entries.add(entry);
        return entry;
    }

    public List<PlaneLogEntry> getEntries() {
        return entries;
    }

    public void setEntries(List<PlaneLogEntry> entries) {
        this.entries = entries;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
