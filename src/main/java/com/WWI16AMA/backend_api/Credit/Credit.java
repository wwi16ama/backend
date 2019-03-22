package com.WWI16AMA.backend_api.Credit;

import com.WWI16AMA.backend_api.Service.ServiceName;

import javax.persistence.*;

@Entity
public class Credit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Enumerated
    private ServiceName serviceName;
    private double amount;
    @Enumerated
    private Period period;

    public Credit() {

    }

    public Credit(ServiceName serviceName, double amount, Period period) {
        this.serviceName = serviceName;
        this.amount = amount;
        this.period = period;
    }

    public ServiceName getServiceName() {
        return serviceName;
    }

    public void setServiceName(ServiceName serviceName) {
        this.serviceName = serviceName;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }
}
