package com.WWI16AMA.backend_api;

import com.WWI16AMA.backend_api.Account.AccountRepository;
import com.WWI16AMA.backend_api.Events.EmailNotificationEvent;
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

}
