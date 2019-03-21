package com.WWI16AMA.backend_api;

import com.WWI16AMA.backend_api.Member.FlightAuthorization;
import com.WWI16AMA.backend_api.Plane.Plane;
import com.WWI16AMA.backend_api.Plane.PlaneRepository;
import com.WWI16AMA.backend_api.PlaneLog.PlaneLogEntry;
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
import java.net.URL;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class PlaneTests {

    @Autowired
    PlaneRepository planeRepository;

    /*
    Sadly a little ugly. The mockMvc is not configured to use the @ControllerAdvice,
    so there is the failMvc, but that one has no possibility of persisting.
     */
    private MockMvc mockMvc;

    @Autowired
    WebApplicationContext wac;


    @Before
    public void beforeTest() {
        mockMvc = webAppContextSetup(wac).build();
    }


    @Test
    public void testRepositoryPlane() throws Exception {

        long found = planeRepository.count();

        FlightAuthorization.Authorization auth = FlightAuthorization.Authorization.PPLA;
        saveAndGetPlane();

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
        Plane plane = new Plane(" D-EJEK", "DR 400 Remorqueur", auth, "Halle1",
                new URL("https://static.independent.co.uk/s3fs-public/thumbnails/image/2017/03/23/17/electricplane.jpg?w968h681"),
                1, 2);

        this.mockMvc.perform(post("/planes").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(plane))).andExpect(status().isOk());

        assertThat(planeRepository.count()).isEqualTo(found + 1);

    }

    @Test
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
    public void testPutPlaneControllerViolatingConstraints() throws Exception {

        FlightAuthorization.Authorization auth = FlightAuthorization.Authorization.PPLB;
        Plane plane = saveAndGetPlane();

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
        Plane plane = saveAndGetPlane();
        Integer id = planeRepository.save(plane).getId();

        this.mockMvc.perform(delete("/planes/" + id))
                .andExpect(status().isNoContent());

        assertThat(found).isEqualTo(planeRepository.count());

    }

    @Test
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
    public void testPostPlaneLog() throws Exception {

        FlightAuthorization.Authorization auth = FlightAuthorization.Authorization.PPLB;
        PlaneLogEntry planeLogEntry = new PlaneLogEntry(LocalDateTime.of(2019, 3, 12, 14, 55, 13), 0, "TestOrt", 69, 88, 5);

        this.mockMvc.perform(post("/planeLog/" + TestUtil.getUnusedId(planeRepository))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(planeLogEntry)))
                .andExpect(status().isNotFound());
    }

    private Plane saveAndGetPlane() throws Exception {
        Plane plane = new Plane("D-EJEK", "DR 400 Adler", FlightAuthorization.Authorization.PPLA, "Halle 1",
                new URL("https://static.independent.co.uk/s3fs-public/thumbnails/image/2017/03/23/17/electricplane.jpg?w968h681"),
                5.2, 0.8);
        return planeRepository.save(plane);
    }
}
