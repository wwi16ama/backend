package com.WWI16AMA.backend_api.PlaneLog;

import com.WWI16AMA.backend_api.Member.MemberRepository;
import com.WWI16AMA.backend_api.Plane.Plane;
import com.WWI16AMA.backend_api.Plane.PlaneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping(path = "planeLog")
public class PlaneLogController {

    @Autowired
    private PlaneRepository planeRepository;
    private MemberRepository memberRepository;


    @PreAuthorize("hasRole('ACTIVE') and #id == principal.id")
    @GetMapping(path = "/{id}")
    public ResponseEntity<List> info(@PathVariable int id) {

        return new ResponseEntity<>(planeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Plane with the id " + id + " does not exist")).getPlaneLog(), HttpStatus.OK);
    }


    @PreAuthorize("hasRole('ACTIVE') and #id == principal.id")
    @PostMapping(path = "/{id}")
    public List addPlaneLogEntry(@RequestBody PlaneLogEntry entry, @PathVariable int id) {
        Plane plane = planeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Plane with the id " + id + " does not exist"));

        if (entry.getRefuelDateTime().isAfter(LocalDateTime.now())) {
            throw new NoSuchElementException("Refuel Date cant be in future.");
        }

        plane.addPlaneLogEntry(entry);


        planeRepository.save(plane);
        return plane.getPlaneLog();
    }


}
