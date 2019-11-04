package ca.levimiller.smsbridge.data.transformer.twilio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import ca.levimiller.smsbridge.data.dto.TwilioSmsDto;
import ca.levimiller.smsbridge.data.fixture.Fixture;
import ca.levimiller.smsbridge.data.model.Media;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MediaTransformerTest {

  private final Fixture<TwilioSmsDto> smsDtoFixture;
  private final MediaTransformer transformer;
  private TwilioSmsDto dto;

  @Autowired
  MediaTransformerTest(
      Fixture<TwilioSmsDto> smsDtoFixture,
      MediaTransformer transformer) {
    this.smsDtoFixture = smsDtoFixture;
    this.transformer = transformer;
  }

  @BeforeEach
  void setUp() {
    dto = smsDtoFixture.create();
  }

  @Test
  void transform() {
    List<Media> media = transformer.transform(dto);

    // verify
    assertEquals(dto.getNumMedia(), media.size());

    List<String> urls = dto.getMediaUrls();
    List<String> contentTypes = dto.getMediaContentTypes();
    for (int i = 0; i < dto.getNumMedia(); i++) {
      assertEquals(urls.get(i), media.get(i).getUrl());
      assertEquals(contentTypes.get(i), media.get(i).getContentType());
    }
  }
}
