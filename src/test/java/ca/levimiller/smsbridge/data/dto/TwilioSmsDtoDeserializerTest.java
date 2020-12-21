package ca.levimiller.smsbridge.data.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TwilioSmsDtoDeserializerTest {
  private final ObjectMapper objectMapper;

  @Autowired
  public TwilioSmsDtoDeserializerTest(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Test
  public void testDeserialize() throws IOException {
    String json = "{\n"
        + "  \"AccountSid\": \"AC2e68c221453e89222d2bc48a37bd50e0\",\n"
        + "  \"Body\": \"body-message\",\n"
        + "  \"From\": \"+12234513434\",\n"
        + "  \"FromCity\": \"FROM_KELOWNA\",\n"
        + "  \"FromCountry\": \"FROM_CA\",\n"
        + "  \"FromState\": \"FROM_BC\",\n"
        + "  \"FromZip\": \"FromZip\",\n"
        + "  \"MediaContentType0\": \"image/jpeg\",\n"
        + "  \"MediaUrl0\": \"https://url.com\",\n"
        + "  \"MediaContentType1\": \"image/png\",\n"
        + "  \"MediaUrl1\": \"https://url2.com\",\n"
        + "  \"MessageSid\": \"MM36deb4a740152b06e4711661fed3f3c8\",\n"
        + "  \"NumMedia\": \"2\",\n"
        + "  \"NumSegments\": \"1\",\n"
        + "  \"SmsMessageSid\": \"MM36deb4a740152b06e4711661fed3f3c8\",\n"
        + "  \"SmsSid\": \"MM36deb4a740152b06e4711661fed3f3c8\",\n"
        + "  \"SmsStatus\": \"received\",\n"
        + "  \"To\": \"+12503488031\",\n"
        + "  \"ToCity\": \"KELOWNA\",\n"
        + "  \"ToCountry\": \"CA\",\n"
        + "  \"ToState\": \"BC\",\n"
        + "  \"ToZip\": \"ToZip\"\n"
        + "}";
    TwilioSmsDto dto = objectMapper.readValue(json, TwilioSmsDto.class);

    assertEquals("MM36deb4a740152b06e4711661fed3f3c8", dto.messageSid);
    assertEquals("AC2e68c221453e89222d2bc48a37bd50e0", dto.accountSid);
    assertEquals("+12234513434", dto.from);
    assertEquals("+12503488031", dto.to);
    assertEquals("body-message", dto.body);
    assertEquals(1, dto.numSegments);
    assertEquals(2, dto.numMedia);
    assertEquals(List.of("image/jpeg", "image/png"), dto.mediaContentTypes);
    assertEquals(List.of("https://url.com", "https://url2.com"), dto.mediaUrls);
    assertEquals("FROM_KELOWNA", dto.fromCity);
    assertEquals("FROM_BC", dto.fromState);
    assertEquals("FromZip", dto.fromZip);
    assertEquals("FROM_CA", dto.fromCountry);
    assertEquals("KELOWNA", dto.toCity);
    assertEquals("BC", dto.toState);
    assertEquals("ToZip", dto.toZip);
    assertEquals("CA", dto.toCountry);
  }
}
