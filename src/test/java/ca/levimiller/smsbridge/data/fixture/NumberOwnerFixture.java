package ca.levimiller.smsbridge.data.fixture;

import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.NumberRegistration;
import ca.levimiller.smsbridge.data.model.NumberRegistrationType;
import ca.levimiller.smsbridge.data.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NumberOwnerFixture implements Fixture<NumberRegistration> {
  private final RandomUtil randomUtil;
  private final Fixture<Contact> contactFixture;

  @Autowired
  public NumberOwnerFixture(RandomUtil randomUtil,
      Fixture<Contact> contactFixture) {
    this.randomUtil = randomUtil;
    this.contactFixture = contactFixture;
  }

  @Override
  public NumberRegistration create() {
    return NumberRegistration.builder()
        .ownerId(randomUtil.getString(255))
        .registrationType(randomUtil.getEnum(NumberRegistrationType.class))
        .contact(contactFixture.create())
        .build();
  }
}
