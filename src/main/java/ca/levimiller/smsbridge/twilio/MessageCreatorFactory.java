package ca.levimiller.smsbridge.twilio;

import com.twilio.rest.api.v2010.account.MessageCreator;
import com.twilio.type.PhoneNumber;

public interface MessageCreatorFactory {

  /**
   * Gets a twilio MessageCreator.
   *
   * @param to - destination phone number
   * @param from - origin phone number
   * @param body - message body
   * @return message creator
   */
  MessageCreator getCreator(PhoneNumber to, PhoneNumber from, String body);
}
