package com.WWI16AMA.backend_api.Events;

import org.springframework.context.ApplicationEvent;

public class EmailNotificationEvent extends ApplicationEvent {


    public EmailNotificationEvent(Object source) {
        super(source);
    }


}
