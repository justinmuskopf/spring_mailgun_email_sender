package com.muskopf.mailgun.emailsender.domain;

import java.util.List;
import java.util.stream.Collectors;

public class InvalidEmailException extends EmailException {
    public InvalidEmailException(String email) {
        super("Invalid Email: " + email);
    }

    public InvalidEmailException(List<String> emails) {
        this(emails.stream().map(e -> e + "\n").collect(Collectors.joining()));
    }
}
