package com.muskopf.mailgun.emailsender.domain;

import java.util.HashSet;
import java.util.Set;

public class Email {
    private Set<String> recipients;
    private String sender;
    private String subject;
    private String body;

    public Set<String> getRecipients() {
        return recipients;
    }
    public void setRecipients(Set<String> recipients) {
        this.recipients = recipients;
    }
    public void addRecipient(String recipient) {
        if (recipients == null) {
            recipients = new HashSet<>();
        }

        this.recipients.add(recipient);
    }

    public String getSubject() {
        return subject;
    }
    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }
    public void setBody(String body) {
        this.body = body;
    }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
}
