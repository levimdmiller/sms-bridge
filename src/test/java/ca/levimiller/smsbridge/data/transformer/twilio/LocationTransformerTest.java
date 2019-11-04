package ca.levimiller.smsbridge.data.transformer.twilio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.levimiller.smsbridge.data.dto.TwilioSmsDto;
import ca.levimiller.smsbridge.data.fixture.Fixture;
import ca.levimiller.smsbridge.data.model.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class LocationTransformerTest {
  private final Fixture<TwilioSmsDto> smsDtoFixture;
  private final LocationTransformer transformer;
  private TwilioSmsDto dto;

  @Autowired
  LocationTransformerTest(
      Fixture<TwilioSmsDto> smsDtoFixture,
      LocationTransformer transformer) {
    this.smsDtoFixture = smsDtoFixture;
    this.transformer = transformer;
  }

  @BeforeEach
  void setUp() {
    dto = smsDtoFixture.create();
  }

  @Test
  void transformTo() {
    Location location = transformer.transformTo(dto);
    assertEquals(dto.getToCity(), location.getCity());
    assertEquals(dto.getToState(), location.getState());
    assertEquals(dto.getToCountry(), location.getCountry());
    assertEquals(dto.getToZip(), location.getZip());
  }

  @Test
  void transformFrom() {
    Location location = transformer.transformFrom(dto);
    assertEquals(dto.getFromCity(), location.getCity());
    assertEquals(dto.getFromState(), location.getState());
    assertEquals(dto.getFromCountry(), location.getCountry());
    assertEquals(dto.getFromZip(), location.getZip());
  }
}
