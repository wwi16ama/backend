package com.WWI16AMA.backend_api;

import com.WWI16AMA.backend_api.Member.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void testRepository() {

        long found = memberRepository.count();

        Address adr = new Address(25524, "Itzehoe", "Twietbergstraße 53");
        Member mem = new Member("Karl", "Hansen",
                LocalDate.of(1996, Month.DECEMBER, 21), Gender.MALE, Status.PASSIVE,
                "karl.hansen@mail.com", adr, "123456789", false);
        memberRepository.save(mem);

        assertThat(memberRepository.count()).isEqualTo(found + 1);
    }

    @Test
    public void testGetMemberController() throws Exception {

        long found = memberRepository.count();
        String limit = found != 0 ? Long.toString(found) : "1337";

        this.mockMvc.perform(get("/members").param("limit", limit))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", IsCollectionWithSize.hasSize((int) found)));
    }

    @Test
    public void testDeleteMemberController() throws Exception {

        long found = memberRepository.count();

        Address adr = new Address(25524, "Itzehoe", "Twietbergstraße 53");
        Member mem = new Member("Hauke", "Haien",
                LocalDate.of(1796, Month.DECEMBER, 3), Gender.MALE, Status.PASSIVE,
                "karl.hansen@mail.com", adr, "123456789", false);
        Integer id = memberRepository.save(mem).getId();

        this.mockMvc.perform(delete("/members/" + id))
                .andExpect(status().isNoContent());

        assertThat(found).isEqualTo(memberRepository.count());


        this.mockMvc.perform(delete("/members/" + this.getUnusedId(memberRepository)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testPostMemberController() throws Exception {

        long found = memberRepository.count();

        Address adr = new Address(12345, "Hamburg", "Hafenstraße 5");
        Member mem = new Member("Kurt", "Krömer",
                LocalDate.of(1975, Month.DECEMBER, 2), Gender.MALE, Status.PASSIVE,
                "karl.hansen@mail.com", adr, "123456789", false);

        System.out.println(this.marshal(mem));
        throw new Error(this.marshal(mem));

//        this.mockMvc.perform(post("/members")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(this.marshal(mem))).andExpect(status().isOk());
//
//        assertThat(memberRepository.count()).isEqualTo(found + 1);
    }

    @Test
    public void testPutMemberController() throws Exception {

        long found = memberRepository.count();

        Address adr = new Address(54231, "Krefeld", "Bühnenstraße 5");
        Member mem = new Member("Kurt", "Prödel",
                LocalDate.of(1975, Month.MAY, 10), Gender.MALE, Status.PASSIVE,
                "karl.hansen@mail.com", adr,
                "123456789", false);

        memberRepository.save(mem);

        Address newAddr = new Address(12345, "Neustadt", "Neustraße 5");
        mem.setAddress(newAddr);
        this.mockMvc.perform(put("/members/" + mem.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.marshal(mem)))
                .andExpect(status().isNoContent());
        assertThat(Optional.of(newAddr)).isEqualTo(memberRepository.findById(mem.getId()));

        mem.setAddress(null);
        this.mockMvc.perform(put("members/" + mem.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.marshal(mem)))
                .andExpect(status().isBadRequest());

        this.mockMvc.perform(put("members/" + this.getUnusedId(memberRepository))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.marshal(mem)))
                .andExpect(status().isNotFound());

    }

    /**
     * returns an id from an repository, which points to no entry, so repository.existsById(unusedId) returns false.
     *
     * @param repository
     * @return unusedId
     */
    private int getUnusedId(CrudRepository repository) {
        //get random id with no associated member
        int randomId;
        Random random = new Random();
        do {
            randomId = random.nextInt();
        } while (repository.existsById(randomId));
        return randomId;
    }

    private String marshal(Object o) throws com.fasterxml.jackson.core.JsonProcessingException {
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        return ow.writeValueAsString(o);
    }

}