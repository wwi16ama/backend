package com.WWI16AMA.backend_api.Service;

import com.WWI16AMA.backend_api.Credit.Credit;
import com.WWI16AMA.backend_api.Credit.CreditRepository;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import java.time.Year;

@Entity
public class YearlyService extends Service {

    @NotNull
    private Year year;

    public YearlyService(ServiceName name, Year year, CreditRepository creditRepository) {
        if (name.toString().charAt(0) != 'J') {
            throw new IllegalArgumentException("Der angegebene Dienst ist kein jährlicher Dienst");
        }
        this.setName(name);
        this.year = year;
        Credit credit = creditRepository.findCreditByServiceName(name).orElseThrow(() -> new IllegalArgumentException("Keine Gebühr zum Service gefunden"));
        this.setGutschrift(credit.getAmount());
    }

    public YearlyService() {

    }

    public Year getYear() {
        return year;
    }

    public void setYear(Year year) {
        this.year = year;
    }
}
