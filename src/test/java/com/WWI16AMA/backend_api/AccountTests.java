package com.WWI16AMA.backend_api;

import com.WWI16AMA.backend_api.Account.Account;
import com.WWI16AMA.backend_api.Account.AccountRepository;
import com.WWI16AMA.backend_api.Account.Transaction;
import com.WWI16AMA.backend_api.Member.MemberRepository;
import com.WWI16AMA.backend_api.Member.OfficeRepository;
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

import static com.WWI16AMA.backend_api.TestUtil.saveAndGetMember;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class AccountTests {

    @Autowired
    AccountRepository accountRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private OfficeRepository officeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testRepository() {

        saveAndGetMember(memberRepository, officeRepository, passwordEncoder, "123password");

        // Check that an Account was created
        assertThat(memberRepository.count()).isEqualTo(accountRepository.count());
    }

    @Test
    public void testPostAccountController() throws Exception {

        saveAndGetMember(memberRepository, officeRepository, passwordEncoder, "123pasword");
        int memberId = memberRepository.findAll().iterator().next().getId();
        Account memAcc = memberRepository.findById(memberId).get().getMemberBankingAccount();

        long found = memAcc.getTransactions().size();
        System.out.println("AccId: " + memAcc.getId());

        Transaction transaction = new Transaction(500, Transaction.FeeType.values()[0]);

        this.mockMvc.perform(post("/accounts/{id}/transactions", memAcc.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(transaction))).andExpect(status().isOk());

        assertThat(((long) accountRepository.findById(memAcc.getId()).get().getTransactions().size())).isEqualTo(found + 1);
    }


}
