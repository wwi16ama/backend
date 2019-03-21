package com.WWI16AMA.backend_api;

import com.WWI16AMA.backend_api.Account.Account;
import com.WWI16AMA.backend_api.Account.AccountRepository;
import com.WWI16AMA.backend_api.Account.Transaction;
import com.WWI16AMA.backend_api.Events.EmailNotificationEvent;
import com.WWI16AMA.backend_api.Events.TransactionEvent;
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


    @Async
    @EventListener
    public void sendEmail(final EmailNotificationEvent emailNotificationEvent) {

        Member member = (Member) emailNotificationEvent.getSource();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo("him.kourouma@hotmail.com");
        message.setSubject("Test Spring ");
        message.setText("Hallo");
        javaMailSender.send(message);

    }

    @Async
    @EventListener
    public void makeTransaction(final TransactionEvent transactionEvent) {

        Member member = (Member) transactionEvent.getSource();
        Account account = member.getMemberBankingAccount();
        account.addTransaction(new Transaction(transactionEvent.getAmount(), transactionEvent.getType()));

        if (transactionEvent.getType().equals(Transaction.FeeType.GEBÜHR)) {
            account.add2Balance(-transactionEvent.getAmount());
        } else if (transactionEvent.getType().equals(Transaction.FeeType.ZAHLUNG)) {
            account.add2Balance(transactionEvent.getAmount());
        } else {
            account.add2Balance(transactionEvent.getAmount());
        }

        accountRepository.save(account);
    }
}
