package com.WWI16AMA.backend_api;

import com.WWI16AMA.backend_api.Account.AccountRepository;
import com.WWI16AMA.backend_api.Credit.Credit;
import com.WWI16AMA.backend_api.Credit.CreditRepository;
import com.WWI16AMA.backend_api.Member.Member;
import com.WWI16AMA.backend_api.Member.MemberRepository;
import com.WWI16AMA.backend_api.Member.OfficeRepository;
import com.WWI16AMA.backend_api.Service.Service;
import com.WWI16AMA.backend_api.Service.ServiceName;
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

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;

import static com.WWI16AMA.backend_api.Billing.BillingTask.getNextBillingDate;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ServiceTest {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private OfficeRepository officeRepository;
    @Autowired
    private PasswordEncoder enc;
    @Autowired
    private CreditRepository crRep;
    @Autowired
    private MockMvc mockMvc;

    // Daily und Yearly Service anlegen, abrufen
    @Test
    @WithMockUser(roles = {"VORSTANDSVORSITZENDER"})
    public void testHappyPath() throws Exception {

        Member mem = TestUtil.saveAndGetMember(memberRepository, officeRepository, enc, "wasGehtDenn1");

        ServiceName sn = ServiceName.T_TAGESEINSATZ;
        Service service = new Service(sn, LocalDate.now(),
                getNextBillingDate().minusDays(1), 123);
        this.mockMvc.perform(post("/services/" + mem.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(service)))
                .andExpect(status().isNoContent());

        ServiceName snYearly = ServiceName.J_FLUGLEHRER;
        Service yearlyService = new Service(snYearly, getNextBillingDate().minusYears(1),
                getNextBillingDate().minusDays(1), 123);
        this.mockMvc.perform(post("/services/" + mem.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(yearlyService)))
                .andExpect(status().isNoContent());

        Credit cr = crRep.findCreditByServiceName(sn)
                .orElseThrow(() -> new NoSuchElementException("Hier ist ein Credit verloren gegangen"));

        this.mockMvc.perform(get("/services/" + mem.getId()))
                .andExpect(jsonPath("$", IsCollectionWithSize.hasSize(2)))
                .andExpect(jsonPath("$[0].name").value(service.getName().toString()))
                .andExpect(jsonPath("$[0].startDate").value(service.getStartDate().toString()))
                .andExpect(jsonPath("$[0].endDate").value(service.getEndDate().toString()))
                .andExpect(jsonPath("$[0].gutschrift").value(cr.getAmount()
                        * (double) ChronoUnit.DAYS.between(service.getStartDate(), service.getEndDate().plusDays(1)) + ""));

        cr = crRep.findCreditByServiceName(snYearly)
                .orElseThrow(() -> new NoSuchElementException("Hier ist ein Credit verloren gegangen"));

        this.mockMvc.perform(get("/services/" + mem.getId()))
                .andExpect(jsonPath("$", IsCollectionWithSize.hasSize(2)))
                .andExpect(jsonPath("$[1].name").value(yearlyService.getName().toString()))
                .andExpect(jsonPath("$[1].startDate").value(yearlyService.getStartDate().toString()))
                .andExpect(jsonPath("$[1].endDate").value(yearlyService.getEndDate().toString()))
                .andExpect(jsonPath("$[1].gutschrift").value(cr.getAmount()));
    }

    /**
     * Auf Fehler prüfen
     * - Datum verkehrt
     * - Jährliche Rolle zu oft
     * - Außerhalb des Abrechnungsjahres
     * - Falsche Berechtigungen
     */
    @Test
    @WithMockUser(roles = {"SYSTEMADMINISTRATOR"})
    public void testFalschesDatumBeiService() throws Exception {

        Member mem = TestUtil.saveAndGetMember(memberRepository, officeRepository, enc, "wasGeht1");
        Service ds = new Service(ServiceName.T_TAGESEINSATZ,
                LocalDate.now(), LocalDate.now().minusDays(2), 123);

        this.mockMvc.perform(post("/services/" + mem.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(ds)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = {"SYSTEMADMINISTRATOR"})
    public void testJährlicherServiceDoppelt() throws Exception {

        Member mem = TestUtil.saveAndGetMember(memberRepository, officeRepository, enc, "wasGeht1");
        Service ys = new Service(ServiceName.J_FLUGLEHRER, getNextBillingDate().minusYears(1),
                getNextBillingDate().minusDays(1), 123);

        this.mockMvc.perform(post("/services/" + mem.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(ys)))
                .andExpect(status().isNoContent());

        this.mockMvc.perform(post("/services/" + mem.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(ys)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = {"SYSTEMADMINISTRATOR"})
    public void testDienstAußerhalbDesAbrechnungsjahres() throws Exception {

        Member mem = TestUtil.saveAndGetMember(memberRepository, officeRepository, enc, "wasGeht1");
        Service ys = new Service(ServiceName.J_FLUGLEHRER, LocalDate.of(1900, 1, 1),
                LocalDate.of(2025, 1, 1), 123);

        this.mockMvc.perform(post("/services/" + mem.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(ys)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testeDienstAnlegenOhneBerechtigung() throws Exception {

        String pw = "wasGeht1";
        Member mem = TestUtil.saveAndGetMember(memberRepository, officeRepository, enc, pw);
        Service ys = new Service(ServiceName.J_FLUGLEHRER, getNextBillingDate().minusYears(1),
                getNextBillingDate().minusDays(1), 123);

        this.mockMvc.perform(post("/services/" + mem.getId())
                .headers(TestUtil.createBasicAuthHeader(mem.getId().toString(), pw))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(ys)))
                .andExpect(status().isForbidden());
    }

}
