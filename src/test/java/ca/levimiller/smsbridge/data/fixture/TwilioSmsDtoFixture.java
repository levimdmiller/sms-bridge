package ca.levimiller.smsbridge.data.fixture;

import ca.levimiller.smsbridge.data.dto.TwilioSmsDto;
import java.util.Arrays;
import org.springframework.stereotype.Component;

@Component
public class TwilioSmsDtoFixture implements Fixture<TwilioSmsDto> {

  @Override
  public TwilioSmsDto create() {
    return TwilioSmsDto.builder()
        .messageSid("messageSid")
        .accountSid("accountSid")
        .messagingServiceSid("messagingServiceSid")
        .from("+123")
        .to("+456")
        .body("body")
        .numSegments(3)
        .numMedia(2)
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
}
