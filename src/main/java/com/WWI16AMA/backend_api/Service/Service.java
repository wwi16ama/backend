package com.WWI16AMA.backend_api.Service;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public abstract class Service {

    @Id
    @GeneratedValue
    private Integer id;
    @Enumerated(EnumType.STRING)
    @NotNull
    private ServiceName name;
    @NotNull
    private double gutschrift;

    public Service() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ServiceName getName() {
        return name;
    }

    /**
     * TODO
     * ausprobieren, ob der hier von Jackson genutzt wird. Wenn ja:
     * Die J_, T_-Checks ausm Konstruktor hierhin verlagern..
     */
    public void setName(ServiceName name) {
        this.name = name;
    }

    public double getGutschrift() {
        return gutschrift;
    }

    public void setGutschrift(double gutschrift) {
        this.gutschrift = gutschrift;
    }
}
