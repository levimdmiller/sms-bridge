package ca.levimiller.smsbridge.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.Message;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public abstract class MessageServiceTest {
  private final MessageService messageService;

  protected MessageServiceTest(MessageService messageService) {
    this.messageService = messageService;
  }

  private Contact validContact;
  private Contact invalidContact;

  @BeforeEach
  void setup() {
    validContact = Contact.builder()
        .number("1234567890")
        .build();
    invalidContact = Contact.builder().build();
  }

  @Test
  void save_NullThrowsConstraintViolationException() {
    ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
        () -> messageService.save(null));
    assertEquals("save.message: must not be null", thrown.getMessage());
  }

  @Test
  void save_NullToContactThrowsConstraintViolationException() {
    ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
            () -> messageService.save(Message.builder()
                .fromContact(validContact)
                .build()));
    assertEquals("save.message.toContact: must not be null", thrown.getMessage());
  }

  @Test
  void save_NullFromContactThrowsConstraintViolationException() {
    ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
        () -> messageService.save(Message.builder()
            .toContact(validContact)
            .build()));
    assertEquals("save.message.fromContact: must not be null", thrown.getMessage());
  }

  @Test
  void save_InvalidToContactThrowsConstraintViolationException() {
    ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
        () -> messageService.save(Message.builder()
            .fromContact(validContact)
            .toContact(invalidContact)
            .build()));
    assertEquals("save.message.toContact.number: must not be null", thrown.getMessage());
  }

  @Test
  void save_InvalidFromContactThrowsConstraintViolationException() {
    ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class,
        () -> messageService.save(Message.builder()
            .fromContact(invalidContact)
            .toContact(validContact)
            .build()));
    assertEquals("save.message.fromContact.number: must not be null", thrown.getMessage());
  }
}