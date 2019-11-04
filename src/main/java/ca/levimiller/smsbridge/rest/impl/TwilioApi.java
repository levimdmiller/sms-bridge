package ca.levimiller.smsbridge.rest.impl;

import ca.levimiller.smsbridge.data.dto.TwilioSmsDto;
import ca.levimiller.smsbridge.rest.TwilioController;
import ca.levimiller.smsbridge.service.MessageService;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TwilioApi implements TwilioController {
  private final MessageService messageService;

  @Inject
  public TwilioApi(MessageService messageService) {
    this.messageService = messageService;
  }

  @Override
  public void createSms(TwilioSmsDto sms) {
    log.info("Received sms: {}", sms);

  }
}
