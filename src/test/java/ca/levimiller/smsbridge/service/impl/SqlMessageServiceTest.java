package ca.levimiller.smsbridge.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import ca.levimiller.smsbridge.data.db.ContactRepository;
import ca.levimiller.smsbridge.data.db.MessageRepository;
import ca.levimiller.smsbridge.data.fixture.Fixture;
import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.Message;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
class SqlMessageServiceTest {

  private final Fixture<Message> messageFixture;
  private final Fixture<Contact> contactFixture;
  private final SqlMessageService messageService;
  private Message message;
  private Message expectedMessage;
  @MockBean
  private ContactRepository contactRepository;
  @MockBean
  private MessageRepository messageRepository;

  @Autowired
  SqlMessageServiceTest(
      Fixture<Message> messageFixture,
      Fixture<Contact> contactFixture,
      SqlMessageService messageService) {
    this.messageFixture = messageFixture;
    this.contactFixture = contactFixture;
    this.messageService = messageService;
  }

  @BeforeEach
  void setUp() {
    message = messageFixture.create();
    Contact expectedTo = contactFixture.create();
    Contact expectedFrom = contactFixture.create();

    String toNumber = message.getToContact().getNumber();
    when(contactRepository.findDistinctByNumber(toNumber))
        .thenReturn(Optional.of(expectedTo));
    String fromNumber = message.getFromContact().getNumber();
    when(contactRepository.findDistinctByNumber(fromNumber))
        .thenReturn(Optional.of(expectedFrom));

    expectedMessage = Message.builder()
        .uid(message.getUid())
        .body(message.getBody())
        .media(message.getMedia())
        .toContact(expectedTo)
        .fromContact(expectedFrom)
        .build();
    when(messageRepository.save(expectedMessage)).thenReturn(expectedMessage);
  }

  @Test
  void save() {
    Message result = messageService.save(message);
    assertEquals(expectedMessage, result);
    verify(messageRepository, times(1)).save(expectedMessage);
  }
}
