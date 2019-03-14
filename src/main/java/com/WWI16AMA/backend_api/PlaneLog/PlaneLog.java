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
    private int id;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "plane_log_id")
    private List<PlaneLogEntry> entrys = new ArrayList<>();

    public void addPlaneLogEntry(PlaneLogEntry entry) {
        entrys.add(entry);
    }
}
