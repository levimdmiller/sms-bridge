package ca.levimiller.smsbridge.service;

import ca.levimiller.smsbridge.data.model.Message;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

@Validated
public interface MessageService {

  /**
   * Saves the given message.
   *
   * @param message - message to save
   * @return - updated message
   */
  Message save(@Valid @NotNull Message message);

}
