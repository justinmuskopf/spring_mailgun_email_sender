package com.muskopf.mailgun.emailsender.domain;

public class EmailException extends RuntimeException {
    public EmailException(String s) {
        super(s);
    }
}
