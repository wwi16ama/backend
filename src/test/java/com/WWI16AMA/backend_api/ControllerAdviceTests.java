package com.WWI16AMA.backend_api;


import com.WWI16AMA.backend_api.Member.FlightAuthorization;
import com.WWI16AMA.backend_api.Member.Member;
import com.WWI16AMA.backend_api.Member.MemberRepository;
import com.WWI16AMA.backend_api.Member.OfficeRepository;
import com.WWI16AMA.backend_api.Plane.Plane;
import com.WWI16AMA.backend_api.Plane.PlaneRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.WWI16AMA.backend_api.TestUtil.saveAndGetMember;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(secure = false)
public class ControllerAdviceTests {

    @Autowired
    PlaneRepository planeRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    OfficeRepository officeRepository;

    /*
    Sadly a little ugly. The mockMvc is not configured to use the @ControllerAdvice,
    so there is the failMvc, but that one has no possibility of persisting.
    */
    private MockMvc failMvc;


    @Before
    public void beforeTest() {
        this.failMvc = standaloneSetup()
                .setControllerAdvice(new ControllerAdvice())
                .build();

    }

    @Test
    public void testPutPlaneControllerMalformedInput() throws Exception {

        FlightAuthorization.Authorization auth = FlightAuthorization.Authorization.PPLB;
        Plane plane = new Plane(" D-EJEK", "DR 400 Adler", auth, "Halle2", 0.1, 0.2);

        this.failMvc.perform(put("/planes/" + TestUtil.getUnusedId(planeRepository))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(plane)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testPutMemberControllerMalformedInput() throws Exception {

        Member mem = saveAndGetMember(memberRepository, officeRepository);

        this.failMvc.perform(put("/members/" + TestUtil.getUnusedId(memberRepository))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(mem)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testDeleteNonexistingMember() throws Exception {
        this.failMvc.perform(delete("/members/" + TestUtil.getUnusedId(memberRepository)))
                .andExpect(status().isNotFound());
    }


}
