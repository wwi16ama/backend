package com.WWI16AMA.backend_api.Member;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "members")
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private OfficeRepository officeRepository;

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
    public Iterable<MemberView> getAllUsersPaged(
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
                .forEach(member -> listing.add(new MemberView(member.getId(),
                        member.getFirstName(), member.getLastName())));
        return listing;
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Member> detail(@PathVariable int id) {

        return new ResponseEntity<>(memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Member with the id " + id + " does not exist")),
                HttpStatus.OK);
    }

    @PostMapping(value = "")
    public ResponseEntity<Member> create(@RequestBody Member reqMember) {

        if (reqMember.getId() != null) {
            throw new IllegalArgumentException("Member has the id: " + reqMember.getId() + ". " +
                    "Id has to be null when a new member shall be created");
        }
        List<Office> mOffices = new ArrayList<>();
        reqMember.getOffices().forEach(office -> officeRepository.findAll().forEach(dbOffice -> {
            if (dbOffice.getTitle().equals(office.getTitle())) {
                mOffices.add(dbOffice);
            }
        }));

        reqMember.setOffices(mOffices);
        memberRepository.save(reqMember);

        return new ResponseEntity<>(reqMember, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> updateMember(@RequestBody Member regMember, @PathVariable int id)
            throws NoSuchElementException {

        if (memberRepository.existsById(id)) {
            regMember.setId(id);
            memberRepository.save(regMember);   //TODO weitere validierung?
        } else {
            throw new NoSuchElementException("Member with the id " + id + " does not exist");
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) throws NoSuchElementException {

        Member dbMember = memberRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Member with the id " + id + " does not exist"));
        memberRepository.delete(dbMember);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
