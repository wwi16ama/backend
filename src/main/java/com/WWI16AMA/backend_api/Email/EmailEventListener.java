package com.WWI16AMA.backend_api.Email;

import com.WWI16AMA.backend_api.Account.AccountRepository;
import com.WWI16AMA.backend_api.Events.EmailNotificationEvent;
import com.WWI16AMA.backend_api.Member.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.Locale;

@Component
public class EmailEventListener {

    // nimmt den Wert aus der application.properties
    @Value("${wwi16ama.mail.enabled:false}")
    private boolean mailEnabled;
    @Autowired
    private EmailService service;
    @Autowired
    private AccountRepository accountRepository;


    @Async
    @EventListener
    public void sendEmail(final EmailNotificationEvent emailNotificationEvent) {

        if (mailEnabled) {
            Member member = emailNotificationEvent.getMember();
            Locale locale = new Locale("de");
            String contact = "Jörg Steinfeld";


            if (emailNotificationEvent.getType().equals(EmailNotificationEvent.Type.AUFWENDUNGEN)) {
                String subjectContent = "des Mitgliedbeitrages";
                try {
                    service.sendBillingNotification(member, locale, emailNotificationEvent.getTransaction());
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
            }

        } else {
            System.out.print("Der Mailversand ist deaktiviert. ");
            System.out.println("Es wäre eine Mail an "
                    + emailNotificationEvent.getMember().getEmail()
                    + " geschickt worden.");
        }
    }

}
