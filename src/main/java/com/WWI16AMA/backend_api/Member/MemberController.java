package com.WWI16AMA.backend_api.Member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;

@RestController
@RequestMapping(path = "members")
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private OfficeRepository officeRepository;
//    @Autowired
//    private PasswordEncoder passwordEncoder;

    /**
     * Get Request which delivers all Users.
     * <p>
     * //     * @param limit     Defines the amount of objects to receive
     * //     * @param start     Defines the page to view
     * //     * @param direction Defines the sorting order
     * //     * @param orderBy   Defines the field by which the sort is to be performed
     * //     * @return Returns an Iterable of Members paged and sorted by given parameters
     */
    @GetMapping(value = "")
    public Iterable<MemberView> getAllUsersPaged(
//            @RequestParam(defaultValue = "20") int limit,
//            @RequestParam(defaultValue = "0") int start,
//            @RequestParam(defaultValue = "asc") String direction,
//            @RequestParam(defaultValue = "lastName") String orderBy
    ) {

//        Sort sort = new Sort(Sort.Direction.fromString(direction), orderBy);
//        int page = (int)Math.ceil((double)start/(double)limit);
//        return memberRepository.findAll(PageRequest.of(page, limit, sort)).stream()
        Iterable<Member> mem = memberRepository.findAll();
        return StreamSupport.stream(mem.spliterator(), false)
                .map(MemberView::new)
                .collect(toList());
    }

    @GetMapping(value = "/{id}")
    public Member detail(@PathVariable int id) {

        return memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Member with the id " + id + " does not exist"));
    }

    @PostMapping(value = "")
    public Member create(@RequestBody Member mem) {

        if (mem.getId() != null) {
            throw new IllegalArgumentException("Id has to be null if you want to save a new Member");
        }

        List<Office> offices = mem.getOffices()
                .stream()
                .map(Office::getTitle)
                .map(officeRepository::findByTitle)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());


        mem.setOffices(offices);

        //TODO lass mal nicht so machen
//        mem.setPassword(passwordEncoder.encode(mem.getPassword()));
        memberRepository.save(mem);
        return mem;
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> updateMember(@RequestBody Member mem, @PathVariable int id)
            throws NoSuchElementException {

        if (memberRepository.existsById(id)) {
            Member foundMember = memberRepository.findById(id)
                    .orElseThrow(() ->
                            new NoSuchElementException("Member with the id " + " does not exist"));
            mem.setMemberBankingAccount(foundMember.getMemberBankingAccount());
            mem.setId(id);
            List<Office> offices = mem.getOffices()
                    .stream()
                    .map(Office::getTitle)
                    .map(officeRepository::findByTitle)
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

            mem.setOffices(offices);
            memberRepository.save(mem);
        } else {
            throw new NoSuchElementException("Member with the id " + id + " does not exist");
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {

        Member dbMember = memberRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Member with the id " + id + " does not exist"));
        memberRepository.delete(dbMember);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
