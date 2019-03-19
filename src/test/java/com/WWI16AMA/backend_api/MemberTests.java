package com.WWI16AMA.backend_api;

import com.WWI16AMA.backend_api.Member.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.hamcrest.collection.IsCollectionWithSize;
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


@RunWith(SpringRunner.class)
//@WebMvcTest(MemberController.class)
//@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class MemberTests {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    OfficeRepository officeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;


    @Transactional
    public void testGetOffices() {
        saveAndGetMember(memberRepository, officeRepository, passwordEncoder, "password123");
        List<Office> of = memberRepository.findAll().iterator().next().getOffices();
        System.out.println(of.size());
    }

    @Test
    public void testRepository() {

        long found = memberRepository.count();
        saveAndGetMember(memberRepository, officeRepository, passwordEncoder, "password123");

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
                .andExpect(status().isOk())
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

        // sicherstellen, dass PW in der Datenbank und klartexPW matchen
        assertThat(passwordEncoder.matches(klartextPw, hashPw)).isTrue();

        // sicherstellen, dass Member gespeichert ist
        assertThat(memberRepository.count()).isEqualTo(found + 1);
    }

    @Test
    public void testPostMemberControllerBadPw() throws Exception {

        long found = memberRepository.count();

        Address adr = new Address(12345, "Hamburg", "Hafenstraße 5");
        Member mem = new Member("Kurt", "Krömer",
                LocalDate.of(1975, Month.DECEMBER, 2), Gender.MALE, Status.PASSIVE,
                "karl.hansen@mail.com", adr, "DE12345678901234567890", false, "");

        // mindest. 8 Zeichen, 1 Zahl, Buchstabe
        String klartextPw = "123";
        mem.setPassword(klartextPw);

        mem.setMemberBankingAccount(null);

        Office[] off = {new Office(Office.Title.FLUGWART), new Office(Office.Title.KASSIERER)};
        mem.setOffices(asList(off));

        ObjectNode objNode = mutableJson(mem);
        objNode.put("password", klartextPw);

        this.mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objNode.toString()))
                .andExpect(status().isBadRequest());

        // sicherstellen, dass Member nicht gespeichert wurde
        assertThat(memberRepository.count()).isEqualTo(found);
    }

    @Test
    public void testPutMemberController() throws Exception {

        Member mem = saveAndGetMember(memberRepository, officeRepository, passwordEncoder, "123Password");
        Address newAddr = new Address(12345, "Neustadt", "Neustraße 5");
        mem.setAddress(newAddr);

        this.mockMvc.perform(put("/members/" + mem.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(mem)))
                .andExpect(status().isNoContent());

        Member resMem = memberRepository.findById(mem.getId())
                .orElseThrow(() -> new NoSuchElementException("[TEST]: member not found"));

        assertThat(mem.getAddress()).isEqualToIgnoringGivenFields(resMem.getAddress(), "id");
    }


    @Test
    public void testPutMemberControllerViolatingConstraints() throws Exception {

        Member mem = saveAndGetMember(memberRepository, officeRepository, passwordEncoder, "123password");

        // This shall not be allowed
        mem.setAddress(null);

        this.mockMvc.perform(put("/members/" + mem.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(mem)))
                .andExpect(status().isBadRequest());
    }


    @Test
    public void testChangePasswordAsUser() throws Exception {

        String pw = "123password";
        Member mem = saveAndGetMember(memberRepository, officeRepository, passwordEncoder, pw);
        MemberPwChangeMessage msg = new MemberPwChangeMessage(pw, "1BuchstabeUndZahl");

        this.mockMvc.perform(put("/members/" + mem.getId() + "/changePasswordAsMember")
                .headers(TestUtil.createBasicAuthHeader(mem.getId().toString(), pw))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(msg)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testChangePasswordAsMemberWrongPw() throws Exception {

        Member mem = saveAndGetMember(memberRepository, officeRepository, passwordEncoder, "123richtig");
        MemberPwChangeMessage msg = new MemberPwChangeMessage("falschesPw", "1BuchstabeUndZahl");

        this.mockMvc.perform(put("/members/" + mem.getId() + "/changePasswordAsMember")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(msg)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testChangePasswordAsMemberWrongUser() throws Exception {

        String pw = "123WasGeht";
        Member mem = saveAndGetMember(memberRepository, officeRepository, passwordEncoder, pw);
        MemberPwChangeMessage msg = new MemberPwChangeMessage(pw, "1BuchstabeUndZahl");

        Member wrongMember = saveAndGetMember(memberRepository, officeRepository, passwordEncoder, pw);

        this.mockMvc.perform(put("/members/" + mem.getId() + "/changePasswordAsMember")
                .headers(TestUtil.createBasicAuthHeader(wrongMember.getId().toString(), pw))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(msg)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"SYSTEMADMINISTRATOR"})
    public void testChangePasswordAsAdmin() throws Exception {

        Member mem = saveAndGetMember(memberRepository, officeRepository, passwordEncoder, "123password");
        AdminPwChangeMessage msg = new AdminPwChangeMessage("1BuchstabeUndZahl");

        this.mockMvc.perform(put("/members/" + mem.getId() + "/changePasswordAsAdmin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(msg)))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testChangePassworAsAdminNotAdmin() throws Exception {

        Member mem = saveAndGetMember(memberRepository, officeRepository, passwordEncoder, "123password");
        AdminPwChangeMessage msg = new AdminPwChangeMessage("1BuchstabeUndZahl");

        this.mockMvc.perform(put("/members/" + mem.getId() + "/changePasswordAsAdmin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(msg)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"SYSTEMADMINISTRATOR"})
    public void testChangePasswordAsAdminInvalidPw() throws Exception {

        Member mem = saveAndGetMember(memberRepository, officeRepository, passwordEncoder, "123password");
        AdminPwChangeMessage msg = new AdminPwChangeMessage("123");

        this.mockMvc.perform(put("/members/" + mem.getId() + "/changePasswordAsAdmin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(msg)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteMemberController() throws Exception {

        long found = memberRepository.count();
        Member mem = saveAndGetMember(memberRepository, officeRepository, passwordEncoder, "123password");
        this.mockMvc.perform(delete("/members/" + mem.getId()))
                .andExpect(status().isNoContent());

        assertThat(found).isEqualTo(memberRepository.count());
    }

    @Test
    public void testPutMemberControllerMalformedInput() throws Exception {

        Member mem = saveAndGetMember(memberRepository, officeRepository, passwordEncoder, "123password");

        this.mockMvc.perform(put("/members/" + TestUtil.getUnusedId(memberRepository))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(mem)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteNonexistingMember() throws Exception {
        this.mockMvc.perform(delete("/members/" + TestUtil.getUnusedId(memberRepository)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"SYSTEMADMINISTRATOR"})
    public void testChangePassworAsAdminUserNotFound() throws Exception {

        AdminPwChangeMessage msg = new AdminPwChangeMessage("1BuchstabeUndZahl");

        this.mockMvc.perform(put("/members/"
                + TestUtil.getUnusedId(memberRepository)
                + "/changePasswordAsAdmin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(msg)))
                .andExpect(status().isNotFound());
    }
}