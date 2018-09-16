package com.WWI16AMA.backend_api.Member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping(path = "/members")
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

    /**
     * Get Request which delivers all Users.
     *
     * @param limit Defines the amount of objects to receive
     * @param start Defines the page to view
     * @param direction Defines the sorting order
     * @param field Defines the field by which the sort is to be performed
     * @return Returns an Iterable of Members paged and sorted by given parameters
     */
    @GetMapping(path="")
    public Iterable<Member> getAllUsersPaged(@RequestParam int limit, @RequestParam int start, @RequestParam(defaultValue = "asc") String direction, @RequestParam(defaultValue = "id") String field) {

        Sort sort;

        if (direction.equals("desc")) sort = new Sort(Sort.Direction.DESC, field);
        else sort = new Sort(Sort.Direction.ASC, field);



        Iterable<Member> members = memberRepository.findAll(sort);
        return members;
    }

}

