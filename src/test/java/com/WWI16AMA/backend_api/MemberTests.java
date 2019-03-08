package com.WWI16AMA.backend_api;

import com.WWI16AMA.backend_api.Member.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.RollbackException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.NoSuchElementException;

import static com.WWI16AMA.backend_api.TestUtil.*;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class MemberTests {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    OfficeRepository officeRepository;

    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private MockMvc mockMvc;

    @Before
    public void beforeTest() {
        mockMvc = webAppContextSetup(wac).build();
    }


    @Test
    @Transactional
    public void testGetOffices() {
        saveAndGetMember(memberRepository, officeRepository, passwordEncoder);
        List<Office> of = memberRepository.findAll().iterator().next().getOffices();
        System.out.println(of.size());
    }

    @Test
    public void testRepository() {

        long found = memberRepository.count();

        saveAndGetMember(memberRepository, officeRepository, passwordEncoder);

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
    public void testPostMemberController() throws Exception {

        long found = memberRepository.count();

        Address adr = new Address(12345, "Hamburg", "Hafenstraße 5");
        Member mem = new Member("Kurt", "Krömer",
                LocalDate.of(1975, Month.DECEMBER, 2), Gender.MALE, Status.PASSIVE,
                "karl.hansen@mail.com", adr, "DE12345678901234567890", false, "");

        // mindest. 8 Zeichen, 1 Zahl, Buchstabe
        String klartextPw = "testPasswort123";
        mem.setPassword(klartextPw);

        mem.setMemberBankingAccount(null);

  
        Office[] off = {new Office(Office.Title.FLUGWART), new Office(Office.Title.KASSIERER)};
        mem.setOffices(asList(off));

        ObjectNode objNode = mutableJson(mem);
        objNode.put("password", klartextPw);

        String res = this.mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objNode.toString()))
//                .content("test"))
                .andExpect(status().isOk())
//                .content(new Gson().toJson(mem))).andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        JsonNode json = unMarsal(res);

        // sicherstellen, dass PW nicht im POST gesendet wird
        assertThat(json.get("password")).isNull();

        int id = Integer.parseInt(json.get("id").asText());
        String hashPw = memberRepository.findById(id)
                .orElseThrow(() ->
                        new NoSuchElementException("Kein Member im repo unter der von POST return'ten ID gefunden"))
                .getPassword();

        System.out.println("\r\n\r\n\r\nhash: " + hashPw);
        System.out.println("klar: " + klartextPw);
        System.out.println("has1: " + passwordEncoder.encode(klartextPw) + "\r\n\r\n\r\n");

        // sicherstellen, dass PW in der Datenbank und klartexPW matchen
        assertThat(passwordEncoder.matches(klartextPw, hashPw)).isTrue();

        // sicherstellen, dass Member gespeichert ist
        assertThat(memberRepository.count()).isEqualTo(found + 1);
    }

    @Test
    public void testPutMemberController() throws Exception {

        Member mem = saveAndGetMember(memberRepository, officeRepository, passwordEncoder);

        Address newAddr = new Address(12345, "Neustadt", "Neustraße 5");
        mem.setAddress(newAddr);


        String neuesKlartextPw = "testTost4321";
        ObjectNode objNode = mutableJson(mem);
        // Das bisherige Password, siehe TestUtil.saveAndGetMember()
        objNode.put("password", "123Koala");
        objNode.put("newPassword", neuesKlartextPw);

        this.mockMvc.perform(put("/members/" + mem.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objNode.toString()))
                .andExpect(status().isNoContent());

        Member resMem = memberRepository.findById(mem.getId())
                .orElseThrow(() -> new NoSuchElementException("[TEST]: member not found"));

        assertThat(mem.getAddress()).isEqualToIgnoringGivenFields(resMem.getAddress(), "id");

        assertThat(passwordEncoder.matches(neuesKlartextPw, resMem.getPassword())).isTrue();
    }


    /**
     * Here ugly because we watch a cornercase. We need the mockMvc because we need Database-validation
     * but this will result in an Exception, which would only be handled in failMvc. So we handle it by ourselves.
     */
    @Test
    public void testPutMemberControllerViolatingConstraints() throws Exception {

        Member mem = saveAndGetMember(memberRepository, officeRepository, passwordEncoder);

        mem.setAddress(null);
        try {
            // this should throw an exception, because the validation of the member
            // should fail. If there is no exception, the status won't be "Bad Request"
            this.mockMvc.perform(put("/members/" + mem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.marshal(mem)))
                    .andExpect(status().isBadRequest());
        } catch (org.springframework.web.util.NestedServletException e) {
            // It's expected behavior to have a specific exception, which should
            // fit these criteria.
            if (!(e.getCause() instanceof TransactionSystemException &&
                    e.getCause().getCause() instanceof RollbackException)) {
                throw new Exception("Es wurden andere verursachende Exceptions erwartet.\r\n" +
                        "Damit ist nicht der erwartete Fall eingetreten.");
            }
        }
    }

    @Test
    public void testPutPwChangeFail() throws Exception {

        Member mem = saveAndGetMember(memberRepository, officeRepository, passwordEncoder);

        String neuesKlartextPw = "testTost4321";
        ObjectNode objNode = mutableJson(mem);
        // ein falsch gesetztes Passwort, wodurch das neusetzen fehlschlagen sollte
        objNode.put("password", "");
        objNode.put("newPassword", neuesKlartextPw);

        //wie 1 weiter oben
        try {
            this.mockMvc.perform(put("/members/" + mem.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objNode.toString()))
                    .andExpect(status().isBadRequest());

            Member resMem = memberRepository.findById(mem.getId())
                    .orElseThrow(() -> new NoSuchElementException("[TEST]: member not found"));

            assertThat(passwordEncoder.matches(neuesKlartextPw, resMem.getPassword())).isFalse();
        } catch (org.springframework.web.util.NestedServletException e) {
            // It's expected behavior to have a specific exception, which should
            // fit these criteria.
            if (!(e.getCause() instanceof IllegalArgumentException)) {
                throw new Exception("Es wurden andere verursachende Exceptions erwartet.\r\n" +
                        "Damit ist nicht der erwartete Fall eingetreten.\r\n");
            }
        }
    }

    @Test
    public void testDeleteMemberController() throws Exception {

        long found = memberRepository.count();
        Member mem = saveAndGetMember(memberRepository, officeRepository, passwordEncoder);
        this.mockMvc.perform(delete("/members/" + mem.getId()))
                .andExpect(status().isNoContent());

        assertThat(found).isEqualTo(memberRepository.count());
    }


}