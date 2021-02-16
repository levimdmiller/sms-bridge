package ca.levimiller.smsbridge.data.transformer.matrix;

import ca.levimiller.smsbridge.data.model.Media;
import ca.levimiller.smsbridge.error.TransformationException;
import io.github.ma1uta.matrix.event.content.RoomMessageContent;
import io.github.ma1uta.matrix.event.message.Audio;
import io.github.ma1uta.matrix.event.message.File;
import io.github.ma1uta.matrix.event.message.Image;
import io.github.ma1uta.matrix.event.message.Video;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class MatrixMediaTransformer {

  List<Media> transform(RoomMessageContent content) {
    String url = null;
    String contentType = null;

    // Mapstruct doesn't support polymorphism :(
    if (content instanceof Audio) {
      Audio audio = (Audio) content;
      url = audio.getUrl();
      contentType = audio.getInfo().getMimetype();
    } else if (content instanceof File) {
      File file = (File) content;
      url = file.getUrl();
      contentType = file.getInfo().getMimetype();
    } else if (content instanceof Image) {
      Image image = (Image) content;
      url = image.getUrl();
      contentType = image.getInfo().getMimetype();
    } else if (content instanceof Video) {
      Video video = (Video) content;
      url = video.getUrl();
      contentType = video.getInfo().getMimetype();
    }

    return Collections.singletonList(Media.builder()
        .url(url)
        .contentType(contentType)
        .build());
  }
}
