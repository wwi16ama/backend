package com.WWI16AMA.backend_api.Member;

import com.WWI16AMA.backend_api.ErrorInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import java.awt.*;
import java.io.FileNotFoundException;

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
     * @param orderBy Defines the field by which the sort is to be performed
     * @return Returns an Iterable of Members paged and sorted by given parameters
     */
    @GetMapping(path="")
    public ResponseEntity<Iterable<Member>> getAllUsersPaged(
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "lastName") String orderBy
    ) throws IllegalArgumentException{

        Sort sort;

        if (direction.equals("desc")) sort = new Sort(Sort.Direction.DESC, orderBy);
        else if (direction.equals("asc")) sort = new Sort(Sort.Direction.ASC, orderBy);
        else throw new IllegalArgumentException("Sorting direction is neiter 'asc' nor 'desc'");

        Iterable<Member> members = memberRepository.findAll(PageRequest.of(start, limit, sort));
        return new ResponseEntity<Iterable<Member>>(members, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({org.springframework.data.mapping.PropertyReferenceException.class, IllegalArgumentException.class})
    @ResponseBody ErrorInfo
    handleBadRequest(HttpServletRequest req, Exception ex) {
        return new ErrorInfo( req.getRequestURL().toString()+"?"+req.getQueryString(), ex );
    }

}

