package com.muskopf.mailgun.emailsender.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@ComponentScan(basePackages = "com.muskopf.mailgun.emailsender")
@ConfigurationProperties(prefix = "mailgun.email-sender")
public class EmailSenderConfiguration {
    private String messagesUrl;
    private String apiKey;
    private String defaultSubject;
    private String defaultSenderEmailAddress;
    private String defaultSenderName;
    private Set<String> defaultRecipients;

    public String getMessagesUrl() {
        return messagesUrl;
    }
    public void setMessagesUrl(String messagesUrl) {
        this.messagesUrl = messagesUrl;
    }

    public String getApiKey() {
        return apiKey;
    }
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getDefaultSubject() {
        return defaultSubject;
    }
    public void setDefaultSubject(String defaultSubject) {
        this.defaultSubject = defaultSubject;
    }

    public Set<String> getDefaultRecipients() {
        return defaultRecipients;
    }
    public void setDefaultRecipients(Set<String> defaultRecipients) {
        this.defaultRecipients = defaultRecipients;
    }

    public String getDefaultSenderEmailAddress() { return defaultSenderEmailAddress; }
    public void setDefaultSenderEmailAddress(String defaultSenderEmailAddress) { this.defaultSenderEmailAddress = defaultSenderEmailAddress; }

    public String getDefaultSenderName() { return defaultSenderName; }
    public void setDefaultSenderName(String defaultSenderName) { this.defaultSenderName = defaultSenderName; }
}
