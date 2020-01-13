package ca.levimiller.smsbridge.data.db;

import ca.levimiller.smsbridge.data.fixture.Fixture;
import ca.levimiller.smsbridge.data.model.Message;
import org.springframework.beans.factory.annotation.Autowired;

class TransactionRepositoryIT extends AbstractDbIT<Message> {

  @Autowired
  TransactionRepositoryIT(MessageRepository messageRepository,
      Fixture<Message> messageFixture) {
    super(messageFixture, messageRepository);
  }
}
