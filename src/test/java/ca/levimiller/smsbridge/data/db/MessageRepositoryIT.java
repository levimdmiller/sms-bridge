package ca.levimiller.smsbridge.data.db;

import ca.levimiller.smsbridge.data.fixture.Fixture;
import ca.levimiller.smsbridge.data.model.Message;
import org.springframework.beans.factory.annotation.Autowired;

class MessageRepositoryIT extends AbstractDbIT<Message> {

  @Autowired
  MessageRepositoryIT(MessageRepository messageRepository,
      Fixture<Message> messageFixture) {
    super(messageFixture, messageRepository);
  }
}
