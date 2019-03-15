package com.WWI16AMA.backend_api.Billing;

import com.WWI16AMA.backend_api.Account.Account;
import com.WWI16AMA.backend_api.Account.AccountRepository;
import com.WWI16AMA.backend_api.Account.Transaction;
import com.WWI16AMA.backend_api.Fee.Fee;
import com.WWI16AMA.backend_api.Fee.FeeRepository;
import com.WWI16AMA.backend_api.Member.Member;
import com.WWI16AMA.backend_api.Member.MemberRepository;
import com.WWI16AMA.backend_api.Member.Status;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.Period;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public class BillingTask {

    private AccountRepository accountRepository;
    private FeeRepository feeRepository;
    private MemberRepository memberRepository;

    public BillingTask(AccountRepository accountRepository, FeeRepository feeRepository, MemberRepository memberRepository) {
        this.accountRepository = accountRepository;
        this.feeRepository = feeRepository;
        this.memberRepository = memberRepository;
    }

    @Scheduled(cron = "0 0 12 1 2 ? *", zone = "Europe/Berlin")
    public void calculateFee() {

        Stream<Member> stream = StreamSupport.stream(memberRepository.findAll().spliterator(), false);
        stream
                .filter(member -> Period.between(member.getDateOfBirth(), LocalDate.now()).getYears() > 20)
                .forEach(member -> {

                    if (!member.getStatus().equals(Status.HONORARYMEMBER)) {
                        Account account = member.getMemberBankingAccount();
                        makeTransaction(account,
                                feeRepository.findByCategory(Fee.Status.valueOf(member.getStatus().name())).get().getFee(),
                                Transaction.FeeType.GEBÜHR);
                    }
                });

        stream
                .filter(member -> Period.between(member.getDateOfBirth(), LocalDate.now()).getYears() <= 20)
                .forEach(member -> {

                    if (!member.getStatus().equals(Status.HONORARYMEMBER)) {

                        if (member.getStatus().equals(Status.ACTIVE)) {
                            Account account = member.getMemberBankingAccount();
                            makeTransaction(account,
                                    feeRepository.findByCategory(Fee.Status.U20ACTIVE).get().getFee(),
                                    Transaction.FeeType.GEBÜHR);
                        } else {
                            Account account = member.getMemberBankingAccount();
                            makeTransaction(account,
                                    feeRepository.findByCategory(Fee.Status.valueOf(member.getStatus().name())).get().getFee(),
                                    Transaction.FeeType.GEBÜHR);
                        }
                    }
                });
    }


    private void makeTransaction(Account account, int amount, Transaction.FeeType type) {
        account.addTransaction(new Transaction(amount, type));
        if(type.equals(Transaction.FeeType.GEBÜHR)){
            account.add2Balance(-amount);
        } else if(type.equals(Transaction.FeeType.ZAHLUNG)){
            account.add2Balance(amount);
        } else {
            account.add2Balance(amount);
        }

        accountRepository.save(account);
    }

}
