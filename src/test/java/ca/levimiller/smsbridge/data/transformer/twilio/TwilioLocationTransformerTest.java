package ca.levimiller.smsbridge.data.transformer.twilio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.levimiller.smsbridge.data.dto.TwilioSmsDto;
import ca.levimiller.smsbridge.data.model.Location;
import java.util.Arrays;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TwilioLocationTransformerTest {
  private final TwilioLocationTransformer transformer;
  private TwilioSmsDto dto;

  @Autowired
  TwilioLocationTransformerTest(
      TwilioLocationTransformer transformer) {
    this.transformer = transformer;
  }

  @BeforeEach
  void setUp() {
    dto = TwilioSmsDto.builder()
        .messageSid("messageSid")
        .accountSid("accountSid")
        .messagingServiceSid("messagingServiceSid")
        .from("from")
        .to("to")
        .body("body")
        .numSegments(2)
        .numMedia(3)
        .mediaContentTypes(Arrays.asList("media1", "media2"))
        .mediaUrls(Arrays.asList("url1", "url2"))
        .fromCity("fromCity")
        .fromState("fromState")
        .fromCountry("fromCountry")
        .fromZip("fromZip")
        .toCity("toCity")
        .toState("toState")
        .toCountry("toCountry")
        .toZip("toZip")
        .build();
  }

  @Test
  void transformTo() {
    Location location = transformer.transformTo(dto);
    assertEquals("toCity", location.getCity());
    assertEquals("toState", location.getState());
    assertEquals("toCountry", location.getCountry());
    assertEquals("toZip", location.getZip());
  }

  @Test
  void transformFrom() {
    Location location = transformer.transformFrom(dto);
    assertEquals("fromCity", location.getCity());
    assertEquals("fromState", location.getState());
    assertEquals("fromCountry", location.getCountry());
    assertEquals("fromZip", location.getZip());
  }
}
