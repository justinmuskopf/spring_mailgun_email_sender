package com.muskopf.mailgun.emailsender.proc;

import com.muskopf.mailgun.emailsender.domain.InvalidEmailException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
        EmailValidator.class
})
public class EmailValidatorTests {
    private String VALID_EMAIL = "test@test.com";
    private String INVALID_EMAIL = "malformed@bad_example.123";

    @Autowired
    private EmailValidator validator;

    @Test
    public void testValidatesValidEmail() {
        validator.validate(VALID_EMAIL);
    }

    @Test(expected = InvalidEmailException.class)
    public void testThrowsOnInvalidEmail() {
        validator.validate(INVALID_EMAIL);
    }
}
