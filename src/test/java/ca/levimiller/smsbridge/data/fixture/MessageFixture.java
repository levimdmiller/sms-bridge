package ca.levimiller.smsbridge.data.fixture;

import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.Media;
import ca.levimiller.smsbridge.data.model.Message;
import ca.levimiller.smsbridge.data.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MessageFixture implements Fixture<Message> {
  private final RandomUtil randomUtil;
  private final Fixture<Contact> contactFixture;
  private final Fixture<Media> mediaFixture;

  @Autowired
  public MessageFixture(RandomUtil randomUtil,
      Fixture<Contact> contactFixture,
      Fixture<Media> mediaFixture) {
    this.randomUtil = randomUtil;
    this.contactFixture = contactFixture;
    this.mediaFixture = mediaFixture;
  }

  @Override
  public Message create() {
    Message message = Message.builder()
        .uid(randomUtil.getString(255))
        .body(randomUtil.getString(255))
        .fromContact(contactFixture.create())
        .toContact(contactFixture.create())
        .media(randomUtil.getList(mediaFixture))
        .build();
    message.getMedia().forEach(media -> media.setMessage(message));
    return message;
  }
}
