package ca.levimiller.smsbridge.data.fixture;

import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.NumberOwner;
import ca.levimiller.smsbridge.data.model.NumberOwnerType;
import ca.levimiller.smsbridge.data.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NumberOwnerFixture implements Fixture<NumberOwner> {
  private final RandomUtil randomUtil;
  private final Fixture<Contact> contactFixture;

  @Autowired
  public NumberOwnerFixture(RandomUtil randomUtil,
      Fixture<Contact> contactFixture) {
    this.randomUtil = randomUtil;
    this.contactFixture = contactFixture;
  }

  @Override
  public NumberOwner create() {
    return NumberOwner.builder()
        .ownerId(randomUtil.getString(255))
        .ownerType(randomUtil.getEnum(NumberOwnerType.class))
        .contact(contactFixture.create())
        .build();
  }
}
