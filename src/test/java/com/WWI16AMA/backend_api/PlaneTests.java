package com.WWI16AMA.backend_api;

import com.WWI16AMA.backend_api.Account.AccountRepository;
import com.WWI16AMA.backend_api.Account.ProtectedAccount.VereinsAccount;
import com.WWI16AMA.backend_api.Member.FlightAuthorization;
import com.WWI16AMA.backend_api.Member.Member;
import com.WWI16AMA.backend_api.Member.MemberRepository;
import com.WWI16AMA.backend_api.Member.OfficeRepository;
import com.WWI16AMA.backend_api.Plane.Plane;
import com.WWI16AMA.backend_api.Plane.PlaneRepository;
import com.WWI16AMA.backend_api.PlaneLog.PlaneLogEntry;
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

import java.net.URL;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class PlaneTests {

    private static int nameSuffix = 'A';
    @Autowired
    PlaneRepository planeRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    OfficeRepository officeRepository;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    PasswordEncoder enc;
    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testRepositoryPlane() throws Exception {

        long found = planeRepository.count();

        FlightAuthorization.Authorization auth = FlightAuthorization.Authorization.PPLA;
        saveAndGetPlane();

        assertThat(planeRepository.count()).isEqualTo(found + 1);

    }

    @Test
    @WithMockUser
    public void testGetPlaneController() throws Exception {

        long found = planeRepository.count();

        this.mockMvc.perform(get("/planes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", IsCollectionWithSize.hasSize((int) found)));
    }

    @Test
    @WithMockUser(roles = {"SYSTEMADMINISTRATOR"})
    public void testPostPlaneController() throws Exception {

        long found = planeRepository.count();

        FlightAuthorization.Authorization auth = FlightAuthorization.Authorization.PPLB;
        Plane plane = new Plane("D-ALL" + (char) nameSuffix++, "DR 400 Remorqueur", auth, "Halle1",
                new URL("https://static.independent.co.uk/s3fs-public/thumbnails/image/2017/03/23/17/electricplane.jpg?w968h681"),
                1, 2);

        this.mockMvc.perform(post("/planes").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(plane))).andExpect(status().isOk());

        assertThat(planeRepository.count()).isEqualTo(found + 1);
    }

    @Test
    @WithMockUser
    public void createPlaneWithoutPermission() throws Exception {
        this.mockMvc.perform(post("/planes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(new Plane("D-AJAX", "Karl-Georg",
                        FlightAuthorization.Authorization.BZFI, "1", new URL("http://was.com"),
                        2.3, 2.3))))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"VORSTANDSVORSITZENDER"})
    public void testPutPlaneController() throws Exception {

        FlightAuthorization.Authorization auth = FlightAuthorization.Authorization.PPLB;
        Plane plane = saveAndGetPlane();

        Plane newPlaneInfos = new Plane("D-EJEK", "DR 500 Adler", auth, "Halle1",
                new URL("https://static.independent.co.uk/s3fs-public/thumbnails/image/2017/03/23/17/electricplane.jpg?w968h681"),
                1, 2);
        this.mockMvc.perform(put("/planes/" + plane.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(plane)))
                .andExpect(status().isNoContent());

        assertThat(plane.getName()).isEqualToIgnoringCase(planeRepository.findById(plane.getId())
                .orElseThrow(() -> new NoSuchElementException("[TEST]: plane not found")).getName()
        );
    }

    @Test
    @WithMockUser(roles = {"SYSTEMADMINISTRATOR"})
    public void testPutPlaneControllerViolatingConstraints() throws Exception {

        FlightAuthorization.Authorization auth = FlightAuthorization.Authorization.PPLB;
        Plane plane = saveAndGetPlane();

        plane.setNumber(null);

        this.mockMvc.perform(put("/planes/" + plane.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(plane)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser
    public void updatePlaneWithoutPermission() throws Exception {
        this.mockMvc.perform(put("/planes/" + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(new Plane("n", "name",
                        FlightAuthorization.Authorization.BZFI, "1", new URL("http://was.com"),
                        2.3, 2.3))))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"SYSTEMADMINISTRATOR"})
    public void testPutPlaneControllerMalformedInput() throws Exception {

        FlightAuthorization.Authorization auth = FlightAuthorization.Authorization.PPLB;
        Plane plane = new Plane(" D-EJEK", "DR 400 Adler", auth, "Halle2",
                new URL("https://static.independent.co.uk/s3fs-public/thumbnails/image/2017/03/23/17/electricplane.jpg?w968h681"),
                0.1, 0.2);

        this.mockMvc.perform(put("/planes/" + TestUtil.getUnusedId(planeRepository))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(plane)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"SYSTEMADMINISTRATOR"})
    public void testDeletePlaneController() throws Exception {

        long found = planeRepository.count();
        FlightAuthorization.Authorization auth = FlightAuthorization.Authorization.PPLB;
        Plane plane = saveAndGetPlane();
        Integer id = planeRepository.save(plane).getId();

        this.mockMvc.perform(delete("/planes/" + id))
                .andExpect(status().isNoContent());

        assertThat(found).isEqualTo(planeRepository.count());
    }

    @Test
    @WithMockUser(roles = {"SYSTEMADMINISTRATOR"})
    public void testUniqueNumber() throws Exception {

        FlightAuthorization.Authorization authorization = FlightAuthorization.Authorization.PPLA;
        Plane plane = new Plane("D-ERFI", "Mannheimer Adler", authorization, "Halle1",
                new URL("https://static.independent.co.uk/s3fs-public/thumbnails/image/2017/03/23/17/electricplane.jpg?w968h681"),
                4.60, 1.60);

        this.mockMvc.perform(post("/planes/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(plane)))
                .andExpect(status().isBadRequest());
    }

    //PlaneLog Tests

    @Test
    @WithMockUser(roles = {"SYSTEMADMINISTRATOR"})
    public void testPostPlaneLogNotFound() throws Exception {

        FlightAuthorization.Authorization auth = FlightAuthorization.Authorization.PPLB;
        PlaneLogEntry planeLogEntry = new PlaneLogEntry(LocalDateTime.of(2019, 3, 12, 14, 55, 13), 0, "TestOrt", 69, 88, 5);

        int id = TestUtil.getUnusedId(planeRepository);

        this.mockMvc.perform(post("/planeLog/" + id)
                .headers(TestUtil.createBasicAuthHeader(id + "", "wasGeht123"))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(planeLogEntry)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = {"SYSTEMADMINISTRATOR"})
    public void testPostPlaneLogMemberNotFound() throws Exception {

        FlightAuthorization.Authorization auth = FlightAuthorization.Authorization.PPLB;
        PlaneLogEntry planeLogEntry = new PlaneLogEntry(LocalDateTime.of(2019, 3, 12, 14,
                55, 13), TestUtil.getUnusedId(memberRepository), "TestOrt",
                69, 88, 5);
        this.mockMvc.perform(post("/planeLog/" + TestUtil.getUnusedId(memberRepository))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(planeLogEntry)))
                .andExpect(status().isForbidden());
    }


    @Test
    public void testPostPlaneLogOk() throws Exception {

        String pw = "wasGeht123";
        Member mem = TestUtil.saveAndGetMember(memberRepository, officeRepository, enc, pw);

        FlightAuthorization.Authorization auth = FlightAuthorization.Authorization.PPLB;
        PlaneLogEntry planeLogEntry = new PlaneLogEntry(LocalDateTime.of(2019, 3, 12, 14, 55, 13), mem.getId(), "TestOrt", 69, 88, 5);

        this.mockMvc.perform(post("/planeLog/" + planeRepository.findAll().iterator().next().getId())
                .headers(TestUtil.createBasicAuthHeader(mem.getId().toString(), pw))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(planeLogEntry)))
                .andExpect(status().isOk());
    }

    @Test
    public void testPostPlaneLogFutureDate() throws Exception {

        String pw = "wasGeht123";
        Member mem = TestUtil.saveAndGetMember(memberRepository, officeRepository, enc, pw);

        FlightAuthorization.Authorization auth = FlightAuthorization.Authorization.PPLB;
        PlaneLogEntry planeLogEntry = new PlaneLogEntry(LocalDateTime.of(2999, 3, 12, 14, 55, 13), mem.getId(), "TestOrt", 69, 88, 5);

        this.mockMvc.perform(post("/planeLog/" + planeRepository.findAll().iterator().next().getId())
                .headers(TestUtil.createBasicAuthHeader(mem.getId().toString(), pw))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(planeLogEntry)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testPostPlaneLogTransactionCreate() throws Exception {

        String pw = "test1234";
        Member member = TestUtil.saveAndGetMember(memberRepository, officeRepository, enc, pw);

        int oldMemberTransactionSize = member.getMemberBankingAccount().getTransactions().size();
        int oldVereinTransactionSize = VereinsAccount.getInstance(accountRepository).getTransactions().size();
        double oldBalanceMember = member.getMemberBankingAccount().getBalance();
        double oldBalanceVerein = VereinsAccount.getInstance(accountRepository).getBalance();
        float amount = 56.00f;

        FlightAuthorization.Authorization auth = FlightAuthorization.Authorization.PPLB;
        PlaneLogEntry planeLogEntry = new PlaneLogEntry(LocalDateTime.of(2019, 3, 12, 14,
                55, 13), member.getId(), "TestOrt",
                69, 88, amount);

        this.mockMvc.perform(post("/planeLog/" + planeRepository.findAll().iterator().next().getId())
                .headers(TestUtil.createBasicAuthHeader(member.getId().toString(), pw))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(planeLogEntry)))
                .andExpect(status().isOk());

        member = memberRepository.findById(member.getId()).orElseThrow(() -> new IllegalStateException(("Member verloren gegangen")));

        assertThat(member.getMemberBankingAccount().getTransactions().size()).isEqualTo(oldMemberTransactionSize + 1);
        assertThat(VereinsAccount.getInstance(accountRepository).getTransactions().size()).isEqualTo(oldVereinTransactionSize + 1);

        assertThat(member.getMemberBankingAccount().getBalance()).isEqualTo(oldBalanceMember + amount);
        assertThat(VereinsAccount.getInstance(accountRepository).getBalance()).isEqualTo(oldBalanceVerein - amount);
    }

    private Plane saveAndGetPlane() throws Exception {

        String number = "D-ALL" + (char) nameSuffix++;

        Plane plane = new Plane(number, "DR 400 Adler", FlightAuthorization.Authorization.PPLA, "Halle 1",
                new URL("https://static.independent.co.uk/s3fs-public/thumbnails/image/2017/03/23/17/electricplane.jpg?w968h681"),
                5.2, 0.8);
        return planeRepository.save(plane);
    }
}
