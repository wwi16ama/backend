package com.WWI16AMA.backend_api.Member;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/members")
public class MemberController {

    private final static SessionFactory SESSION_FACTORY = HibernateUtil.getSessionFactory();
    @Autowired
    private MemberRepository memberRepository;


    @GetMapping(value = "")
    public ResponseEntity<List<memberView>> show() {

        List<memberView> listing = new ArrayList<>();
        memberRepository.findAll().forEach(member -> {
            listing.add(new memberView(member.getId(), member.getFirstName(), member.getLastName()));
        });

        return new ResponseEntity<List<memberView>>(listing, HttpStatus.OK);
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Member> delete(@PathVariable int id) {

        memberRepository.deleteById(id);
        return new ResponseEntity<>(new Member(), HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Member> detail(@PathVariable int id) {

        //return greetingRepository.findById(id).orElse(new Greeting("Muss ja.")

        return new ResponseEntity<>(memberRepository.findById(id).get(), HttpStatus.OK);

    }


    @PostMapping(value = "")
    public ResponseEntity<Member> update(@RequestBody Member member) {

        Session session = SESSION_FACTORY.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Office> criteria = builder.createQuery(Office.class);
        Root<Office> officeRoot = criteria.from(Office.class);
        criteria.select(officeRoot);
        List<Office> listing = session.createQuery(criteria).getResultList();
        List<Office> myOffices = new ArrayList<>();

        for (Office office : member.getOffices()) {

            for (Office db_Office : listing) {

                if (db_Office.getOfficeName().equals(office.getOfficeName())) {

                    myOffices.add(db_Office);
                }
            }
        }

        member.setOffices(myOffices);

        memberRepository.save(member);

        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Member> updateNewAdress(@RequestBody Member member, @PathVariable int id) {

        member.setId(id);
        memberRepository.save(member);

        return new ResponseEntity<Member>(member, HttpStatus.OK);
    }


}
