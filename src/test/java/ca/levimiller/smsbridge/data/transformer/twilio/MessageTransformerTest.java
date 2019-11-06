package ca.levimiller.smsbridge.data.transformer.twilio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ca.levimiller.smsbridge.data.dto.TwilioSmsDto;
import ca.levimiller.smsbridge.data.fixture.Fixture;
import ca.levimiller.smsbridge.data.model.Contact;
import ca.levimiller.smsbridge.data.model.Location;
import ca.levimiller.smsbridge.data.model.Media;
import ca.levimiller.smsbridge.data.model.Message;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MessageTransformerTest {

  private final Fixture<TwilioSmsDto> smsDtoFixture;
  private final MessageTransformer transformer;
  private TwilioSmsDto dto;

  @Autowired
  MessageTransformerTest(
      Fixture<TwilioSmsDto> smsDtoFixture,
      MessageTransformer transformer) {
    this.smsDtoFixture = smsDtoFixture;
    this.transformer = transformer;
  }

  @BeforeEach
  void setUp() {
    dto = smsDtoFixture.create();
  }

  @Test
  void transform() {
    Message message = transformer.transform(dto);

    assertEquals(dto.getMessageSid(), message.getUid());
    assertEquals(dto.getBody(), message.getBody());

    // to equals
    Contact to = message.getToContact();
    assertEquals(dto.getTo(), to.getNumber());

    Location toLocation = to.getLocation();
    assertNotNull(toLocation);
    assertEquals(dto.getToCity(), toLocation.getCity());
    assertEquals(dto.getToState(), toLocation.getState());
    assertEquals(dto.getToCountry(), toLocation.getCountry());
    assertEquals(dto.getToZip(), toLocation.getZip());

    // from equals
    Contact from = message.getFromContact();
    assertEquals(dto.getFrom(), from.getNumber());

    Location fromLocation = from.getLocation();
    assertNotNull(fromLocation);
    assertEquals(dto.getFromCity(), fromLocation.getCity());
    assertEquals(dto.getFromState(), fromLocation.getState());
    assertEquals(dto.getFromCountry(), fromLocation.getCountry());
    assertEquals(dto.getFromZip(), fromLocation.getZip());

    // media equals
    List<Media> media = message.getMedia();
    assertEquals(dto.getNumMedia(), media.size());
    List<String> urls = dto.getMediaUrls();
    List<String> contentTypes = dto.getMediaContentTypes();
    for (int i = 0; i < dto.getNumMedia(); i++) {
      assertEquals(urls.get(i), media.get(i).getUrl());
      assertEquals(contentTypes.get(i), media.get(i).getContentType());
    }
  }
}
