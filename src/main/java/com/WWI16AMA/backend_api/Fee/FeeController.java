package com.WWI16AMA.backend_api.Fee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "fees")
public class FeeController {

    @Autowired
    private FeeRepository feeRepository;

    @GetMapping(value = "")
    public Iterable<Fee> showAllFees() {

        return feeRepository.findAll();
    }

}
