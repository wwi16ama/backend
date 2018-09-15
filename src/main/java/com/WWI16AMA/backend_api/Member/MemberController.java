package com.WWI16AMA.backend_api.Member;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/members")
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;


    @GetMapping(path="")
    public  Iterable<Member> getAllUsersPaged(@RequestParam int limit, @RequestParam int start) {

        return memberRepository.findAll(PageRequest.of(start,limit));
    }


}

