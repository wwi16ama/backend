package com.WWI16AMA.backend_api.Email;

import com.WWI16AMA.backend_api.Account.AccountRepository;
import com.WWI16AMA.backend_api.Events.EmailNotificationEvent;
import com.WWI16AMA.backend_api.Member.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

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
    public void sendEmail(final EmailNotificationEvent ev) {

        if (mailEnabled) {
            Member member = ev.getMember();

            if (ev.getType().equals(EmailNotificationEvent.Type.AUFWENDUNGEN)) {
                service.sendBillingNotification(member, ev.getTransaction());
            }

            if (ev.getType().equals(EmailNotificationEvent.Type.AUFWANDSENTSCHÄDIGUNG)) {
                service.sendGutschriftNotification(member, ev.getTransaction());
            }

            if (ev.getType().equals(EmailNotificationEvent.Type.TANKEN)) {
                service.sendTankNotification(member, ev.getTransaction(), ev.getPlane());
            }

        } else {
            System.out.print("Der Mailversand ist deaktiviert. ");
            System.out.println("Es wäre eine Mail vom Typ "
                    + ev.getType().toString()
                    + " an " + ev.getMember().getEmail()
                    + " geschickt worden.");
        }
    }

}
