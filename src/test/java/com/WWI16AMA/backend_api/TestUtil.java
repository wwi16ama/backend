package com.WWI16AMA.backend_api;

import com.WWI16AMA.backend_api.Member.*;
import com.WWI16AMA.backend_api.SerializationHelp.CustomObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

class TestUtil {

    /**
     * returns an id from an repository, which points to no entry, so repository.existsById(unusedId) returns false.
     */
    static int getUnusedId(CrudRepository repository) {
        //get random id with no associated member
        int randomId;
        Random random = new Random();
        do {
            randomId = random.nextInt();
        } while (repository.existsById(randomId));
        return randomId;
    }

    static String marshal(Object o) throws JsonProcessingException {
        ObjectWriter ow = new CustomObjectMapper().writer();
        return ow.writeValueAsString(o);
    }

    static JsonNode unMarsal(String json) throws IOException {
        ObjectReader or = new CustomObjectMapper().reader();
        return or.readTree(json);
    }

    // TODO hässlich wie die Nacht
    static ObjectNode mutableJson(Object o) throws IOException {
        ObjectWriter ow = new CustomObjectMapper().writer();
        String json = ow.writeValueAsString(o);
        ObjectReader or = new CustomObjectMapper().reader();
        return or.readTree(json).deepCopy();
    }

    static Member saveAndGetMember(MemberRepository memberRepository, OfficeRepository officeRepository, PasswordEncoder enc, String password) {
        Address adr = new Address(68167, "Mannheim", "Hambachstraße 3");
        Member mem = new Member("Hauke", "Haien",
                LocalDate.of(1796, Month.DECEMBER, 3), Gender.MALE, Status.PASSIVE,
                "karl.hansen@mail.com", adr, "DE12345678901234567890", false, enc.encode(password));

        List<Office> off = StreamSupport.stream(officeRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
        mem.setOffices(off);

        memberRepository.save(mem);
        return mem;
    }
}
