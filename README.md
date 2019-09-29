# Spring & Mailgun Email Sender
This component utilizes Spring 5 Boot & Reactor to facilitate the sending of Emails using the MailGun API.

## API
### [Email](src/main/java/com/muskopf/mailgun/emailsender/domain/Email.java)
The `Email` object has:
  - A `Set<String>` of recipient Email addresses
  - A `String` email representing the sender
  - A `String` subject
  - A `String` body
  
### [EmailBuilder](src/main/java/com/muskopf/mailgun/emailsender/proc/EmailBuilder.java)
Included within the project is a helper class, `EmailBuilder`.
`EmailBuilder` is a Spring Component, and can be `@Autowired` into any of your existing beans.

#### Email Builder Defaults
The `EmailBuilder` has a default value for the following fields:
  - The sender's email address (`defaultSenderEmailAddress`, `mailgun.email-sender.default`)
  - The sender's name (`defaultSenderName`, `mailgun.email-sender.default-sender-email-address`)
  - The email subject (`defaultSubject`, `mailgun.email-sender.default-subject`)
  - The email's recipients (`defaultRecipients`, `mailgun.email-sender.default-recipients`)
  
Where the first value within the parentheses is the field's name within the `EmailBuilder` class, and the second is the Spring property value within your Spring context. These defaults can be defined statically (e.g. defined in your `application.properties` file as seen [here](src/test/resources/application.properties)) or programatically using the `setDefault` methods of the `EmailBuilder` class.

#### Example Usage
Here's an example of a basic usage of the `EmailBuilder`:
```java
@Component
class MyClass {
  private EmailBuilder emailBuilder;

  @Autowired
  public MyClass(EmailBuilder emailBuilder) {
    this.emailBuilder = emailBuilder;
  }
  
  /**
    When building an Email, the building methods can be called in any order.
  */
  public Email buildEmail() {
    return emailBuilder.builder()
                       .subject("Subject");
                       .body("Hello, world!")
                       .addRecipient("you@yourplace.com")
                       .sender("me@info.com")
                       .build();
  }
}
```
Take notice of some of the `useDefault` convenience methods that the `EmailBuilder` defines:
  - `useDefaults` - When called, the default values for the `subject`, `recipients`, and `sender` are used:
  
    ```java
      public Email buildEmail() {
        return emailBuilder.builder()
                           .useDefaults()
                           .body("Hello, world!")
                           .build();
      }
    ```
    **NOTE:** This requires that each of the defaults for the `subject`, `recipients`, and `sender name/email address` are defined in your Spring Context (see: [Defining Defaults](#email-builder-defaults)). If _any_ of them are not, a corresponding EmailException](#EmailException) will be thrown.
  
  - There are similar methods to use the individual default values instead of all of them:
  
    ```java
    public Email buildEmail() {
      return emailBuilder.builder()
                         .useDefaultSubject()
                         .body("Hello, world!")
                         .useDefaultSender()
                         .addRecipient("you@yourplace.com")
                         .build();
    }
    ```
    **NOTE:** These methods (`useDefaultSubject`, `useDefaultSender`, `useDefaultRecipients`) will all throw a corresponding
    `EmailException` when their default value isn't set.

### [EmailSender](src/main/java/com/muskopf/mailgun/emailsender/EmailSender.java) (`SpringMailgunEmailSenderImpl`)
The `EmailSender` interface in this project has one direct implementation, [SpringMailgunEmailSenderImpl](src/main/java/com/muskopf/mailgun/emailsender/impl/SpringMailgunEmailSenderImpl.java).

#### Email Sender Defaults
The `EmailSender` requires two default values to be defined in your Spring Context:
  - `mailgun.email-sender.api-key` - Your API key as provided by MailGun
  - `mailgun.email-sender.messages-url` - Your messages URL as provided by MailGun (e.g. _https://api.mailgun.net/v3/YourCustomDomain.com/messages_)
  
#### Example Usage
An example usage of the `EmailSender` might look something like this:
```java
@Component
class MyClass {
  private EmailBuilder emailBuilder;
  private EmailSender emailSender;
  
  @Autowired
  public MyClass(EmailBuilder emailBuilder, EmailSender emailSender) {
    this.emailBuilder = emailBuilder;
    this.emailSender = emailSender;
  }

  public buildAndSendEmail() {
      Email email = emailBuilder.builder()
                                .useDefaults()
                                .body("Text")
                                .build();
                                
      MailgunResponse response = emailSender.send(email);
  }
}
```

## Using the Module
As of right now the best way to utilize this module is to clone and include it directly (... _Import Existing Module_) into your project.
