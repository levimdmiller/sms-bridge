package ca.levimiller.smsbridge.rest.impl;

import ca.levimiller.smsbridge.data.dto.SmsDto;
import ca.levimiller.smsbridge.rest.TwilioController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class TwilioApi implements TwilioController {

  @Override
  public void createSms(SmsDto sms) {
    log.info("Received sms: {}", sms);
  }
}
