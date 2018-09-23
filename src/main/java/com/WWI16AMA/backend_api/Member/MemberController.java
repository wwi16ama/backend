package com.WWI16AMA.backend_api.Member;

import com.WWI16AMA.backend_api.Exception.EntryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
                .forEach(dbMember -> {
                    listing.add(new MemberView(dbMember.getId(), dbMember.getFirstName(), dbMember.getLastName()));
                });
        return new ResponseEntity<Iterable<MemberView>>(listing, HttpStatus.OK);
    }


    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Member> delete(@PathVariable int id) throws EntryNotFoundException {

        Member dbMember = memberRepository.findById(id).orElseThrow(() -> new EntryNotFoundException("Member cannot be found", new Throwable("Member with the Id" + id + "does not exist")));
        memberRepository.delete(dbMember);
        return new ResponseEntity<>(new Member(), HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Member> detail(@PathVariable int id) throws EntryNotFoundException {

        Member dbMember = memberRepository.findById(id).orElseThrow(() -> new EntryNotFoundException("Member cannot be found", new Throwable("Member with the Id" + id + "does not exist")));
        return new ResponseEntity<>(dbMember, HttpStatus.OK);

    }


    @PostMapping(value = "")
    public ResponseEntity<Member> create(@RequestBody Member reqMember) {

        List<Office> mOffices = new ArrayList<>();
        reqMember.getOffices().forEach(office -> {
            officeRepository.findAll().forEach(dbOffice -> {
                if (dbOffice.getTitle().equals(office.getTitle())) {
                    mOffices.add(dbOffice);
                }
            });
        });

        reqMember.setOffices(mOffices);
        memberRepository.save(reqMember);

        return new ResponseEntity<>(reqMember, HttpStatus.OK);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Member> updateNewAdress(@RequestBody Member regMember, @PathVariable int id) throws EntryNotFoundException {

        Member dbMember = memberRepository.findById(id).orElseThrow(() -> new EntryNotFoundException("Member cannot be found", new Throwable("Member with the Id" + id + "does not exist")));

        memberRepository.save(dbMember);

        return new ResponseEntity<Member>(dbMember, HttpStatus.OK);
    }


}
