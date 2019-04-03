package com.WWI16AMA.backend_api.Service;

import com.WWI16AMA.backend_api.Credit.CreditRepository;
import com.WWI16AMA.backend_api.Member.Member;
import com.WWI16AMA.backend_api.Member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "services")
public class ServiceController {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CreditRepository creditRepository;


    @PreAuthorize("hasAnyRole('VORSTANDSVORSITZENDER', 'SYSTEMADMINISTRATOR') or #id == principal.id")
    @GetMapping(value = "/{id}")
    public List<? extends Service> getServiceList(@PathVariable Integer id) {

        return memberRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Kein Member unter dieser Nummer"))
                .getServices();
    }

    @PreAuthorize("hasAnyRole('VORSTANDSVORSITZENDER', 'SYSTEMADMINISTRATOR')")
    @PostMapping(value = "/daily/{id}")
    public ResponseEntity<Void> addDailyService(@PathVariable Integer id, @RequestBody DailyService service) {
        Member member = memberRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Kein Member unter dieser Nummer"));
        DailyService dailyService;
        if (service.getEndTime() == null) {
            dailyService = new DailyService(service.getName(), service.getStartTime(), creditRepository);
        } else {
            dailyService = new DailyService(service.getName(), service.getStartTime(), service.getEndTime(), creditRepository);
        }
        member.getServices().add(dailyService);
        memberRepository.save(member);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAnyRole('VORSTANDSVORSITZENDER', 'SYSTEMADMINISTRATOR')")
    @PostMapping(value = "/yearly/{id}")
    public ResponseEntity<Void> addYearlyService(@PathVariable Integer id, @RequestBody YearlyService service) {
        Member member = memberRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Kein Member unter dieser Nummer"));
        YearlyService yearlyService = new YearlyService(service.getName(), service.getYear(), creditRepository);
        member.getServices().add(yearlyService);
        memberRepository.save(member);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
