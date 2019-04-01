package com.WWI16AMA.backend_api.PlaneLog;

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
    private MemberRepository memberRepository;

    @Autowired
    private ApplicationEventPublisher publisher;



    @GetMapping(path = "/{id}")
    public ResponseEntity<List> info(@PathVariable int id) {

        return new ResponseEntity<>(planeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Plane with the id " + id + " does not exist")).getPlaneLog(), HttpStatus.OK);
    }



    @PostMapping(path = "/{id}")
    public List addPlaneLogEntry(@RequestBody PlaneLogEntry entry, @PathVariable int id) {
        Plane plane = planeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Plane with the id " + id + " does not exist"));

        if (entry.getRefuelDateTime().isAfter(LocalDateTime.now())) {
            throw new IllegalArgumentException("Refuel Date cant be in future.");
        }
    Integer memberId = entry.getMemberId();
        //Member member = memberRepository.findById(entry.getMemberId()).orElseThrow(() -> new NoSuchElementException("Member with the id " + entry.getMemberId() + " does not exist"));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() ->
                new NoSuchElementException("Member with the id " + memberId + " does not exist"));
       publisher.publishEvent(new IntTransactionEvent(
               member.getMemberBankingAccount(),
               new Transaction(entry.getTotalPrice(), Transaction.FeeType.BETANKUNGSKOSTEN)));
        plane.addPlaneLogEntry(entry);


        planeRepository.save(plane);
        return plane.getPlaneLog();
    }


}
