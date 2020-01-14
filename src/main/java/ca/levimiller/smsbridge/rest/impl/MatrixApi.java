package ca.levimiller.smsbridge.rest.impl;

import ca.levimiller.smsbridge.data.db.TransactionRepository;
import ca.levimiller.smsbridge.data.model.Transaction;
import ca.levimiller.smsbridge.rest.MatrixController;
import ca.levimiller.smsbridge.service.MatrixEventService;
import io.github.ma1uta.matrix.EmptyResponse;
import io.github.ma1uta.matrix.application.model.TransactionRequest;
import javax.inject.Inject;
import org.springframework.stereotype.Component;

@Component
public class MatrixApi implements MatrixController {
  private final TransactionRepository transactionRepository;
  private final MatrixEventService eventService;

  @Inject
  public MatrixApi(TransactionRepository transactionRepository,
      MatrixEventService eventService) {
    this.transactionRepository = transactionRepository;
    this.eventService = eventService;
  }

  @Override
  public EmptyResponse transaction(String txnId, TransactionRequest request) {
    // only process if not started already
    transactionRepository.findDistinctByTransactionId(txnId).orElseGet(() -> {
      Transaction transaction = transactionRepository.save(Transaction.builder()
          .transactionId(txnId)
          .completed(false)
          .build());

      // process all events
      request.getEvents().forEach(eventService::process);

      transaction.setCompleted(true);
      return transactionRepository.save(transaction);
    });
    return new EmptyResponse();
  }

  @Override
  public EmptyResponse rooms(String roomAlias) {
    return new EmptyResponse();
  }

  @Override
  public EmptyResponse users(String userId) {
    return new EmptyResponse();
  }
}
