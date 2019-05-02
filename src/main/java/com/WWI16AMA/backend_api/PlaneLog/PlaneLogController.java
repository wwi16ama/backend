package com.WWI16AMA.backend_api.PlaneLog;

import com.WWI16AMA.backend_api.Account.Transaction;
import com.WWI16AMA.backend_api.Events.EmailNotificationEvent;
import com.WWI16AMA.backend_api.Events.IntTransactionEvent;
import com.WWI16AMA.backend_api.Member.Member;
import com.WWI16AMA.backend_api.Member.MemberRepository;
import com.WWI16AMA.backend_api.Plane.Plane;
import com.WWI16AMA.backend_api.Plane.PlaneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping(path = "planeLog")
public class PlaneLogController {

    @Autowired
    private PlaneRepository planeRepository;
    @Autowired
    private PlaneLogRepository planeLogRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ApplicationEventPublisher publisher;


    @PreAuthorize("hasRole('ACTIVE')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<List> info(@PathVariable int id) {

        return new ResponseEntity<>(planeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Plane with the id " + id + " does not exist")).getPlaneLog(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ACTIVE') and #entry.memberId == principal.id")
    @PostMapping(path = "/{id}")
    public List addPlaneLogEntry(@Validated @RequestBody PlaneLogEntry entry, @PathVariable int id) {
        Plane plane = planeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Plane with the id " + id + " does not exist"));

        entry.setRefuelDateTime(LocalDateTime.now());

        Integer memberId = entry.getMemberId();
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("Member with the id " + memberId + " does not exist"));

        Transaction tr = new Transaction(entry.getFuelPrice(), "Betankungskosten für " + member.getId(),
                Transaction.FeeType.BETANKUNGSKOSTENERSTATTUNG);

        publisher.publishEvent(new IntTransactionEvent(member.getMemberBankingAccount(), tr));
        publisher.publishEvent(new EmailNotificationEvent(member, EmailNotificationEvent.Type.TANKEN, tr, plane));

        plane.addPlaneLogEntry(entry);

        planeRepository.save(plane);
        return plane.getPlaneLog();
    }

    @PreAuthorize("hasAnyRole('VORSTANDSVORSITZENDER')")
    @PutMapping(path = "/{id}/{planeLogId}")
    public ResponseEntity<PlaneLogEntry> put(@RequestBody PlaneLogEntry putPlaneLogEntry, @PathVariable int id, @PathVariable long planeLogId) {

        Plane foundPlane = planeRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Plane with the id " + id + " does not exist"));

        PlaneLogEntry foundPlaneLogEntry = planeLogRepository.findById(planeLogId).orElseThrow(() ->
                new NoSuchElementException("PlaneLogEntry with the id " + planeLogId + " does not exist"));

        if (!memberRepository.existsById(putPlaneLogEntry.getMemberId())) {
            throw new NoSuchElementException("Member with the id " + putPlaneLogEntry.getMemberId() + " does not exist");
        }

        putPlaneLogEntry.setId(foundPlaneLogEntry.getId());
        planeLogRepository.save(putPlaneLogEntry);
        planeRepository.save(foundPlane);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
