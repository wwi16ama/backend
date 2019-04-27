package com.WWI16AMA.backend_api.Email;

import com.WWI16AMA.backend_api.Account.Transaction;
import com.WWI16AMA.backend_api.Member.Gender;
import com.WWI16AMA.backend_api.Member.Member;
import com.WWI16AMA.backend_api.Plane.Plane;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Locale;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine htmlTemplateEngine;


    public void sendBillingNotification(Member member, Transaction transaction) {

        String subject = "Abbuchung Ihres Mitgliedsbeitrages";
        String template = "billing-email.html";

        Context ctx = new Context(new Locale("de"));
        ctx.setVariable("amount", formatAmount(transaction.getAmount()));

        sendMail(subject, ctx, template, member);
    }

    public void sendGutschriftNotification(Member member, Transaction transaction) {

        String subject = "Auszahlung für getätigte Aufwände";
        String template = "gutschrift-email.html";

        Context ctx = new Context(new Locale("de"));
        ctx.setVariable("amount", formatAmount(transaction.getAmount()));

        sendMail(subject, ctx, template, member);
    }

    public void sendTankNotification(Member member, Transaction transaction, Plane plane) {

        String subject = "Erstattung Tankkosten";
        String template = "betankung-email.html";

        Context ctx = new Context(new Locale("de"));
        ctx.setVariable("amount", formatAmount(transaction.getAmount()));
        ctx.setVariable("planeName", plane.getName());

        sendMail(subject, ctx, template, member);
    }

    private void sendMail(String subject, Context ctx, String template, Member member) {
        try {

            ctx.setVariable("anrede", member.getGender().equals(Gender.MALE) ? "Herr" : "Frau");
            ctx.setVariable("lastName", member.getLastName());

            final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
            final MimeMessageHelper message; // true = multipart
            message = new MimeMessageHelper(mimeMessage, true, "UTF-8");

            message.setSubject(subject);
            message.setFrom("Flugverein Reilingen <flugverein@reilingen.com>");
            message.setTo(member.getEmail());

            MultipartFile multipartFile = getLogo();
            ctx.setVariable("imageResourceName", multipartFile.getName());

            String htmlContent = this.htmlTemplateEngine.process(template, ctx);
            message.setText(htmlContent, true); // true = isHtml

            InputStreamSource imageSource = new ByteArrayResource(multipartFile.getBytes());

            message.addInline(multipartFile.getName(), imageSource, multipartFile.getContentType());
            this.javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private MultipartFile getLogo() {
        File file = null;
        FileInputStream input = null;
        MultipartFile multipartFile = null;
        try {
            file = ResourceUtils.getFile("classpath:images/logo.png");
            input = new FileInputStream(file);
            multipartFile = new MockMultipartFile("fileItem",
                    file.getName(), "image/png", IOUtils.toByteArray(input));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return multipartFile;
    }

    private String formatAmount(double amount) {
        amount = Math.abs(amount);
        return BigDecimal.valueOf(amount).setScale(2, BigDecimal.ROUND_HALF_DOWN).toString();
    }

}

