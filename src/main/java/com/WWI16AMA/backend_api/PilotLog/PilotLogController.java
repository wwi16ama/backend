package com.WWI16AMA.backend_api.PilotLog;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.NoSuchElementException;


@RestController
@RequestMapping(path = "pilotlog")
public class PilotLogController {

    @Autowired
    private PilotLogRepository pilotLogRepository;

    @GetMapping(path = "")
    public Iterable<PilotLog> showAllLogs() {

        return pilotLogRepository.findAll();
    }

    @GetMapping(path = "/{id}")
    public PilotLog showPilotLogDetail(@PathVariable int id) {
        return pilotLogRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pilotlog with the id " + id + " does not exist"));
    }

}