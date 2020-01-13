package ca.levimiller.smsbridge.rest.impl;

import ca.levimiller.smsbridge.data.db.TransactionRepository;
import ca.levimiller.smsbridge.data.model.Transaction;
import ca.levimiller.smsbridge.rest.MatrixController;
import ca.levimiller.smsbridge.service.impl.matrix.MatrixEventServiceFactory;
import io.github.ma1uta.matrix.EmptyResponse;
import io.github.ma1uta.matrix.application.model.TransactionRequest;
import javax.inject.Inject;
import org.springframework.stereotype.Component;

@Component
public class MatrixApi implements MatrixController {
  private final TransactionRepository transactionRepository;
  private final MatrixEventServiceFactory eventServiceFactory;

  @Inject
  public MatrixApi(TransactionRepository transactionRepository,
      MatrixEventServiceFactory eventServiceFactory) {
    this.transactionRepository = transactionRepository;
    this.eventServiceFactory = eventServiceFactory;
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
      request.getEvents().forEach(
          e -> eventServiceFactory.getEventService(e.getClass()).process(e));

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
