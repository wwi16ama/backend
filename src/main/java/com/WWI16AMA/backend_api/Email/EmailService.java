package com.WWI16AMA.backend_api.Email;

import com.WWI16AMA.backend_api.Member.Member;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.util.Locale;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine htmlTemplateEngine;


    public void sendBillingNotification(
            final Member member, Locale locale)
            throws MessagingException {

        Context billingCtx = prepareBasicData(locale);
        billingCtx.setVariable("subject", "Aufwandszahlung");

        //billingCtx.setVariable("", "");

        final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        final MimeMessageHelper message =
                new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
        message.setSubject("Example HTML email with inline image");
        message.setFrom("Flugverein Reilingen <flugverein@reilingen.com>");
        message.setTo("him.kourouma@hotmail.com");

        final String htmlContent = this.htmlTemplateEngine.process("billing-email.html", billingCtx);
        message.setText(htmlContent, true); // true = isHtml

        //final InputStreamSource imageSource = new ByteArrayResource(imageBytes);
        //message.addInline(imageResourceName, imageSource, imageContentType);

        this.javaMailSender.send(mimeMessage);


    }

    public void sendMailWithInline() {

    }

    private Context prepareBasicData(Locale locale){
        final Context ctx = new Context(locale);
        ctx.setVariable("sender", "Flugsportverein Reilingen");
        ctx.setVariable("club-name", "Flugsportverein Reilingen");
        ctx.setVariable("contact", "Ibrahima Kourouma"); // so that we can reference it from HTML
        return ctx;
    }

}

