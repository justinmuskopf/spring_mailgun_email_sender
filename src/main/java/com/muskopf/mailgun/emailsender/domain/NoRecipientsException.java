package com.muskopf.mailgun.emailsender.domain;

public class NoRecipientsException extends EmailException {
    public NoRecipientsException() {
        super("No Email recipients provided!");
    }
}
