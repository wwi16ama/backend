package com.WWI16AMA.backend_api;

import com.WWI16AMA.backend_api.Events.EmailNotificationEvent;
import com.WWI16AMA.backend_api.Member.Member;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.mail.MailSendException;

import java.lang.reflect.Method;

public class CustomAsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(
            Throwable throwable, Method method, Object... obj) {

        if (throwable instanceof MailSendException) {
            System.out.println("Mailversand fehlgeschlagen");
            for (Object param : obj) {
                System.out.println("Parameter value - " + param);
                if (param instanceof EmailNotificationEvent) {
                    Member member = ((EmailNotificationEvent) param).getMember();
                    System.out.println("\tMitgliedsnr:\t" + member.getId());
                    System.out.println("\tName:\t" + member.getFirstName() + " " + member.getLastName());
                    System.out.println("\tMailadresse:\t" + member.getEmail());
                }
            }
        } else {
            System.out.println("Exception message - " + throwable.getMessage());
            System.out.println("Method name - " + method.getName());

            for (StackTraceElement st : throwable.getStackTrace()) {
                System.out.println(st.toString());
            }
        }

    }

}