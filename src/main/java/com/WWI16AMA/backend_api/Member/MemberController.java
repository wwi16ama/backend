package com.WWI16AMA.backend_api.Member;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/members")
public class MemberController {

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
    public ResponseEntity<Member> update(@RequestBody Member greet) {

        memberRepository.save(greet);
        return new ResponseEntity<>(greet, HttpStatus.OK);
    }

    @RequestMapping(value="/updateNewAdress/{id}")
    public ResponseEntity<Member> updateNewAdress(@RequestBody Member greet, @PathVariable long id){



        return new ResponseEntity<Member>(greet, HttpStatus.OK);
    }


}
