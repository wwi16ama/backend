package com.WWI16AMA.backend_api.PilotLog;


import com.WWI16AMA.backend_api.Account.Transaction;
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
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping(path = "pilotlog")
public class PilotLogController {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PlaneRepository planeRepository;
    @Autowired
    private ApplicationEventPublisher publisher;

    @PreAuthorize("hasRole('ACTIVE') and #memberId == principal.id")
    @GetMapping(path = "/{memberId}")
    public ResponseEntity<List> info(@PathVariable int memberId) {

        return new ResponseEntity<>(memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("A Member's Pilotlog with the memberId " + memberId + " does not exist")).getPilotLog().getPilotLogEntries(), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ACTIVE') and #memberId == principal.id")
    @PostMapping(path = "/{memberId}/pilotlogentry")
    public PilotLogEntry addPilotLogEntry(@RequestBody PilotLogEntry pilotLogEntry, @PathVariable int memberId) {

        if (pilotLogEntry.getFlightId() != 0L) {
            throw new IllegalArgumentException("PilotLogEntry has the ID: " + pilotLogEntry.getFlightId() +
                    ". Id has to be null when a new PilotLogEntry shall be created");
        }
        if (pilotLogEntry.getArrivalTime().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("This PilotLogEntry has an invalid Arrival Time: " + pilotLogEntry.getArrivalTime() +
                    ". The Arrival Time has to be earlier than today. ");
        }
        if (pilotLogEntry.getDepartureTime().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("This PilotLogEntry has an invalid Departure Time: " + pilotLogEntry.getDepartureTime() +
                    ". The Departure Time has to be earlier than today. ");
        }

        if (pilotLogEntry.getDepartureTime().isAfter(pilotLogEntry.getArrivalTime())) {
            throw new IllegalArgumentException("This PilotLogEntry has an invalid Departure Time: " + pilotLogEntry.getDepartureTime() +
                    ". The Depature Time has to be earlier than the Arrival Time (" + pilotLogEntry.getArrivalTime() + "). ");
        }

        if (pilotLogEntry.getFlightPrice() != 0L) {
            throw new IllegalArgumentException(("Airfair has to be null when a new PilotLogEntry shall be created"));
        }

        Member mem = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("A Member's Pilotlog with the memberId " + memberId + " does not exist"));

        Plane plane = planeRepository.findByNumber(pilotLogEntry.getPlaneNumber())
                .orElseThrow(() -> new NoSuchElementException(("A Plane with the number " + pilotLogEntry.getPlaneNumber() + " does not exist.")));

        PilotLog pilotLog = mem.getPilotLog();


        if (!pilotLogEntry.isFlightWithGuests()) {

            long minutes = ChronoUnit.MINUTES.between(pilotLogEntry.getDepartureTime(), pilotLogEntry.getArrivalTime());

            pilotLogEntry.setFlightPrice(plane.getPricePerFlightMinute() * minutes + pilotLogEntry.getUsageTime() * plane.getPricePerBookedHour());

            double price = -pilotLogEntry.getFlightPrice();

            Transaction tr = new Transaction(price, "Mitgliedsnummer: " + memberId + " Flug: " + pilotLog.getLastEntry().getFlightId() + 1, Transaction.FeeType.GEBÜHRFLUGZEUG);
            publisher.publishEvent(new IntTransactionEvent(mem.getMemberBankingAccount(), tr));

        } else {
            pilotLogEntry.setFlightPrice(0);
        }

        pilotLog.addPilotLogEntry(pilotLogEntry);


        memberRepository.save(mem);
        return pilotLog.getLastEntry();

    }

}