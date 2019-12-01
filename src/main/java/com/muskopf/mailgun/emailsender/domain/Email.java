package com.muskopf.mailgun.emailsender.domain;

import java.util.Arrays;
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

    @Override
    public String toString() {
        StringBuilder formattedRecipients = new StringBuilder().append("[\n");
        recipients.forEach(r -> formattedRecipients.append("\t").append(r).append("\n"));
        formattedRecipients.append("]\n");

        StringBuilder formattedBody = new StringBuilder();
        Arrays.stream(body.split("\n")).forEach(line -> formattedBody.append("\t").append(line).append("\n"));

        StringBuilder sb = new StringBuilder()
                .append("----------------------------------------------------------\n")
                .append("Recipients: ").append(formattedRecipients.toString())
                .append("Sender    : [").append(sender).append("]\n")
                .append("Subject   : ").append(subject).append("\n")
                .append("Body      :\n").append(formattedBody.toString())
                .append("----------------------------------------------------------\n");

        return sb.toString();
    }
}
