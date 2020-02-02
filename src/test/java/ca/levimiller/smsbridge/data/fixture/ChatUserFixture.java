package ca.levimiller.smsbridge.data.fixture;

import ca.levimiller.smsbridge.data.model.ChatUser;
import ca.levimiller.smsbridge.data.model.ChatUserType;
import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatUserFixture implements Fixture<ChatUser> {
  private final RandomUtil randomUtil;
  private final Fixture<Contact> contactFixture;

  @Autowired
  public ChatUserFixture(RandomUtil randomUtil,
      Fixture<Contact> contactFixture) {
    this.randomUtil = randomUtil;
    this.contactFixture = contactFixture;
  }

  @Override
  public ChatUser create() {
    return ChatUser.builder()
        .ownerId(randomUtil.getString(255))
        .userType(randomUtil.getEnum(ChatUserType.class))
        .contact(contactFixture.create())
        .build();
  }
}
