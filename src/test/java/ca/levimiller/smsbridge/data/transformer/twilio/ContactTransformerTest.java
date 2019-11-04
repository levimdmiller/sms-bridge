package ca.levimiller.smsbridge.data.transformer.twilio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import ca.levimiller.smsbridge.data.dto.TwilioSmsDto;
import ca.levimiller.smsbridge.data.fixture.Fixture;
import ca.levimiller.smsbridge.data.fixture.TwilioSmsDtoFixture;
import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ContactTransformerTest {
  private final Fixture<TwilioSmsDto> smsDtoFixture;
  private final ContactTransformer transformer;
  private TwilioSmsDto dto;

  @Autowired
  ContactTransformerTest(
      TwilioSmsDtoFixture smsDtoFixture,
      ContactTransformer transformer) {
    this.smsDtoFixture = smsDtoFixture;
    this.transformer = transformer;
  }

  @BeforeEach
  void setUp() {
    dto = smsDtoFixture.create();
  }

  @Test
  void transformTo() {
    Contact contact = transformer.transformTo(dto);
    assertEquals(dto.getTo(), contact.getNumber());

    Location location = contact.getLocation();
    assertNotNull(location);
    assertEquals(dto.getToCity(), location.getCity());
    assertEquals(dto.getToState(), location.getState());
    assertEquals(dto.getToCountry(), location.getCountry());
    assertEquals(dto.getToZip(), location.getZip());
  }

  @Test
  void transformFrom() {
    Contact contact = transformer.transformFrom(dto);
    assertEquals(dto.getFrom(), contact.getNumber());

    Location location = contact.getLocation();
    assertNotNull(location);
    assertEquals(dto.getFromCity(), location.getCity());
    assertEquals(dto.getFromState(), location.getState());
    assertEquals(dto.getFromCountry(), location.getCountry());
    assertEquals(dto.getFromZip(), location.getZip());
  }
}
