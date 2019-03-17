package com.WWI16AMA.backend_api;


import com.WWI16AMA.backend_api.Member.Member;
import com.WWI16AMA.backend_api.Member.MemberRepository;
import com.WWI16AMA.backend_api.Member.OfficeRepository;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.WWI16AMA.backend_api.TestUtil.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthorizationTests {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OfficeRepository officeRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    @Test
    public void testReceiveMemberWithID() throws Exception {

        String pw = "koala";
        Member mem = saveAndGetMember(memberRepository, officeRepository, passwordEncoder, pw);

        int id = mem.getId();

        mockMvc
                .perform(
                        get("/members/"+id).header(mem.getId().toString(), pw))
                .andExpect(status().is2xxSuccessful());



    }



}
