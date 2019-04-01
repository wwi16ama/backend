package com.WWI16AMA.backend_api.Service;

import com.WWI16AMA.backend_api.Credit.Credit;
import com.WWI16AMA.backend_api.Credit.CreditRepository;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;


@Entity
public class DailyService extends Service {

    @NotNull
    private LocalDate startTime;
    // Darf Null sein
    private LocalDate endTime;

    public DailyService(ServiceName name, LocalDate startTime, CreditRepository creditRepository) {
        if (name.toString().charAt(0) != 'T') {
            throw new IllegalArgumentException("Der angegebene Dienst ist kein täglicher Dienst");
        }
        this.setName(name);
        this.startTime = startTime;
        Credit credit = creditRepository.findCreditByServiceName(name).orElseThrow(() -> new IllegalArgumentException("Keine Gebühr zum Service gefunden"));
        this.setGutschrift(credit.getAmount());
    }

    public DailyService(ServiceName name, LocalDate startTime, LocalDate endTime, CreditRepository creditRepository) {
        this(name, startTime, creditRepository);
        if (!startTime.isBefore(endTime)) {
            throw new IllegalArgumentException("Der Endzeitpunkt liegt hinter dem Startzeitpunkt!");
        }
        this.endTime = endTime;
        Credit credit = creditRepository.findCreditByServiceName(name).orElseThrow(() -> new IllegalArgumentException("Keine Gebühr zum Service gefunden"));
        this.setGutschrift(credit.getAmount() * ChronoUnit.DAYS.between(startTime, endTime));
    }

    public DailyService() {

    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
    }
}
