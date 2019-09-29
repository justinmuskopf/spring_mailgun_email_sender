package com.muskopf.mailgun.emailsender.impl;

import com.muskopf.mailgun.emailsender.EmailSender;
import com.muskopf.mailgun.emailsender.config.EmailSenderConfiguration;
import com.muskopf.mailgun.emailsender.domain.Email;
import com.muskopf.mailgun.emailsender.domain.EmailException;
import com.muskopf.mailgun.emailsender.domain.InvalidEmailException;
import com.muskopf.mailgun.emailsender.domain.MailgunResponse;
import com.muskopf.mailgun.emailsender.proc.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class SpringMailgunEmailSenderImpl implements EmailSender {
    private String apiKey;
    private String messagesUrl;
    private EmailValidator emailValidator;
    private WebClient webClient;
    private Logger logger = LoggerFactory.getLogger(SpringMailgunEmailSenderImpl.class);

    @Autowired
    public SpringMailgunEmailSenderImpl(EmailSenderConfiguration configuration, EmailValidator emailValidator) {
        this.apiKey = configuration.getApiKey();
        this.messagesUrl = configuration.getMessagesUrl();
        this.emailValidator = emailValidator;
        this.webClient = WebClient.builder().baseUrl(messagesUrl).build();
    }

    private Set<String> getValidEmailRecipients(Email email) {
        Set<String> recipients = new HashSet<>();
        for (String recipient : email.getRecipients()) {
            try {
                emailValidator.validate(recipient);
                recipients.add(recipient);
            } catch (InvalidEmailException e) {
                logger.error(e.getMessage());
            }
        }

        return recipients;
    }

    public MailgunResponse sendEmailToApi(String from, Set<String> recipients, String subject, String body) {
        logger.debug("Sending Email: " +
                "From: " + from + "\n" +
                "Recipient(s): " + recipients.toString()+ "\n" +
                "Subject: " + subject + "\n");

        MailgunResponse response = null;
        try {
            response = webClient.post()
                    .uri(uriBuilder -> {
                        recipients.forEach(r -> uriBuilder.queryParam("to", r));
                        uriBuilder.queryParam("from", from);
                        uriBuilder.queryParam("subject", subject);
                        uriBuilder.queryParam("text", body);
                        return uriBuilder.build();
                    })
                    .headers(h -> h.setBasicAuth("api", apiKey))
                    .retrieve()
                    .bodyToMono(MailgunResponse.class)
                    .block();
        } catch (WebClientException e) {
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public MailgunResponse sendEmail(Email email) throws EmailException {
        Set<String> recipients = getValidEmailRecipients(email);
        if (recipients.isEmpty()) {
            return null;
        }

        return sendEmailToApi(email.getSender(), recipients, email.getSubject(), email.getBody());
    }

    @Override
    public List<MailgunResponse> sendEmails(List<Email> emails) {
        return emails
                .stream()
                .map(this::sendEmail)
                .collect(Collectors.toList());
    }
}
