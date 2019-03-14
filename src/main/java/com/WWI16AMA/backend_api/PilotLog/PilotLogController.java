package com.WWI16AMA.backend_api.PilotLog;


import com.WWI16AMA.backend_api.Member.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;


@RestController
@RequestMapping(path = "pilotlog")
public class PilotLogController {

    @Autowired
    private MemberRepository memberRepository;

    @GetMapping(path = "/{id}")
    public ResponseEntity<PilotLog> detail(@PathVariable int id) {

        return new ResponseEntity<>(memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Plane with the id " + id + " does not exist")).getPilotLog(), HttpStatus.OK);
    }

}