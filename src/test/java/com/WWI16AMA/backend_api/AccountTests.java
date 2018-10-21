package com.WWI16AMA.backend_api;

import com.WWI16AMA.backend_api.Account.Account;
import com.WWI16AMA.backend_api.Account.AccountRepository;
import com.WWI16AMA.backend_api.Account.Transaction;
import com.WWI16AMA.backend_api.Member.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.Month;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AccountTests {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testRepository() {

        generateMember();

        assertThat(memberRepository.count()).isEqualTo(accountRepository.count());
    }

    @Test
    public void testPosAccountController() throws Exception {

        generateMember();
        int memberId = memberRepository.findAll().iterator().next().getId();
        Account memAcc = memberRepository.findById(memberId).get().getMemberBankingAccount();

        long found = memAcc.getTransactions().size();

        Transaction transaction = new Transaction(500, Transaction.Fee.GEBÜHR);

        this.mockMvc.perform(post("/accounts/" + memAcc.getId() + "/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(transaction))).andExpect(status().isOk());

        assertThat(memAcc.getTransactions()).isEqualTo(found + 1);
    }


    private void generateMember() {

        Address adr = new Address(68167, "Mannheim", "Hambachstraße 3");
        Member mem = new Member("Hauke", "Haien",
                LocalDate.of(1796, Month.DECEMBER, 3), Gender.MALE, Status.PASSIVE,
                "karl.hansen@mail.com", adr, "DE12345678901234567890", false);

        memberRepository.save(mem);

    }
}
