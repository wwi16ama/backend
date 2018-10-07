package com.WWI16AMA.backend_api;

import com.WWI16AMA.backend_api.Member.*;
import com.WWI16AMA.backend_api.Plane.Plane;
import com.WWI16AMA.backend_api.Plane.PlaneRepository;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.TransactionSystemException;

import javax.persistence.RollbackException;
import java.time.LocalDate;
import java.time.Month;
import java.util.NoSuchElementException;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTests {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    PlaneRepository planeRepository;
    /*
    Sadly a little ugly. The mockMvc is not configured to use the @ControllerAdvice,
    so there is the failMvc, but that one has no possibility of persisting.
     */
    @Autowired
    private MockMvc mockMvc;
    private MockMvc failMvc;

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
    public void testPostMemberController() throws Exception {

        long found = memberRepository.count();

        Address adr = new Address(12345, "Hamburg", "Hafenstraße 5");
        Member mem = new Member("Kurt", "Krömer",
                LocalDate.of(1975, Month.DECEMBER, 2), Gender.MALE, Status.PASSIVE,
                "karl.hansen@mail.com", adr, "123456789", false);

        Office[] off = {new Office(Office.Title.FLUGWART), new Office(Office.Title.KASSIERER)};
        mem.setOffices(asList(off));

        this.mockMvc.perform(post("/members")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(mem))).andExpect(status().isOk());

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

    @Test
    public void testPutMemberControllerMalformedInput() throws Exception {

        Address adr = new Address(54231, "Krefeld", "Bühnenstraße 5");
        Member mem = new Member("Kurt", "Prödel",
                LocalDate.of(1975, Month.MAY, 10), Gender.MALE, Status.PASSIVE,
                "karl.hansen@mail.com", adr,
                "123456789", false);

        this.failMvc.perform(put("/members/" + TestUtil.getUnusedId(memberRepository))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(mem)))
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
        this.failMvc.perform(delete("/members/" + TestUtil.getUnusedId(memberRepository)))
                .andExpect(status().isNotFound());
    }


    @Test
    public void testRepositoryPlane() {

        long found = planeRepository.count();

        FlightAuthorization.Authorization auth = FlightAuthorization.Authorization.PPLA;
        Plane plane = new Plane("D-ERFI", "Diamond DA-40 TDI", auth, "Halle1");
        planeRepository.save(plane);

        assertThat(planeRepository.count()).isEqualTo(found + 1);

    }

    @Test
    public void testGetPlaneController() throws Exception {

        long found = planeRepository.count();
        String limit = found != 0 ? Long.toString(found) : "1337";

        this.mockMvc.perform(get("/planes").param("limit", limit))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$", IsCollectionWithSize.hasSize((int) found)));
    }

    @Test
    public void testPostPlaneController() throws Exception {

        long found = planeRepository.count();

        FlightAuthorization.Authorization auth = FlightAuthorization.Authorization.PPLB;
        Plane plane = new Plane(" D-EJEK", "DR 400 Remorqueur", auth, "Halle1");

        this.mockMvc.perform(post("/planes").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(plane))).andExpect(status().isOk());

        assertThat(planeRepository.count()).isEqualTo(found + 1);

    }

    @Test
    public void testPutPlaneController() throws Exception {

        FlightAuthorization.Authorization auth = FlightAuthorization.Authorization.PPLB;
        Plane plane = new Plane(" D-EJEK", "DR 400 Adler", auth, "Halle2");
        planeRepository.save(plane);

        Plane newPlaneInfos = new Plane(" D-EJEK", "DR 500 Adler", auth, "Halle1");
        this.mockMvc.perform(put("/planes/" + plane.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(plane)))
                .andExpect(status().isNoContent());

        assertThat(plane.getName()).isEqualToIgnoringCase(planeRepository.findById(plane.getId())
                .orElseThrow(() -> new NoSuchElementException("[TEST]: member not found")).getName()
        );
    }

    @Test
    public void testPutPlaneControllerMalformedInput() throws Exception {

        FlightAuthorization.Authorization auth = FlightAuthorization.Authorization.PPLB;
        Plane plane = new Plane(" D-EJEK", "DR 400 Adler", auth, "Halle2");

        this.failMvc.perform(put("/planes/" + TestUtil.getUnusedId(memberRepository))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(plane)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testPutPlaneControllerViolatingConstraints() throws Exception {

        FlightAuthorization.Authorization auth = FlightAuthorization.Authorization.PPLB;
        Plane plane = new Plane(" D-EJEK", "DR 400 Adler", auth, "Halle2");
        planeRepository.save(plane);

        plane.setNumber(null);
        try {
            // this should throw an exception, because the validation of the member
            // should fail. If there is no exception, the status won't be "Bad Request"
            this.mockMvc.perform(put("/planes/" + plane.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.marshal(plane)))
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
    public void testDeletePlaneController() throws Exception {

        long found = planeRepository.count();
        FlightAuthorization.Authorization auth = FlightAuthorization.Authorization.PPLB;
        Plane plane = new Plane(" D-EJEK", "DR 400 Adler", auth, "Halle2");
        Integer id = planeRepository.save(plane).getId();

        this.mockMvc.perform(delete("/planes/" + id))
                .andExpect(status().isNoContent());

        assertThat(found).isEqualTo(planeRepository.count());

    }

    @Test
    public void testDeleteNonexistingPlane() throws Exception {

        this.failMvc.perform(delete("/planes/" + TestUtil.getUnusedId(planeRepository)))
                .andExpect(status().isNotFound());

    }
}