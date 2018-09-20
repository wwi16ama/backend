package com.WWI16AMA.backend_api.Member;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@RestController
@RequestMapping(path = "/members")
public class MemberController {

    private final static SessionFactory SESSION_FACTORY = HibernateUtil.getSessionFactory();
    @Autowired
    private MemberRepository memberRepository;

    @RequestMapping("/")
    public ResponseEntity<Iterable<Member>> home(){

        return new ResponseEntity<>(memberRepository.findAll(), HttpStatus.OK);

    }



    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<Member> delete(@PathVariable int id){

        //return greetingRepository.findById(id).orElse(new Greeting("Muss ja.")
        memberRepository.deleteById(id);

        return new ResponseEntity<>(new Member(), HttpStatus.OK);

    }

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    public ResponseEntity<Member> detail(@PathVariable int id){

        //return greetingRepository.findById(id).orElse(new Greeting("Muss ja.")

        return new ResponseEntity<>(memberRepository.findById(id).get(), HttpStatus.OK);

    }

    @RequestMapping(value = "/adding", method = RequestMethod.POST)
    public ResponseEntity<Member> update(@RequestBody Member member) {

        Session session = SESSION_FACTORY.openSession();
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Office> criteria = builder.createQuery(Office.class);
        Root<Office> officeRoot = criteria.from(Office.class);
        criteria.select(officeRoot);
        List<Office> listing = session.createQuery(criteria).getResultList();
        List<Office> myOffices = new ArrayList<>();

        for(Office office : member.getOffices()){

            for(Office db_Office : listing){

                if(db_Office.getOfficeName().equals(office.getOfficeName())){

                    myOffices.add(db_Office);
                }
            }
        }

        member.setOffices(myOffices);

        memberRepository.save(member);

        return new ResponseEntity<>(member, HttpStatus.OK);
    }

    @RequestMapping(value="/updateNewAdress/{id}")
    public ResponseEntity<Member> updateNewAdress(@RequestBody Member greet, @PathVariable long id){



        return new ResponseEntity<Member>(greet, HttpStatus.OK);
    }


}
