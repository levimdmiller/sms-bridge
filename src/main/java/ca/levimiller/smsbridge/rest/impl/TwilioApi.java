package ca.levimiller.smsbridge.rest.impl;

import ca.levimiller.smsbridge.data.db.MediaRepository;
import ca.levimiller.smsbridge.data.dto.TwilioSmsDto;
import ca.levimiller.smsbridge.data.model.Media;
import ca.levimiller.smsbridge.data.model.Message;
import ca.levimiller.smsbridge.data.transformer.twilio.MessageTransformer;
import ca.levimiller.smsbridge.error.NotFoundException;
import ca.levimiller.smsbridge.rest.TwilioController;
import ca.levimiller.smsbridge.service.ChatService;
import ca.levimiller.smsbridge.service.FileService;
import ca.levimiller.smsbridge.service.MessageService;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TwilioApi implements TwilioController {

  private final ChatService chatService;
  private final MessageTransformer messageTransformer;
  private final MessageService messageService;
  private final MediaRepository mediaRepository;
  private final FileService fileService;

  @Inject
  public TwilioApi(
      @Qualifier("matrixChatService")
          ChatService chatService,
      MessageTransformer messageTransformer,
      MessageService messageService,
      MediaRepository mediaRepository,
      @Qualifier("matrixFileService") FileService fileService) {
    this.chatService = chatService;
    this.messageTransformer = messageTransformer;
    this.messageService = messageService;
    this.mediaRepository = mediaRepository;
    this.fileService = fileService;
  }

  @Override
  public void createSms(TwilioSmsDto sms) {
    log.debug("Received sms: {}", sms);
    Message message = messageTransformer.transform(sms);
    if (message.getMedia() != null) {
      message.getMedia().forEach(media -> media.setMessage(message));
    }
    messageService.save(message);
    chatService.sendMessage(message);
  }

  @Override
  public ResponseEntity<InputStreamResource> getAttachment(UUID mediaUid) {
    Media media = mediaRepository.findDistinctByUid(mediaUid)
        .orElseThrow(() -> new NotFoundException("Requested attachment not found."));
    try {
      return ResponseEntity.ok()
          .contentType(MediaType.valueOf(media.getContentType()))
          .body(new InputStreamResource(fileService.getFileStream(media.getUrl())));
    } catch (URISyntaxException | IOException e) {
      log.error("Error getting attachment from matrix", e);
      throw new NotFoundException("Media url malformed or doesn't exist.");
    }
  }
}
