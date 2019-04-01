package com.WWI16AMA.backend_api;

import com.WWI16AMA.backend_api.Account.AccountRepository;
import com.WWI16AMA.backend_api.Account.ProtectedAccount.Account;
import com.WWI16AMA.backend_api.Account.ProtectedAccount.VereinsAccount;
import com.WWI16AMA.backend_api.Account.Transaction;
import com.WWI16AMA.backend_api.Events.IntTransactionEvent;
import com.WWI16AMA.backend_api.Member.Member;
import com.WWI16AMA.backend_api.Member.MemberRepository;
import com.WWI16AMA.backend_api.Member.OfficeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static com.WWI16AMA.backend_api.TestUtil.createBasicAuthHeader;
import static com.WWI16AMA.backend_api.TestUtil.saveAndGetMember;
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
    private MemberRepository memberRepository;
    @Autowired
    private OfficeRepository officeRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testRepository() {

        long i = accountRepository.count();
        saveAndGetMember(memberRepository, officeRepository, passwordEncoder, "123password");

        // Check that an Account was created
        assertThat(accountRepository.count()).isEqualTo(i + 1);
    }

    @Test
    public void testPostAccountController() throws Exception {

        String pw = "123pasword";
        Member mem = saveAndGetMember(memberRepository, officeRepository, passwordEncoder, pw);
        Account acc = mem.getMemberBankingAccount();

        long found = acc.getTransactions().size();

        Transaction transaction = new Transaction(500, Transaction.FeeType.EINZAHLUNG);

        this.mockMvc.perform(post("/accounts/" + acc.getId() + "/transactions")
                .headers(createBasicAuthHeader(mem.getId().toString(), pw))
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(transaction))).andExpect(status().isOk());

        assertThat(((long) accountRepository.findById(acc.getId()).get().getTransactions().size())).isEqualTo(found + 1);
    }

    @Test
    @WithMockUser(roles = {"KASSIERER"})
    public void createTransactionAsKassierer() throws Exception {

        String pw = "123pasword";
        Member mem = saveAndGetMember(memberRepository, officeRepository, passwordEncoder, pw);
        Account acc = mem.getMemberBankingAccount();

        long found = acc.getTransactions().size();

        Transaction transaction = new Transaction(500, Transaction.FeeType.EINZAHLUNG);

        this.mockMvc.perform(post("/accounts/{id}/transactions", acc.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.marshal(transaction))).andExpect(status().isOk());

        assertThat(((long) accountRepository.findById(acc.getId()).get().getTransactions().size())).isEqualTo(found + 1);
    }


    @Test
    public void internalTransaction() {
        Member mem = saveAndGetMember(memberRepository, officeRepository, passwordEncoder, "wasGEht123");

        double oldBalanceMember = mem.getMemberBankingAccount().getBalance();
        double oldBalanceVerein = VereinsAccount.getInstance().getBalance();

        double amount = 20.0;

        publisher.publishEvent(new IntTransactionEvent(
                mem.getMemberBankingAccount(),
                new Transaction(amount, Transaction.FeeType.GUTSCHRIFTLEISTUNG)));

        assertThat(oldBalanceMember + amount).isEqualTo(mem.getMemberBankingAccount().getBalance());
        assertThat(oldBalanceVerein - amount).isEqualTo(VereinsAccount.getInstance().getBalance());
    }

}
