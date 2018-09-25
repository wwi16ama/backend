package com.WWI16AMA.backend_api;

import com.WWI16AMA.backend_api.Member.*;
import com.WWI16AMA.backend_api.SerializationHelp.CustomObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTestContextBootstrapper;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.MediaType;
import org.springframework.test.context.BootstrapWith;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.RollbackException;
import javax.validation.ConstraintViolationException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;
import java.util.Random;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTests {
/*
Sadly a little ugly. The mockMvc is not configured to use the @ControllerAdvice, so there is the failMvc, but that one
has no possibility of persisting and all that..
 */
    @Autowired
    private MockMvc mockMvc;

    private MockMvc failMvc;

    @Autowired
    MemberRepository memberRepository;
//    @MockBean
//    OfficeRepository officeRepository;

    @Before
    public void beforeTest() {
        this.failMvc = standaloneSetup()
                .setControllerAdvice(new MemberControllerAdvice())
                .build();
    }

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
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", IsCollectionWithSize.hasSize((int) found)));
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
    }

    @Test
    public void testDeleteNonexistingMember() throws Exception {
        this.failMvc.perform(delete("/members/"+getUnusedId(memberRepository)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testPostMemberController() throws Exception {

        long found = memberRepository.count();

        Address adr = new Address(12345, "Hamburg", "Hafenstraße 5");
        Member mem = new Member("Kurt", "Krömer",
                LocalDate.of(1975, Month.DECEMBER, 2), Gender.MALE, Status.PASSIVE,
                "karl.hansen@mail.com", adr, "123456789", false);


        this.mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.marshal(mem))).andExpect(status().isOk());

        assertThat(memberRepository.count()).isEqualTo(found + 1);
    }

    @Test
    public void testPutMemberController() throws Exception {


        Address adr = new Address(54231, "Krefeld", "Bühnenstraße 5");
        Member mem = new Member("Kurt", "Prödel",
                LocalDate.of(1975, Month.MAY, 10), Gender.MALE, Status.PASSIVE,
                "karl.hansen@mail.com", adr,
                "123456789", false);

        memberRepository.save(mem);
        long id = mem.getId();

        Address newAddr = new Address(12345, "Neustadt", "Neustraße 5");
        mem.setAddress(newAddr);
        this.mockMvc.perform(put("/members/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.marshal(mem)))
                .andExpect(status().isNoContent());
//        assertThat(Optional.of(mem)).isEqualToComparingFieldByField(memberRepository.findById(mem.getId()));
        assertThat(mem.getAddress()).isEqualToIgnoringGivenFields(
                memberRepository.findById(mem.getId()).get().getAddress(), "id");



        this.failMvc.perform(put("/members/" + this.getUnusedId(memberRepository))
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.marshal(mem)))
                .andExpect(status().isNotFound());

    }

    /**
     * Here ugly because we watch a cornercase. We need the mockMvc because we need Database-validation
     * but this will result in an Exception, which would only be handled in failMvc. So we handle it by ourselves.
     */
    @Test
    public void testPutMemberControllerViolatingConstraints() throws Exception {

        Address adr = new Address(54231, "Krefeld", "Bühnenstraße 5");
        Member mem = new Member("Kurt", "Prödel",
                LocalDate.of(1975, Month.MAY, 10), Gender.MALE, Status.PASSIVE,
                "karl.hansen@mail.com", adr,
                "123456789", false);

        memberRepository.save(mem);

        mem.setAddress(null);
        try {
            this.mockMvc.perform(put("/members/" + mem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(this.marshal(mem)))
                    .andExpect(status().isBadRequest());
        } catch (org.springframework.web.util.NestedServletException e){
            //ConstraintViolationException
            if(!(e.getCause() instanceof TransactionSystemException &&
                    e.getCause().getCause() instanceof RollbackException))
                throw new Exception("Es wurden andere verursachende Exceptions erwartet.\r\n" +
                        "Damit ist nicht der erwartete Fall eingetreten.");
        }

    }
    /**
     * returns an id from an repository, which points to no entry, so repository.existsById(unusedId) returns false.
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
        ObjectWriter ow = new CustomObjectMapper().writer();
        return ow.writeValueAsString(o);
    }


}