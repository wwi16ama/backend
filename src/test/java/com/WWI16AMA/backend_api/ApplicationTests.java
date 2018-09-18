package com.WWI16AMA.backend_api;

import com.WWI16AMA.backend_api.Member.Address;
import com.WWI16AMA.backend_api.Member.Member;
import com.WWI16AMA.backend_api.Member.MemberRepository;
import com.WWI16AMA.backend_api.Member.Status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.jayway.jsonpath.spi.json.GsonJsonProvider;
import org.hamcrest.collection.IsCollectionWithSize;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.GsonJsonParser;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.Month;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    MemberRepository memberRepository;

    @Test
    public void testRepository() {

        long found = memberRepository.count();

        Address adr = new Address(25524, "Itzehoe", "Twietbergstraße", 53);
        Member mem = new Member("Karl", "Hansen", LocalDate.of(1996, Month.DECEMBER, 21), "male", Status.PASSIVE, "karl.hansen@mail.com", adr, "123456789", false);
        memberRepository.save(mem);

        assertThat(memberRepository.count()).isEqualTo(found + 1);
    }

    @Test
    public void testGetMemberController() throws Exception {

        long found = memberRepository.count();
        String limit = found != 0 ? Long.toString(found) : "1337";

        System.out.println("\r\n\tfound:"+found+"\r\n\r\n");

        this.mockMvc.perform(get("/members").param("limit", limit))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", IsCollectionWithSize.hasSize((int) found)));


//        this.mockMvc.perform(get("/greeting")).andDo(print()).andExpect(status().isOk())
//                .andExpect(jsonPath("$.content").value("Hello, World!"));
    }

//    @Test
//	public void testPostMemberController() throws Exception{
//
//	    long found = memberRepository.count();
//
//        Address adr = new Address(12345, "Hamburg", "Hafenstraße", 5);
//        Member mem = new Member("Kurt", "Krömer", LocalDate.of(1975, Month.DECEMBER, 2), "male", Status.PASSIVE, "karl.hansen@mail.com", adr, "123456789", false);
//
//
//        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
//        String json = ow.writeValueAsString(mem);
//
//        this.mockMvc.perform(post("/members")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json)).andExpect(status().isOk());
//
//        assertThat(memberRepository.count()).isEqualTo(found +1);
//    }

}
