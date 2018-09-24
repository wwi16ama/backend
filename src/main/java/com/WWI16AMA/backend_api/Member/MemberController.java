package com.WWI16AMA.backend_api.Member;

import com.WWI16AMA.backend_api.ErrorInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "members")
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

    /**
     * Get Request which delivers all Users.
     *
     * @param limit     Defines the amount of objects to receive
     * @param start     Defines the page to view
     * @param direction Defines the sorting order
     * @param orderBy   Defines the field by which the sort is to be performed
     * @return Returns an Iterable of Members paged and sorted by given parameters
     */
    @GetMapping(value = "")
    public ResponseEntity<Iterable<MemberView>> getAllUsersPaged(
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "0") int start,
            @RequestParam(defaultValue = "asc") String direction,
            @RequestParam(defaultValue = "lastName") String orderBy
    ) throws IllegalArgumentException {

        Sort sort;

        if (direction.equals("desc")) sort = new Sort(Sort.Direction.DESC, orderBy);
        else if (direction.equals("asc")) sort = new Sort(Sort.Direction.ASC, orderBy);
        else throw new IllegalArgumentException("Sorting direction is neiter 'asc' nor 'desc'");
        List<MemberView> listing = new ArrayList<>();
        memberRepository.findAll(PageRequest.of(start, limit, sort))
                .forEach(member -> {
                    listing.add(new MemberView(member.getId(), member.getFirstName(), member.getLastName()));
                });
        return new ResponseEntity<Iterable<MemberView>>(listing, HttpStatus.OK);
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Member> delete(@PathVariable int id) {

        //return greetingRepository.findById(id).orElse(new Greeting("Muss ja.")
        memberRepository.deleteById(id);
        return new ResponseEntity<>(new Member(), HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Member> detail(@PathVariable int id) throws Exception {

        //return greetingRepository.findById(id).orElse(new Greeting("Muss ja.")

        Member member = memberRepository.findById(id).get();

//        System.out.println(member.getOffices().get(0));
//
//        for (Office office : member.getOffices()){
//            System.out.println(office);
//        }


        System.out.println(this.marshal(member));

        return new ResponseEntity<>(member, HttpStatus.OK);

    }


    @PostMapping(value = "")
    public ResponseEntity<Member> update(@RequestBody Member member) throws Exception{

        memberRepository.save(member);

        for (Office office : member.getOffices()){
            System.out.println(office);
        }

        System.out.print(this.marshal(member));

        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Member> updateNewAdress(@RequestBody Member member, @PathVariable int id) {

        member.setId(id);// TODO
        memberRepository.save(member);

        return new ResponseEntity<Member>(member, HttpStatus.OK);
    }


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({org.springframework.data.mapping.PropertyReferenceException.class, IllegalArgumentException.class})
    @ResponseBody
    ErrorInfo
    handleBadRequest(HttpServletRequest req, Exception ex) {
        return new ErrorInfo(req.getRequestURL().toString() + "?" + req.getQueryString(), ex);
    }


    //TODO wieder löschen
    private String marshal(Object o) throws com.fasterxml.jackson.core.JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(o);
    }

}
