package com.muskopf.mailgun.emailsender.domain;

public class NoBodyException extends EmailException {
    public NoBodyException() {
        super("No Email body provided!");
    }
}
