package ca.levimiller.smsbridge.service.impl.twilio;

import ca.levimiller.smsbridge.data.model.Media;
import ca.levimiller.smsbridge.data.model.Message;
import ca.levimiller.smsbridge.service.OutgoingAttachmentService;
import ca.levimiller.smsbridge.service.impl.HostedUrlService;
import ca.levimiller.smsbridge.twilio.MessageCreatorFactory;
import java.net.URISyntaxException;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.stereotype.Service;
import com.twilio.type.PhoneNumber;

@Slf4j
@Service
public class TwilioAttachmentService implements OutgoingAttachmentService {
  private final MessageCreatorFactory messageCreatorFactory;
  private final HostedUrlService hostedUrlService;

  @Inject
  public TwilioAttachmentService(
      MessageCreatorFactory messageCreatorFactory,
      HostedUrlService hostedUrlService) {
    this.messageCreatorFactory = messageCreatorFactory;
    this.hostedUrlService = hostedUrlService;
  }
  @Override
  public void sendAttachment(Media attachment) {
    Message message = attachment.getMessage();
    try {
      // proxy matrix attachments so twilio has access
      String attachmentUrl = new URIBuilder(hostedUrlService.getBaseUrl())
          .setPath("/twilio/attachment/" + attachment.getUid())
          .toString();

      // send attachment
      messageCreatorFactory.getCreator(
          new PhoneNumber(message.getToContact().getNumber()),
          new PhoneNumber(message.getFromContact().getNumber()),
          ""
      ).setMediaUrl(attachmentUrl)
       .create();
    } catch (URISyntaxException e) {
      log.error("Error getting attachment url for twilio.", e);
    }
  }
}
