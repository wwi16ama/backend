package com.WWI16AMA.backend_api.Billing;

import com.WWI16AMA.backend_api.Account.AccountRepository;
import com.WWI16AMA.backend_api.Account.Transaction;
import com.WWI16AMA.backend_api.Events.EmailNotificationEvent;
import com.WWI16AMA.backend_api.Events.ExtTransactionEvent;
import com.WWI16AMA.backend_api.Fee.Fee;
import com.WWI16AMA.backend_api.Fee.FeeRepository;
import com.WWI16AMA.backend_api.Member.Member;
import com.WWI16AMA.backend_api.Member.MemberRepository;
import com.WWI16AMA.backend_api.Member.Status;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.Period;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;


public class BillingTask {

    private AccountRepository accountRepository;
    private FeeRepository feeRepository;
    private MemberRepository memberRepository;
    private ApplicationEventPublisher publisher;

    public BillingTask(AccountRepository accountRepository, FeeRepository feeRepository, MemberRepository memberRepository, ApplicationEventPublisher publisher) {
        this.accountRepository = accountRepository;
        this.feeRepository = feeRepository;
        this.memberRepository = memberRepository;
        this.publisher = publisher;
    }

    @Scheduled(cron = "0 0 12 1 2 ? *", zone = "Europe/Berlin")
    public void calculateFee() {

        Stream<Member> stream = StreamSupport.stream(memberRepository.findAll().spliterator(), false);
        stream
                .filter(member -> Period.between(member.getDateOfBirth(), LocalDate.now()).getYears() > 20)
                .forEach(member -> {

                    if (!member.getStatus().equals(Status.HONORARYMEMBER)) {
                        publisher.publishEvent(new ExtTransactionEvent(member,
                                feeRepository.findByCategory(Fee.Status.valueOf(member.getStatus().name())).get().getFee(),
                                Transaction.FeeType.GEBÜHRFLUGZEUG));
                        publisher.publishEvent(new EmailNotificationEvent(member));
                    }
                });

        stream
                .filter(member -> Period.between(member.getDateOfBirth(), LocalDate.now()).getYears() <= 20)
                .forEach(member -> {

                    if (!member.getStatus().equals(Status.HONORARYMEMBER)) {

                        if (member.getStatus().equals(Status.ACTIVE)) {
                            publisher.publishEvent(new ExtTransactionEvent(member,
                                    feeRepository.findByCategory(Fee.Status.U20ACTIVE).get().getFee(),
                                    Transaction.FeeType.GEBÜHRFLUGZEUG));
                            publisher.publishEvent(new EmailNotificationEvent(member));
                        } else {

                            publisher.publishEvent(new ExtTransactionEvent(member,
                                    feeRepository.findByCategory(Fee.Status.valueOf(member.getStatus().name())).get().getFee(),
                                    Transaction.FeeType.GEBÜHRFLUGZEUG));
                            publisher.publishEvent(new EmailNotificationEvent(member));
                        }

                    }
                });
    }


}
