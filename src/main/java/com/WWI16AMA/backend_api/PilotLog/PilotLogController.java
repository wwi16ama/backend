package com.WWI16AMA.backend_api.PilotLog;


import com.WWI16AMA.backend_api.Member.Member;
import com.WWI16AMA.backend_api.Member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping(path = "pilotlog")
public class PilotLogController {

    @Autowired
    private MemberRepository memberRepository;

    @GetMapping(path = "/{memberId}")
    public ResponseEntity<List> info(@PathVariable int memberId) {

        return new ResponseEntity<>(memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("A Member's Pilotlog with the memberId " + memberId + " does not exist")).getPilotLog().getPilotLogEntries(), HttpStatus.OK);
    }

    @PostMapping(path = "/{memberId}")
    public List addPilotLogEntry(@RequestBody PilotLogEntry pilotLogEntry, @PathVariable int memberId) {
        Member mem = memberRepository.findById(memberId)
                .orElseThrow(() -> new NoSuchElementException("A Member's Pilotlog with the memberId " + memberId + " does not exist"));

        PilotLog pilotLog = mem.getPilotLog();
        pilotLog.addPilotLogEntry(pilotLogEntry);

        memberRepository.save(mem);
        return pilotLog.getPilotLogEntries();
    }

}