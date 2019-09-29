package com.muskopf.mailgun.emailsender;

import com.muskopf.mailgun.emailsender.domain.Email;
import com.muskopf.mailgun.emailsender.domain.MailgunResponse;

import java.util.List;

public interface EmailSender {
    MailgunResponse sendEmail(Email email);
    List<MailgunResponse> sendEmails(List<Email> emails);
}
