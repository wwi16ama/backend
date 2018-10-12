package com.WWI16AMA.backend_api.Account;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping(path = "accounts")
public class AccountController {
    private AccountRepository accountRepository;
}
