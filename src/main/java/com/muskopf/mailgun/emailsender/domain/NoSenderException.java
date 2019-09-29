package com.muskopf.mailgun.emailsender.domain;

public class NoSenderException extends EmailException {
    public NoSenderException() {
        super("No Email Sender provided!");
    }
}
