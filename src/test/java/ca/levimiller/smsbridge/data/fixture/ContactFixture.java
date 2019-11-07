package ca.levimiller.smsbridge.data.fixture;

import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.Location;
import ca.levimiller.smsbridge.data.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContactFixture implements Fixture<Contact> {
  private final RandomUtil randomUtil;
  private final Fixture<Location> locationFixture;

  @Autowired
  public ContactFixture(RandomUtil randomUtil,
      Fixture<Location> locationFixture) {
    this.randomUtil = randomUtil;
    this.locationFixture = locationFixture;
  }

  @Override
  public Contact create() {
    return Contact.builder()
        .number(randomUtil.getString(15))
        .location(locationFixture.create())
        .build();
  }
}
