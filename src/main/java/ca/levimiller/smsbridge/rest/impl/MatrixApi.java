package ca.levimiller.smsbridge.rest.impl;

import ca.levimiller.smsbridge.data.dto.matrix.EventDto;
import ca.levimiller.smsbridge.data.dto.matrix.TransactionDto;
import ca.levimiller.smsbridge.rest.MatrixController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MatrixApi implements MatrixController {

  @Override
  public void eventTransaction(String transactionId, TransactionDto data) {
    log.info("Transaction id: {}", transactionId);
    for(EventDto event : data.getEvents()) {
      log.info("Body:\n {}", event);
    }
  }

  @Override
  public void users(String userId) {
    log.info("User id: {}", userId);
  }

  @Override
  public void rooms(String alias) {
    log.info("Alias: {}", alias);
  }
}
