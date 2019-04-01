package com.WWI16AMA.backend_api.Email;

import com.WWI16AMA.backend_api.Account.Transaction;
import com.WWI16AMA.backend_api.Member.Member;
import org.apache.commons.io.IOUtils;
import org.apache.tomcat.jni.Local;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
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
import java.net.URL;
import java.time.LocalDate;
import java.util.Date;
import java.util.Locale;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private TemplateEngine htmlTemplateEngine;


    public void sendBillingNotification(
            final Member member, Locale locale, String subjectBody, String contact, Transaction transaction)
            throws MessagingException {

        final MultipartFile multipartFile = getLogo();
        Context billingCtx = prepareBasicData(locale);
        billingCtx.setVariable("subjectBody", subjectBody);
        billingCtx.setVariable("contact", contact);
        billingCtx.setVariable("name", member.getFirstName()+" " +member.getLastName());
        billingCtx.setVariable("amount", -transaction.getAmount());
        billingCtx.setVariable("bankingAccount", member.getBankingAccount());
        billingCtx.setVariable("memberID", member.getId());
        billingCtx.setVariable("date", LocalDate.now());
        billingCtx.setVariable("bankName", "leer");
        billingCtx.setVariable("bic", "leer");
        billingCtx.setVariable("jobNumber", "leer");

        billingCtx.setVariable("imageResourceName", multipartFile.getName());


        final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        final MimeMessageHelper message =
                new MimeMessageHelper(mimeMessage, true, "UTF-8"); // true = multipart
        message.setSubject("Aufwandsentschädigung");
        message.setFrom("Flugverein Reilingen <flugverein@reilingen.com>");
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

    public MultipartFile getLogo()  {


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

    private Context prepareBasicData(Locale locale){
        final Context ctx = new Context(locale);

        return ctx;
    }

}

