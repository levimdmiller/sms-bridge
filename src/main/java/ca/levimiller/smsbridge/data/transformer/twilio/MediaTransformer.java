package ca.levimiller.smsbridge.data.transformer.twilio;

import ca.levimiller.smsbridge.data.dto.TwilioSmsDto;
import ca.levimiller.smsbridge.data.model.Media;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MediaTransformer {

  /**
   * Extracts a list of Media objects from the given dto. Assumes that the numMedia property is
   * accurate (i.e., exactly the size of the contentType and url lists)
   *
   * @param dto - dto to extract.
   * @return - list of media objects.
   */
  default List<Media> transform(TwilioSmsDto dto) {
    List<Media> media = new ArrayList<>();
    for (int i = 0; i < dto.getNumMedia(); i++) {
      media.add(Media.builder()
          .url(dto.getMediaUrls().get(i))
          .contentType(dto.getMediaContentTypes().get(i))
          .build());
    }
    return media;
  }
}
