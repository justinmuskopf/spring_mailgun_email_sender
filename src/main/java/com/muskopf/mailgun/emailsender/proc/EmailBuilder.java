package com.muskopf.mailgun.emailsender.proc;

import com.muskopf.mailgun.emailsender.config.EmailSenderConfiguration;
import com.muskopf.mailgun.emailsender.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.validation.constraints.Past;
import java.util.HashSet;
import java.util.Set;

@Component
public class EmailBuilder {
    private String defaultSenderEmailAddress;
    private String defaultSenderName;
    private String defaultSubject;
    private Set<String> defaultRecipients;

    @Autowired
    public EmailBuilder(EmailSenderConfiguration configuration) {
        this.defaultSenderEmailAddress = configuration.getDefaultSenderEmailAddress();
        this.defaultSenderName = configuration.getDefaultSenderName();
        this.defaultSubject = configuration.getDefaultSubject();
        this.defaultRecipients = configuration.getDefaultRecipients();
    }

    public String getDefaultSubject() { return defaultSubject; }
    public void setDefaultSubject(String defaultSubject) { this.defaultSubject = defaultSubject; }

    public Set<String> getDefaultRecipients() { return defaultRecipients; }
    public void setDefaultRecipients(Set<String> defaultRecipients) { this.defaultRecipients = defaultRecipients; }
    public void addDefaultRecipient(String recipient) { this.defaultRecipients.add(recipient); }

    public String getDefaultSenderEmailAddress() { return defaultSenderEmailAddress; }
    public void setDefaultSenderEmailAddress(String defaultSenderAddress) { this.defaultSenderEmailAddress = defaultSenderAddress; }

    public String getDefaultSenderName() { return defaultSenderName; }
    public void setDefaultSenderName(String defaultSenderName) { this.defaultSenderName = defaultSenderName; }

    public Builder builder() {
        return new Builder(defaultSenderEmailAddress, defaultSenderName, defaultSubject, defaultRecipients);
    }

    public static class Builder {
        private String defaultSubject;
        private String defaultSender;
        private Set<String> defaultRecipients;

        private String subject = "";
        private String body = "";
        private String sender = "";
        private Set<String> recipients = null;

        private Builder(String defaultSenderEmailAddress,
                        String defaultSenderName,
                        String defaultSubject,
                        Set<String> defaultRecipients)
        {
            this.defaultSubject = defaultSubject;
            this.defaultRecipients = defaultRecipients;
            this.defaultSender = buildSenderField(defaultSenderName, defaultSenderEmailAddress);
        }

        private String buildSenderField(String senderName, String senderAddress) {
            return senderName + " " + "<" + senderAddress + ">";
        }

        public Builder sender(String senderName, String senderEmailAddress) {
            this.sender = buildSenderField(senderName, senderEmailAddress);

            return this;
        }

        public Builder subject(String subject) {
            this.subject = subject;

            return this;
        }

        public Builder body(String body) {
            this.body = body;

            return this;
        }

        public Builder recipients(Set<String> recipients) {
            this.recipients = recipients;

            return this;
        }

        public Builder addRecipient(String recipient) {
            if (this.recipients == null) {
                this.recipients = new HashSet<>();
            }

            this.recipients.add(recipient);

            return this;
        }

        public Builder useDefaultSubject() {
            if (defaultSubject == null || defaultSubject.isEmpty()) {
                throw new EmailException("No default subject is defined! (mailgun.email-sender.default-subject)");
            }

            this.subject = defaultSubject;

            return this;
        }

        public Builder useDefaultSender() {
            if (defaultSender == null || defaultSender.isEmpty()) {
                throw new EmailException("No default sender is defined! (mailgun.email-sender.default-sender-email-address/mailgun.email-sender.default-sender-name)");
            }

            this.sender = defaultSender;

            return this;
        }

        public Builder useDefaultRecipients() {
            if (defaultRecipients == null || defaultRecipients.isEmpty()) {
                throw new EmailException("No default recipients defined! (mailgun.email-sender.default-recipients)");
            }

            this.recipients = defaultRecipients;

            return this;
        }


        public Builder useDefaults() {
            useDefaultRecipients();
            useDefaultSubject();
            useDefaultSender();

            return this;
        }

        public Email build() {
            if (StringUtils.isEmpty(sender)) {
                throw new NoSenderException();
            }

            if (StringUtils.isEmpty(subject)) {
                throw new NoSubjectException();
            }

            if (StringUtils.isEmpty(body)) {
                throw new NoBodyException();
            }

            if (recipients == null || recipients.isEmpty()) {
                throw new NoRecipientsException();
            }

            Email email = new Email();
            email.setSender(sender);
            email.setSubject(subject);
            email.setBody(body);
            email.setRecipients(recipients);

            return email;
        }
    }
}
