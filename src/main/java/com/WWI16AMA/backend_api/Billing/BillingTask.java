package com.WWI16AMA.backend_api.Billing;

import com.WWI16AMA.backend_api.Account.AccountRepository;
import com.WWI16AMA.backend_api.Account.Transaction;
import com.WWI16AMA.backend_api.Events.EmailNotificationEvent;
import com.WWI16AMA.backend_api.Events.IntTransactionEvent;
import com.WWI16AMA.backend_api.Fee.Fee;
import com.WWI16AMA.backend_api.Fee.FeeRepository;
import com.WWI16AMA.backend_api.Member.Member;
import com.WWI16AMA.backend_api.Member.MemberRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.time.Period;
import java.time.Year;
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

    public static LocalDate getNextBillingDate() {
        Year year = LocalDate.now().getDayOfYear() < 33 ? Year.now() : Year.now().plusYears(1);
        return LocalDate.of(year.getValue(), 2, 1);
    }

    public static boolean isInCurrentBillingPeriod(LocalDate date) {
        return date.isBefore(getNextBillingDate()) && getNextBillingDate().minusYears(1).minusDays(1).isBefore(date);
    }

    @Scheduled(cron = "0 0 12 1 2 *", zone = "Europe/Berlin")
    public void calculateFee() {

        Stream<Member> stream = StreamSupport.stream(memberRepository.findAll().spliterator(), false);
        stream.forEach(member -> {

            Fee.Status status = getStatus(member);

            double fee = feeRepository.findByCategory(status).get().getFee();
            Transaction tr = new Transaction(-fee, "Mitgliedsbeitrag " + member.getId() + ", " + member.getLastName(),
                    Transaction.FeeType.MITGLIEDSBEITRAG);
            publisher.publishEvent(new IntTransactionEvent(member.getMemberBankingAccount(), tr));
            publisher.publishEvent(new EmailNotificationEvent(member, EmailNotificationEvent.Type.AUFWENDUNGEN, tr));
        });
    }

    public void calculateEntranceFee(Member member) {

        LocalDate currentDate = LocalDate.now();
        int currentYear = currentDate.getYear();
        LocalDate billingDate = getNextBillingDate();
        // Angebrochene Monate werden nicht gezahlt
        int months = Period.between(currentDate, billingDate).getMonths();

        Fee.Status status = getStatus(member);
        double baseFee = feeRepository.findByCategory(status).get().getFee();
        double partialFee = ((baseFee / 12) * months);

        Transaction tr = new Transaction(-partialFee, "anteiliger Mitgliedsbeitrag wegen Neueintritt " + member.getId() + ". " + member.getLastName(),
                Transaction.FeeType.MITGLIEDSBEITRAG);
        publisher.publishEvent(new IntTransactionEvent(member.getMemberBankingAccount(), tr));
        publisher.publishEvent(new EmailNotificationEvent(member, EmailNotificationEvent.Type.AUFWENDUNGEN, tr));

    }

    private Fee.Status getStatus(Member member) {

        Fee.Status status;

        if (member.getStatus().equals(Member.Status.ACTIVE)
                && Period.between(member.getDateOfBirth(), LocalDate.now()).getYears() <= 20) {
            // "Jungtarif"
            status = Fee.Status.U20ACTIVE;
        } else {
            // "Normalfall"
            status = Fee.Status.valueOf(member.getStatus().name());
        }

        return status;
    }

}
