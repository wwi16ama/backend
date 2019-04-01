package com.WWI16AMA.backend_api;

import com.WWI16AMA.backend_api.Account.ProtectedAccount.VereinsAccount;
import com.WWI16AMA.backend_api.Member.FlightAuthorization;
import com.WWI16AMA.backend_api.Member.Member;
import com.WWI16AMA.backend_api.Member.MemberRepository;
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

    @Autowired
    PlaneRepository planeRepository;

    @Autowired
    MemberRepository memberRepository;

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
        Plane plane = new Plane(" D-EJEK", "DR 400 Remorqueur", auth, "Halle1",
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
                .content(TestUtil.marshal(new Plane("n", "name",
                        FlightAuthorization.Authorization.BZFI, "1", new URL("http://was.com"),
                        2.3, 2.3))))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = {"VORSTANDSVORSITZENDER"})
    public void testPutPlaneController() throws Exception {

        FlightAuthorization.Authorization auth = FlightAuthorization.Authorization.PPLB;
        Plane plane = saveAndGetPlane();

        Plane newPlaneInfos = new Plane(" D-EJEK", "DR 500 Adler", auth, "Halle1",
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
    public void testPostPlaneLogNotFound() throws Exception {

        FlightAuthorization.Authorization auth = FlightAuthorization.Authorization.PPLB;
        PlaneLogEntry planeLogEntry = new PlaneLogEntry(LocalDateTime.of(2019, 3, 12, 14,
                55, 13), memberRepository.findAll().iterator().next().getId(), "TestOrt",
                69, 88, 5);

        this.mockMvc.perform(post("/planeLog/" + TestUtil.getUnusedId(planeRepository))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(planeLogEntry)))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(roles = {"SYSTEMADMINISTRATOR"})
    public void testPostPlaneLogMemberNotFound() throws Exception {

        FlightAuthorization.Authorization auth = FlightAuthorization.Authorization.PPLB;
        PlaneLogEntry planeLogEntry = new PlaneLogEntry(LocalDateTime.of(2019, 3, 12, 14,
                55, 13), TestUtil.getUnusedId(memberRepository), "TestOrt",
                69, 88, 5);
        this.mockMvc.perform(post("/planeLog/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(planeLogEntry)))
                .andExpect(status().isNotFound());
    }



    @Test
    @WithMockUser(roles = {"SYSTEMADMINISTRATOR"})
    public void testPostPlaneLogOk() throws Exception {

        FlightAuthorization.Authorization auth = FlightAuthorization.Authorization.PPLB;
        PlaneLogEntry planeLogEntry = new PlaneLogEntry(LocalDateTime.of(2019, 3, 12, 14,
                55, 13), memberRepository.findAll().iterator().next().getId(), "TestOrt",
                69, 88, 5);

        this.mockMvc.perform(post("/planeLog/" + planeRepository.findAll().iterator().next().getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(planeLogEntry)))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = {"SYSTEMADMINISTRATOR"})
    public void testPostPlaneLogFutureDate() throws Exception {

        FlightAuthorization.Authorization auth = FlightAuthorization.Authorization.PPLB;
        PlaneLogEntry planeLogEntry = new PlaneLogEntry(LocalDateTime.of(2999, 3, 12, 14,
                55, 13), memberRepository.findAll().iterator().next().getId(), "TestOrt",
                69, 88, 5);

        this.mockMvc.perform(post("/planeLog/" + planeRepository.findAll().iterator().next().getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(planeLogEntry)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @WithMockUser(roles = {"SYSTEMADMINISTRATOR"})
    public void testPostPlaneLogTransactionCreate() throws Exception {
        Member member = memberRepository.findAll().iterator().next();
        int oldMemberTransactionSize = member.getMemberBankingAccount().getTransactions().size();
        int oldVereinTransactionSize = VereinsAccount.getInstance().getTransactions().size();
        double oldBalanceMember = member.getMemberBankingAccount().getBalance();
        double oldBalanceVerein = VereinsAccount.getInstance().getBalance();
        float amount = 56.78f;
        FlightAuthorization.Authorization auth = FlightAuthorization.Authorization.PPLB;
        PlaneLogEntry planeLogEntry = new PlaneLogEntry(LocalDateTime.of(2019, 3, 12, 14,
                55, 13), member.getId(), "TestOrt",
                69, 88, amount);

        this.mockMvc.perform(post("/planeLog/" + planeRepository.findAll().iterator().next().getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(planeLogEntry)))
                .andExpect(status().isOk());

        assertThat( oldMemberTransactionSize++ == member.getMemberBankingAccount().getTransactions().size() );
        assertThat( oldVereinTransactionSize++ == VereinsAccount.getInstance().getTransactions().size() );
        assertThat(oldBalanceMember == member.getMemberBankingAccount().getBalance() + amount);
        assertThat(oldBalanceVerein == VereinsAccount.getInstance().getBalance() - amount);
    }

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

    private Plane saveAndGetPlane() throws Exception {
        Plane plane = new Plane("D-EJEK", "DR 400 Adler", FlightAuthorization.Authorization.PPLA, "Halle 1",
                new URL("https://static.independent.co.uk/s3fs-public/thumbnails/image/2017/03/23/17/electricplane.jpg?w968h681"),
                5.2, 0.8);
        return planeRepository.save(plane);
    }
}
