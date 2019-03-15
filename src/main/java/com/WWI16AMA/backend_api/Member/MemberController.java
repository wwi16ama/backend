package com.WWI16AMA.backend_api.Member;

import com.WWI16AMA.backend_api.Account.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.regex.Pattern;
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
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Get Request which delivers all Users.
     * <p>
     * //     * @param limit     Defines the amount of objects to receive
     * //     * @param start     Defines the page to view
     * //     * @param direction Defines the sorting order
     * //     * @param orderBy   Defines the field by which the sort is to be performed
     * //     * @return Returns an Iterable of Members paged and sorted by given parameters
     */
    @PreAuthorize("hasAnyRole('VORSTANDSVORSITZENDER') or hasAnyRole('KASSIERER')")
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

    @PreAuthorize("hasAnyRole('VORSTANDSVORSITZENDER') or hasAnyRole('KASSIERER') or hasAnyRole('FLUGWART') or #id == principal.id")
    @GetMapping(value = "/{id}")
    public Member detail(@PathVariable int id) {

        return memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Member with the id " + id + " does not exist"));
    }

    static private void checkPassword(String unhashedPw) {

        if (unhashedPw == null) {
            throw new IllegalArgumentException("Passwort muss angegeben werden");
        }

        if (unhashedPw.length() < 8) {
            throw new IllegalArgumentException("Passwort muss mindestens 8 Zeichen lang sein");
        }

        if (!Pattern.matches(".*\\d.*", unhashedPw)) {
            throw new IllegalArgumentException("Passwort muss mindestens eine Zahl enthalten");
        }

        if (!Pattern.matches(".*\\w.*", unhashedPw)) {
            throw new IllegalArgumentException("Passwort muss mindestens einen Buchstaben enthalten");
        }

    }

    @PreAuthorize("hasAnyRole('VORSTANDSVORSITZENDER')")
    @PostMapping(value = "")
    public Member create(@RequestBody Member mem) {

        if (mem.getId() != null) {
            throw new IllegalArgumentException("Id shall not be send if you want to create a new Member");
        }

        if (mem.getMemberBankingAccount() != null) {
            throw new IllegalArgumentException("Account shall be null to create a new member");
        }
        mem.setMemberBankingAccount(new Account());

        checkPassword(mem.getPassword());
        mem.setPassword(passwordEncoder.encode(mem.getPassword()));

        List<Office> offices = mem.getOffices()
                .stream()
                .map(Office::getTitle)
                .map(officeRepository::findByTitle)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        mem.setOffices(offices);

        memberRepository.save(mem);
        return mem;
    }

    @PreAuthorize("hasAnyRole('VORSTANDSVORSITZENDER')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {

        Member dbMember = memberRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Member with the id " + id + " does not exist"));
        memberRepository.delete(dbMember);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasAnyRole('VORSTANDSVORSITZENDER')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> updateMember(@RequestBody Member mem, @PathVariable int id)
            throws NoSuchElementException {

        Member foundMember = memberRepository.findById(id).orElseThrow(() ->
                new NoSuchElementException("Member with the id " + " does not exist"));

        mem.setMemberBankingAccount(foundMember.getMemberBankingAccount());
        mem.setId(id);

        mem.setPassword(foundMember.getPassword());

        List<Office> offices = mem.getOffices()
                .stream()
                .map(Office::getTitle)
                .map(officeRepository::findByTitle)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        mem.setOffices(offices);

        memberRepository.save(mem);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("#id == principal.id")
    @PutMapping(value = "{id}/changePasswordAsMember")
    public ResponseEntity<Void> updatePassword(@RequestBody MemberPwChangeMessage msg, @PathVariable int id) {

        Member mem = memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Kein Member gefunden"));

        if (msg.getPassword() == null || !passwordEncoder.matches(msg.getPassword(), mem.getPassword())) {
            throw new IllegalArgumentException("Das alte Passwort ist nicht korrekt oder nicht angegeben.");
        }

        checkPassword(msg.getNewPassword());
        mem.setPassword(passwordEncoder.encode(msg.getNewPassword()));

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PreAuthorize("hasRole('SYSTEMADMINISTRATOR')")
    @PutMapping(value = "{id}/changePasswordAsAdmin")
    public ResponseEntity<Void> updatePasswordAsAdmin(@RequestBody AdminPwChangeMessage msg, @PathVariable int id) {

        Member mem = memberRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Keinen Member unter id " + id + " gefunden"));

        checkPassword(msg.getNewPassword());

        mem.setPassword(passwordEncoder.encode(msg.getNewPassword()));

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
