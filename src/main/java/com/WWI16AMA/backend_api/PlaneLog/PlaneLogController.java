package com.WWI16AMA.backend_api.PlaneLog;

import com.WWI16AMA.backend_api.Member.MemberRepository;
import com.WWI16AMA.backend_api.Plane.Plane;
import com.WWI16AMA.backend_api.Plane.PlaneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping(path = "planeLog")
public class PlaneLogController {

    @Autowired
    private PlaneRepository planeRepository;



    @GetMapping(path = "/{id}")
    public ResponseEntity<PlaneLog> detail(@PathVariable int id) {
    private MemberRepository memberRepository;


    @GetMapping(path = "/{id}")
    public ResponseEntity<List> info(@PathVariable int id) {

        return new ResponseEntity<>(planeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Plane with the id " + id + " does not exist")).getPlaneLog().getEntries(), HttpStatus.OK);
    }

    @PostMapping(path = "/{id}")
    public List addPlaneLogEntry(@RequestBody PlaneLogEntry entry, @PathVariable int id) {
        Plane plane = planeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Plane with the id " + id + " does not exist"));


        PlaneLog planeLog = plane.getPlaneLog();
        planeLog.addPlaneLogEntry(entry);


        planeRepository.save(plane);
        return planeLog.getEntries();
    }

    @PostMapping(path = "/{id}")
    public List addPlaneLogEntry(@RequestBody PlaneLogEntry entry, @PathVariable int id) {
        Plane plane = planeRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Plane with the id " + id + " does not exist"));

        plane.getPlaneLog().add(entry);


        planeRepository.save(plane);
        return plane.getPlaneLog();
    }


}
