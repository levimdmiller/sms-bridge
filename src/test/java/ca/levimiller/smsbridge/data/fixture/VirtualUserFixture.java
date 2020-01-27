package ca.levimiller.smsbridge.data.fixture;

import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.VirtualUser;
import ca.levimiller.smsbridge.data.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VirtualUserFixture implements Fixture<VirtualUser> {
  private final RandomUtil randomUtil;
  private final Fixture<Contact> contactFixture;

  @Autowired
  public VirtualUserFixture(RandomUtil randomUtil,
      Fixture<Contact> contactFixture) {
    this.randomUtil = randomUtil;
    this.contactFixture = contactFixture;
  }

  @Override
  public VirtualUser create() {
    return VirtualUser.builder()
        .userId(randomUtil.getString(255))
        .contact(contactFixture.create())
        .build();
  }
}
