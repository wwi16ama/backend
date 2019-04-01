package com.WWI16AMA.backend_api;

import com.WWI16AMA.backend_api.Account.AccountRepository;
import com.WWI16AMA.backend_api.Email.EmailService;
import com.WWI16AMA.backend_api.Events.EmailNotificationEvent;
import com.WWI16AMA.backend_api.Member.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.Locale;

@Component
public class GlobalEventListener {

    @Autowired
    private EmailService service;
    @Autowired
    private AccountRepository accountRepository;


    @Async
    @EventListener
    public void sendEmail(final EmailNotificationEvent emailNotificationEvent) {


        Member member = emailNotificationEvent.getMember();
        Locale locale = new Locale("de");
        String contact = "Jörg Steinfeld";


        if(emailNotificationEvent.getType().equals(EmailNotificationEvent.Type.AUFWENDUNGEN)){
            String subjectContent = "des Mitgliedbeitrages";
            try {
                service.sendBillingNotification(member, locale, subjectContent, contact, emailNotificationEvent.getTransaction());
            } catch (MessagingException e) {
                e.printStackTrace();
            }
        }

    }

}
