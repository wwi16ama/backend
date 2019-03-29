package com.WWI16AMA.backend_api;

import com.WWI16AMA.backend_api.Account.*;
import com.WWI16AMA.backend_api.Events.EmailNotificationEvent;
import com.WWI16AMA.backend_api.Events.ExtTransactionEvent;
import com.WWI16AMA.backend_api.Events.IntTransactionEvent;
import com.WWI16AMA.backend_api.Member.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class GlobalEventListener {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private VereinsKontoTransactionRepository vereinsKontoTransactionRepository;


    @Async
    @EventListener
    public void sendEmail(final EmailNotificationEvent emailNotificationEvent) {

        Member member = emailNotificationEvent.getMember();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(member.getEmail());
        message.setSubject("Test Spring");
        message.setText("Hallo");
        // TODO Wie handeln wir:
        //    MailAuthenticationException - in case of authentication failure
        //    MailSendException - in case of failure when sending a message
        javaMailSender.send(message);
    }

    @Async
    @EventListener
    public void makeTransaction(final ExtTransactionEvent transactionEvent) {

        Member member = transactionEvent.getMember();
        Account account = member.getMemberBankingAccount();
        account.addTransaction(new Transaction(transactionEvent.getAmount(), transactionEvent.getType()));

        //TODO
//         if (transactionEvent.getType().equals(Transaction.FeeType.GEBÜHR)) {
//             account.add2Balance(-transactionEvent.getAmount());
//         } else if (transactionEvent.getType().equals(Transaction.FeeType.ZAHLUNG)) {
//             account.add2Balance(transactionEvent.getAmount());
//         } else {
//             account.add2Balance(transactionEvent.getAmount());
//         }

        accountRepository.save(account);
    }

    @Async
    @EventListener
    public void makeInternalTransaction(IntTransactionEvent ev) {
        Account memAcc = ev.getMember().getMemberBankingAccount();
        Transaction tr = new Transaction(ev.getAmount(), ev.getType());
        memAcc.addTransaction(tr);
        VereinsKontoTransaction vtr = new VereinsKontoTransaction(tr, ev.getMember());

        accountRepository.save(memAcc);
        vereinsKontoTransactionRepository.save(vtr);
    }
}
