package com.WWI16AMA.backend_api.Email;

import com.WWI16AMA.backend_api.Account.Transaction;
import com.WWI16AMA.backend_api.Member.Member;
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
import java.util.Locale;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine htmlTemplateEngine;


    public void sendBillingNotification(
            final Member member, Locale locale, Transaction transaction)
            throws MessagingException {

        final MultipartFile multipartFile = getLogo();

        Context billingCtx = prepareBasicData(locale, member, multipartFile);
        billingCtx.setVariable("amount", formatAmount(transaction.getAmount()));

        final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        final MimeMessageHelper message =
                new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
        message.setSubject("Abbuchung Ihres Mitgliedsbeitrages");
        message.setFrom("Flugverein Reilingen <flugverein@reilingen.com>");
        //message.setTo(member.getEmail());
        message.setTo("him.kourouma@hotmail.com");

        final String htmlContent = this.htmlTemplateEngine.process("billing-email.html", billingCtx);
        message.setText(htmlContent, true); // true = isHtml

        InputStreamSource imageSource = null;

        try {
            imageSource = new ByteArrayResource(multipartFile.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
        message.addInline(multipartFile.getName(), imageSource, multipartFile.getContentType());

        this.javaMailSender.send(mimeMessage);

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

    private Context prepareBasicData(Locale locale, Member member, MultipartFile multipartFile) {
        final Context ctx = new Context(locale);
        ctx.setVariable("lastName", member.getLastName());
        ctx.setVariable("imageResourceName", multipartFile.getName());

        return ctx;
    }

    private String formatAmount(double amount) {
        if (amount < 0) {
            return (-amount) + "0"; // Negatives Vorzeichen wird für die E-Mail entfernt.
        } else {
            return amount + "0";
        }
    }

}

