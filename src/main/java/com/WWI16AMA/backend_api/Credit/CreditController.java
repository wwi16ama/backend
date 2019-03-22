package com.WWI16AMA.backend_api.Credit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "credits")
public class CreditController {

    @Autowired
    private CreditRepository creditRepository;

    @GetMapping(value = "")
    public Iterable<Credit> showAllCredits() {

        return creditRepository.findAll();
    }

}