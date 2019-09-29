package com.muskopf.mailgun.emailsender.domain;

public class NoSubjectException extends EmailException {
    public NoSubjectException() {
        super("No Email subject provided!");
    }
}
