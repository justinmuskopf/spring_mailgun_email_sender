package com.muskopf.mailgun.emailsender.proc;

import com.muskopf.mailgun.emailsender.domain.InvalidEmailException;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class EmailValidator {
    private Pattern emailPattern = Pattern.compile("^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,6}$");

    public void validate(String email) {
        if (!emailPattern.matcher(email).matches()) {
            throw new InvalidEmailException(email);
        }
    }
}
