package com.WWI16AMA.backend_api;


import com.WWI16AMA.backend_api.Member.MemberRepository;
import com.WWI16AMA.backend_api.Member.OfficeRepository;
import com.WWI16AMA.backend_api.PilotLog.PilotLogRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;


import static com.WWI16AMA.backend_api.TestUtil.saveAndGetMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class PilotLogTests {

    @Autowired
    PilotLogRepository pilotLogRepository;

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private OfficeRepository officeRepository;
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
    public void testRepository() {
        saveAndGetMember(memberRepository, officeRepository, passwordEncoder, "123password");

        // Check that an PilotLog was created
        assertThat(memberRepository.count()).isEqualTo(pilotLogRepository.count());
    }

    @Test
    public void testPostPilotLogController() throws Exception {


    }


}
