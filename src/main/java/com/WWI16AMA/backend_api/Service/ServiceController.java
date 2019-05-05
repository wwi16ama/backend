package com.WWI16AMA.backend_api.Service;

import com.WWI16AMA.backend_api.Credit.CreditRepository;
import com.WWI16AMA.backend_api.Member.Member;
import com.WWI16AMA.backend_api.Member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.WWI16AMA.backend_api.Billing.BillingTask.getNextBillingDate;
import static com.WWI16AMA.backend_api.Billing.BillingTask.isInCurrentBillingPeriod;

@RestController
@RequestMapping(path = "services")
public class ServiceController {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CreditRepository creditRepository;


    @PreAuthorize("hasAnyRole('VORSTANDSVORSITZENDER', 'SYSTEMADMINISTRATOR') or #id == principal.id")
    @GetMapping(value = "/{id}")
    public List<Service> getServiceList(@PathVariable Integer id) {

        return memberRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Kein Member unter dieser Nummer"))
                .getServices();
    }

    @PreAuthorize("hasAnyRole('VORSTANDSVORSITZENDER', 'SYSTEMADMINISTRATOR')")
    @PostMapping(value = "/{id}")
    public ResponseEntity<Void> addDailyService(@PathVariable Integer id, @Validated @RequestBody Service service) {


        Member member = memberRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Kein Member unter dieser Nummer"));

        boolean isYearly = service.getName().toString().charAt(0) == 'J';

        if (!service.getStartDate().isBefore(service.getEndDate().plusDays(1))) {
            throw new IllegalArgumentException("Der Endzeitpunkt liegt zeitlich vor dem Startzeitpunkt!");
        }

        if (!isInCurrentBillingPeriod(service.getStartDate()) || !isInCurrentBillingPeriod(service.getEndDate())) {
            throw new IllegalArgumentException("Der angegebene Zeitraum liegt nicht vollständig im aktuellen " +
                    "Abrechnungszeitraum!");
        }

        // Sonderprüfungen für jährliche Services
        if (isYearly) {

            if (!service.getStartDate().equals(getNextBillingDate().minusYears(1))
                    || !service.getEndDate().equals(getNextBillingDate().minusDays(1))) {
                throw new IllegalArgumentException("Jährliche Services müssen immer vom 01.02. des " +
                        "aktuellen Abrechnungsjahres bis zum 31.01. gehen");
            }

            if (isDuplicateYearlyService(member.getServices(), service)) {
                throw new IllegalArgumentException("Dieser Member leistet diesen jährlichen Dienst bereits!");
            }
        }

        double amount = creditRepository.findCreditByServiceName(service.getName())
                .orElseThrow(() -> new IllegalArgumentException("Keine Gebühr zum Service gefunden"))
                .getAmount();
        // Ein Tagesservice hat gleiches Start- und Enddate, bei .between(d1, d2) is d2 aber exklusiv
        double gutschrift = isYearly ? amount : amount * ChronoUnit.DAYS.between(service.getStartDate(), service.getEndDate().plusDays(1));
        Service dailyService = new Service(service.getName(), service.getStartDate(), service.getEndDate(), gutschrift);
        member.getServices().add(dailyService);
        memberRepository.save(member);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    private boolean isDuplicateYearlyService(List<Service> list, Service newService) {

        // Suche alle _alten_ Services mit gleichem Namen, Start- und Enddatum
        List<Service> filteredList = list.stream().filter((service -> service.getName().equals(newService.getName())))
                .filter((service -> service.getStartDate().equals(newService.getStartDate())))
                .filter((service -> service.getEndDate().equals(newService.getEndDate())))
                .collect(Collectors.toList());
        // Wenn's welche gibt, ist der neue Eintrag ein duplikat
        return filteredList.size() != 0;
    }
}
