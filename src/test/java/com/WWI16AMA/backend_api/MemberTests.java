package com.WWI16AMA.backend_api;

import com.WWI16AMA.backend_api.Member.*;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
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

import static com.WWI16AMA.backend_api.TestUtil.saveAndGetMember;
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

    private MockMvc mockMvc;

    @Before
    public void beforeTest() {
        mockMvc = webAppContextSetup(wac).build();
    }


    @Test
    @Transactional
    public void testGetOffices() {
        saveAndGetMember(memberRepository, officeRepository);
        List<Office> of = memberRepository.findAll().iterator().next().getOffices();
        System.out.println(of.size());
    }

    @Test
    public void testRepository() {

        long found = memberRepository.count();

        saveAndGetMember(memberRepository, officeRepository);

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
                "karl.hansen@mail.com", adr, "DE12345678901234567890", false);
        mem.setMemberBankingAccount(null);

        Office[] off = {new Office(Office.Title.FLUGWART), new Office(Office.Title.KASSIERER)};
        mem.setOffices(asList(off));

        this.mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(mem))).andExpect(status().isOk());

        assertThat(memberRepository.count()).isEqualTo(found + 1);
    }

    @Test
    public void testPutMemberController() throws Exception {

        Member mem = saveAndGetMember(memberRepository, officeRepository);

        Address newAddr = new Address(12345, "Neustadt", "Neustraße 5");
        mem.setAddress(newAddr);
        this.mockMvc.perform(put("/members/" + mem.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(mem)))
                .andExpect(status().isNoContent());
        assertThat(mem.getAddress()).isEqualToIgnoringGivenFields(
                memberRepository.findById(mem.getId())
                        .orElseThrow(() -> new NoSuchElementException("[TEST]: member not found"))
                        .getAddress(), "id");
    }


    /**
     * Here ugly because we watch a cornercase. We need the mockMvc because we need Database-validation
     * but this will result in an Exception, which would only be handled in failMvc. So we handle it by ourselves.
     */
    @Test
    public void testPutMemberControllerViolatingConstraints() throws Exception {

        Member mem = saveAndGetMember(memberRepository, officeRepository);

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
    public void testDeleteMemberController() throws Exception {

        long found = memberRepository.count();
        Member mem = saveAndGetMember(memberRepository, officeRepository);
        this.mockMvc.perform(delete("/members/" + mem.getId()))
                .andExpect(status().isNoContent());

        assertThat(found).isEqualTo(memberRepository.count());
    }


}